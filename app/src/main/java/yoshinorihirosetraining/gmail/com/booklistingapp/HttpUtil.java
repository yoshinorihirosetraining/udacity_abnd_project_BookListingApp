package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.content.ContentValues;

import java.io.UnsupportedEncodingException;
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
}
