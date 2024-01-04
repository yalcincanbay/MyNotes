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

public class Languages extends AppCompatActivity {
    String LanguageName, CurrentSituation;
    EditText Lname;
    EditText LCurrent;
    ListView listView;
    Button deleteLanguage;

    private ArrayList<LanguagesAdapterItems> listnewsData = new ArrayList<>();
    private MyCustomAdapter myadapter;
    private DBManagerLanguages dbManager;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        deleteLanguage=findViewById(R.id.buttonDeleteLanguages);
        dbManager = new DBManagerLanguages(this);

        Lname = findViewById(R.id.editTextText5);
        LCurrent = findViewById(R.id.editTextText6);
        listView = findViewById(R.id.lvlist2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDeleteMode) {
                    long selectedID = listnewsData.get(position).ID;
                    int rowCount = dbManager.delete(selectedID);

                    if (rowCount > 0) {
                        Toast.makeText(getApplicationContext(), "Language deleted", Toast.LENGTH_LONG).show();
                        btnLOAD(view);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to delete Language", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Burada delete modu kapalıyken yapılmak istenen diğer işlemleri gerçekleştirebilirsiniz.
                }
            }
        });

        btnLOAD(null);
    }

    public void btnADD(View view) {


        LanguageName = Lname.getText().toString();
        CurrentSituation = LCurrent.getText().toString();
        // Edittext'lerin boş olup olmadığını kontrol et
        if (LanguageName.trim().isEmpty() || CurrentSituation.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Language and Situation cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DBManagerLanguages.ColLanguageName, LanguageName);
        values.put(DBManagerLanguages.ColCurrentSit, CurrentSituation);


        long id = dbManager.Insert(values);
        if (id > 0) {
            Toast.makeText(getApplicationContext(), "Data added and Language id:" + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot insert", Toast.LENGTH_LONG).show();
        }
        btnLOAD(view);
        // Edittext'leri temizle
        Lname.getText().clear();
        LCurrent.getText().clear();
    }


    @SuppressLint("Range")
    public void btnLOAD(View view) {
        listnewsData.clear();
        Cursor cursor = dbManager.query(null, null, null, DBManagerLanguages.ColLanguageName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                listnewsData.add(new LanguagesAdapterItems(
                        (int) cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex(DBManagerLanguages.ColLanguageName)),
                        cursor.getString(cursor.getColumnIndex(DBManagerLanguages.ColCurrentSit))
                ));
            } while (cursor.moveToNext());

            // Cursor'ı kapat
            cursor.close();
        }

        myadapter = new Languages.MyCustomAdapter(listnewsData);
        listView.setAdapter(myadapter);
    }

    public void btnDELETEL(View view){
        isDeleteMode = !isDeleteMode;
        if(isDeleteMode){
            deleteLanguage.setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Delete mode is activated", Toast.LENGTH_SHORT).show();
            deleteLanguage.setText("DELETE MOD : ON");
        } else {
            deleteLanguage.setBackgroundColor(Color.BLACK);
            Toast.makeText(getApplicationContext(), "Delete mode is deactivated", Toast.LENGTH_SHORT).show();
            deleteLanguage.setText("DELETE MOD : OFF");
        }

        Log.d("DeleteMode", "Delete mode: " + isDeleteMode);

    }


    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<LanguagesAdapterItems> listnewsDataAdapter;
        //GÜNCELLENMESİ GEREKEN ALAN
        public MyCustomAdapter(ArrayList<LanguagesAdapterItems> listnewsDataAdapter) {
            this.listnewsDataAdapter = listnewsDataAdapter;
        }//GÜNCELLENMESİ GEREKEN ALAN

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
            View myView = mInflater.inflate(R.layout.layout_languages, null);

            final LanguagesAdapterItems s = listnewsDataAdapter.get(position);//GÜNCELLENMESİ GEREKEN ALAN

            TextView tvIDL = myView.findViewById(R.id.tvIDL);
            tvIDL.setText(String.valueOf(s.ID));

            TextView tvLanguageName = myView.findViewById(R.id.tvLanguageName);
            tvLanguageName.setText(s.UserName);

            TextView tvCurrentSit = myView.findViewById(R.id.tvCurrentSitL);
            tvCurrentSit.setText(s.Password);

            return myView;
        }
    }
}