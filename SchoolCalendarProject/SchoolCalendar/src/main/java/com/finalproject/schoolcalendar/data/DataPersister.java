package com.finalproject.schoolcalendar.data;

import com.finalproject.schoolcalendar.models.UserModel;
import com.google.gson.Gson;

/**
 * Created by Fani on 11/13/13.
 */
public class DataPersister {
    private static String BASE_URL = "http://timetableservices.apphb.com/api/";
    private static Gson mGson = new Gson();

    public static HttpResponse Login(UserModel userModel) {
        String loginUrl = BASE_URL + "auth/token";

        String userModelToString = mGson.toJson(userModel);
        HttpResponse responseResult = HttpRequest.post(loginUrl, userModelToString, null);

        return responseResult;
    }

    public static HttpResponse Register(UserModel userModel) {
        String registerUrl = BASE_URL + "user/register";

        String userModelToString = mGson.toJson(userModel);
        HttpResponse responseResult = HttpRequest.post(registerUrl, userModelToString, null);

        return responseResult;
    }

    public  static HttpResponse Logout(String accessToken) {
        String logoutUrl = BASE_URL + "user/logout";
        HttpResponse responseResult = HttpRequest.put(logoutUrl, accessToken);

        return responseResult;
    }
}
