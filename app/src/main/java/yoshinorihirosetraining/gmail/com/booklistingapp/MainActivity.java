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
        implements LoaderManager.LoaderCallbacks<Book.Adapter> {

    @BindView(R.id.edittext) EditText editText;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.list) ListView listView;

    private final String TAG = "MainActivity";
    private State state;
    private String keyword;
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
        this.emptyText.setText(message);
        this.loadingIndicator.setVisibility(GONE);
        this.listView.setAdapter(Book.getEmptyAdapter(this));
    }

    private void setStateLoading() {
        this.state = State.Loading;
        this.emptyText.setText("");
        this.emptyText.setVisibility(GONE);
        this.loadingIndicator.setVisibility(View.VISIBLE);
        this.listView.setAdapter(Book.getEmptyAdapter(this));
    }

    private void setStateLoaded(Book.Adapter adapter) {
        this.state = State.Loaded;
        this.emptyText.setText("No Books Found.");
        this.loadingIndicator.setVisibility(GONE);
        this.listView.setAdapter(adapter);
        this.listView.setEmptyView(this.emptyText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setStateEmpty("No Results Here.");
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
            manager.getLoader(runningLoaderId).stopLoading();
            manager.destroyLoader(runningLoaderId);
        }
        runningLoaderId++;
        manager.initLoader(runningLoaderId, null, this).forceLoad();
    }

    /**
     * implementations of LoaderManager.LoaderCallBacks
     */

    @Override
    public Loader<Book.Adapter> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "onCreateLoader()");
        return new HttpLoader(this, this.keyword);
    }

    @Override
    public void onLoadFinished(Loader<Book.Adapter> loader, Book.Adapter adapter) {
        Log.v(TAG, "onLoadFinished(), id=" + runningLoaderId + ", keyword=" + ((HttpLoader)loader).getKeyword());
        if (loader.getId() != runningLoaderId) return;
        setStateLoaded(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Book.Adapter> loader) {
        Log.v(TAG, "onLoaderReset()");
        // NOT used
    }
}
