package com.hankhc.pixabayimagefinder;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hankchiu on 16/7/4.
 */
public class JsonHelper {
    private static final String TAG = JsonHelper.class.getSimpleName();

    private JsonHelper() {
    }

    public static JSONObject getJsonFromUrl(String urlToConn) {
        String result = "";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        // Download JSON data from URL
        try {
            URL url = new URL(urlToConn);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line)
                        .append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection: " + e.toString());
        } finally {
            closeQuietly(reader);

            if (conn != null) {
                conn.disconnect();
            }
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON data: " + e.toString());
        }

        return jsonObject;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
