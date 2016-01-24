package com.client.client;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Рома on 25.01.2016.
 */
public class GetRoute extends AsyncTask<Void, Void, Void> {

    StringBuilder response;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        if (!MainActivity.token.equals(""))

            try {
                String url = "https://api.data-center.in.ua/v1/get/route/";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("Authorization", "Bearer " + MainActivity.token);

                /*String urlParameters = "{\"latitude\":\"47.0\",\"longitude\":\"32.0\"," +
                    "\"height\":\"5\", \"direction\":\"N\", \"battery\":\"99\", \"added\":\"2016-01-24 22:00:00\"," +
                    "\"drone_id\":\"1\"}";*/
                //String urlParameters = "latitude=47.0&longitude=32.0&height=5&direction=N&battery=99&added=2016-01-24 22:00:00&drone_id=1";

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                //wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println("response = " + response.toString());
            } catch (IOException ioex) {
                System.out.println(ioex.toString());

            }
        /*catch (JSONException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        Toast.makeText(MainActivity.context, "response: "+response, Toast.LENGTH_LONG).show();

    }
}