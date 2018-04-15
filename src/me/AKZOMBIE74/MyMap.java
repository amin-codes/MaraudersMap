package me.AKZOMBIE74;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

/**
 * Created by Amin on 3/25/2017.
 */
public class MyMap implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        MapView.Scale scale = MM.getInstance().scale();
        if (sender instanceof Player) {
            Player p = (Player) sender;
                if (p.hasPermission("marauders.use")) {
                    if (args.length > 0)
                    {
                        p = Bukkit.getPlayer(args[0]);
                        if (p==null)
                        {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online or does not exist.");
                            return false;
                        }
                    }
                    try {
                        scale = args.length > 1 ? MapView.Scale.valueOf(args[1].toUpperCase()) : scale;
                    } catch (IllegalArgumentException e)
                    {
                        sender.sendMessage(ChatColor.RED + "That is not valid scale so the map will be given the default scale");
                    }
                    giveMap(p, scale);
                    if (p!=sender) sender.sendMessage(ChatColor.GREEN + "Successfully gave map to " + p.getName() + "!");
                }
        } else {
            if (args.length > 0)
            {
                Player p = Bukkit.getPlayer(args[0]);

                if (p==null)
                {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not online or does not exist.");
                    return false;
                }
                try {
                    scale = args.length > 1 ? MapView.Scale.valueOf(args[1].toUpperCase()) : scale;
                } catch (IllegalArgumentException e)
                {
                    sender.sendMessage(ChatColor.RED + "That is not valid scale so the map will be given the default scale");
                }
                giveMap(p, scale);
            }
            else sender.sendMessage(ChatColor.RED + "Please specify a player!");
        }
        return false;
    }

    public void giveMap(Player p, MapView.Scale... scale)
    {
        MapView.Scale scale1 = scale.length > 0? scale[0] : MM.getInstance().scale();
        ItemStack is = MM.getInstance().createMap(p.getWorld()).withScale(scale1);

        p.getInventory().addItem(is);
        p.sendMessage(ChatColor.GREEN + "Received " + ChatColor.stripColor(MM.getInstance().getMapName()));
    }
}
