package com.finalproject.schoolcalendar.models;

/**
 * Created by Fani on 11/10/13.
 */

public class UserModel {

    private int id;
    private String username;
    private String authCode;
    private String accessToken;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
