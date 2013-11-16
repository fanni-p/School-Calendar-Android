package com.finalproject.schoolcalendar.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.finalproject.schoolcalendar.activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by Fani on 11/13/13.
 */
public class SessionManager {

    private static final int PRIVATE_MODE = 0;
    public static final String KEY_NAME = "Name";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String PREF_NAME = "SessionState";
    public static final String KEY_ACCESSTOKEN = "AccessToken";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SessionManager(Context context){
        this.mContext = context;
        this.mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.mEditor = mSharedPreferences.edit();
    }

    public void createLoginSession(String name, String accessToken){
        this.mEditor.putBoolean(IS_LOGIN, true);
        this.mEditor.putString(KEY_NAME, name);
        this.mEditor.putString(KEY_ACCESSTOKEN, accessToken);

        this.mEditor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.mContext.startActivity(intent);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<String, String>();
        userDetails.put(KEY_NAME, mSharedPreferences.getString(KEY_NAME, null));
        userDetails.put(KEY_ACCESSTOKEN, mSharedPreferences.getString(KEY_ACCESSTOKEN, null));

        return userDetails;
    }

    public void logoutUser(){
        this.mEditor.clear();
        this.mEditor.commit();

        Intent intent = new Intent(this.mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }

    private boolean isLoggedIn(){
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
