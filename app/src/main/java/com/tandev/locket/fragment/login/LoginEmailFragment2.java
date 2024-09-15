package com.tandev.locket.fragment.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tandev.locket.R;
import com.tandev.locket.fragment.home.HomeFragment;
import com.tandev.locket.helper.ResponseUtils;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.model.login.check_email.CheckEmailResponse;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.login.error.LoginError;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginEmailFragment2 extends Fragment {
    private ImageView img_back;
    private EditText edt_password;
    private TextView txt_forgot_password;
    private TextView txt_forgot_password_send;
    private LinearLayout linear_continue;
    private TextView txt_continue;
    private ImageView img_continue;

    private Retrofit retrofit;
    private LoginApiService loginApiService;
    private String data, password;
    private boolean isPhone = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_email2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        loginApiService = LoginApiClient.getCheckEmailClient().create(LoginApiService.class);

        retrofit = LoginApiClient.getLoginClient();
        loginApiService = retrofit.create(LoginApiService.class);

        initViews(view);
        conFigViews();
        onClick();
        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            isPhone = getArguments().getBoolean("is_phone");
            data = getArguments().getString("data");
        }
    }

    private void initViews(View view) {
        img_back = view.findViewById(R.id.img_back);
        edt_password = view.findViewById(R.id.edt_password);
        txt_forgot_password = view.findViewById(R.id.txt_forgot_password);
        txt_forgot_password_send = view.findViewById(R.id.txt_forgot_password_send);
        linear_continue = view.findViewById(R.id.linear_continue);
        txt_continue = view.findViewById(R.id.txt_continue);
        img_continue = view.findViewById(R.id.img_continue);
    }

    private void conFigViews() {
        edt_password.requestFocus();

        requireActivity().getWindow().getDecorView().post(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edt_password, InputMethodManager.SHOW_IMPLICIT);
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString().trim();

                if (password.length() >= 3) {
                    linear_continue.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_btn_continue_check));
                    txt_continue.setTextColor(getResources().getColor(R.color.bg));
                    img_continue.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bg));
                    linear_continue.setEnabled(true);
                } else {
                    linear_continue.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_btn_continue_un_check));
                    txt_continue.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint));
                    img_continue.setColorFilter(ContextCompat.getColor(requireContext(), R.color.hint));
                    linear_continue.setEnabled(false);
                }
            }
        });
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        txt_forgot_password.setOnClickListener(view -> forgotPassword(data));
        linear_continue.setOnClickListener(view -> {
            login(data, password);
        });
    }

    private String createJsonDataRequestForgotPassword(String data) {
        return String.format("{\"data\": {\"email\": \"%s\"}}", data);
    }

    private void forgotPassword(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createJsonDataRequestForgotPassword(data));
        Call<ResponseBody> checkEmailResponseCall = loginApiService.FORGOT_PASSWORD_RESPONSE_CALL(requestBody);
        checkEmailResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        int status = resultObject.getInt("status");
                        if (status == 200) {
                            txt_forgot_password.setVisibility(View.GONE);
                            txt_forgot_password_send.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(">>>>>>>>>>>>>>>>>>>>", "Status khác 200: Không thành công");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.e(">>>>>>>>>>>>>>>>>>>>", "Error parsing response: " + e.getMessage());
                    }
                } else {
                    Log.d(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + throwable.getMessage());
            }
        });
    }

    private String createSignInJson(String email, String password) {
        return String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true,\"clientType\":\"CLIENT_TYPE_ANDROID\"}",
                email, password
        );
    }

    private void login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password, "CLIENT_TYPE_ANDROID", true);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), createSignInJson(email, password));

        Call<ResponseBody> call = loginApiService.LOGIN_RESPONSE_CALL(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String contentEncoding = response.headers().get("Content-Encoding");
                        String responseBody = ResponseUtils.getResponseBody(response.body().byteStream(), contentEncoding);

                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

                        //save user
                        // save LoginRequest để refreshToken trong Home
                        SharedPreferencesUser.saveLoginRequest(requireContext(), request);
                        SharedPreferencesUser.saveLoginResponse(requireContext(), loginResponse);

                        getAccountInfo(loginResponse.getIdToken());
                    } catch (IOException e) {
                        Log.e("Auth", "Error reading response body", e);
                    }
                } else {
                    try {
                        String contentEncoding = response.headers().get("Content-Encoding");
                        String responseBody = ResponseUtils.getResponseBody(response.errorBody().byteStream(), contentEncoding);
                        Gson gson = new Gson();
                        LoginError loginError = gson.fromJson(responseBody, LoginError.class);

                        if (loginError.getError().getMessage().toString().equals("INVALID_PASSWORD")) {
                            showAlertDialog("Không thể đăng nhập vào tài khoản của bạn", "Hãy đảm bảo rằng bạn đã điền chính xác mật khẩu của mình.");
                        } else {
                            showAlertDialog("Chặn đăng nhập", "Bạn đã bị chặn đăng nhập do nhiều lần nhập sai email. Vui lòng thử lại sau.");
                        }
                        Log.e("login", "onResponse: " + loginError.getError().getMessage().toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("login", "Error: " + t.getMessage());
            }

        });
    }

    private String createAccountInfoJson(String idToken) {
        return String.format("{\"idToken\":\"%s\"}", idToken);
    }

    private void getAccountInfo(String token) {
        Retrofit retrofit = LoginApiClient.getLoginClient();
        LoginApiService loginApiService = retrofit.create(LoginApiService.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), createAccountInfoJson(token));

        Call<ResponseBody> call = loginApiService.ACCOUNT_INFO_RESPONSE_CALL(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String contentEncoding = response.headers().get("Content-Encoding");
                        String responseBody = ResponseUtils.getResponseBody(response.body().byteStream(), contentEncoding);

                        Gson gson = new Gson();
                        AccountInfo accountInfo = gson.fromJson(responseBody, AccountInfo.class);

                        SharedPreferencesUser.saveAccountInfo(requireContext(), accountInfo);

                        releaseFragment();
                    } catch (IOException e) {
                        Log.e("Auth", "Error reading response body", e);
                    }
                } else {
                    String contentEncoding = response.headers().get("Content-Encoding");
                    try {
                        String responseBody = ResponseUtils.getResponseBody(response.errorBody().byteStream(), contentEncoding);
                        Gson gson = new Gson();
                        LoginError loginError = gson.fromJson(responseBody, LoginError.class);
                        Log.e("login", "onResponse: " + loginError.getError().getMessage().toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("login", "Error: " + t.getMessage());
            }

        });
    }

    private void showAlertDialog(String title, String content) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void releaseFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
        );
        transaction.replace(R.id.frame_layout, new HomeFragment());
        // Xóa toàn bộ back stack để không quay lại các Fragment trước đó
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }
}