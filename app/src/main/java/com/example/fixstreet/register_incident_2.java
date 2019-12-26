package com.example.fixstreet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fixstreet.aws.Aws;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class register_incident_2 extends AppCompatActivity {

    private static final String TAG = "RegisterIncident2";
    private Switch swt_stay_anonymous;
    private EditText name, email, phone, dweller;
    private TextView txt_detail_anonymous;

    String street, house_no, muncipality, incident_type, comment;
    ArrayList uris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident_2);

        Intent i = getIntent();
        street = i.getStringExtra("street");
        house_no = i.getStringExtra("house_no");
        muncipality = i.getStringExtra("muncipality");
        incident_type = i.getStringExtra("incident_type");
        comment = i.getStringExtra("comment");
        uris = i.getStringArrayListExtra("images");

        swt_stay_anonymous = findViewById(R.id.switch_stay_anonymous);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dweller = findViewById(R.id.dweller);
        txt_detail_anonymous = findViewById(R.id.txt_detail_anonymous);

        for (int j=0; j<uris.size(); j++){
            Log.d(TAG, "onCreate: "+uris.get(j));
            File imgFile = null;
            try {
                imgFile = new File(getFilePath(this, Uri.parse(uris.get(j).toString())));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Aws.uploadtos3(this, imgFile);
        }

        swt_stay_anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Disable EditText
                if(isChecked) {
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
                }else {
                    // Enable EditText
                    name.setHint("Name *");
                    email.setHint("Email *");
                    phone.setHint("Phone *");
                    dweller.setHint("Dweller *");
                    // Enable EditText
                    name.setFocusable(true);
                    email.setFocusable(true);
                    phone.setFocusable(true);
                    dweller.setFocusable(true);

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

    public void Btn_Send(){
        if(swt_stay_anonymous.isChecked()){

        }else{
            String name = this.name.getText().toString();
            String email = this.email.getText().toString();
            String phone = this.phone.getText().toString();
            String dweller = this.dweller.getText().toString();
            // if(name.equals("")x)
        }
    }

}
