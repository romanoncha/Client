package com.client.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends Activity implements LocationListener {

    public String ip_address = "", port = "";
    public LocationManager locationManager;
    public static MapView map;
    public IMapController mapController;
    public static ArrayList<Drone> drones;
    public static double my_lat = 0.0, my_lon = 0.0;
    public static Drawable myCurrentLocationMarker, droneMarker;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        context = getApplicationContext();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setIndicator(getResources().getString(R.string.tab1));
        spec.setContent(R.id.tab1);
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setIndicator(getResources().getString(R.string.tab2));
        spec.setContent(R.id.tab2);
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag3");
        spec.setIndicator(getResources().getString(R.string.tab3));
        spec.setContent(R.id.tab3);
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        // start of drones initialization
        drones = new ArrayList<>();
        drones.add(new Drone(16, 47.003, 32.003, 100));
        drones.add(new Drone(17, 46.997, 31.997, 100));
        // end of drones initialization

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(15);
        // default point, opens on map when launched app
        GeoPoint startPoint = new GeoPoint(47.0068, 31.9854);
        mapController.setCenter(startPoint);

        myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.my_location);
        droneMarker = this.getResources().getDrawable(R.drawable.quadcopter);

        String list_of_objects[] = {"Drone 1", "Drone 2", "Drone 3", "Machine 1", "Machine 2", "Machine 3"};
        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_objects);
        list.setAdapter(adapter);

        readSettings();

        EditText ip_addr = (EditText) findViewById(R.id.ip_address);
        if (!ip_address.equals("")) ip_addr.setText(ip_address);

        EditText _port = (EditText) findViewById(R.id.port);
        if (!port.equals("")) _port.setText(port);


        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch (SecurityException ex) {
            Log.i("SecExc", ex.toString());
        }

        DroneAnimation animation = new DroneAnimation();
        animation.execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getApplicationContext(), "Lat="+location.getLatitude()+", Lon="+location.getLongitude(), Toast.LENGTH_LONG).show();

        my_lat = location.getLatitude();
        my_lon = location.getLongitude();

        /*GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setCenter(currentLocation);

        map.getOverlays().clear();

        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", currentLocation);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);

        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(myLocationOverlayItem);

        for (int i=0; i < drones.size(); i++) {
            OverlayItem droneOverlayItem = new OverlayItem("Here", "Current Position", new GeoPoint(drones.get(i).getLat(), drones.get(i).getLon()));
            Drawable droneMarker = this.getResources().getDrawable(R.drawable.quadcopter);
            droneOverlayItem.setMarker(droneMarker);
            items.add(droneOverlayItem);
        }

        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        ItemizedIconOverlay myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                    if (index == 0)
                        Toast.makeText(getApplicationContext(), "My location", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Drone ID"+drones.get(index - 1).getId(), Toast.LENGTH_LONG).show();
                    return true;
                }
                public boolean onItemLongPress(final int index, final OverlayItem item) {
                    if (index > 0) {
                        Intent myIntent = new Intent(MainActivity.this, DroneInfoActivity.class);
                        myIntent.putExtra("id", index-1); //Optional parameters
                        MainActivity.this.startActivity(myIntent);
                    }
                    return true;
                }
            }, resourceProxy);


        map.getOverlays().add(myLocationOverlay);*/
    }

    public static void addMarkersOnMap (ArrayList<OverlayItem> items) {
        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(context);

        ItemizedIconOverlay myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        if (index == 0)
                            Toast.makeText(context, "My location", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Drone ID"+drones.get(index - 1).getId(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        if (index > 0) {
                            Intent myIntent = new Intent(context, DroneInfoActivity.class);
                            myIntent.putExtra("id", index-1); //Optional parameters
                            context.startActivity(myIntent);
                        }
                        return true;
                    }
                }, resourceProxy);


        map.getOverlays().add(myLocationOverlay);
        map.invalidate();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public void onSaveClick(View v) {
        try {
            String data = "";
            EditText _ip_address = (EditText) findViewById(R.id.ip_address);
            ip_address = _ip_address.getText().toString();
            data += _ip_address.getText().toString() + "|";

            EditText _port = (EditText) findViewById(R.id.port);
            port = _port.getText().toString();
            data += _port.getText().toString() + "|";

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void readSettings() {
        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                ip_address = ret.split("\\|")[0];
                port = ret.split("\\|")[1];
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
