package me.AKZOMBIE74;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Amin on 3/26/2017.
 */
public class leave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        HashMap<UUID, Image> playerFaces = MM.getInstance().getData().getImages();
        HashMap<UUID, Image> oldImages = MM.getInstance().getData().getOldImages();
        if (playerFaces.containsKey(p.getUniqueId()) && !oldImages.containsKey(p.getUniqueId())) {
            MM.getInstance().getData().addToOld(p.getUniqueId(), playerFaces.get(p.getUniqueId()));
        }
        MM.getInstance().getData().removeFromNew(p.getUniqueId());

    }
}
