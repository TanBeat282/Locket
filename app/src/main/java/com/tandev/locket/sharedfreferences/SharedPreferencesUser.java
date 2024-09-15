package com.tandev.locket.sharedfreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.user.AccountInfo;

public class SharedPreferencesUser {
    // save user
    private static final String USER_PROFILE = "user_profile";


    private static final String LOGIN_REQUEST = "login_request";
    private static final String LOGIN_RESPONSE = "login_response";
    private static final String ACCOUNT_INFO = "account_info";


    public static void saveLoginRequest(Context context, LoginRequest loginRequest) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        editor.putString(LOGIN_REQUEST, json);
        editor.apply();
    }

    public static LoginRequest getLoginRequest(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LOGIN_REQUEST, null);
        Gson gson = new Gson();
        return gson.fromJson(json, LoginRequest.class);
    }


    public static void saveLoginResponse(Context context, LoginResponse loginResponse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResponse);
        editor.putString(LOGIN_RESPONSE, json);
        editor.apply();
    }

    public static LoginResponse getLoginResponse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LOGIN_RESPONSE, null);
        Gson gson = new Gson();
        return gson.fromJson(json, LoginResponse.class);
    }

    public static void saveAccountInfo(Context context, AccountInfo accountInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountInfo);
        editor.putString(ACCOUNT_INFO, json);
        editor.apply();
    }

    public static AccountInfo getAccountInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(ACCOUNT_INFO, null);
        Gson gson = new Gson();
        return gson.fromJson(json, AccountInfo.class);
    }

    // Hàm xóa tất cả thông tin
    public static void clearAll(Context context) {

    }
}
