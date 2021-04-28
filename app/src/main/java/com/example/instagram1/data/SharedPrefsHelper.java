package com.example.instagram1.data;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {

    public static final String MY_PREFS = "MY_PREFS";

    public static final String USERNAME = "EMAIL";
    public static final String ISLOGGED = "ISLOGGED";

    SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public String getUsername() {
        return mSharedPreferences.getString("USERNAME", "");
    }

    public void setUsername(String username) {
        mSharedPreferences.edit().putString("USERNAME", username).apply();
    }


    public boolean getIslogged() {
        return mSharedPreferences.getBoolean("ISLOGGED", false);
    }

    public void setIslogged(boolean islogged) {
        mSharedPreferences.edit().putBoolean("ISLOGGED", islogged).apply();
    }


    public String getCustom(String key) {
        return mSharedPreferences.getString(key,"");
    }

    public void setCustom(String key , String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }



}