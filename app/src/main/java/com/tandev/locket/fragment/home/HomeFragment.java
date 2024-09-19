package com.tandev.locket.fragment.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.adapter.HomeViewPager2Adapter;
import com.tandev.locket.api.ApiCaller;
import com.tandev.locket.api.LoginApiService;
import com.tandev.locket.api.MomentApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.bottomsheet.BottomSheetFriend;
import com.tandev.locket.bottomsheet.BottomSheetInfo;
import com.tandev.locket.fragment.login.LoginEmailFragment2;
import com.tandev.locket.fragment.login.LoginOrRegisterFragment;
import com.tandev.locket.helper.ImageUtils;
import com.tandev.locket.helper.ResponseUtils;
import com.tandev.locket.model.auth.AuthResponse;
import com.tandev.locket.model.login.error.LoginError;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.login.request.LoginRequest;
import com.tandev.locket.model.moment.Moment;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager;
    private LoginResponse loginResponse;

    private RelativeLayout relative_profile;
    private RelativeLayout relative_send_friend;

    private RoundedImageView img_profile;
    private LinearLayout linear_friends;
    private TextView txt_number_friends;
    private ImageView img_message;
    private MomentApiService momentApiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        momentApiService = LoginApiClient.getCheckEmailClient().create(MomentApiService.class);
        initViews(view);
        onCLick();
        conFigViews();
        getDataUser();
    }

    private void getDataUser() {
        loginResponse = SharedPreferencesUser.getLoginResponse(requireContext());
        checkExpiresToken();
        setupViewPager();
        setData();
    }

    private void initViews(View view) {
        relative_profile = view.findViewById(R.id.relative_profile);
        relative_send_friend = view.findViewById(R.id.relative_send_friend);

        img_profile = view.findViewById(R.id.img_profile);
        linear_friends = view.findViewById(R.id.linear_friends);
        txt_number_friends = view.findViewById(R.id.txt_number_friends);
        img_message = view.findViewById(R.id.img_message);

        viewPager = view.findViewById(R.id.viewPager);
    }

    private void conFigViews() {

    }

    private void onCLick() {
        linear_friends.setOnClickListener(view -> {
            BottomSheetFriend bottomSheetFriend = new BottomSheetFriend(requireContext(), getActivity());
            bottomSheetFriend.show(getParentFragmentManager(), bottomSheetFriend.getTag());
        });
        img_profile.setOnClickListener(view -> {
            BottomSheetInfo bottomSheetInfo = new BottomSheetInfo(requireContext(), getActivity());
            bottomSheetInfo.show(getParentFragmentManager(), bottomSheetInfo.getTag());
        });
    }

    private void setData() {
        Glide.with(this).load(loginResponse.getProfilePicture()).into(img_profile);
    }

    private void setupViewPager() {
        HomeViewPager2Adapter adapter = new HomeViewPager2Adapter(requireActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }

    @SuppressLint("SetTextI18n")
    private void checkExpiresToken() {
        AccountInfo accountInfo = SharedPreferencesUser.getAccountInfo(requireContext());
        long lastLoginTime = accountInfo.getUsers().get(0).getLastLoginAt();
        long expiryDuration = 3600 * 1000;
        long expiryTime = lastLoginTime + expiryDuration;
        long currentTime = System.currentTimeMillis();
        if (currentTime > expiryTime) {
            refreshToken();
        }else {
            getMomentV2(null);
        }
    }

    private String createSignInJson(String grantType, String refreshToken) {
        return String.format(
                "{\"grantType\":\"%s\",\"refreshToken\":\"%s\"}",
                grantType, refreshToken
        );
    }

    private void refreshToken() {
        Retrofit retrofit = LoginApiClient.getRefreshTokenClient();
        LoginApiService loginApiService = retrofit.create(LoginApiService.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), createSignInJson("refresh_token", loginResponse.getRefreshToken()));
        Call<ResponseBody> call = loginApiService.REFRESH_TOKEN_RESPONSE_CALL(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String contentEncoding = response.headers().get("Content-Encoding");
                        String responseBody = ResponseUtils.getResponseBody(response.body().byteStream(), contentEncoding);

                        Gson gson = new Gson();
                        AuthResponse authResponse = gson.fromJson(responseBody, AuthResponse.class);

                        //save user
                        LoginResponse newLoginResponse = new LoginResponse();
                        newLoginResponse.setIdToken(authResponse.getIdToken());
                        newLoginResponse.setRefreshToken(authResponse.getRefreshToken());
                        SharedPreferencesUser.saveLoginResponse(requireContext(), newLoginResponse);


                        getMomentV2(null);
                    } catch (IOException e) {
                        Log.e("Auth", "Error reading response body", e);
                    }
                } else {
                    showAlertDialog("Phiên đăng nhập hết hạn", "Vui lòng đăng nhập lại để tiếp tục sử dụng ứng dụng.");
                    SharedPreferencesUser.clearAll(requireContext());
                    releaseFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("login", "Error: " + t.getMessage());
            }

        });
    }

    @SuppressLint("DefaultLocale")
    private String createGetMomentV2ExcludedUsersJson(List<String> excludedUsers) {
        String excludedUsersJson = (excludedUsers == null || excludedUsers.isEmpty()) ? "[]" : new Gson().toJson(excludedUsers);

        return String.format(
                "{\"data\":{" +
                        "\"excluded_users\":%s," +
                        "\"last_fetch\":%d," +
                        "\"should_count_missed_moments\":%b" +
                        "}}",
                excludedUsersJson,
                1,
                true
        );
    }

    private void getMomentV2(List<String> excludedUsers) {
        if (excludedUsers == null) {
            excludedUsers = new ArrayList<>();
        }

        String token = "Bearer " + loginResponse.getIdToken();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createGetMomentV2ExcludedUsersJson(excludedUsers));
        Call<ResponseBody> ResponseBodyCall = momentApiService.GET_MOMENT_V2(token, requestBody);
        List<String> finalExcludedUsers = excludedUsers;
        ResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        Moment moment = gson.fromJson(responseBody, Moment.class);
                        if (!moment.getResult().getData().isEmpty()) {
                            finalExcludedUsers.add(moment.getResult().getData().get(0).getUser());
                            getMomentV2(finalExcludedUsers); // Gọi đệ quy với danh sách đã cập nhật
                        } else {
                            SharedPreferencesUser.saveUserFriends(requireContext(), finalExcludedUsers);
                            txt_number_friends.setText(finalExcludedUsers.size() + " bạn bè");
                        }
                    } catch (IOException e) {
                        Log.e("Response Error", "Error reading response body", e);
                    }
                } else {
                    Log.e("Response Error", "Failed response from getMomentV2");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("Response Error", "Unsuccessful response: " + throwable.getMessage());
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
        transaction.replace(R.id.frame_layout, new LoginOrRegisterFragment());
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }
}
