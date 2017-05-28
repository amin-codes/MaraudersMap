package me.AKZOMBIE74;

import org.bukkit.entity.Player;
import org.bukkit.map.*;


/**
 * Created by Amin on 3/25/2017.
 */
public class myRender extends MapRenderer{

    myRender(){
        super(true);
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        mapView.setScale(MM.getInstance().scale());
        mapView.setWorld(player.getWorld());
        Integer playerX = (int)player.getLocation().getBlockX();
        Integer playerZ = (int)player.getLocation().getBlockZ();

        Integer realImageSize = MM.getInstance().imageSize();
        Integer imageSize = realImageSize/2;

        if (playerX != 0) {
            if (playerZ != 0) {
                mapView.setCenterX(playerX);
                mapView.setCenterZ(playerZ);
            } else {
                mapView.setCenterX(playerX);
                mapView.setCenterZ(playerZ+1);
            }
        } else {
            mapView.setCenterX(playerX+1);
            if (playerZ!=0) {
                mapView.setCenterZ(playerZ);
            } else {
                mapView.setCenterZ(playerZ+1);
            }
        }


        Integer centerX = mapView.getCenterX();
        Integer centerZ = mapView.getCenterZ();
        MM.getInstance().getData().getImages().forEach((key, image) -> {
            if (key.getWorld().equals(player.getWorld())) {
                Integer distanceX = (int) (63.5 * key.getLocation().getBlockX())/centerX;
                Integer distanceZ = (int) (63.5 * key.getLocation().getBlockZ())/centerZ;
                Integer totalDistance = (int) key.getLocation().distance(player.getLocation());


                mapCanvas.drawImage((int) ((distanceX - imageSize)), (int) ((distanceZ - imageSize)), image);
            }
        });
    }

    @Override
    public void initialize(MapView map) {
        super.initialize(map);
    }

    static void applyToMap(MapView map) {
        if(map != null){
            for (MapRenderer renderer : map.getRenderers()) {
                map.removeRenderer(renderer);
            }
            map.addRenderer(MM.getInstance().getData().getDefaultMap());
            map.addRenderer(new myRender());
        }
    }
}
