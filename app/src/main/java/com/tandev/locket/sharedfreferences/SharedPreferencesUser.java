package com.tandev.locket.sharedfreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.user.AccountInfo;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesUser {
    // save user
    private static final String USER_PROFILE = "user_profile";


    private static final String LOGIN_REQUEST = "login_request";
    private static final String LOGIN_RESPONSE = "login_response";
    private static final String ACCOUNT_INFO = "account_info";
    private static final String USER_FRIENDS = "user_friends";


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

    public static void saveUserFriends(Context context, List<String> user_friends) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user_friends);
        editor.putString(USER_FRIENDS, json);
        editor.apply();
    }

    public static List<String> getUserFriends(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(USER_FRIENDS, null);
        Gson gson = new Gson();
        // Chuyển đổi json thành List<String> thay vì AccountInfo
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Xóa tất cả dữ liệu
        editor.apply();  // Áp dụng thay đổi
    }

}
