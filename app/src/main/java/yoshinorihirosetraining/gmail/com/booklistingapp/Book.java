package yoshinorihirosetraining.gmail.com.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * stores the information of Google Books API.
 */

public class Book {
    private String title;
    private String author;
    private String publisher;
    private String date;

    public static Adapter getDummyAdapter(Context context) {
        BookList lst = new BookList();
        lst.add("1Q84", "Haruki Murakami, Jay Rubin, Philip Gabirel",
                "Vintage", "May 14, 2012");
        lst.add("The C++ Programming Language, 4th Edition", "Bjarne Stroustrup",
                "Addison-Wesley Professional", "May 19, 2013");
        lst.add("Star Wars: A Droid's Tale Soundstory",
                "John Whitman, Anthony Daniels, et al.",
                "Golden Books", "November 1997");
        return new Adapter(context, lst);
    }

    /**
     * List class of Books.
     * -- for avoiding many "add new"s.
     */
    public static class BookList extends ArrayList<Book> {
        public void add(String title, String author, String publisher, String date) {
            Book book = new Book();
            book.title = title;
            book.author = author;
            book.publisher = publisher;
            book.date = date;
            super.add(book);
        }
    }

    /**
     * Adapter class for ListView.
     */
    public static class Adapter extends ArrayAdapter<Book> {

        private Adapter(Context context, List<Book> books) {
            super(context, R.layout.list_item, books);
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = inflater.inflate(R.layout.list_item, parent, false);
            Book book = getItem(position);
            setText(item, R.id.list_item_title, book.title);
            setText(item, R.id.list_item_body,
                    book.author + " / " + book.publisher + ", " + book.date);
            return item;
        }

        private void setText(View view, int id, String str) {
            ((TextView)view.findViewById(id)).setText(str);
        }
    }
}
