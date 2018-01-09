# MaraudersMap:world_map::feet:
A Minecraft spigot/bukkit plugin that displays the location of players on the map as the face of their characters skin.
Players' faces are retrieved through [Minotar](http://minotar.net/).

# How To Install
1. Download the plugin from either [Spigot](https://www.spigotmc.org/resources/marauders-map.38505/) or [Bukkit](https://dev.bukkit.org/projects/marauders-map). There is no difference.
2. Put the plugin in your server's "plugins" folder.
3. You are done. Now you can just start the server and hopefully enjoy this plugin.

# Config.yml
[Default config.yml with tutorial](src/config.yml)

# Permissions
  + marauders.use - Allows the player to use the /mm command.
  + marauders.activate - Allows the player to use the map.

# Commands
  + /mm - Gives the sender a marauders map if they are a player.
  + /mm [player] - Gives the specified player a marauders map.
  + /mm <player_name> [CLOSEST|CLOSE|NORMAL|FAR|FARTHEST] - Gives the specified player a marauders map with a custom scale.

# Demonstration
![Marauders Map Plugin Demonstration](Demonstration/MaraudersMapDemonstration.gif)

# What info do I collect?
If you set `show-update-message` in the lang.yml file to `true`. Then whenever the plugin is checking for an update, it will ping my website to check for the update and upon that ping, the software I use shows me the IP of those who have pinged my website.

Your IP address will not be shared nor used by me or any third-party services.

# Servers Using This Plugin
  + [Govindas Limework](http://gmn.us.to/)
  + [Barbercraft](http://mc-barbercraft.com/)
  + [Dumbledore's Army](http://dumbledoresarmy.enjin.com/)
