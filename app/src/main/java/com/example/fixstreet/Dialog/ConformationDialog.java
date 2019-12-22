package com.example.fixstreet.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fixstreet.R;

import java.io.File;
import java.io.IOException;

public class ConformationDialog extends DialogFragment implements
        android.view.View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;


    Uri outPutfileUri;

    public Activity c;
    public Dialog d;
    public Button take_pic, choose_pic, no;
    AddPictureDialogInterface ad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_conformation_dialog, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        take_pic = view.findViewById(R.id.take_pic);
        choose_pic = view.findViewById(R.id.choose_pic);
        no = view.findViewById(R.id.btn_cancel);
        take_pic.setOnClickListener(this);
        choose_pic.setOnClickListener(this);
        no.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_pic:
                //Camera permission required for Marshmallow version
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Callback onRequestPermissionsResult interceptadona Activity MainActivity
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PICK_FROM_CAMERA);
                } else {
                    // permission has been granted, continue as usual
                    Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                    outPutfileUri = Uri.fromFile(file);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(captureIntent, PICK_FROM_CAMERA);
                    dismiss();
                }

                break;
            case R.id.choose_pic:
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Callback onRequestPermissionsResult interceptadona Activity MainActivity
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PICK_FROM_CAMERA);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, PICK_FROM_GALLARY);
                    //dismiss();
                }

                break;
            case R.id.btn_cancel:
                dismiss();
            default:
                break;
        }
        // dismiss();
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
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), outPutfileUri);
                        ((AddPictureDialogInterface) getContext()).userTakenApicture(outPutfileUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case PICK_FROM_GALLARY:

                if (resultCode == Activity.RESULT_OK) {
                    //pick image from gallery
                    Uri selectedImage = data.getData();
                    ((AddPictureDialogInterface) getContext()).userSelectedApicture(selectedImage);
                    Log.e("cG", "onActivityResult: "+selectedImage );
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                }
                break;
            default:

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
