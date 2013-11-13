package com.finalproject.schoolcalendar.models;

/**
 * Created by Fani on 11/10/13.
 */

public class UserModel {
    private String username;
    private String authCode;
    private String accessToken;

    public UserModel(){
    }

    public UserModel(String username, String authCode){
        this.username = username;
        this.authCode = authCode;
        this.accessToken = null;
    }

    public UserModel(String username, String authCode, String accessToken){
        this.username = username;
        this.authCode = authCode;
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
