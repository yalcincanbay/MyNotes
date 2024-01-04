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

public class Projects extends AppCompatActivity {
    String ProjectsName, CurrentSituation;
    EditText Pname;
    EditText PCurrent;
    ListView listView;
    Button delete;

    private ArrayList<ProjectsAdapterItems> listnewsData = new ArrayList<>();
    private MyCustomAdapter myadapter;
    private DBManagerProjects dbManager;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        dbManager = new DBManagerProjects(this);

        Pname = findViewById(R.id.editTextText7);
        PCurrent = findViewById(R.id.editTextText8);
        listView = findViewById(R.id.lvlist3);
        delete=findViewById(R.id.buttonDeleteP);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDeleteMode) {
                    long selectedID = listnewsData.get(position).ID;
                    int rowCount = dbManager.delete(selectedID);

                    if (rowCount > 0) {
                        Toast.makeText(getApplicationContext(), "Project deleted", Toast.LENGTH_LONG).show();
                        btnLOAD(view);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to delete project", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Burada delete modu kapalıyken yapılmak istenen diğer işlemleri gerçekleştirebilirsiniz.
                }
            }
        });

        btnLOAD(null);
    }

    public void btnADD(View view) {
        ProjectsName = Pname.getText().toString();
        CurrentSituation = PCurrent.getText().toString();

        // Edittext'lerin boş olup olmadığını kontrol et
        if (ProjectsName.trim().isEmpty() || CurrentSituation.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Project name and Explanation cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DBManagerProjects.ColProjectsName, ProjectsName);
        values.put(DBManagerProjects.ColCurrentSitP, CurrentSituation);

        long id = dbManager.Insert(values);
        if (id > 0) {
            Toast.makeText(getApplicationContext(), "Data added and Project id:" + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot insert", Toast.LENGTH_LONG).show();
        }

        btnLOAD(view);

        // Edittext'leri temizle
        Pname.getText().clear();
        PCurrent.getText().clear();
    }


    @SuppressLint("Range")
    public void btnLOAD(View view) {
        listnewsData.clear();
        Cursor cursor = dbManager.query(null, null, null, DBManagerProjects.ColProjectsName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                listnewsData.add(new ProjectsAdapterItems(
                        (int) cursor.getLong(cursor.getColumnIndex("ID")), // ID'yi doğru bir şekilde alalım
                        cursor.getString(cursor.getColumnIndex(DBManagerProjects.ColProjectsName)),
                        cursor.getString(cursor.getColumnIndex(DBManagerProjects.ColCurrentSitP))
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }

        myadapter = new MyCustomAdapter(listnewsData);
        listView.setAdapter(myadapter);
    }


    public void btnDELETE(View view){
        isDeleteMode = !isDeleteMode;
        if(isDeleteMode){
            delete.setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Delete mode is activated", Toast.LENGTH_SHORT).show();
            delete.setText("DELETE MOD : ON");
        } else {
            delete.setBackgroundColor(Color.BLACK);
            Toast.makeText(getApplicationContext(), "Delete mode is deactivated", Toast.LENGTH_SHORT).show();
            delete.setText("DELETE MOD : OFF");
        }

        Log.d("DeleteMode", "Delete mode: " + isDeleteMode);
    }


    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<ProjectsAdapterItems> listnewsDataAdapter;

        public MyCustomAdapter(ArrayList<ProjectsAdapterItems> listnewsDataAdapter) {
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
            View myView = mInflater.inflate(R.layout.layout_projects, null);

            final ProjectsAdapterItems s = listnewsDataAdapter.get(position);

            TextView tvIDL = myView.findViewById(R.id.tvIDP);
            tvIDL.setText(String.valueOf(s.ID));

            TextView tvProjectsName = myView.findViewById(R.id.tvProjectsName);
            tvProjectsName.setText(s.UserName);

            TextView tvCurrentSitP = myView.findViewById(R.id.tvCurrentSitP);
            tvCurrentSitP.setText(s.Password);

            return myView;
        }
    }
}
