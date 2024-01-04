package com.example.yalcin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalcin.finalproject.R;

import java.util.ArrayList;

public class Books extends AppCompatActivity {

    String BookName,CurrentSituation;
    EditText Bname;
    EditText BCurrent;
    ListView listView;
    Button deleteButton;
    private ArrayList<BooksAdapterItems> listnewsData = new ArrayList<>();
    private MyCustomAdapter myadapter;
    private DBManagerBooks dbManager;
    private boolean isDeleteMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);


        dbManager = new DBManagerBooks(this);

        Bname = findViewById(R.id.editTextText3);
        BCurrent = findViewById(R.id.editTextText4);
        listView = findViewById(R.id.lvlist);
        deleteButton=findViewById(R.id.buttonDeleteB);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDeleteMode) {
                    long selectedID = listnewsData.get(position).ID;
                    int rowCount = dbManager.delete(selectedID);

                    if (rowCount > 0) {
                        Toast.makeText(getApplicationContext(), "Book deleted", Toast.LENGTH_LONG).show();
                        btnLOAD(view);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to delete Book", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Burada delete modu kapalıyken yapılmak istenen diğer işlemleri gerçekleştirebilirsiniz.
                }
            }
        });



        btnLOAD(null);
    }

    public void btnADD(View view) {
        BookName = Bname.getText().toString();
        CurrentSituation = BCurrent.getText().toString();

        // Edittext'lerin boş olup olmadığını kontrol et
        if (BookName.trim().isEmpty() || CurrentSituation.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Book name and notes are cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DBManagerBooks.ColBookName, BookName);
        values.put(DBManagerBooks.ColCurrentSitBO, CurrentSituation);

        long id = dbManager.Insert(values);
        if (id > 0) {
            Toast.makeText(getApplicationContext(), "Data added and Book id:" + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot insert", Toast.LENGTH_LONG).show();
        }

        btnLOAD(view);

        // Edittext'leri temizle
        Bname.getText().clear();
        BCurrent.getText().clear();
    }

    @SuppressLint("Range")
    public void btnLOAD(View view) {
        listnewsData.clear();
        Cursor cursor = dbManager.query(null, null, null, DBManagerBooks.ColBookName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                listnewsData.add(new BooksAdapterItems(
                        (int) cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex(DBManagerBooks.ColBookName)),
                        cursor.getString(cursor.getColumnIndex(DBManagerBooks.ColCurrentSitBO))
                ));
            } while (cursor.moveToNext());

            // Cursor'ı kapat
            cursor.close();
        }

        myadapter = new MyCustomAdapter(listnewsData);
        listView.setAdapter(myadapter);
    }

    public void btnDELETEB(View view){
        isDeleteMode = !isDeleteMode;
        if(isDeleteMode){
            deleteButton.setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Delete mode is activated", Toast.LENGTH_SHORT).show();
            deleteButton.setText("DELETE MOD : ON");
        } else {
            deleteButton.setBackgroundColor(Color.BLACK);
            Toast.makeText(getApplicationContext(), "Delete mode is deactivated", Toast.LENGTH_SHORT).show();
            deleteButton.setText("DELETE MOD : OFF");
        }

        Log.d("DeleteMode", "Delete mode: " + isDeleteMode);
    }


    public class MyCustomAdapter extends BaseAdapter {
        public ArrayList<BooksAdapterItems> listnewsDataAdapter;

        public MyCustomAdapter(ArrayList<BooksAdapterItems> listnewsDataAdapter) {
            this.listnewsDataAdapter = listnewsDataAdapter;
        }

        @Override
        public int getCount() {
            return listnewsDataAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_books, null);

            final BooksAdapterItems s = listnewsDataAdapter.get(position);

            TextView tvID = myView.findViewById(R.id.tvID);
            tvID.setText(String.valueOf(s.ID)); // ID'yi String'e çevirin

            TextView tvBookName = myView.findViewById(R.id.tvBookName);
            tvBookName.setText(s.UserName);

            TextView tvCurrentSit = myView.findViewById(R.id.tvCurrentSit);
            tvCurrentSit.setText(s.Password);

            return myView;
        }
    }


}