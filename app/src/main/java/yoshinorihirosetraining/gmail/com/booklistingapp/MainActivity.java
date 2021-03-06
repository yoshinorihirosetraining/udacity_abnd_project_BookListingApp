package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.View.GONE;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Book.BookList> {

    private EditText editText = null;
    private TextView emptyText = null;
    private ProgressBar loadingIndicator = null;
    private ListView listView = null;

    private static final String TAG = "MainActivity";
    private static State state = State.Empty;
    private static String emptyMessage = "No Results Here.";
    private static Book.BookList bookList = new Book.BookList();
    private static String keyword;

    /**
     * represents States of MainActivity
     */
    private enum State {
        Empty,      // displaying EmptyView
        Loading,    // downloading from Google Books API, displaying ProgressBar
        Loaded      // displaying ListView
    }

    private void setStateEmpty(String message) {
        this.state = State.Empty;
        this.emptyMessage = message;
        this.bookList = null;
        updateUi();
    }

    private void setStateLoading() {
        this.state = State.Loading;
        this.emptyMessage = "";
        this.bookList = null;
        updateUi();
    }

    private void setStateLoaded(Book.BookList bookList) {
        this.state = State.Loaded;
        this.emptyMessage = "No Books Found.";
        this.bookList = bookList;
        updateUi();
    }

    private void updateUi() {
        this.emptyText.setText(this.emptyMessage);
        int loadingIndicatorVisibility = this.state == State.Loading ? View.VISIBLE : GONE;
        this.loadingIndicator.setVisibility(loadingIndicatorVisibility);
        if (this.bookList != null) {
            this.listView.setAdapter(new Book.Adapter(this, this.bookList));
        } else {
            this.listView.setAdapter(Book.getEmptyAdapter(this));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.edittext);
        emptyText = (TextView)findViewById(R.id.empty_text);
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
        listView = (ListView)findViewById(R.id.list);

        this.listView.setEmptyView(this.emptyText);

        // if loader exists, relink this Activity to loader.
        LoaderManager manager = getLoaderManager();
        if (manager.getLoader(0) != null) {
            manager.restartLoader(0, null, this);
        }

        // if loading, restart loading
        if (state == State.Loading) submit((Button)findViewById(R.id.button));
        updateUi();
    }

    public void submit(View view) {
        this.keyword = this.editText.getText().toString().trim();
        if (this.keyword.isEmpty()) {
            setStateEmpty("Input Search Keyword.");
            return;
        }
        setStateLoading();

        LoaderManager manager = getLoaderManager();
        Loader loader = manager.getLoader(0);
        if (loader != null) {
            loader.stopLoading();
            manager.restartLoader(0, null, this).forceLoad();
        } else {
            manager.initLoader(0, null, this).forceLoad();
        }
    }

    /**
     * implementations of LoaderManager.LoaderCallBacks
     */

    @Override
    public Loader<Book.BookList> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "onCreateLoader()");
        return new HttpLoader(this, this.keyword);
    }

    @Override
    public void onLoadFinished(Loader<Book.BookList> loader, Book.BookList bookList) {
        Log.v(TAG, "onLoadFinished(), keyword=" + ((HttpLoader)loader).getKeyword());
        Log.v(TAG, bookList.toString());

        if (bookList.isFailed()) {
            setStateEmpty(bookList.getErrorMessage());
            return;
        }

        setStateLoaded(bookList);
    }

    @Override
    public void onLoaderReset(Loader<Book.BookList> loader) {
        Log.v(TAG, "onLoaderReset()");
        // NOT used
    }
}
