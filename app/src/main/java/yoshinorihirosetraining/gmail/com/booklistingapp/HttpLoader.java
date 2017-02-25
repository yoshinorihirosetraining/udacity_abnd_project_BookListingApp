package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Asynchronous Downloader class
 */

public class HttpLoader extends AsyncTaskLoader<Book.BookList> {

    private static final String TAG = "HttpLoader";
    private String keyword = null;

    public HttpLoader(Context context, String keyword) {
        super(context);
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public Book.BookList loadInBackground() {
        Log.v(TAG, "loadInBackground()");

        String requestUrl = HttpUtil.getRequestString(this.keyword, 40);
        if (requestUrl == null) {
            return Book.getErrorBookList("Internal Error.");
        }
        String json = HttpUtil.download(requestUrl);
        if (json == null) {
            return Book.getErrorBookList("Network Connection Error.");
        }
        Log.v("TAG", json);
        Book.BookList lst = HttpUtil.parseJSON(json);
        if (lst == null) {
            return Book.getErrorBookList("Unexpected Response from Server.");
        }
        return lst;
    }

    @Override
    protected void onStopLoading() {
        Log.v(TAG, "onStopLoading()");
        cancelLoad();
    }

    @Override
    public void onCanceled(Book.BookList lst) {
        Log.v(TAG, "onCanceled()");
        super.onCanceled(lst);
    }
}
