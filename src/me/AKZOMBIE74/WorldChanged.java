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
        if (MM.getInstance().canUseMap(p)) {
            for (int i = 0; i < p.getInventory().getSize(); i++) {
                ItemStack is = p.getInventory().getItem(i);
                if (MM.getInstance().isMaraudersMap(is)) {
                    p.getInventory().setItem(i, MM.getInstance().createMap(p.getWorld(), is.getAmount()).withScale(Bukkit.getMap(is.getDurability()).getScale()));
                }
            }
        }
    }
}
