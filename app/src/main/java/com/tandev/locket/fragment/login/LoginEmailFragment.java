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
import com.tandev.locket.helper.ResponseUtils;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.login.error.LoginError;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginEmailFragment extends Fragment {
    private ImageView img_back;
    private EditText edt_email;
    private LinearLayout linear_continue;
    private TextView txt_continue;
    private ImageView img_continue;
    private static final String PASSWORD = "123";

    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        conFigViews();
        onClick();
    }

    private void initViews(View view) {
        img_back = view.findViewById(R.id.img_back);
        edt_email = view.findViewById(R.id.edt_email);
        linear_continue = view.findViewById(R.id.linear_continue);
        txt_continue = view.findViewById(R.id.txt_continue);
        img_continue = view.findViewById(R.id.img_continue);
    }

    private void conFigViews() {
        edt_email.requestFocus(); // Yêu cầu focus vào EditText

        // Đảm bảo rằng bàn phím được mở sau khi focus vào EditText
        requireActivity().getWindow().getDecorView().post(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edt_email, InputMethodManager.SHOW_IMPLICIT);
        });
        // Thêm TextWatcher để theo dõi sự thay đổi văn bản trong EditText
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = s.toString().trim();

                if (isValidEmail(email)) {
                    // Đổi màu nền và kích hoạt LinearLayout nếu email hợp lệ
                    linear_continue.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_btn_continue_check));
                    txt_continue.setTextColor(getResources().getColor(R.color.bg));
                    img_continue.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bg)); // Màu tint là màu xanh dương
                    linear_continue.setEnabled(true);
                } else {
                    linear_continue.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_btn_continue_un_check));
                    txt_continue.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint));
                    img_continue.setColorFilter(ContextCompat.getColor(requireContext(), R.color.hint)); // Màu tint là màu xanh dương
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
        linear_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmail(email);
            }
        });
    }

    private void checkEmail(String email) {
        Retrofit retrofit = LoginApiClient.getLoginClient();
        LoginApiService loginApiService = retrofit.create(LoginApiService.class);

        LoginRequest request = new LoginRequest(email, PASSWORD, "CLIENT_TYPE_IOS", true);
        Call<ResponseBody> call = loginApiService.LOGIN_RESPONSE_CALL(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() && response.body() == null) {
                    String contentEncoding = response.headers().get("Content-Encoding");
                    try {
                        String responseBody = ResponseUtils.getResponseBody(response.errorBody().byteStream(), contentEncoding);
                        Gson gson = new Gson();
                        LoginError loginError = gson.fromJson(responseBody, LoginError.class);
                        if (loginError.getError().getMessage().equals("EMAIL_NOT_FOUND")) {
                            showAlertDialog("Tài khoản với email này không tồn tại", "Hãy đảm bảo rằng bạn đã điền chính xác email của mình.");
                        } else if (loginError.getError().getMessage().equals("INVALID_PASSWORD")) {
                            releaseFragment(email);
                            hideKeyboard();
                        } else {
                            showAlertDialog("Chặn đăng nhập", "Bạn đã bị chặn đăng nhập do nhiều lần nhập sai email. Vui lòng thử lại sau.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("checkEmail", "Error: " + t.getMessage());
            }

        });
    }

    public void hideKeyboard() {
        // Lấy InputMethodManager
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Kiểm tra xem có view nào đang hiển thị bàn phím không
        View view = getView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void releaseFragment(String email) {
        // Tạo Bundle và thêm dữ liệu vào đó
        Bundle bundle = new Bundle();
        bundle.putString("email", email); // Ví dụ gửi email

        // Tạo PasswordFragment và thiết lập Bundle
        LoginEmailFragment2 passwordFragment = new LoginEmailFragment2();
        passwordFragment.setArguments(bundle);

        // Thay thế EmailFragment bằng PasswordFragment và thêm vào back stack
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
        );
        transaction.replace(R.id.frame_layout, passwordFragment);
        transaction.addToBackStack(null); // Thêm vào back stack
        transaction.commit();
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

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}