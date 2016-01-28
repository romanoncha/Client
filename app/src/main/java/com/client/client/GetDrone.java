package com.client.client;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Рома on 28.01.2016.
 */
public class GetDrone extends AsyncTask<Void, Void, Void> {

    StringBuilder response;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        if (!MainActivity.token.equals(""))

            try {
                String url = "https://api.data-center.in.ua/v1/get/drone/";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                //add request header
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("Authorization", "Bearer " + MainActivity.token);


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println("response = " + response.toString());

                JSONObject jObj = new JSONObject(response.toString());

                JSONArray drones = jObj.getJSONArray("data");

                MainActivity.drones.clear();

                for (int i=0; i<drones.length(); i++) {

                    JSONObject drone = drones.getJSONObject(i);

                    int id = drone.getInt("id");
                    String name = drone.getString("name");
                    double lat = Double.parseDouble(drone.getString("latitude"));
                    double lon = Double.parseDouble(drone.getString("longitude"));
                    int battery = drone.getInt("battery");

                    MainActivity.drones.add(new Drone(id, name, lat, lon, battery));
                }
            }
            catch (IOException ioex) {
                System.out.println(ioex.toString());

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        /*catch (JSONException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}
