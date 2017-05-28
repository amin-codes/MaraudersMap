package me.AKZOMBIE74;

import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Amin on 3/25/2017.
 */
public class MapData {
    private HashMap<Player, Image> images = new HashMap<>();
    private HashMap<Player, Image> oldImages = new HashMap<>();
    private MapRenderer defaultMap = null;

    HashMap<Player, Image> getImages(){
        return images;
    }

    public HashMap<Player, Image> getOldImages() {
        return oldImages;
    }

    MapRenderer getDefaultMap(){
        return defaultMap;
    }

    void setDefaultMap(MapRenderer r){
        defaultMap = r;
    }

}
