package com.client.client;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Рома on 21.01.2016.
 */
public class GetLogin extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            String url = "https://api.data-center.in.ua/v1/get/token/";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");

            String urlParameters = "email=roma.oncha@gmail.com&password=oPHzVp";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            //System.out.println("response = "+response.toString());

            JSONObject jObj = new JSONObject(response.toString());
            MainActivity.token = jObj.getString("data");
        }
        catch (IOException ioex) {
            System.out.println(ioex.toString());

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        // Если токен не пришел - пробуем снова
        if (MainActivity.token.equals("")) {
            GetLogin login = new GetLogin();
            login.execute();
        }
        else {
            GetDrone get_drone = new GetDrone();
            get_drone.execute();
        }
        System.out.println("token: "+MainActivity.token);
    }
}
