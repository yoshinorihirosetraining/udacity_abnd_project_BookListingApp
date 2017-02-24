package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Asynchronous Downloader class
 */

public class HttpLoader extends AsyncTaskLoader<Book.Adapter> {

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
    public Book.Adapter loadInBackground() {
        Log.v(TAG, "loadInBackground()");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            ; // void
        }

        return Book.getDummyAdapter(getContext());
    }

    @Override
    protected void onStopLoading() {
        Log.v(TAG, "onStopLoading()");
        cancelLoad();
    }

    @Override
    public void onCanceled(Book.Adapter adapter) {
        Log.v(TAG, "onCanceled()");
        super.onCanceled(adapter);
    }
}
