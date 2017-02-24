package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView)findViewById(R.id.list);
        Book.Adapter adapter = Book.getDummyAdapter(this);
        list.setAdapter(adapter);
    }
}
