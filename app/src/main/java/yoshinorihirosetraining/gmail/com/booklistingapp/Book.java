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

    public String toString() {
        return "Book";
    }

    public static Adapter getEmptyAdapter(Context context) {
        return new Adapter(context, new BookList());
    }

    public static BookList getErrorBookList(String message) {
        BookList lst = new BookList();
        lst.failed = true;
        lst.errorMessage = message;
        return lst;
    }

    /**
     * List class of Books.
     * -- for avoiding many "add new"s.
     */
    public static class BookList extends ArrayList<Book> {
        private boolean failed = false;
        private String errorMessage = "";

        public void add(String title, String author, String publisher, String date) {
            Book book = new Book();
            book.title = title;
            book.author = author;
            book.publisher = publisher;
            book.date = date;
            super.add(book);
        }

        public boolean isFailed() {
            return failed;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("Book.BookList { isFailed=");
            b.append(failed ? "true" : "false");
            b.append(", errorMessage=\"");
            b.append(errorMessage);
            b.append("\", data=[");
            for (int i = 0; i < this.size(); i++) {
                b.append(this.get(i).toString());
                if (i != this.size() - 1) b.append(", ");
            }
            b.append("] }");
            return b.toString();
        }
    }

    /**
     * Adapter class for ListView.
     */
    public static class Adapter extends ArrayAdapter<Book> {

        public Adapter(Context context, List<Book> books) {
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
