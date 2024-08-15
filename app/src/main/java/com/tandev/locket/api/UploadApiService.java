package com.tandev.locket.api;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface UploadApiService {

    //Start Upload
    @POST
    Call<ResponseBody> startUploadImage(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody data);

    //Upload Image
    @PUT
    Call<ResponseBody> uploadImage(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody image);


    //Get Download Token
    @GET
    Call<ResponseBody> getDownloadTokenImage(@Url String url, @HeaderMap Map<String, String> headers);


    //Post Image
    @POST()
    Call<ResponseBody> postImage(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody body);


    //VIDEO
    //Start Upload
    @POST
    Call<ResponseBody> startUploadVideo(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody data);

    //Upload Image
    @PUT
    Call<ResponseBody> uploadVideo(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody image);


    //Get Download Token
    @GET
    Call<ResponseBody> getDownloadTokenVideo(@Url String url, @HeaderMap Map<String, String> headers);


    //Post Image
    @POST("postMomentV2")
    Call<ResponseBody> postVideo(@HeaderMap Map<String, String> headers, @Body RequestBody body);
}
