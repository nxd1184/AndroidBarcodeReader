package com.ifd.androidbarcodereader.service;

import android.os.StrictMode;
import android.util.Log;

import com.ifd.androidbarcodereader.utils.Constant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    public Map<String, Object> getPdfDocument(String username, String password, String archiveName, String fileName, int page, boolean getNumOfPage){
        Map<String, Object> result = new HashMap<>();
        int responseCode = -1;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://"  + Constant.server_url + "/api/v1/pdf-content/" + username + "/" + password + "/" + archiveName+"/" + fileName + "/" +page + "/" + getNumOfPage;
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
    public Map<String, Object> getSignatureLocation(String username, String password, String archiveName){
        Map<String, Object> result = new HashMap<>();
        int responseCode = -1;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://"  + Constant.server_url + "/api/v1/signature-location/" + username + "/" + password + "/" + archiveName;
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
    public Map<String, Object> savePdfDocument(String username, String password, String archiveName, String fileName, int page, String base64, int left, int top, int width, int height){
        Map<String, Object> result = new HashMap<>();
        int responseCode = -1;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://"  + Constant.server_url + "/api/v1/save-signature";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            // act like a browser
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("archive", archiveName);
            jsonObject.put("fileName", fileName);
            jsonObject.put("page", page);
            jsonObject.put("left", left);
            jsonObject.put("top", top);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
            jsonObject.put("base64", base64);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url + "  query: " + jsonObject.toString());
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
    public Map<String, Object> saveSignLocation(String username, String password, String archiveName, int xLoc, int yLoc, int width, int height, String color, Double lineWidth, int canvasHeight, int canvasWidth){
        Map<String, Object> result = new HashMap<>();
        int responseCode = -1;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://"  + Constant.server_url + "/api/v1/save-signature-location";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            // act like a browser
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("archive", archiveName);
            jsonObject.put("xLoc", xLoc);
            jsonObject.put("yLoc", yLoc);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
            jsonObject.put("lineWidth", lineWidth);
            jsonObject.put("color", color);
            jsonObject.put("canvasHeight", canvasHeight);
            jsonObject.put("canvasWidth", canvasWidth);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url + "  query: " + jsonObject.toString());
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
    public String getAuthentication(String username, String password){
        String result = "USER";
        int responseCode = -1;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = "http://"  + Constant.server_url + "/api/v1/authorization/" + username + "/" + password;
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
            JSONObject resultJson = new JSONObject(response.toString());
            if (resultJson.has("role")){
                result = resultJson.getString("role").toString();
            }
            Log.i("DEBUG_", result.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
