package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.edittext) EditText editText;
    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.list) ListView listView;

    private State state;

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
        String keyword = editText.getText().toString().trim();
        if (keyword.isEmpty()) {
            setStateEmpty("Input Search Keyword.");
            return;
        }
        setStateLoading();

        /* ProgressBar is not appeared in case of sleeping Main Thread.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ; // void
        }

        setStateLoaded(Book.getDummyAdapter(this));
        */
    }
}
