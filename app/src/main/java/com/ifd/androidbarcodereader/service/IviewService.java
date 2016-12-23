package com.ifd.androidbarcodereader.service;

import android.os.StrictMode;
import android.util.Log;

import com.ifd.androidbarcodereader.utils.Constant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IviewService {
    public Boolean login(String username, String password){
        Boolean result = false;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://" + Constant.server_url +"/api/v1/login/" + username + "/" + password;
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // default is GET
            conn.setRequestMethod("GET");

            conn.setUseCaches(false);
            // act like a browser
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject reader = new JSONObject(response.toString());
            if(reader.getInt("code") == 1)
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, Object> search(String username, String password, String barcode){
        Map<String, Object> result = new HashMap<>();
        int responseCode = -1;
        try {
            String url = "http://"  + Constant.server_url + "/api/v1/search/" + username + "/" + password + "/" + barcode;
            String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // default is GET
            conn.setRequestMethod("GET");

            conn.setUseCaches(false);
            // act like a browser
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            result.put("result", new JSONObject(response.toString()));
            Log.i("DEBUG_", result.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        result.put("status_code", responseCode);
        return result;
    }
}
