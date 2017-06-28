package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by Amin on 3/25/2017.
 */
public class MM extends JavaPlugin {
    private static MM instance;
    private int FACE_SIZE = -1;
    private MapView.Scale scale;

    private MapData data;

    //Update stuff
    String VERSION, CURRENT_VERSION, CHANGELOG;
    Boolean shouldUpdate;

    @Override
    public void onEnable(){
        instance = this;
        data = new MapData();

        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new ChangeIt(), instance);
        pm.registerEvents(new MapListener(), instance);
        pm.registerEvents(new leave(), instance);
        pm.registerEvents(new WorldChanged(), instance);

        getCommand("mm").setExecutor(new MyMap());

        createConfig();

        FACE_SIZE=getConfig().getInt("faceSize");
        scale = MapView.Scale.valueOf(getConfig().getString("scale"));
        checkForUpdates();
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        try {
            HashMap<Player, Image> playerFaces = MM.getInstance().getData().getImages();
            HashMap<Player, Image> oldFaces = MM.getInstance().getData().getOldImages();
            playerFaces.clear();
            oldFaces.clear();
        } catch (NoSuchMethodError e){

        }
    }

    static MM getInstance(){
        return instance;
    }

    MapData getData(){
        return data;
    }

    ItemStack createMap() {
        ItemStack map = new ItemStack(Material.MAP);
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+"Marauders Map");

        map.setItemMeta(meta);

        return map;
    }

    Integer imageSize(){
        return FACE_SIZE;
    }

    MapView.Scale scale(){
        return scale;
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
                getConfig().set("faceSize", 4);
                getConfig().set("scale", "CLOSEST");
                getConfig().set("show-update-message", true);
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
}
