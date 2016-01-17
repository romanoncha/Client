package com.client.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Рома on 16.01.2016.
 */
public class DroneInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drone_info);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        setTitle("Drone ID"+MainActivity.drones.get(id).getId());

        //MainActivity.drones
        TextView drone_lat = (TextView) findViewById(R.id.drone_lat);
        TextView drone_lon = (TextView) findViewById(R.id.drone_lon);
        TextView drone_battery = (TextView) findViewById(R.id.drone_battery);

        drone_lat.setText(getResources().getString(R.string.drone_lat)+MainActivity.drones.get(id).getLat());
        drone_lon.setText(getResources().getString(R.string.drone_lon)+MainActivity.drones.get(id).getLon());
        drone_battery.setText(getResources().getString(R.string.drone_battery)+MainActivity.drones.get(id).getBattery());
    }

    public void onClose(View v) {
        this.finish();
    }

}
