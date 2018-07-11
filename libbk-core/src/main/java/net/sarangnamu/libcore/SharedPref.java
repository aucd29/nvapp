package net.sarangnamu.libcore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public class SharedPref {
    private static final String PREF_FILE_NAME = "bk-pref";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPref(@NonNull Context context) {
        prefs  = context.getSharedPreferences(PREF_FILE_NAME, Activity.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public SharedPref(@NonNull Context context, String fileName) {
        prefs  = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public void set(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            editor.putString(key, value);
        } else {
            editor.putString(key, Base64.encodeToString(value.getBytes(), Base64.DEFAULT));
        }

        editor.commit();
    }

    public void set(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void set(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public String get(String key, String defaultVal) {
        if (TextUtils.isEmpty(defaultVal)) {
            defaultVal = Base64.encodeToString(defaultVal.getBytes(), Base64.DEFAULT);
        }

        String loaded = prefs.getString(key, defaultVal);
        if (TextUtils.isEmpty(loaded)) {
            return loaded;
        }

        return new String(Base64.decode(loaded, Base64.DEFAULT));
    }

    public int get(String key, int defaultVal) {
        return prefs.getInt(key, defaultVal);
    }

    public boolean get(String key, boolean defaultVal) {
        return prefs.getBoolean(key, defaultVal);
    }
}