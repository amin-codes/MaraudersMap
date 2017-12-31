package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Created by Amin on 3/25/2017.
 */
public class myRender extends MapRenderer{
    private static String server_version = MM.getInstance().getServerVersion();
    private static final String craft_maprenderer = "org.bukkit.craftbukkit." + server_version + ".map.CraftMapRenderer";
    private static final String craft_map_view = "org.bukkit.craftbukkit." + server_version + ".map.CraftMapView";
    private static final String world_map = "net.minecraft.server." + server_version + ".WorldMap";
    private static final String minecraft_server = "net.minecraft.server." + server_version + ".MinecraftServer";
    private static final String world_server = "net.minecraft.server." + server_version + ".WorldServer";
    private static final String persistant_collection ="net.minecraft.server." + server_version + ".PersistentCollection";


    private static final Class<?> CraftMapRender = getClass(craft_maprenderer);
    private static final Class<?> CraftMapV = getClass(craft_map_view);
    private static final Class<?> WorldM = getClass(world_map);
    private static final Class<?> MinecraftS = getClass(minecraft_server);
    private static final Class<?> WorldS = getClass(world_server);
    private static final Class<?> PersistantC = getClass(persistant_collection);

    private boolean cleared;

    myRender(){
        super(true);
        cleared = false;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

        if (!MM.getInstance().canUseMap(player))
        {
            if (!cleared) {
                for (int i = 0; i < 128; i++) {
                    for (int j = 0; j < 128; j++) {
                        mapCanvas.setPixel(i, j, (byte) 0);
                    }
                }
                cleared = true;
            }
            return;
        }
        cleared = false;
        MapView.Scale scale = MM.getInstance().scale();
        try {
            Method getServer = MinecraftS.getMethod("getServer");
            Object s = getServer.invoke(null); //Returns MinecraftServer
            Field worlds_1 = s.getClass().getField("worlds");
            worlds_1.setAccessible(true);
            List worlds_final = (List) worlds_1.get(s);
            Object worlds = WorldS.cast(worlds_final.get(0));

            Field world_maps = worlds.getClass().getField("worldMaps");
            Object world_maps_final = PersistantC.cast(world_maps.get(worlds));
            Method w = world_maps_final.getClass().getMethod("get", PersistantC.getClass(), String.class);


            Constructor craftMapConstructor = CraftMapRender.getDeclaredConstructor(CraftMapV, WorldM);
            craftMapConstructor.setAccessible(true);
            Object world_map = WorldM.cast(w.invoke(world_maps_final, WorldM.getClass(), "map_"+mapView.getId()));
            Object craftMapRenderer = craftMapConstructor.newInstance(CraftMapV.cast(mapView),
                    world_map);


            /*CraftMapRenderer craftMapRenderer1 = new CraftMapRenderer((CraftMapView) mapView,
                    (WorldMap) MinecraftServer.getServer().worlds.get(0).worldMaps.get(WorldMap.class, "map_" + mapView.getId()));*/
            craftMapRenderer.getClass().getMethod("initialize", MapView.class).invoke(craftMapRenderer, mapView);
            //craftMapRenderer.initialize(mapView);
            craftMapRenderer.getClass().getMethod("render", MapView.class, MapCanvas.class, Player.class).invoke(craftMapRenderer, mapView, mapCanvas, player);
            //craftMapRenderer.render(mapView, mapCanvas, player);

        } catch (NoSuchMethodException |  IllegalAccessException | NoSuchFieldException | InstantiationException | InvocationTargetException exception) {
            //Do nothing
            exception.printStackTrace();
        }

        int playerX = player.getLocation().getBlockX();
        int playerZ = player.getLocation().getBlockZ();

        int realImageSize = 4;
        try {
            realImageSize = MM.getInstance().imageSize();
        } catch (NullPointerException e) {}
        int imageSize = realImageSize/2;

        if (playerX != 0) {
            if (playerZ != 0) {
                mapView.setCenterX(playerX);
                mapView.setCenterZ(playerZ);
            } else {
                mapView.setCenterX(playerX);
                mapView.setCenterZ(playerZ+1);
            }
        } else {
            mapView.setCenterX(playerX+1);
            if (playerZ!=0) {
                mapView.setCenterZ(playerZ);
            } else {
                mapView.setCenterZ(playerZ+1);
            }
        }

        int centerX = mapView.getCenterX();
        int centerZ = mapView.getCenterZ();

        MM.getInstance()
                .getData()
                .getImages()
                .entrySet()
                .stream()
                .filter(map -> Bukkit.getServer().getPlayer(map.getKey()).getWorld().equals(player.getWorld()) && Bukkit.getServer().getPlayer(map.getKey()).isOnline() && isInRange(Bukkit.getServer().getPlayer(map.getKey()).getLocation(), player.getLocation(), scale) && MM.getInstance().showOrNot(Bukkit.getServer().getPlayer(map.getKey()))).forEach(set -> { //key = player uuid, value = image
                Player key = Bukkit.getServer().getPlayer(set.getKey());
                //int distanceX = (int) (63.5 * key.getLocation().getBlockX())/centerX;
                //int distanceZ = (int) (63.5 * key.getLocation().getBlockZ())/centerZ;
                int[] coords = convertLocationToMapCoords(centerX, centerZ, key.getLocation(), scale);

                mapCanvas.drawImage(coords[0] - imageSize, coords[1] - imageSize, set.getValue());
        });
    }

    @Override
    public void initialize(MapView map) {
        super.initialize(map);
    }

    private static int getScaleSize(MapView.Scale scale ) {
        if ( scale.equals( MapView.Scale.CLOSEST ) ) return 1;
        else if ( scale.equals( MapView.Scale.CLOSE ) ) return 2;
        else if ( scale.equals( MapView.Scale.NORMAL ) ) return 4;
        else if ( scale.equals( MapView.Scale.FAR ) ) return 8;
        else if ( scale.equals( MapView.Scale.FARTHEST ) ) return 16;
        return 0;
    }

    //returns [x, y]
    private static int[] convertLocationToMapCoords(int centerX, int centerZ, Location l1, MapView.Scale scale)
    {
        int scale_size = getScaleSize(scale);
        int distanceX = ((l1.getBlockX() - centerX) / scale_size) + 63;
        int distanceZ = ((l1.getBlockZ() - centerZ) / scale_size) + 63;
        return new int[]{distanceX, distanceZ};
    }

    private static boolean isInRange(Location p1, Location p2, MapView.Scale scale)
    {
        int scale_size = getScaleSize(scale) * 128;
        return p1.distance(p2) <= scale_size;
    }



    static void applyToMap(MapView map, MapView.Scale... scale) {
        if(map != null){
            if (scale.length > 0) map.setScale(scale[0]);
            else map.setScale(MM.getInstance().scale());
            for (MapRenderer renderer : map.getRenderers()) {
                map.removeRenderer(renderer);

            }
            map.addRenderer(new myRender());
            //map.addRenderer(MM.getInstance().getData().getDefaultMap());
            //map.addRenderer();
        }
    }

    private static Class<?> getClass(String className, boolean... inner)
    {
        Class<?> type = null;
        boolean in = inner.length > 0 ? inner[0] : false;
        try {
            if (in)
            {
                int last = className.lastIndexOf(".");
                className = className.substring(0, last) + "$" + className.substring(last+1);
            }
            type = Class.forName(className);
        } catch (ClassNotFoundException e) {}
        return type;
    }
}
