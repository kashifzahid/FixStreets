package com.example.fixstreet.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fixstreet.Adaptor.incident_adaptor;
import com.example.fixstreet.Adaptor.incident_pictures_adaptor;
import com.example.fixstreet.BuildConfig;
import com.example.fixstreet.Dialog.AddPictureDialog;
import com.example.fixstreet.Dialog.AddPictureDialogInterface;
import com.example.fixstreet.Dialog.IncidentTypeDialog;
import com.example.fixstreet.MapsActivity;
import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;
import com.example.fixstreet.RecyclerViewClicked;
import com.example.fixstreet.Utils.RealPathUtil;
import com.example.fixstreet.Utils.checkitemcallback;
import com.example.fixstreet.Volley.Urls;
import com.example.fixstreet.Volley.VolleyPostCallBack;
import com.example.fixstreet.Volley.VolleyRequest;
import com.example.fixstreet.register_incident_2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterIncident extends AppCompatActivity implements AddPictureDialogInterface, RecyclerViewClicked {
    private static final String TAG = "RegisterIncident.java";
    private RecyclerView recyclerView;
    private incident_pictures_adaptor adaptor;
    private List<incident_type> modelClassList;
    private Button galleryButton, cameraButton, map;
    private static final int PICK_FROM_CAMERA = 1;
    private String status;
    private static final int PICK_FROM_GALLARY = 2;



    Uri outPutfileUri;
    private Dialog dialog;

    TextView throughfare, locality, street;


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText ed_comment;
    TextView tv_incident_type;
    String incident_id, incident_name;
    Double        lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident);

        throughfare = findViewById(R.id.throughfare);
        locality = findViewById(R.id.locality);
        street = findViewById(R.id.street);
        tv_incident_type=findViewById(R.id.tv_incident_type);

        ed_comment = findViewById(R.id.comments);

        modelClassList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_incident_pictures);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        adaptor = new incident_pictures_adaptor(this, modelClassList);
        recyclerView.setAdapter(adaptor);
        incident_id="";
        tv_incident_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTypes();
            }
        });
       // AddDataToRecyclerView();

    }

    IncidentTypeDialog incidentTypeDialog;
    public void OpenTypes() {
        incidentTypeDialog = IncidentTypeDialog.display(getSupportFragmentManager(),"one","null");

    }

    public void OpenImagePicker(View view) {
//        AddPictureDialog cdd=new AddPictureDialog();
//        cdd.show(getSupportFragmentManager(), "TAG");
        launchDismissDlg();

    }
    private void AddDataToRecyclerView() {
        modelClassList.add(new incident_type(Uri.parse("")));
    }

    @Override
    public void userSelectedApicture(Uri value) {
        Log.e(TAG, "userSelectedApicture: " + value);
        modelClassList.add(new incident_type(value));
        adaptor.setItems(modelClassList);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void userTakenApicture(Uri value) {
        Log.e(TAG, "userTakenApicture: " + value);
        modelClassList.add(new incident_type(value));
        adaptor.setItems(modelClassList);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void userCanceled() {
        Log.e(TAG, "Cancel: " + "canceled");
    }

    private void launchDismissDlg() {

        isStoragePermissionGranted();

        dialog = new Dialog(RegisterIncident.this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_picture_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Button choose_pic = (Button) dialog.findViewById(R.id.choose_pic);
        Button take_pic = (Button) dialog.findViewById(R.id.take_pic);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegisterIncident.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && isStoragePermissionGranted()) {
                    // Callback onRequestPermissionsResult interceptadona Activity MainActivity
                    ActivityCompat.requestPermissions(RegisterIncident.this, new String[]{Manifest.permission.CAMERA}, PICK_FROM_CAMERA);
                } else {
                    // permission has been granted, continue as usual
                    Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                    String format = s.format(new Date());
                    File file = new File(Environment.getExternalStorageDirectory(), "Stop"+format+".jpg");
                    outPutfileUri = FileProvider.getUriForFile(RegisterIncident.this, BuildConfig.APPLICATION_ID +".fileprovider", file);

                    Log.d(TAG, "onClick: "+MediaStore.EXTRA_OUTPUT);
                    Log.d(TAG, "onClick: "+outPutfileUri);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(captureIntent, PICK_FROM_CAMERA);
                    dialog.dismiss();
                }
            }
        });


        choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegisterIncident.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Callback onRequestPermissionsResult interceptadona Activity MainActivity
                    ActivityCompat.requestPermissions(RegisterIncident.this, new String[]{Manifest.permission.CAMERA}, PICK_FROM_CAMERA);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, PICK_FROM_GALLARY);
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("cG", "onActivityResult: "+requestCode );
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    //pic coming from camera
                    Bitmap bitmap=null;
                    try {
                        // Uri selectedImage = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                        // String selectedImage = RealPathUtil.getRealPath(getApplicationContext(), outPutfileUri);
                        // userTakenApicture(Uri.parse(selectedImage));
                        bitmap = MediaStore.Images.Media.getBitmap(RegisterIncident.this.getContentResolver(), outPutfileUri);
                        Log.e(TAG, "onActivityResult: " + outPutfileUri );
                        userTakenApicture(outPutfileUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case PICK_FROM_GALLARY:

                if (resultCode == Activity.RESULT_OK) {
                    //pick image from gallery
                    Uri selectedImage = data.getData();
                    userSelectedApicture(Uri.parse(String.valueOf(selectedImage)));
                    Log.e("cG", "onActivityResult: "+Uri.parse(String.valueOf(selectedImage)) );
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                }
                break;
            case 10:
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("result");
                    String throughfare = data.getStringExtra("throughfare");
                    String street = data.getStringExtra("street");
                    String locality = data.getStringExtra("locality");
                    lat = data.getDoubleExtra("lat", 0);
                    lng = data.getDoubleExtra("lng", 0);
                    Log.e(TAG, lat +" "+ lng);

                    this.throughfare.setText(throughfare);
                    this.street.setText(street);
                    this.locality.setText(locality);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            default:

        }
    }

    public void OpenMap(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, 10);
    }

    public void btn_submit(View view) {
        Intent i = new Intent(this, register_incident_2.class);
        i.putExtra("street", throughfare.getText().toString());
        i.putExtra("house_no", street.getText().toString());
        i.putExtra("municipality", locality.getText().toString());
        i.putExtra("incident_id", incident_id);
        Log.d(TAG, incident_id +" "+lat +" "+ lng);
        i.putExtra("incident_name", incident_name);
        i.putExtra("comment", ed_comment.getText().toString());
        i.putExtra("lat", lat);
        i.putExtra("lng", lng);
        ArrayList a = new ArrayList();
        for (int j=0; j<modelClassList.size(); j++){
            a.add(modelClassList.get(j).getUrl());
        }
        i.putStringArrayListExtra("images", a);
        startActivity(i);
    }

    public void back_pressed(View view) {
        finish();
    }

    public boolean isStoragePermissionGranted () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void getRecyclerViewItem(String str, String type) {
        Log.d(TAG, "getRecyclerViewItem: "+str);
        incident_id=str;
        incidentTypeDialog.dismissAllDialogs(getSupportFragmentManager());
        GetItemName(str,type);
    }





    public String checkitem(String id, String type, final checkitemcallback callback){

        status="";
        String screen="";
        if(type.equals("one")){
            screen="CheckCategoryById";

        }else if(type.equals("two")){
            screen="CheckSubCategoryById";
        }else if(type.equals("three")){
            screen="CheckSubCategoryItemById";

        }

        String uri= Urls.GetCategory+"?id="+id+"&screen="+screen;
        VolleyRequest.GetRequest(RegisterIncident.this, uri, new VolleyPostCallBack() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {
                Log.e(TAG, "OnSuccess: loaded"+jsonObject );
                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        String name = js.getString("status");
                        if(name.equals("0")){

                            callback.returnString("not exists");
                        }else {

                            callback.returnString("exists");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnFailure(String err) {
                status="not exists";

            }
        });
        return status;
    }

    private void GetItemName( String str,String type) {
        String screen="";
        if(type.equals("one")){
            screen="GetCategoryById";

        }else if(type.equals("two")){
            screen="GetSubCategoryById";
        }else if(type.equals("three")){
            screen="GetSubCategoryItemById";

        }


       String url= Urls.GetCategory+"?screen="+screen+"&id="+str;

        VolleyRequest.GetRequest(RegisterIncident.this, url, new VolleyPostCallBack() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {
                Log.e(TAG, "OnSuccess: "+jsonObject );

                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("result");
                    JSONObject js=jsonArray.getJSONObject(0);

                    tv_incident_type.setText(js.getString("str"));
                    incident_name = js.getString("str");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnFailure(String err) {

            }
        });
    }
}
