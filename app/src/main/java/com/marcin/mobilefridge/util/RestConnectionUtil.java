package com.marcin.mobilefridge.util;

import android.util.Base64;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marcin on 11.12.2016.
 */
public class RestConnectionUtil {

    public String GET(String urll,String username, String password) throws IOException {
        URL url;
        HttpURLConnection urlConnection = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            url = new URL(urll);

            String auth = username + ":" + password;
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));

            if(urlConnection.getResponseCode()!=200)
                throw new IOException();
            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();

            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                stringBuffer.append((char) data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return stringBuffer.toString();
    }
}
