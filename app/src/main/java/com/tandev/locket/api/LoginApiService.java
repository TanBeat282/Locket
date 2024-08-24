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
            "Accept: */*",
            "Accept-Encoding: gzip",
            "Accept-Language: en",
            "Connection: keep-alive",
            "Content-Type: application/json; charset=utf-8"
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
            "Accept: */*",
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: en",
            "baggage: sentry-environment=production,sentry-public_key=78fa64317f434fd89d9cc728dd168f50,sentry-release=com.locket.Locket@1.82.0+3,sentry-trace_id=90310ccc8ddd4d059b83321054b6245b",
            "Connection: keep-alive",
            "Content-Length: 117",
            "Content-Type: application/json",
            "Host: www.googleapis.com",
            "sentry-trace: 90310ccc8ddd4d059b83321054b6245b-3a4920b34e94401d-0",
            "User-Agent: FirebaseAuth.iOS/10.23.1 com.locket.Locket/1.82.0 iPhone/18.0 hw/iPhone12_1",
            "X-Client-Version: iOS/FirebaseSDK/10.23.1/FirebaseCore-iOS",
            "X-Firebase-AppCheck: eyJraWQiOiJNbjVDS1EiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxOjY0MTAyOTA3NjA4Mzppb3M6Y2M4ZWI0NjI5MGQ2OWIyMzRmYTYwNiIsImF1ZCI6WyJwcm9qZWN0c1wvNjQxMDI5MDc2MDgzIiwicHJvamVjdHNcL2xvY2tldC00MjUyYSJdLCJwcm92aWRlciI6ImRldmljZV9jaGVja19kZXZpY2VfaWRlbnRpZmljYXRpb24iLCJpc3MiOiJodHRwczpcL1wvZmlyZWJhc2VhcHBjaGVjay5nb29nbGVhcGlzLmNvbVwvNjQxMDI5MDc2MDgzIiwiZXhwIjoxNzIyMTY3ODk4LCJpYXQiOjE3MjIxNjQyOTgsImp0aSI6ImlHUGlsT1dDZGg4Mll3UTJXRC1neEpXeWY5TU9RRFhHcU5OR3AzTjFmRGcifQ.lqTOJfdoYLpZwYeeXtRliCdkVT7HMd7_Lj-d44BNTGuxSYPIa9yVAR4upu3vbZSh9mVHYS8kJGYtMqjP-L6YXsk_qsV_gzKC2IhVAV6KbPDRHdevMfBC6fRiOSVn7vt749GVFdZqAuDCXhCILsaMhvgDBgZoDilgAPtpNwyjz-VtRB7OdOUbuKTCqdoSOX0SJWVUMyuI8nH0-unY--YRctunK8JHZDxBaM_ahVggYPWBCpzxq9Yeq8VSPhadG_tGNaADStYPaeeUkZ7DajwWqH5ze6ESpuFNgAigwPxCM735_ZiPeD7zHYwppQA9uqTWszK9v9OvWtFCsgCEe22O8awbNbuEBTKJpDQ8xvZe8iEYyhfUPncER3S-b1CmuXR7tFCdTgQe5j7NGWjFvN_CnL7D2nudLwxWlpqwASCHvHyi8HBaJ5GpgriTLXAAinY48RukRDBi9HwEzpRecELX05KTD2lTOfQCjKyGpfG2VUHP5Xm36YbA3iqTDoDXWMvV",
            "X-Firebase-GMPID: 1:641029076083:ios:cc8eb46290d69b234fa606",
            "X-Ios-Bundle-Identifier: com.locket.Locket"
    })
    @POST("identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyCQngaaXQIfJaH0aS2l7REgIjD7nL431So")
    Call<ResponseBody> LOGIN_RESPONSE_CALL(@Body LoginRequest request);


}
