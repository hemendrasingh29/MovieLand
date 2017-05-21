package com.example.hemendra.movieland.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by hemendra on 3/17/2017.
 */
public class FetchData {
    public static String fetch(String movieCat) {
        HttpURLConnection con = null;
        String jsonData = "";

        try {
           URL url = new URL("https://api.themoviedb.org/3/movie/"+movieCat+"?api_key=9f896648c9f266a78f90c9e049723d60");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response = new StringBuffer();
            while ((inputline = br.readLine()) != null) {
                response.append(inputline);
                jsonData = response.toString();
            }
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonData;

    }
}
