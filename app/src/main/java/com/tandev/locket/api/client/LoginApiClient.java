package com.tandev.locket.api.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApiClient {
    private static Retrofit loginRetrofit = null;
    private static final String LOGIN_BASE_URL = "https://www.googleapis.com/";

    // Retrofit cho việc đăng nhập
    public static Retrofit getLoginClient() {
        if (loginRetrofit == null) {
            loginRetrofit = new Retrofit.Builder()
                    .baseUrl(LOGIN_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return loginRetrofit;
    }
}
