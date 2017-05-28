package me.AKZOMBIE74;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Amin on 3/26/2017.
 */
public class leave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        HashMap<Player, Image> playerFaces = MM.getInstance().getData().getImages();
        HashMap<Player, Image> oldImages = MM.getInstance().getData().getOldImages();
        if ((!oldImages.containsKey(p)) && playerFaces.containsKey(p)) {
            MM.getInstance().getData().getOldImages().put(p, playerFaces.get(p));
            playerFaces.remove(p);
        }
        playerFaces.remove(e.getPlayer());
    }
}
