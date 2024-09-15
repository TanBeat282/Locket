package com.tandev.locket.api;

import com.tandev.locket.model.login.check_email.CheckEmailRequest;
import com.tandev.locket.model.login.check_email.CheckEmailResponse;
import com.tandev.locket.model.login.request.LoginRequest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginApiService {

    //Check email
    @Headers({
            "Accept-Encoding: gzip",
            "Connection: Keep-Alive",
            "Content-Type: application/json; charset=utf-8",
            "Host: api.locketcamera.com",
            "User-Agent: okhttp/4.9.2",
    })
    @POST("validateEmailAddress")
    Call<CheckEmailResponse> CHECK_EMAIL_RESPONSE_CALL(@Body RequestBody body);

    //Check phone
    @Headers({
            "Accept: */*",
            "Accept-Encoding: gzip",
            "Accept-Language: en",
            "Connection: keep-alive",
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("sendVerificationCode")
    Call<ResponseBody> CHECK_PHONE_RESPONSE_CALL(@Body RequestBody body);


    //Forgot password
    @Headers({
            "Accept: */*",
            "Accept-Encoding: gzip",
            "Accept-Language: en",
            "Connection: keep-alive",
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("sendPasswordResetEmail")
    Call<ResponseBody> FORGOT_PASSWORD_RESPONSE_CALL(@Body RequestBody body);


    //Login
    @Headers({
            "Accept-Encoding: gzip",
            "Accept-Language: vi-VN, en-US",
            "Connection: Keep-Alive",
            "Content-Type: application/json",
            "Host: www.googleapis.com",
            "User-Agent: Dalvik/2.1.0 (Linux; U; Android 11; sdk_gphone_x86 Build/RSR1.240422.006)",
            "X-Android-Cert: 187A27D3D7364A044307F56E66230F973DCCD5B7",
            "X-Android-Package: com.locket.Locket",
            "X-Client-Version: Android/Fallback/X22002000/FirebaseCore-Android",
            "X-Firebase-AppCheck: eyJlcnJvciI6IlVOS05PV05fRVJST1IifQ==",
            "X-Firebase-Client: H4sIAAAAAAAAAKtWykhNLCpJSk0sKVayio7VUSpLLSrOzM9TslIyUqoFAFyivEQfAAAA",
            "X-Firebase-GMPID: 1:641029076083:android:eac8183b796b856d4fa606"
    })
    @POST("identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyB5dTd-xiLD5dEfWq5OpptnQtnMpE0W0u8")
    Call<ResponseBody> LOGIN_RESPONSE_CALL(@Body  RequestBody body);


    //Account Info
    @Headers({
            "Accept-Encoding: gzip",
            "Accept-Language: vi-VN, en-US",
            "Connection: Keep-Alive",
            "Content-Type: application/json",
            "Host: www.googleapis.com",
            "User-Agent: Dalvik/2.1.0 (Linux; U; Android 11; sdk_gphone_x86 Build/RSR1.240422.006)",
            "X-Android-Cert: 187A27D3D7364A044307F56E66230F973DCCD5B7",
            "X-Android-Package: com.locket.Locket",
            "X-Client-Version: Android/Fallback/X22002000/FirebaseCore-Android",
            "X-Firebase-AppCheck: eyJlcnJvciI6IlVOS05PV05fRVJST1IifQ==",
            "X-Firebase-Client: H4sIAAAAAAAAAKtWykhNLCpJSk0sKVayio7VUSpLLSrOzM9TslIyUqoFAFyivEQfAAAA",
            "X-Firebase-GMPID: 1:641029076083:android:eac8183b796b856d4fa606"
    })
    @POST("identitytoolkit/v3/relyingparty/getAccountInfo?key=AIzaSyB5dTd-xiLD5dEfWq5OpptnQtnMpE0W0u8")
    Call<ResponseBody> ACCOUNT_INFO_RESPONSE_CALL(@Body RequestBody body);


}
