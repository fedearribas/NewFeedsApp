package com.fedearribas.newfeedsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by fedea on 20/04/2017.
 */

public final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";

    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Article> extractArticles(String jsonString) {

        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        ArrayList<Article> articles = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jArrayItems = jsonObject.getJSONArray("items");
            for (int i = 0; i < jArrayItems.length(); i++) {
                JSONObject currentItem = jArrayItems.getJSONObject(i);
                JSONObject currentEnclosure = currentItem.getJSONObject("enclosure");
                articles.add(new Article(currentItem.getString("title"), currentItem.getString("author"), currentItem.getString("pubDate"), currentItem.getString("link"), currentEnclosure.getString("link")));
            }

            return articles;

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return null;
    }

    public static ArrayList<Article> fetchArticlesData(String jsonUrl) {
        URL url = createUrl(jsonUrl);
        String jsonString = null;
        try {
            jsonString = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractArticles(jsonString);
    }
}
