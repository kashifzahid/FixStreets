package com.example.fixstreet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fixstreet.View.RegisterIncident;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OpenIncident(View view) {
        Intent i=new Intent(MainActivity.this, RegisterIncident.class);
        startActivity(i);
    }
}
