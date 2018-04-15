package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Amin on 3/25/2017.
 */
public class ChangeIt implements Listener {

    @EventHandler
    public void change(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        HashMap<UUID, Image> oldFaces = MM.getInstance().getData().getOldImages();

        if (oldFaces.containsKey(p.getUniqueId())) {
            //p.sendMessage("OLD FACE");
            MM.getInstance().getData().addToNew(p.getUniqueId(), oldFaces.get(p.getUniqueId()));
        } else {
            //p.sendMessage("NEW FACE");
            MM.getInstance().getData().addToNew(p.getUniqueId(), MM.getInstance().generateFace(name));
        }

        if (MM.getInstance().canUseMap(p)) {
            for (int i = 0; i < p.getInventory().getSize(); i++) {
                ItemStack itemStack = p.getInventory().getItem(i);
                if (MM.getInstance().isMaraudersMap(itemStack)) {
                    p.getInventory().setItem(i, MM.getInstance().createMap(p.getWorld(), itemStack.getAmount()).withScale(Bukkit.getMap(itemStack.getDurability()).getScale()));
                }
            }
        }

    }
}
