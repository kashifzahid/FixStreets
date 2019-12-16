package com.example.fixstreet.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.fixstreet.Adaptor.incident_adaptor;
import com.example.fixstreet.Adaptor.incident_pictures_adaptor;
import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterIncident extends AppCompatActivity {
    private RecyclerView recyclerView;
    private incident_pictures_adaptor adaptor;
    private List<incident_type> modelClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident);
        modelClassList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_incident_pictures);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(recyclerLayoutManager);
       AddDataToRecyclerView();




    }


    public void OpenTypes(View view) {
        IncidentTypeDialog.display(getSupportFragmentManager(),"one","null");

    }

    public void OpenImagePicker(View view) {
        modelClassList.add(new incident_type("sewage"));
        adaptor.setItems(modelClassList);
        adaptor.notifyDataSetChanged();
    }
    private void AddDataToRecyclerView() {
        modelClassList.add(new incident_type("sewage"));

       adaptor = new incident_pictures_adaptor(this, modelClassList);
       recyclerView.setAdapter(adaptor);
    }

}
