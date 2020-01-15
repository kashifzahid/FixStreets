package com.example.fixstreet.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;


/**
 * Created by turbo on 2017/2/16.
 */

public class LanguageUtil {
    public static void changeLanguageType(Context context, Locale localelanguage) {
        Log.i("=======", "context = " + context + localelanguage);
//        Resources resources = context.getResources();
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        //localelanguage=Locale.setDefault(new Locale("ur"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(localelanguage);
        } else {
            config.locale = localelanguage;

            resources.updateConfiguration(config, dm);
        }
    }

    public static Locale getLanguageType(Context context) {
        Log.i("=======", "context = " + context);
//        Resources resources = context.getResources();
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }
}
