package com.example.fixstreet.Adaptor;


import android.content.Context;
import android.content.Intent;
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


import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.JSONData.LoadJson;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;

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
                LoadJson loadJson = new LoadJson(context);
                if(type.equals("one")){
                    IncidentTypeDialog.display(fragmentManager,"two",modelClass.getId());
                }else if(type.equals("two")) {

                    String status = loadJson.CheckProduct(modelClass.getId());
                    if (status.equals("yes")) {
                        IncidentTypeDialog.display(fragmentManager, "three", modelClass.getId());
                    } else if (status.equals("not")) {
                        String name = loadJson.GetProductId(modelClass.getId(), "two");
                        Toast.makeText(context, "Id is" + modelClass.getId() + " name is " + name, Toast.LENGTH_SHORT).show();

                    }
                }else if(type.equals("three")){
                        String name= loadJson.GetProductId(modelClass.getId(),"three");
                        Toast.makeText(context, "Id is"+modelClass.getId()+" name is "+name, Toast.LENGTH_SHORT).show();
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
