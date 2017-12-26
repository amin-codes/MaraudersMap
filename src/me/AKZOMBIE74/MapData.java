package me.AKZOMBIE74;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Amin on 3/25/2017.
 */
public class MapData {
    private HashMap<UUID, Image> images = new HashMap<>();
    private HashMap<UUID, Image> oldImages = new HashMap<>();

    HashMap<UUID, Image> getImages(){
        return images;
    }

    public HashMap<UUID, Image> getOldImages() {
        return oldImages;
    }

    public void addToOld(UUID p, Image image)
    {
        oldImages.put(p, image);
    }

    public void addToNew(UUID p, Image image)
    {
        images.put(p, image);
    }

    public void removeFromNew(UUID p)
    {
        images.remove(p);
    }
}
