package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.util.Log;

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
}
