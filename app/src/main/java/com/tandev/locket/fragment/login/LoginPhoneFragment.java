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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tandev.locket.R;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.model.login.check_email.CheckEmailResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPhoneFragment extends Fragment {
    private ImageView img_back;
    private EditText edt_phone;
    private LinearLayout linear_continue;
    private TextView txt_continue;
    private ImageView img_continue;
    private LoginApiService checkEmailApiService;
    private String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkEmailApiService = LoginApiClient.getCheckEmailClient().create(LoginApiService.class);
        initViews(view);
        conFigViews();
        onClick();
    }

    private void initViews(View view) {
        img_back = view.findViewById(R.id.img_back);
        edt_phone = view.findViewById(R.id.edt_phone);
        linear_continue = view.findViewById(R.id.linear_continue);
        txt_continue = view.findViewById(R.id.txt_continue);
        img_continue = view.findViewById(R.id.img_continue);
    }

    private void conFigViews() {
        edt_phone.requestFocus(); // Yêu cầu focus vào EditText

        // Đảm bảo rằng bàn phím được mở sau khi focus vào EditText
        requireActivity().getWindow().getDecorView().post(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edt_phone, InputMethodManager.SHOW_IMPLICIT);
        });
        // Thêm TextWatcher để theo dõi sự thay đổi văn bản trong EditText
        edt_phone.addTextChangedListener(new TextWatcher() {
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
                phone = "+84" + s.toString().trim();

                if (isValidEmail(phone)) {
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
        linear_continue.setOnClickListener(view -> checkEmail(phone));
    }

    private String createSignInJson(String operation, String phone, boolean usePasswordIfAvailable) {
        return String.format(
                "{\"data\":{\"use_password_if_available\":%b,\"phone\":\"%s\",\"operation\":\"%s\"}}",
                usePasswordIfAvailable, phone, operation
        );
    }


    private void checkEmail(String phone) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createSignInJson("sign_in", phone, true));
        Call<ResponseBody> checkEmailResponseCall = checkEmailApiService.CHECK_PHONE_RESPONSE_CALL(requestBody);
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
                            releaseFragment(phone);
                            hideKeyboard();
                        } else {
                            showAlertDialog("Không thể gửi mã", "Hãy đảm bảo thiết bị của bạn bắt sóng tốt. Chúng tôi cũng có thể đang bị quá tải, vui lòng quay lại sau và thử lại.");
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

    public void hideKeyboard() {
        // Lấy InputMethodManager
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Kiểm tra xem có view nào đang hiển thị bàn phím không
        View view = getView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void releaseFragment(String phone) {
        // Tạo Bundle và thêm dữ liệu vào đó
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_phone", true); // Ví dụ gửi email
        bundle.putString("data", phone); // Ví dụ gửi email

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
        return email != null && Patterns.PHONE.matcher(email).matches();
    }
}