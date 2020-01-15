package com.example.fixstreet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixstreet.Utils.CommonUtils;
import com.example.fixstreet.Utils.LanguageUtil;
import com.example.fixstreet.Utils.SharedPrefence;
import com.example.fixstreet.View.RegisterIncident;

import java.util.Locale;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {

    SharedPrefence sharedPrefence;
    ImageView iv;
    RelativeLayout mContainerView;
    TextView text_detail, text_above_button;
    Button button_report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefence = new SharedPrefence(this );

        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))){
            sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "en");
            Locale.setDefault(new Locale("en"));
            LanguageUtil.changeLanguageType(this,new Locale(Locale.getDefault().getLanguage()));
        }else{
            Locale.setDefault(new Locale(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
            LanguageUtil.changeLanguageType(this,new Locale(Locale.getDefault().getLanguage()));
        }

        setContentView(R.layout.activity_main);

        text_detail = findViewById(R.id.text_detail);
        text_above_button = findViewById(R.id.text_above_button);
        button_report = findViewById(R.id.button_report);

        mContainerView = findViewById(R.id.container);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
//        Bitmap blurredBitmap = CommonUtils.blur( this, originalBitmap );
//        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))){
            Locale.setDefault(new Locale(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
            LanguageUtil.changeLanguageType(this,new Locale(Locale.getDefault().getLanguage()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))){
            Locale.setDefault(new Locale(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
            LanguageUtil.changeLanguageType(this,new Locale(Locale.getDefault().getLanguage()));
        }

    }

    public void OpenIncident(View view) {

        Intent i=new Intent(MainActivity.this, RegisterIncident.class);
        startActivity(i);

    }

    public void onClickEn(View view) {

        Resources res = this.getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

        Locale.setDefault(new Locale("en"));
        LanguageUtil.changeLanguageType(this, new Locale(Locale.getDefault().getLanguage()));
        sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "en");
        Toast.makeText(view.getContext(), "English is set.", Toast.LENGTH_SHORT).show();

        text_detail.setText("Report incidents in the Brussels Public spaces and participate in the improvement of your city !");
        text_detail.setTypeface(null, Typeface.ITALIC);
        text_above_button.setText("Please call the police in case of emergency");
        button_report.setText("REPORT AN INCIDENT");
        //recreate();
//        ViewGroup vg = findViewById (R.id.container);
//        vg.invalidate();
    }

    public void onClickFr(View view) {

        Resources res = this.getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("fr")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

        Locale.setDefault(new Locale("fr"));
        sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "fr");
        LanguageUtil.changeLanguageType(this, new Locale(Locale.getDefault().getLanguage()));
        Toast.makeText(view.getContext(), "French is set.", Toast.LENGTH_SHORT).show();
//        text_detail.setTextLocale(new Locale("fr"));
        text_detail.setText("Ne restez pas seul, nous pouvons vous aider !");
        text_detail.setTypeface(null, Typeface.ITALIC);
        text_above_button.setText("En cas de danger, Appelez la Police !");
        button_report.setText("Signalez un incident");
        //recreate();
//        ViewGroup vg = findViewById (R.id.container);
//        vg.invalidate();
    }
}
