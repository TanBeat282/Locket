package com.tandev.locket.sharedfreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tandev.locket.model.login.reponse.LoginResponse;
import com.tandev.locket.model.login.request.LoginRequest;

public class SharedPreferencesUser {
    private static final String USER_PROFILE = "user_profile";
    private static final String USER = "user";

    private static final String LOGIN_REQUEST_PROFILE = "login_request";
    private static final String LOGIN_REQUEST = "login_request";

    private static final String LAST_LOGIN_MILLISECOND = "last_login_millisecond";

    public static void saveUserProfile(Context context, LoginResponse loginResponse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResponse);
        editor.putString(USER, json);
        editor.apply();
    }

    public static LoginResponse getUserProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(USER, null);
        Gson gson = new Gson();
        return gson.fromJson(json, LoginResponse.class);
    }

    public static void saveLoginRequest(Context context, LoginRequest loginRequest) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_REQUEST_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        editor.putString(LOGIN_REQUEST, json);
        editor.apply();
    }

    public static LoginRequest getLoginRequest(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_REQUEST_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LOGIN_REQUEST, null);
        Gson gson = new Gson();
        return gson.fromJson(json, LoginRequest.class);
    }
    // Hàm lưu millisecond
    public static void saveLastLoginMillisecond(Context context, long millisecond) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_LOGIN_MILLISECOND, millisecond);
        editor.apply();
    }

    // Hàm lấy millisecond
    public static long getLastLoginMillisecond(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_LOGIN_MILLISECOND, 0);
    }

    // Hàm xóa tất cả thông tin
    public static void clearAll(Context context) {
        SharedPreferences userProfilePreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences loginRequestPreferences = context.getSharedPreferences(LOGIN_REQUEST_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences lastLoginMillisecondPreferences = context.getSharedPreferences(LAST_LOGIN_MILLISECOND, Context.MODE_PRIVATE);

        SharedPreferences.Editor userProfileEditor = userProfilePreferences.edit();
        SharedPreferences.Editor loginRequestEditor = loginRequestPreferences.edit();
        SharedPreferences.Editor lastLoginMillisecondEditor = lastLoginMillisecondPreferences.edit();

        userProfileEditor.clear();
        loginRequestEditor.clear();
        lastLoginMillisecondEditor.clear();

        userProfileEditor.apply();
        loginRequestEditor.apply();
        lastLoginMillisecondEditor.apply();
    }
}
