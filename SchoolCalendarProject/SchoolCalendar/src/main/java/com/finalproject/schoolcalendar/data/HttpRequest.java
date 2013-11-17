package com.finalproject.schoolcalendar.data;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Fani on 11/13/13.
 */
public class HttpRequest {
    public static HttpResponseHelper get(String url, String accessToken) {
        String content = null;
        String error = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-type", "application/json");
            if (accessToken != null) {
                httpGet.setHeader("X-accessToken", accessToken);
            }

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            content = client.execute(httpGet, responseHandler);
        } catch (Exception e) {
            error = e.getMessage();
        }

        if (error != null) {
            return new HttpResponseHelper(false, error);
        } else {
            return new HttpResponseHelper(true, content);
        }
    }

    public static HttpResponseHelper post(String url, String data, String accessToken) {
        String content = null;
        String error = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data));
            httpPost.setHeader("Content-type", "application/json");
            if (accessToken != null) {
                httpPost.setHeader("X-accessToken", accessToken);
            }

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            content = client.execute(httpPost, responseHandler);
        } catch (Exception e) {
            error = e.getMessage();
        }

        if (error != null) {
            return new HttpResponseHelper(false, error);
        } else {
            return new HttpResponseHelper(true, content);
        }
    }

    public static HttpResponseHelper put(String url, String accessToken) {
        String content = null;
        String error = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setHeader("X-accessToken", accessToken);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            content = client.execute(httpPut, responseHandler);
        } catch (Exception e) {
            error = e.getMessage();
        }

        if (error != null) {
            return new HttpResponseHelper(false, error);
        } else {
            return new HttpResponseHelper(true, content);
        }
    }
}
