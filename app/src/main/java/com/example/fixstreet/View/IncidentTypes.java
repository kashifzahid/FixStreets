package com.example.fixstreet.View;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fixstreet.Adaptor.incident_adaptor;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncidentTypes extends Fragment {
    private RecyclerView recyclerView;
    private incident_adaptor adaptor;
    private List<incident_type> modelClassList;




    public IncidentTypes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_incident_types, container, false);
        modelClassList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.Recycler_incident_types);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        AddDataToRecyclerView();
        return view;
    }

    private void AddDataToRecyclerView() {
        modelClassList.add(new incident_type("sewage","1",R.drawable.ic_location_on_black_24dp));
        modelClassList.add(new incident_type("sewage2","2",R.drawable.ic_camera_alt_blue_24dp));
        modelClassList.add(new incident_type("sewage3","3",R.drawable.ic_keyboard_arrow_down_black_24dp));
//        adaptor = new incident_adaptor(getActivity(), modelClassList,"n");
//        recyclerView.setAdapter(adaptor);
    }

}
