package com.example.fixstreet.aws;

import android.content.Context;
import android.util.Log;
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

import java.io.File;

public class Aws {
    private static String TAG = "Aws";
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
                    if (state.equals(TransferState.COMPLETED)) {
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
