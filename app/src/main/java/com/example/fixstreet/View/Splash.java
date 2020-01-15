package com.example.fixstreet.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.fixstreet.MainActivity;
import com.example.fixstreet.R;
import com.example.fixstreet.Utils.CommonUtils;
import com.example.fixstreet.Utils.LanguageUtil;
import com.example.fixstreet.Utils.SharedPrefence;

import java.util.Locale;

public class Splash extends AppCompatActivity {
    VideoView videoView;
    SharedPrefence sharedPrefence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = findViewById(R.id.video_view);
        sharedPrefence = new SharedPrefence(this);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.abc);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();

        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))){
            sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "en");
            Locale.setDefault(new Locale("en"));
            LanguageUtil.changeLanguageType(this, new Locale(Locale.getDefault().getLanguage()));
        }else{
            Locale.setDefault(new Locale(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
            LanguageUtil.changeLanguageType(this, new Locale(Locale.getDefault().getLanguage()));
        }
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    }

