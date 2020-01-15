package com.example.fixstreet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.example.fixstreet.Utils.RealPathUtil;
import com.example.fixstreet.Volley.Urls;
import com.example.fixstreet.Volley.VolleyPostCallBack;
import com.example.fixstreet.Volley.VolleyRequest;
import com.example.fixstreet.aws.Aws;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class register_incident_2 extends AppCompatActivity {

    private static final String TAG = "RegisterIncident2";
    private Switch swt_stay_anonymous;
    private EditText name, email, phone, dweller;
    private TextView txt_detail_anonymous;

    String street, house_no, muncipality, incident_id, incident_name, comment;
    Double lat, lng;
    ArrayList uris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident_2);

        Intent i = getIntent();
        street = i.getStringExtra("street");
        house_no = i.getStringExtra("house_no");
        muncipality = i.getStringExtra("municipality");
        incident_id = i.getStringExtra("incident_id");
        incident_name = i.getStringExtra("incident_name");
        comment = i.getStringExtra("comment");
        uris = i.getStringArrayListExtra("images");
        lat = i.getDoubleExtra("lat", 0);
        lng = i.getDoubleExtra("lng", 0);
        Log.e(TAG, "onCreate: "+ lat + " " + lng+" "+muncipality);
        swt_stay_anonymous = findViewById(R.id.switch_stay_anonymous);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dweller = findViewById(R.id.dweller);
        txt_detail_anonymous = findViewById(R.id.txt_detail_anonymous);
        Log.e(TAG, "onCreate: " + uris.size());


        swt_stay_anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Disable EditText
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: " + isChecked);
                    name.setHint("");
                    email.setHint("");
                    phone.setHint("");
                    dweller.setHint("");
                    // Disable EditText
                    name.setFocusable(false);
                    email.setFocusable(false);
                    phone.setFocusable(false);
                    dweller.setFocusable(false);

                    name.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    email.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    phone.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    dweller.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    txt_detail_anonymous.setVisibility(View.VISIBLE);
                } else {
                    // Enable EditText
                    name.setHint("Name *");
                    email.setHint("Email *");
                    phone.setHint("Phone *");
                    dweller.setHint("Dweller *");
                    // Enable EditText
                    name.setFocusable(true);
                    name.setFocusableInTouchMode(true);
                    email.setFocusable(true);
                    email.setFocusableInTouchMode(true);
                    phone.setFocusable(true);
                    phone.setFocusableInTouchMode(true);
                    dweller.setFocusable(true);
                    dweller.setFocusableInTouchMode(true);

                    name.setBackground(getDrawable(R.drawable.square_edit_text));
                    email.setBackground(getDrawable(R.drawable.square_edit_text));
                    phone.setBackground(getDrawable(R.drawable.square_edit_text));
                    dweller.setBackground(getDrawable(R.drawable.square_edit_text));

                    txt_detail_anonymous.setVisibility(View.GONE);
                }
            }
        });
    }

    public void back_pressed(View view) {
        finish();
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void Btn_Send(View view) {

        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String phone = this.phone.getText().toString();
        String dweller = this.dweller.getText().toString();


        // if(name.equals("")x)

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);


        JSONObject jsonObject = new JSONObject();
        JSONObject type = new JSONObject();
        JSONObject detail = new JSONObject();
        JSONArray items = new JSONArray();

        try {
            jsonObject.put("screen", "AddIncidentReport");
            // Log.e("tag", "getDashboard: "+id );
            jsonObject.put("id", incident_id);
            jsonObject.put("item", incident_name);
            jsonObject.put("region", muncipality);
            jsonObject.put("address", street);
            jsonObject.put("lat", lat);
            jsonObject.put("long", lng);
            jsonObject.put("status", Urls.NEW_INCIDENT_STATUS);


            type.put("by", swt_stay_anonymous.isChecked() ? "Anonymous" : "User");

            detail.put("name", swt_stay_anonymous.isChecked()?"":name);
            detail.put("email", swt_stay_anonymous.isChecked()?"":email);
            detail.put("number", swt_stay_anonymous.isChecked()?"":phone);
            detail.put("type", swt_stay_anonymous.isChecked()?"":dweller);
            type.put("detail", detail);
            jsonObject.put("type", type);
            if (uris.size() > 0) {
                for (int j = 0; j < uris.size(); j++) {
                    Log.d(TAG, "onCreate: " + uris.get(j));
                    File imgFile = null;
                    JSONObject itemDetails = new JSONObject();
                    imgFile = new File(RealPathUtil.getRealPath(this, Uri.parse(uris.get(j).toString())));
                    itemDetails.put("msg", imgFile.getName());
                    itemDetails.put("type", "image");
                    items.put(j, itemDetails);

                    Log.e(TAG, "onCreate: ju j " + imgFile.getName() + items.length());
                    uploadtos3(this, imgFile);
                }
                JSONObject itemDetails2 = new JSONObject();

                itemDetails2.put("msg", comment);
                itemDetails2.put("type", "comment");
                Log.e(TAG, "Btn_Send: size is " + uris.size());
                items.put(uris.size(), itemDetails2);
            } else {
                JSONObject itemDetails2 = new JSONObject();
                itemDetails2.put("msg", comment);
                itemDetails2.put("type", "comment");
                items.put(0, itemDetails2);
            }


            jsonObject.put("detail", items);
            Log.e(TAG, "Btn_Send: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyRequest.PostRequest(this, Urls.add_incident, jsonObject, new VolleyPostCallBack() {

            @Override
            public void OnSuccess(JSONObject jsonObject) {
                AlertDialog.Builder builder = new AlertDialog.Builder(register_incident_2.this);

                builder.setTitle("Confirmation");
                builder.setMessage("Thank you ! Your incident has been sent to the competent service. An E-mail containing the incident information has been sent to your address");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }

            @Override
            public void OnFailure(String err) {
                Log.e(TAG, "OnFailure: " + err);

            }
        });





//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 3000);
//
//        JSONObject jsonObject = new JSONObject();
//        JSONObject type = new JSONObject();
//        JSONObject detail = new JSONObject();
//        JSONArray items = new JSONArray();
//        JSONObject itemDetails = new JSONObject();
//        try {
//            jsonObject.put("screen", "AddIncidentReport");
//            // Log.e("tag", "getDashboard: "+id );
//            jsonObject.put("id", incident_id);
//            jsonObject.put("item", incident_name);
//            jsonObject.put("region", muncipality);
//            jsonObject.put("address", street);
//            jsonObject.put("lat", lat);
//            jsonObject.put("long", lng);
//            jsonObject.put("status", Urls.NEW_INCIDENT_STATUS);
//
//
//            type.put("by", swt_stay_anonymous.isChecked() ? "Anonymous" : "User");
//
//            detail.put("name", swt_stay_anonymous.isChecked() ? "" : name);
//            detail.put("email", swt_stay_anonymous.isChecked() ? "" : email);
//            detail.put("number", swt_stay_anonymous.isChecked() ? "" : phone);
//            detail.put("type", swt_stay_anonymous.isChecked() ? "" : dweller);
//            type.put("detail", detail);
//            jsonObject.put("type", type);
//
//            if (uris.size() > 0) {
//                for (int j = 0; j < uris.size(); j++) {
//                    Log.d(TAG, "onCreate: " + uris.get(j));
//                    File imgFile = null;
//                    try {
//                        imgFile = new File(getFilePath(this, Uri.parse(uris.get(j).toString())));
//                        itemDetails.put("msg", imgFile.getName());
//                        itemDetails.put("type", "image");
//                        items.put(j, itemDetails);
//                        Log.e(TAG, "onCreate: " + imgFile.getName());
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    Aws.uploadtos3(this, imgFile);
//                }
//                itemDetails.put("msg", comment);
//                itemDetails.put("type", "comment");
//                items.put(uris.size(), itemDetails);
//            }
//            else {
//                itemDetails.put("msg", comment);
//                itemDetails.put("type", "comment");
//                items.put(0, itemDetails);
//            }
//
//            jsonObject.put("detail", items);
//            Log.e(TAG, "Btn_Send: " + jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        VolleyRequest.PostRequest(this, Urls.add_incident, jsonObject, new VolleyPostCallBack() {
//
//            @Override
//            public void OnSuccess(JSONObject jsonObject) {
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(register_incident_2.this);
//
//                builder.setTitle("Confirmation");
//                builder.setMessage("Thank you ! Your incident has been sent to the competent service. An E-mail containing the incident information has been sent to your address");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//
//                        dialog.dismiss();
//                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
//
//
//
//            }
//
//            @Override
//            public void OnFailure(String err) {
//                Log.e(TAG, "OnFailure: " + err);
//
//            }
//        });
    }

    public static void uploadtos3 (final Context context, final File file) {

        if(file !=null){
            CognitoCachingCredentialsProvider credentialsProvider;
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-east-2:b1849c81-adb6-405c-bfde-edce60e601b1", // Identity Pool ID
                    Regions.US_EAST_2 // Region
            );

            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);


            TransferUtility transferUtility = new TransferUtility(s3, context);
            final TransferObserver observer = transferUtility.upload(
                    "alfaisaltehfeezimages",  //this is the bucket name on S3
                    file.getName(),
                    file,
                    CannedAccessControlList.PublicRead //to make the file public
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state == TransferState.COMPLETED) {
                    if (state.equals(TransferState.COMPLETED)) {
                        Log.e("onStateChanged", id + state.name());

                            String url = "https://alfaisaltehfeezimages.s3.amazonaws.com/" + observer.getKey();
                            Log.e("URL :,", url);
//we just need to share this File url with Api service request.
                        }
                    } else if (state.equals(TransferState.FAILED)) {
                        Toast.makeText(context,"Failed to upload", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    Log.e(TAG, "onProgressChanged: " + bytesCurrent + "Total: " + bytesTotal );
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, "onError: " + ex );
                }
            });
        }
    }

}
