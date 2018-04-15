//MaraudersMain
package me.AKZOMBIE74;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Amin on 3/25/2017.
 */
public class MM extends JavaPlugin {
    private static MM instance;
    private int FACE_SIZE = -1;
    private MapView.Scale SCALE;
    private String MAP_NAME;

    private MapData data;
    private String server_version;

    private ItemStack map;
    private int am;//Amount
    private World w;

    private boolean INDIVIDUAL_SCALES;
    private boolean SHOW_IF_INVISIBLE;
    private boolean SHOW_IF_SPECTATING;

    //Update stuff
    String VERSION, CURRENT_VERSION, CHANGELOG;
    Boolean shouldUpdate;

    @Override
    public void onEnable(){
        instance = this;
        data = new MapData();

        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new ChangeIt(), instance);
        //pm.registerEvents(new MapListener(), instance);
        pm.registerEvents(new leave(), instance);
        server_version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        pm.registerEvents(new WorldChanged(), instance);
        //pm.registerEvents(new Updater(), instance);

        getCommand("mm").setExecutor(new MyMap());

        createConfig();
        MAP_NAME = ChatColor.translateAlternateColorCodes('&', getConfig().getString("map-display-name"));
        FACE_SIZE=getConfig().getInt("faceSize");
        SCALE = MapView.Scale.valueOf(getConfig().getString("scale"));
        INDIVIDUAL_SCALES = getConfig().getBoolean("individual-scales");
        SHOW_IF_INVISIBLE = getConfig().getBoolean("show-players-when-invisible");
        SHOW_IF_SPECTATING = getConfig().getBoolean("show-players-when-spectating");
        checkForUpdates();
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        MM.getInstance().getData().getImages().clear();
        MM.getInstance().getData().getOldImages().clear();
        Bukkit.getServer().getOnlinePlayers().forEach(MM::removeMyRender);
        instance = null;
    }

    public static MM getInstance(){
        return instance;
    }

    public MapData getData(){
        return data;
    }

    MM createMap(World world, int... amount) {
        map = null;
        w = world;
        am = amount.length > 0 ? amount[0] : 1;
        return this;
    }

    public ItemStack withScale(MapView.Scale... scale1)
    {

        MapView.Scale scale = INDIVIDUAL_SCALES && scale1.length > 0 ? scale1[0] : SCALE;

        MapView mapView = Bukkit.createMap(w);
        myRender.applyToMap(mapView, scale);
        map = new ItemStack(Material.MAP, am);
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setDisplayName(MAP_NAME);
        meta.setScaling(true);
        map.setItemMeta(meta);
        map.setDurability(mapView.getId());

        am = 1;
        w = null;
        return map;
    }

    Integer imageSize(){
        return FACE_SIZE;
    }

    MapView.Scale scale(){
        return SCALE;
    }

    private void createConfig() {

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
                getConfig().set("map-display-name", "&eMarauders Map");
                getConfig().set("faceSize", 4);
                getConfig().set("scale", "CLOSEST");
                getConfig().set("individual-scales", true);
                getConfig().set("show-update-message", true);
                getConfig().set("show-players-when-invisible", true);
                getConfig().set("show-players-when-spectating", false);
                saveConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
                checkForValues();
                getLogger().info("Config.yml loaded!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void checkForValues(){
        if (!(getConfig().isInt("faceSize")
                && getConfig().isString("scale")
                && getConfig().isBoolean("show-update-message"))) {
            if (!getConfig().isInt("faceSize")) {
                getConfig().set("faceSize", 4);
            }
            if (!getConfig().isString("scale")) {
                getConfig().set("scale", "CLOSEST");
            }
            if (!getConfig().isBoolean("show-update-message")) {
                getConfig().set("show-update-message", true);
            }
            saveConfig();
        }
    }

    private String connectToVersion(String server){
        URL uri;
        try {
            uri = new URL(server);
            URLConnection ec = uri.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    ec.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);

            in.close();
            return a.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }
    private void checkForUpdates(){
        CURRENT_VERSION = getInstance().getDescription().getVersion();
        if (getConfig().getBoolean("show-update-message")) {
            String VersionAndChangelog = connectToVersion(
                    "https://private-8f513b-myspigotpluginupdates.apiary-mock.com/questions").split(",")[0]
                    .replaceAll("\"", ""); //MaraudersMap:Version:Changelog
            VERSION = VersionAndChangelog.split(":")[1]
                    .replaceAll(" ", "");//Version
            CHANGELOG = VersionAndChangelog.split(":")[2];//Changelog
            //Set boolean variable
            shouldUpdate = versionCompare(CURRENT_VERSION, VERSION) < 0;
        }
    }


    Image generateFace(String name) {
        try {
            URL url = new URL("https://minotar.net/avatar/" + name + "/"+imageSize()+".png");
            Image image = ImageIO.read(url);
            return image;
        } catch (IOException e) {
            getInstance().getLogger().info("Something went wrong with getting the image");
        }
        return null;
    }

    public String getServerVersion()
    {
        return server_version;
    }

    public boolean isMaraudersMap(ItemStack is)
    {
        return is != null && is.hasItemMeta() && is.getType() == Material.MAP && is.getItemMeta().getDisplayName().equals(MAP_NAME) && ((MapMeta)is.getItemMeta()).isScaling();
    }

    public static void removeMyRender(Player player)
    {
        for (ItemStack is : player.getInventory().getContents())
        {
            if (MM.getInstance().isMaraudersMap(is))
            {
                MapView view = Bukkit.getMap(is.getDurability());
                view.getRenderers().forEach(view::removeRenderer);
            }
        }
    }

    public String getMapName()
    {
        return MAP_NAME;
    }

    public boolean canUseMap(Player p)
    {
        String CAN_USE_MAP_PERMISSION = "marauders.activate";
        return p.hasPermission(CAN_USE_MAP_PERMISSION);
    }

    public boolean showIfInvisible(Player p) {
        if (SHOW_IF_INVISIBLE) return true;

        for (PotionEffect potionEffect : p.getActivePotionEffects())
        {
            if (potionEffect.getType().equals(PotionEffectType.INVISIBILITY))
                return false;
        }
        return true;
    }

    public boolean showIfSpectating(Player p) {
        if (!SHOW_IF_SPECTATING && p.getGameMode() == GameMode.SPECTATOR)
            return false;
        return true;
    }

    public boolean showOrNot(Player p)
    {
        if (!showIfSpectating(p)) return false;
        else if (!showIfInvisible(p))return false;
        return true;
    }
}
