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

public class Films extends AppCompatActivity {
    String FilmName, CurrentSituation;
    EditText Fname;
    EditText FCurrent;
    ListView listView;
    Button deletefilms;

    private ArrayList<FilmsAdapterItems> listnewsData = new ArrayList<>();
    private MyCustomAdapter myadapter;
    private DBManagerFilms dbManager;

    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);
        deletefilms=findViewById(R.id.buttonDeleteFilms);

        dbManager = new DBManagerFilms(this);

        Fname = findViewById(R.id.editTextText4);
        FCurrent = findViewById(R.id.editTextText5);
        listView = findViewById(R.id.lvlist1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDeleteMode) {
                    long selectedID = listnewsData.get(position).ID;
                    int rowCount = dbManager.delete(selectedID);

                    if (rowCount > 0) {
                        Toast.makeText(getApplicationContext(), "Film deleted", Toast.LENGTH_LONG).show();
                        btnLOAD(view);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to delete Film", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Burada delete modu kapalıyken yapılmak istenen diğer işlemleri gerçekleştirebilirsiniz.
                }
            }
        });

        btnLOAD(null);
    }

    public void btnADD(View view) {
        FilmName = Fname.getText().toString();
        CurrentSituation = FCurrent.getText().toString();

        // Edittext'lerin boş olup olmadığını kontrol et
        if (FilmName.trim().isEmpty() || CurrentSituation.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Film/Series name and Current situation cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DBManagerFilms.ColFilmName, FilmName);
        values.put(DBManagerFilms.ColCurrentSit, CurrentSituation);

        long id = dbManager.Insert(values);
        if (id > 0) {
            Toast.makeText(getApplicationContext(), "Data added and film id:" + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot insert", Toast.LENGTH_LONG).show();
        }
        btnLOAD(view);

        // Edittext'leri temizle
        Fname.getText().clear();
        FCurrent.getText().clear();
    }


    @SuppressLint("Range")
    public void btnLOAD(View view) {
        listnewsData.clear();
        Cursor cursor = dbManager.query(null, null, null, DBManagerFilms.ColFilmName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                listnewsData.add(new FilmsAdapterItems(
                        (int) cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex(DBManagerFilms.ColFilmName)),
                        cursor.getString(cursor.getColumnIndex(DBManagerFilms.ColCurrentSit))
                ));
            } while (cursor.moveToNext());

            // Cursor'ı kapat
            cursor.close();
        }

        myadapter = new MyCustomAdapter(listnewsData);
        listView.setAdapter(myadapter); // listView değişkenini kullanarak setAdapter metodu çağrılıyor
    }


    public void btnDELETEFILMS(View view){
        isDeleteMode = !isDeleteMode;
        if(isDeleteMode){
            deletefilms.setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Delete mode is activated", Toast.LENGTH_SHORT).show();
            deletefilms.setText("DELETE MOD : ON");
        } else {
            deletefilms.setBackgroundColor(Color.BLACK);
            Toast.makeText(getApplicationContext(), "Delete mode is deactivated", Toast.LENGTH_SHORT).show();
            deletefilms.setText("DELETE MOD : OFF");
        }

        Log.d("DeleteMode", "Delete mode: " + isDeleteMode);

    }



    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<FilmsAdapterItems> listnewsDataAdapter;

        public MyCustomAdapter(ArrayList<FilmsAdapterItems> listnewsDataAdapter) {
            this.listnewsDataAdapter = listnewsDataAdapter;
        }

        @Override
        public int getCount() {
            return listnewsDataAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return listnewsDataAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_films, null);

            final FilmsAdapterItems s = listnewsDataAdapter.get(position);

            TextView tvID = myView.findViewById(R.id.tvIDF);
            tvID.setText(String.valueOf(s.ID));

            TextView tvFilmName = myView.findViewById(R.id.tvFilmName);
            tvFilmName.setText(s.UserName);

            TextView tvCurrentSit = myView.findViewById(R.id.tvCurrentSitF);
            tvCurrentSit.setText(s.Password);

            return myView;
        }
    }
}
