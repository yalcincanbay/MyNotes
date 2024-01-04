package com.example.yalcin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yalcin.finalproject.R;

public class MainActivity extends AppCompatActivity {
    Button book,film,language,project;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        book=findViewById(R.id.ButtonBooks);
        film=findViewById(R.id.ButtonFilms);
        language=findViewById(R.id.ButtonLanguage);
        project=findViewById(R.id.buttonProjects);


    }

    public void btnBOOKS(View view){
        Intent goBOOKS= new Intent(MainActivity.this, Books.class);
        startActivity(goBOOKS);
    }

    public void btnFILMS(View view){
        Intent goFILMS= new Intent(MainActivity.this, Films.class);
        startActivity(goFILMS);
    }

    public void btnLANGUAGE(View view){
        Intent goLANGUAGE=new Intent(MainActivity.this, Languages.class);
        startActivity(goLANGUAGE);
    }

    public void btnPROJECTS(View view){
        Intent goPROJECTS=new Intent(MainActivity.this, Projects.class );
        startActivity(goPROJECTS);
    }
}