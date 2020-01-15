package com.example.fixstreet.Adaptor;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fixstreet.Dialog.AddPictureDialogInterface;
import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.JSONData.LoadJson;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;
import com.example.fixstreet.RecyclerViewClicked;
import com.example.fixstreet.Utils.checkitemcallback;
import com.example.fixstreet.View.IncidentTypes;
import com.example.fixstreet.View.RegisterIncident;

import java.util.List;

public class incident_adaptor extends RecyclerView.Adapter<incident_adaptor.ViewHolder> {

    private Context context;
    private List<incident_type> modelClassList;
    private String type;
    private FragmentManager fragmentManager;


    public incident_adaptor(Context context, List<incident_type> modelClassList,String types,FragmentManager fragmentManagers) {
        this.context = context;
        this.modelClassList = modelClassList;
        this.type=types;
        this.fragmentManager=fragmentManagers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_recycler_incident_type,null);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final incident_type modelClass = modelClassList.get(i);
        viewHolder.name.setText(modelClass.getType());
        viewHolder.name.setCompoundDrawablesWithIntrinsicBounds(modelClass.getImg(),0,0,0);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, AttendanceDetail.class);
//                intent.putExtra("id", modelClass.getEmp_code());
//                context.startActivity(intent);
                //  Toast.makeText(context, "Id is"+modelClass.getId(), Toast.LENGTH_SHORT).show();

                if (type.equals("three")) {
                    ((RegisterIncident) context).getRecyclerViewItem(modelClass.getId(),type);
                } else {
                    String results = ((RegisterIncident) context).checkitem(modelClass.getId(), type, new checkitemcallback() {
                        @Override
                        public void returnString(String abc) {
                            if (abc.equals("exists")) {
                                if(type.equals("one")){
                                    type="two";

                                }else if(type.equals("two")){
                                    type="three";
                                }
                                IncidentTypeDialog.display(fragmentManager, type, modelClass.getId());
                            } else if (abc.equals("not exists")) {
                                ((RegisterIncident) context).getRecyclerViewItem(modelClass.getId(), type);

                            }
                        }
                    });
                    Log.e("tags", "onClick: "+results );


            }





                }





        });

    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


         TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text_type);


        }
    }
}
