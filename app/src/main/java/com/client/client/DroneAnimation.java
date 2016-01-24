package com.client.client;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Рома on 17.01.2016.
 */
public class DroneAnimation extends AsyncTask<Void, Void, Void> {

    static int direction = 1;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        if (direction == 1) {
            direction = 0;
            for (int i=0; i<MainActivity.drones.size(); i++) {
                MainActivity.drones.get(i).updateCoords(MainActivity.drones.get(i).getLat()+0.001, MainActivity.drones.get(i).getLon()+0.001);
            }
        }
        else {
            direction = 1;
            for (int i=0; i<MainActivity.drones.size(); i++) {
                MainActivity.drones.get(i).updateCoords(MainActivity.drones.get(i).getLat()-0.001, MainActivity.drones.get(i).getLon()-0.001);
            }
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        MainActivity.map.getOverlays().clear();

        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        //if (MainActivity.my_lat != 0 && MainActivity.my_lon != 0) {
            OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", new GeoPoint(MainActivity.my_lat, MainActivity.my_lon));
            myLocationOverlayItem.setMarker(MainActivity.myCurrentLocationMarker);
            items.add(myLocationOverlayItem);
        //}

        for (int i=0; i < MainActivity.drones.size(); i++) {
            OverlayItem droneOverlayItem = new OverlayItem("Here", "Current Position", new GeoPoint(MainActivity.drones.get(i).getLat(), MainActivity.drones.get(i).getLon()));
            droneOverlayItem.setMarker(MainActivity.droneMarker);
            items.add(droneOverlayItem);
        }

        MainActivity.addMarkersOnMap(items);

        DroneAnimation animation = new DroneAnimation();
        animation.execute();
    }
}
