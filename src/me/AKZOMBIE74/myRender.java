package me.AKZOMBIE74;

import me.AKZOMBIE74.utils.MapMethods;
import me.AKZOMBIE74.utils.NMS;
import org.bukkit.Bukkit;
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

    private boolean cleared;

    myRender(){
        super(true);
        cleared = false;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

        if (!cleared) {
            MapMethods.clearCanvas(mapCanvas);
            cleared = true;
        }
        if (!MM.getInstance().canUseMap(player)) return;

        MapView.Scale scale = MM.getInstance().scale();
        try {
            Method getServer = NMS.MinecraftS.getMethod("getServer");
            Object s = getServer.invoke(null); //Returns MinecraftServer
            Field worlds_1 = s.getClass().getField("worlds");
            worlds_1.setAccessible(true);
            List worlds_final = (List) worlds_1.get(s);
            Object worlds = NMS.WorldS.cast(worlds_final.get(0));

            Field world_maps = worlds.getClass().getField("worldMaps");
            Object world_maps_final = NMS.PersistantC.cast(world_maps.get(worlds));
            Method w = world_maps_final.getClass().getMethod("get", NMS.PersistantC.getClass(), String.class);


            Constructor craftMapConstructor = NMS.CraftMapRender.getDeclaredConstructor(NMS.CraftMapV, NMS.WorldM);
            craftMapConstructor.setAccessible(true);
            Object world_map = NMS.WorldM.cast(w.invoke(world_maps_final, NMS.WorldM.getClass(), "map_"+mapView.getId()));
            Object craftMapRenderer = craftMapConstructor.newInstance(NMS.CraftMapV.cast(mapView),
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
                .filter(map -> Bukkit.getServer().getPlayer(map.getKey()).getWorld().equals(player.getWorld()) && Bukkit.getServer().getPlayer(map.getKey()).isOnline() && MapMethods.isInRange(Bukkit.getServer().getPlayer(map.getKey()).getLocation(), player.getLocation(), scale) && MM.getInstance().showOrNot(Bukkit.getServer().getPlayer(map.getKey()))).forEach(set -> { //key = player uuid, value = image
                Player key = Bukkit.getServer().getPlayer(set.getKey());
                //int distanceX = (int) (63.5 * key.getLocation().getBlockX())/centerX;
                //int distanceZ = (int) (63.5 * key.getLocation().getBlockZ())/centerZ;
                int[] coords = MapMethods.convertLocationToMapCoords(centerX, centerZ, key.getLocation(), scale);

                mapCanvas.drawImage(coords[0] - imageSize, coords[1] - imageSize, set.getValue());
        });
        cleared = false;
    }

    @Override
    public void initialize(MapView map) {
        super.initialize(map);
    }
}
