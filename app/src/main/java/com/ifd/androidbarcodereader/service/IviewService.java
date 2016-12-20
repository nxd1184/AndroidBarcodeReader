package com.ifd.androidbarcodereader.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IviewService {
    public Boolean login(String username, String password){
        Boolean result = false;
        try {
            String url = "http://192.168.126.1:8080/iview/api/v1/login/" + username + "/" + password;
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
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String url = "http://192.168.126.1:8080/iview/api/v1/search/" + username + "/" + password + "/" + barcode;
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
