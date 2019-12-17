package com.example.fixstreet.Adaptor;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.JSONData.LoadJson;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class incident_pictures_adaptor extends RecyclerView.Adapter<incident_pictures_adaptor.ViewHolder> {

    private Context context;
    private List<incident_type> modelClassList;




    public incident_pictures_adaptor(Context context, List<incident_type> modelClassList) {
        this.context = context;
        this.modelClassList = modelClassList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_register_incident_picture,null);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int position=i;

        final incident_type modelClass = modelClassList.get(i);
        Bitmap bitmap = null;
        Log.e("h", "onBindViewHolder:000 "+modelClass.getUrl() );
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), modelClass.getUrl());

        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.img.setImageBitmap(bitmap);
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelClassList.remove(position);
                notifyDataSetChanged();
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, AttendanceDetail.class);
//                intent.putExtra("id", modelClass.getEmp_code());
//                context.startActivity(intent);
              //  Toast.makeText(context, "Id is"+modelClass.getId(), Toast.LENGTH_SHORT).show();


                }





        });

    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }
    public void setItems(List<incident_type> persons) {
        this.modelClassList = persons;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


         CircleImageView img;
         ImageButton del;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.profile_image);
            del=itemView.findViewById(R.id.delete);



        }
    }
}
