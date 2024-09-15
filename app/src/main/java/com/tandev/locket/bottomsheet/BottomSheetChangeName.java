package com.tandev.locket.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.api.UserApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.fragment.login.LoginOrRegisterFragment;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetChangeName extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;

    private UserApiService userApiService;

    private EditText edt_name, edt_surname;
    private LinearLayout linear_continue;
    private TextView txt_continue;
    private ImageView img_continue;
    private AccountInfo accountInfo;
    private String name, surname;

    public BottomSheetChangeName(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_change_name, null);
        bottomSheetDialog.setContentView(view);
        // Customize BottomSheet
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        userApiService = LoginApiClient.getCheckEmailClient().create(UserApiService.class);
        accountInfo = SharedPreferencesUser.getAccountInfo(context);
        initViews(bottomSheetDialog);
        conFigViews();
        setData();
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        edt_name = bottomSheetDialog.findViewById(R.id.edt_name);
        edt_surname = bottomSheetDialog.findViewById(R.id.edt_surname);
        linear_continue = bottomSheetDialog.findViewById(R.id.linear_continue);
        txt_continue = bottomSheetDialog.findViewById(R.id.txt_continue);
    }

    private void conFigViews() {
        name = edt_name.getText().toString().trim();
        surname = edt_surname.getText().toString().trim();

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString().trim();

                if (!name.isEmpty() && !surname.isEmpty()) {
                    linear_continue.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_continue_check));
                    txt_continue.setTextColor(getResources().getColor(R.color.bg));
                    linear_continue.setEnabled(true);
                } else {
                    linear_continue.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_continue_un_check));
                    txt_continue.setTextColor(ContextCompat.getColor(context, R.color.hint));
                    linear_continue.setEnabled(false);
                }
            }
        });
        edt_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                surname = s.toString().trim();

                if (!surname.isEmpty() && !name.isEmpty()) {
                    linear_continue.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_continue_check));
                    txt_continue.setTextColor(getResources().getColor(R.color.bg));
                    linear_continue.setEnabled(true);
                } else {
                    linear_continue.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_continue_un_check));
                    txt_continue.setTextColor(ContextCompat.getColor(context, R.color.hint));
                    linear_continue.setEnabled(false);
                }
            }
        });
    }

    private void setData() {
        Log.d(">>>>>>>>>>>>>>>>>>>>>>>", "setData:  "+SharedPreferencesUser.getLoginResponse(context).getIdToken());
        String[] result = splitName(accountInfo.getUsers().get(0).getDisplayName());
        edt_name.setText(result[0]);
        edt_surname.setText(result[1]);
    }

    private void onClick() {

        linear_continue.setOnClickListener(view -> {
            changeName();
        });
    }

    private String createChangeNameJson(String name, String surname) {
        return String.format(
                "{\"data\":{\"last_name\":\"%s\",\"first_name\":\"%s\"}}",
                name, surname
        );
    }


    private void changeName() {
        String token = "Bearer " + SharedPreferencesUser.getLoginResponse(context).getIdToken();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createChangeNameJson(name, surname));
        Call<ResponseBody> ResponseBodyCall = userApiService.CHANGE_NAME_RESPONSE_CALL(token, requestBody);
        ResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txt_continue.setText("Các thay đổi đã được lưu");
                    bottomSheetDialog.dismiss();
                } else {
                    Log.d(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(">>>>>>>>>>>>>>>>>>>>", "Unsuccessful response: " + throwable.getMessage());
            }
        });
    }

    public static String[] splitName(String displayName) {
        String[] nameParts = displayName.trim().split("\\s+");
        int length = nameParts.length;

        // Họ là phần cuối cùng
        String surname = nameParts[length - 1];

        // Tên là tất cả phần còn lại, nối lại thành chuỗi
        String name = String.join(" ", java.util.Arrays.copyOf(nameParts, length - 1));

        return new String[]{name, surname};
    }

    private void releaseFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, new LoginOrRegisterFragment());
        // Xóa toàn bộ back stack để không quay lại các Fragment trước đó
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }
}
