package com.example.fixstreet.Dialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixstreet.Adaptor.incident_adaptor;
import com.example.fixstreet.JSONData.LoadJson;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;
import com.example.fixstreet.Volley.Urls;
import com.example.fixstreet.Volley.VolleyPostCallBack;
import com.example.fixstreet.Volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncidentTypeDialog extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private incident_adaptor adaptor;
    private List<incident_type> modelClassList;
    private static String type;
    private static String name;
    String url,screen;
    private static FragmentManager fragmentManager;


    public static IncidentTypeDialog display(FragmentManager fragmentManagers, String types, String names) {
        IncidentTypeDialog exampleDialog = new IncidentTypeDialog();
        exampleDialog.show(fragmentManagers, "INCIDENT TYPES");
        type = types;
        name = names;
        fragmentManager = fragmentManagers;

        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_incident_types, container, false);
        modelClassList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.Recycler_incident_types);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        url="";
//        if (type.equals("one")) {
//            AddDataToRecyclerView();
//        } else if (type.equals("two")) {
//            AddDataToRecyclerViewDetail(name);
//        } else if (type.equals("three")) {
//            AddDataToRecyclerViewDetailProduct(name);
//        }
        AddDataToRecyclerViewFromServer(type,name);
        return view;
    }

    private void AddDataToRecyclerViewFromServer(final String type, final String name) {
        if(type.equals("one")){
            screen="GetCategory";

        }else if(type.equals("two")){
            screen="GetSubCategoryByCategory";
        }else if(type.equals("three")){
            screen="GetItemBySubCategory";

        }
        url=Urls.GetCategory+"?screen="+screen+"&id="+name;

        VolleyRequest.GetRequest(getActivity(), url, new VolleyPostCallBack() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(0);
                        String name = js.getString("Name");
                        String id = js.getString("id");
                        modelClassList.add(new incident_type( name,id, R.drawable.ic_location_on_black_24dp));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adaptor = new incident_adaptor(getActivity(), modelClassList, type, fragmentManager);
                recyclerView.setAdapter(adaptor);
            }

            @Override
            public void OnFailure(String err) {

            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void AddDataToRecyclerView() {
        LoadJson loadJson = new LoadJson(getActivity());

        try {
            JSONObject obj = new JSONObject(loadJson.loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");


            for (int i = 0; i < m_jArry.length(); i++) {
                String names = m_jArry.getJSONObject(i).getString("Category");
                String id = m_jArry.getJSONObject(i).getString("id");
                modelClassList.add(new incident_type(names, id, R.drawable.ic_location_on_black_24dp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        modelClassList.add(new incident_type("sewage","1",R.drawable.ic_location_on_black_24dp));
//        modelClassList.add(new incident_type("sewage2","2",R.drawable.ic_camera_alt_blue_24dp));
//        modelClassList.add(new incident_type("sewage3","3",R.drawable.ic_keyboard_arrow_down_black_24dp));
        adaptor = new incident_adaptor(getActivity(), modelClassList, type, fragmentManager);
        recyclerView.setAdapter(adaptor);
    }

    private void AddDataToRecyclerViewDetail(String name) {
        LoadJson loadJson = new LoadJson(getActivity());

        try {
            JSONObject obj = new JSONObject(loadJson.loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject objs = m_jArry.getJSONObject(i);
                String names = objs.getString("Category");
                String id = objs.getString("id");
                if (name.equals(id)) {
                    Log.d(TAG, "AddDataToRecyclerViewDetail:" + id);
                    JSONArray sub_m_jArry = objs.getJSONArray("SubCategory");
                    for (int j = 0; j < sub_m_jArry.length(); j++) {
                        JSONObject objs2 = sub_m_jArry.getJSONObject(j);
                        String sub_names = objs2.getString("Sub_name");
                        String sub_id = objs2.getString("id");
                        modelClassList.add(new incident_type(sub_names, sub_id, R.drawable.ic_location_on_black_24dp));

                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        modelClassList.add(new incident_type("Road","1",R.drawable.ic_location_on_black_24dp));
//        modelClassList.add(new incident_type("Truck","2",R.drawable.ic_camera_alt_blue_24dp));
//        modelClassList.add(new incident_type("Blocked","3",R.drawable.ic_keyboard_arrow_down_black_24dp));
        adaptor = new incident_adaptor(getActivity(), modelClassList, type, fragmentManager);
        recyclerView.setAdapter(adaptor);
    }

    private void AddDataToRecyclerViewDetailProduct(String name) {
        LoadJson loadJson = new LoadJson(getActivity());

        JSONArray pro_m_jArry = loadJson.GetProduct(name);
        if (pro_m_jArry.length() > 0) {
            for (int k = 0; k < pro_m_jArry.length(); k++) {
                JSONObject objs3 = null;
                try {
                    objs3 = pro_m_jArry.getJSONObject(k);
                    String pro_id = objs3.getString("id");
                    String pro_name = objs3.getString("name");
                    modelClassList.add(new incident_type(pro_name, pro_id, R.drawable.ic_camera_alt_blue_24dp));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            adaptor = new incident_adaptor(getActivity(), modelClassList, type, fragmentManager);
            recyclerView.setAdapter(adaptor);

        }
//        modelClassList.add(new incident_type("Road","1",R.drawable.ic_location_on_black_24dp));
//        modelClassList.add(new incident_type("Truck","2",R.drawable.ic_camera_alt_blue_24dp));
//        modelClassList.add(new incident_type("Blocked","3",R.drawable.ic_keyboard_arrow_down_black_24dp));


    else{
            Toast.makeText(getActivity(), "no aa", Toast.LENGTH_SHORT).show();
        }
    }
}
