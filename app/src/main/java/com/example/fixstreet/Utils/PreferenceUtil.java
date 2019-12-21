package com.example.fixstreet.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtil {
    private static final String PREFS_FILE_NAME = "firstTimeAskingPermission";

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }
    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true);
    }
}
