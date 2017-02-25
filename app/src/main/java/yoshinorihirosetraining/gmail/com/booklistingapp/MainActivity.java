package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Book.BookList> {

    @BindView(R.id.edittext) EditText editText;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.list) ListView listView;

    private final String TAG = "MainActivity";
    private static State state = State.Empty;
    private static String emptyMessage = "No Results Here.";
    private static Book.BookList bookList = new Book.BookList();
    private static String keyword;
    private static int runningLoaderId = 0;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.listView.setEmptyView(this.emptyText);
        updateUi();
    }

    @OnClick(R.id.button)
    public void submit() {
        this.keyword = this.editText.getText().toString().trim();
        if (this.keyword.isEmpty()) {
            setStateEmpty("Input Search Keyword.");
            return;
        }
        setStateLoading();

        LoaderManager manager = getLoaderManager();
        if (runningLoaderId != 0) {
            Loader<Book.BookList> loader = manager.getLoader(runningLoaderId);
            if (loader != null) {
                manager.getLoader(runningLoaderId).stopLoading();
                manager.destroyLoader(runningLoaderId);
            }
        }
        runningLoaderId++;
        manager.initLoader(runningLoaderId, null, this).forceLoad();
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
        Log.v(TAG, "onLoadFinished(), id=" + runningLoaderId + ", keyword=" + ((HttpLoader)loader).getKeyword());
        Log.v(TAG, bookList.toString());

        if (loader.getId() != runningLoaderId) return;

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
