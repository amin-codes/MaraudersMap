package me.AKZOMBIE74.utils;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

/**
 * Created by Amin on 1/18/2018.
 */
public class MapMethods {

    public static int getScaleSize(MapView.Scale scale ) {
        if ( scale.equals( MapView.Scale.CLOSEST ) ) return 1;
        else if ( scale.equals( MapView.Scale.CLOSE ) ) return 2;
        else if ( scale.equals( MapView.Scale.NORMAL ) ) return 4;
        else if ( scale.equals( MapView.Scale.FAR ) ) return 8;
        else if ( scale.equals( MapView.Scale.FARTHEST ) ) return 16;
        return 0;
    }

    public static void clearCanvas(MapCanvas mapCanvas)
    {
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                mapCanvas.setPixel(i, j, (byte) 0);
            }
        }
    }

    //returns [x, y]
    public static int[] convertLocationToMapCoords(int centerX, int centerZ, Location l1, MapView.Scale scale)
    {
        int scale_size = getScaleSize(scale);
        int distanceX = ((l1.getBlockX() - centerX) / scale_size) + 63;
        int distanceZ = ((l1.getBlockZ() - centerZ) / scale_size) + 63;
        return new int[]{distanceX, distanceZ};
    }

    public static boolean isInRange(Location p1, Location p2, MapView.Scale scale)
    {
        int scale_size = getScaleSize(scale) * 128;
        return p1.distance(p2) <= scale_size;
    }
}
