package me.AKZOMBIE74.utils;

import me.AKZOMBIE74.MM;

/**
 * Created by Amin on 1/18/2018.
 */
public class NMS {

    private static final String server_version = MM.getInstance().getServerVersion();
    private static final String craft_maprenderer = "org.bukkit.craftbukkit." + server_version + ".map.CraftMapRenderer";
    private static final String craft_map_view = "org.bukkit.craftbukkit." + server_version + ".map.CraftMapView";
    private static final String world_map = "net.minecraft.server." + server_version + ".WorldMap";
    private static final String minecraft_server = "net.minecraft.server." + server_version + ".MinecraftServer";
    private static final String world_server = "net.minecraft.server." + server_version + ".WorldServer";
    private static final String persistant_collection ="net.minecraft.server." + server_version + ".PersistentCollection";


    public static final Class<?> CraftMapRender = getClass(craft_maprenderer);
    public static final Class<?> CraftMapV = getClass(craft_map_view);
    public static final Class<?> WorldM = getClass(world_map);
    public static  final Class<?> MinecraftS = getClass(minecraft_server);
    public static final Class<?> WorldS = getClass(world_server);
    public static final Class<?> PersistantC = getClass(persistant_collection);

    private static Class<?> getClass(String className, boolean... inner)
    {
        Class<?> type = null;
        boolean in = inner.length > 0 ? inner[0] : false;
        try {
            if (in)
            {
                int last = className.lastIndexOf(".");
                className = className.substring(0, last) + "$" + className.substring(last+1);
            }
            type = Class.forName(className);
        } catch (ClassNotFoundException e) {}
        return type;
    }
}
