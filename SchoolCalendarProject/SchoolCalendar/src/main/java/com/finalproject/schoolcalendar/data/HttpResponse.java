package com.finalproject.schoolcalendar.data;

/**
 * Created by Fani on 11/13/13.
 */
public class HttpResponse {
    private boolean mIsStatusOk;
    private String mMessage;

    public HttpResponse(boolean isStatusOk, String message){
        this.mIsStatusOk = isStatusOk;
        this.mMessage = message;
    }

    public boolean isStatusOk() {
        return this.mIsStatusOk;
    }

    public String getMessage() {
        return this.mMessage;
    }
}
