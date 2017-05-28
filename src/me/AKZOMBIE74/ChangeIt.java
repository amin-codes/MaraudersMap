package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Amin on 3/25/2017.
 */
public class ChangeIt implements Listener {

    @EventHandler
    public void change(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        HashMap<Player, Image> playerFaces = MM.getInstance().getData().getImages();
        HashMap<Player, Image> oldFaces = MM.getInstance().getData().getOldImages();
        ItemStack is = MM.getInstance().createMap();

        MapView mapView = Bukkit.getMap(is.getDurability());

        if (MM.getInstance().getData().getDefaultMap() == null) {
            MM.getInstance().getData().setDefaultMap(mapView.getRenderers().get(0));
        }
        myRender.applyToMap(mapView);

        if (oldFaces.containsKey(p)) {
            playerFaces.put(p, oldFaces.get(p));
        } else {
            playerFaces.put(p, MM.getInstance().generateFace(name));
        }

        if (p.getInventory().contains(is)){
            p.getInventory().all(is).forEach((key, item) -> p.getInventory().setItem(key, is));
        }
    }
}
