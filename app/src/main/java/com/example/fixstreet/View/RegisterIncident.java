package com.example.fixstreet.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.R;

public class RegisterIncident extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident);




    }


    public void OpenTypes(View view) {
        IncidentTypeDialog.display(getSupportFragmentManager(),"one","null");

    }
}
