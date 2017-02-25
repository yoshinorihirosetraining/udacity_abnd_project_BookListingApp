package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Utility class for Http Downloading.
 */

public final class HttpUtil {

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes";

    public static String getRequestString(String keyword, int maxResults) {
        String res = "";
        try {
            res = API_URL + "?q=\""
                    + URLEncoder.encode(keyword, "UTF-8")
                    + "\"&maxResults=" + maxResults;
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        return res;
    }

    public static String download(String reqUrl) {
        final String TAG = "HttpUtil.download()";
        String response = null;
        URL url = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        BufferedReader reader = null;
        StringBuilder b = null;
        String line = null;
        boolean errorFlag = false;

        try {
            url = new URL(reqUrl);
        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException");
            return null;
        }

        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            Log.d(TAG, "url.openConnection failed");
            return null;
        }

        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            Log.d(TAG, "ProtocolException");
            return null;
        }

        try {
            in = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            Log.d(TAG, "new BufferedInputStream failed");
            return null;
        }

        reader = new BufferedReader(new InputStreamReader(in));
        b = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                b.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.d(TAG, "BufferedReader.readLine() failed");
            errorFlag = true;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.d(TAG, "BufferedReader.close() failed");
                errorFlag = true;
            }
        }
        if (errorFlag)
            return null;

        return b.toString();
    }

    public static Book.BookList parseJSON(String jsonString) {
        final String TAG = "HttpUtil.parseJSON()";
        Book.BookList lst = new Book.BookList();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray ary = json.getJSONArray("items");
            for (int i = 0; i < ary.length(); i++) {
                JSONObject item = ary.getJSONObject(i);
                JSONObject info = item.getJSONObject("volumeInfo");
                String title = info.getString("title");
                JSONArray authors = info.optJSONArray("authors");
                String author = "Unknown Author";
                if (authors != null) {
                    StringBuilder b = new StringBuilder();
                    for (int j = 0; j < authors.length(); j++) {
                        if (j != 0) b.append(", ");
                        b.append(authors.getString(j));
                    }
                    author = b.toString();
                }
                String publisher = info.optString("publisher", "Unknown Publisher");
                String date = info.optString("publishedDate", "Unknown Published Date");
                lst.add(title, author, publisher, date);
            }
        } catch (JSONException e) {
            Log.d(TAG, "unexpected JSON format\n" + e.getMessage());
            return null;
        }

        return lst;
    }
}
