package com.example.mmclient.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ankit on 21-Sep-14.
 */
public class AuthPreferences {

    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL  = "Email";

    private SharedPreferences preferences;
    //private SharedPreferences.Editor edit= preferences.edit();

    public AuthPreferences(Context context) {
        preferences = context
                .getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public void setUser(String user) {
        preferences.edit().putString(KEY_USER, user).apply();
    }

    public void setToken(String token) {
        //Storing Access Token using SharedPreferences
        preferences.edit().putString(KEY_TOKEN, token).apply();    }

    public String getUser() {
        return preferences.getString(KEY_USER, null);
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public void setEmail(String avail_account_pos) {

        //Storing Data using SharedPreferences
        preferences.edit().putString(KEY_EMAIL, avail_account_pos).apply();
    }
}
