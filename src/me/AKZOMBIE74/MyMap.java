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
                            sender.sendMessage(ChatColor.RED+"Player not found!");
                            return false;
                        }
                    }
                    scale = args.length > 1 ? MapView.Scale.valueOf(args[1]) : scale;
                    giveMap(p, scale);
                    if (p!=sender) sender.sendMessage(ChatColor.GREEN+"Successfully gave map to " + p.getName() + "!");
                    if (args.length > 1 && scale!=MapView.Scale.valueOf(args[1])) sender.sendMessage(ChatColor.RED+"That scale could not be found so the map given has the default scale");
                }
        } else {
            if (args.length > 0)
            {
                scale = args.length > 1 ? MapView.Scale.valueOf(args[1]) : scale;
                giveMap(Bukkit.getPlayer(args[0]), scale);
                if (args.length > 1 && scale!=MapView.Scale.valueOf(args[1])) sender.sendMessage(ChatColor.RED+"That scale could not be found so the map given has the default scale");
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
        p.sendMessage(ChatColor.GREEN + "Received Marauders Map");
    }
}
