package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

/**
 * Created by Amin on 3/26/2017.
 */
public class preProcess implements Listener {

    @EventHandler
    public void aVoid(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (p.hasPermission("marauders.use")) {
            ItemStack is = MM.getInstance().createMap();
            if (e.getMessage().startsWith("/mm")) {
                MapView mapView = Bukkit.getMap(is.getDurability());
                myRender.applyToMap(mapView);
                p.getInventory().addItem(is);
            }
        }
    }
}
