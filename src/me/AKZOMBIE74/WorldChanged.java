package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Amin on 6/27/2017.
 */
public class WorldChanged implements Listener {

    @EventHandler
    public void change(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        ItemStack is = MM.getInstance().createMap();
        if (p.getInventory().contains(is)) {
            myRender.applyToMap(Bukkit.getMap(is.getDurability()));
        }
    }
}
