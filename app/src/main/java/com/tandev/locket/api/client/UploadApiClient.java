package com.tandev.locket.api.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadApiClient {
    private static Retrofit uploadImageRetrofit = null;
    private static Retrofit uploadVideoRetrofit = null;
    private static Retrofit postRetrofit = null;
    private static final String BASE_POST_URL = "https://api.locketcamera.com/";
    private static final String BASE_UPLOAD_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/locket-img/o/";
    private static final String BASE_UPLOAD_VIDEO_URL = "https://firebasestorage.googleapis.com/v0/b/locket-video/o/";

    public static Retrofit getUploadImageRetrofit() {
        if (uploadImageRetrofit == null) {
            uploadImageRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_UPLOAD_IMAGE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return uploadImageRetrofit;
    }

    public static Retrofit getUploadVideoRetrofit() {
        if (uploadVideoRetrofit == null) {
            uploadVideoRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_UPLOAD_VIDEO_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return uploadVideoRetrofit;
    }

    public static Retrofit getPostRetrofit() {
        if (postRetrofit == null) {
            postRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_POST_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return postRetrofit;
    }
}
