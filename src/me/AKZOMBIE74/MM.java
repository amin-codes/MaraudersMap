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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Amin on 3/25/2017.
 */
public class MM extends JavaPlugin {
    private static MM instance;
    private int FACE_SIZE = -1;
    private MapView.Scale scale;

    private MapData data;

    @Override
    public void onEnable(){
        instance = this;
        data = new MapData();

        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new ChangeIt(), instance);
        pm.registerEvents(new MapListener(), instance);
        pm.registerEvents(new leave(), instance);
        pm.registerEvents(new preProcess(), instance);

        getCommand("mm").setExecutor(new MyMap());

        createConfig();

        FACE_SIZE=getConfig().getInt("faceSize");
        scale = MapView.Scale.valueOf(getConfig().getString("scale"));
        getLogger().info("Test commit");


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
                saveConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

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
