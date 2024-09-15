package com.tandev.locket.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApiService {

    //Check email
    @Headers({
            "Accept-Encoding: gzip",
            "Connection: Keep-Alive",
            "Content-Type: application/json; charset=utf-8",
            "Firebase-Instance-ID-Token: dTNmyvemRZa4oRGQQVW4yF:APA91bH_VREpca_sHe-nCHxjR_O9jUlYtq2kEBY5DXmozxBRscqCojkc4HltOMIjtvVZHrgTAWY3WkWhH-hgAveHN6Q2-BqolZQp9FKAcZNOcHg86luXfaDh94dMsmOpBdGAJLzWuwGl",
            "Host: api.locketcamera.com",
            "User-Agent: okhttp/4.9.2",
    })
    @POST("changeProfileInfo")
    Call<ResponseBody> CHANGE_NAME_RESPONSE_CALL(
            @Header("Authorization") String token,  // Token động
            @Body RequestBody body
    );

    //Check User name
    @Headers({
            "Accept-Encoding: gzip",
            "Connection: Keep-Alive",
            "Content-Type: application/json; charset=utf-8",
            "Firebase-Instance-ID-Token: dTNmyvemRZa4oRGQQVW4yF:APA91bH_VREpca_sHe-nCHxjR_O9jUlYtq2kEBY5DXmozxBRscqCojkc4HltOMIjtvVZHrgTAWY3WkWhH-hgAveHN6Q2-BqolZQp9FKAcZNOcHg86luXfaDh94dMsmOpBdGAJLzWuwGl",
            "Host: api.locketcamera.com",
            "User-Agent: okhttp/4.9.2",
    })
    @POST("validateUsername")
    Call<ResponseBody> CHECK_USERNAME_RESPONSE_CALL(
            @Header("Authorization") String token,  // Token động
            @Body RequestBody body
    );

}
