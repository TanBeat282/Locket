package com.tandev.locket.fragment.register_username;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tandev.locket.R;
import com.tandev.locket.api.UserApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.fragment.home.HomeFragment;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserNameFragment extends Fragment {
    private UserApiService userApiService;

    private EditText edt_username;
    private TextView txt_note, txt_check;
    private ProgressBar progress_bar;
    private ImageView img_error, img_close;
    private LinearLayout linear_continue, linear_check;
    private TextView txt_continue;
    private AccountInfo accountInfo;
    private String username;
    private boolean is_check = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userApiService = LoginApiClient.getCheckEmailClient().create(UserApiService.class);
        accountInfo = SharedPreferencesUser.getAccountInfo(requireContext());
        initViews(view);
        conFigViews();
        setData();
        onClick();
    }

    private void initViews(View view) {
        img_close = view.findViewById(R.id.img_close);
        edt_username = view.findViewById(R.id.edt_username);
        txt_note = view.findViewById(R.id.txt_note);

        progress_bar = view.findViewById(R.id.progress_bar);
        img_error = view.findViewById(R.id.img_error);
        linear_continue = view.findViewById(R.id.linear_continue);
        txt_continue = view.findViewById(R.id.txt_continue);
        linear_check = view.findViewById(R.id.linear_check);
        txt_check = view.findViewById(R.id.txt_check);

    }

    private void conFigViews() {
        edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString().trim();

                if (username.length() >= 3) {
                    txt_note.setVisibility(View.GONE);
                    linear_check.setVisibility(View.VISIBLE);
                    img_error.setVisibility(View.GONE);
                    progress_bar.setVisibility(View.VISIBLE);
                    txt_check.setText("Đang kiểm tra...");
                    txt_check.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint));
                    new Handler().postDelayed(() -> checkUserName(), 1500);
                } else if (username.isEmpty()) {
                    txt_note.setVisibility(View.VISIBLE);
                    linear_check.setVisibility(View.GONE);
                    edt_username.setBackgroundResource(R.drawable.background_edit_text);
                } else {
                    txt_note.setVisibility(View.GONE);
                    linear_check.setVisibility(View.VISIBLE);
                    img_error.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                    txt_check.setText("Phải dài hơn 3 ký tự");
                    txt_check.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                    edt_username.setBackgroundResource(R.drawable.background_edit_text_error);
                }
            }
        });
    }

    private void setData() {
//        Log.d(">>>>>>>>>>>>>>>>>>>>>>>", "setData:  " + SharedPreferencesUser.getLoginResponse(context).getIdToken());
//        String[] result = splitName(accountInfo.getUsers().get(0).getDisplayName());
//        edt_name.setText(result[0]);
//        edt_surname.setText(result[1]);
    }

    private void onClick() {

        linear_continue.setOnClickListener(view -> {

        });
        img_close.setOnClickListener(view -> {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, newFragment);
            transaction.commit();

        });
    }

    private String createUserNameJson(String username) {
        return String.format(
                "{\"data\":{\"username\":\"%s\"}}",
                username
        );
    }


    private void checkUserName() {
        String token = "Bearer " + SharedPreferencesUser.getLoginResponse(requireContext()).getIdToken();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createUserNameJson(username));
        Call<ResponseBody> ResponseBodyCall = userApiService.CHECK_USERNAME_RESPONSE_CALL(token, requestBody);
        ResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txt_note.setVisibility(View.GONE);
                    linear_check.setVisibility(View.VISIBLE);
                    img_error.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                    txt_check.setText("AAAAAAAAAAA");
                    edt_username.setBackgroundResource(R.drawable.background_edit_text_successful);
                } else {
                    txt_note.setVisibility(View.GONE);
                    linear_check.setVisibility(View.VISIBLE);
                    img_error.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                    txt_check.setText("Đã xảy ra lỗi");
                    txt_check.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                    edt_username.setBackgroundResource(R.drawable.background_edit_text_error);
                    Log.d(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + throwable.getMessage());
            }
        });
    }

}