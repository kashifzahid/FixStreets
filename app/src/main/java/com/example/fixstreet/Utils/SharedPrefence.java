package com.example.fixstreet.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPrefence {
    private static final String MY_PREFS_NAME = "MY_PERFER";
    public static final String Logged = "Logged";
    public static final String INTERVALTIME = "Internaltime";
    public static final String ID = "id";
    public static final String TOKEN = "token";
    public static final String USERDETAILS = "userdetails";
    public static final String FAVLIST = "favList";
    public static final String SOSLIST = "SOSLIST";
    public static final String LANGUANGE = "LANGUANGE ";
    public static final String FCMTOKEN = "FCMTOKEN";
    public static final String LATITUDE= "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CURRENT_COUNTRY = "current_country";
    public static final String AR = "ar";
    public static final String LANGUAGES = "languages";
    public static final String EN = "en";
    public static final String ES = "es";
    public static final String ZH = "zh";
    public static final String JA = "ja";
    public static final String KO = "ko";
    public static final String PT = "pt";
    public static final String VI = "vi";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Context context;

    public SharedPrefence(Context context) {
        prefs = context.getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void savevalue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    public void saveSet(String key, Set value) {
        editor.putStringSet(key, value);
        editor.apply();
    }
    public String Getvalue(String key) {
        return prefs.getString(key, "");
    }
    public Set<String> getSet(String key) {
        return prefs.getStringSet(key, null);
    }
    public void saveCURRENT_COUNTRY(String value) {
        editor.putString(CURRENT_COUNTRY, value);
        editor.apply();
    }
    public String getCURRENT_COUNTRY() {
        return prefs.getString(CURRENT_COUNTRY, "");
    }


    public void clearAll(){
        editor.clear().commit();
    }
}