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
import com.tandev.locket.MainActivity;
import com.tandev.locket.R;
import com.tandev.locket.fragment.home.HomeFragment;
import com.tandev.locket.helper.ResponseUtils;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.login.reponse.LoginResponse;
import com.tandev.locket.model.login.error.LoginError;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginEmailFragment2 extends Fragment {
    private ImageView img_back;
    private EditText edt_password;
    private TextView txt_forgot_password;
    private LinearLayout linear_continue;
    private TextView txt_continue;
    private ImageView img_continue;

    private String email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_email2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        conFigViews();
        onClick();
        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }
    }

    private void initViews(View view) {
        img_back = view.findViewById(R.id.img_back);
        edt_password = view.findViewById(R.id.edt_password);
        txt_forgot_password = view.findViewById(R.id.txt_forgot_password);
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
        linear_continue.setOnClickListener(view -> {
            login(email, password);
        });
    }

    private void login(String email, String password) {
        Retrofit retrofit = LoginApiClient.getLoginClient();
        LoginApiService loginApiService = retrofit.create(LoginApiService.class);

        LoginRequest request = new LoginRequest(email, password, "CLIENT_TYPE_IOS", true);
        Call<ResponseBody> call = loginApiService.LOGIN_RESPONSE_CALL(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String contentEncoding = response.headers().get("Content-Encoding");
                        String responseBody = ResponseUtils.getResponseBody(response.body().byteStream(), contentEncoding);

                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

                        SharedPreferencesUser.saveUserProfile(requireContext(), loginResponse);
                        SharedPreferencesUser.saveLoginRequest(requireContext(), request);
                        SharedPreferencesUser.saveLastLoginMillisecond(requireContext(), System.currentTimeMillis());

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

                        if (loginError.getError().toString().equals("INVALID_PASSWORD")) {
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