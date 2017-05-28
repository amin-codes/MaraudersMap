package me.AKZOMBIE74;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Amin on 3/26/2017.
 */
public class MapListener implements Listener {

    @EventHandler
    public void onit(MapInitializeEvent e){
        ItemStack is = MM.getInstance().createMap();
        if (e.getMap().getId() == is.getDurability()) {
            myRender.applyToMap(e.getMap());
        }
    }

}
