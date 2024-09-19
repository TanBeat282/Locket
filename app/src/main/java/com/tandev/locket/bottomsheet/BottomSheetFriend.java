package com.tandev.locket.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.adapter.FriendsAdapter;
import com.tandev.locket.api.FriendApiService;
import com.tandev.locket.api.MomentApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.model.firend.Friend;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.moment.Moment;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetFriend extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;

    private LoginResponse loginResponse;
    private List<String> user_id;
    private FriendApiService friendApiService;
    private ArrayList<Friend> friendArrayList;
    private FriendsAdapter friendsAdapter;

    private LinearLayout linear_view1, linear_view2;
    private TextView txt_cancel;
    private TextView txt_number_friends;
    private EditText edt_search_friend;
    private RecyclerView rv_friends;

    public BottomSheetFriend(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_friend, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        loginResponse = SharedPreferencesUser.getLoginResponse(context);

        friendApiService = LoginApiClient.getCheckEmailClient().create(FriendApiService.class);
        user_id = SharedPreferencesUser.getUserFriends(context);
        friendArrayList = new ArrayList<>();

        initViews(bottomSheetDialog);
        setAdapters();
        onClick();
        user_id();
        getFetchUserV2(user_id);

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        linear_view1 = bottomSheetDialog.findViewById(R.id.linear_view1);
        linear_view2 = bottomSheetDialog.findViewById(R.id.linear_view2);
        edt_search_friend = bottomSheetDialog.findViewById(R.id.edt_search_friend);
        txt_cancel = bottomSheetDialog.findViewById(R.id.txt_cancel);
        txt_number_friends = bottomSheetDialog.findViewById(R.id.txt_number_friends);
        rv_friends = bottomSheetDialog.findViewById(R.id.rv_friends);
    }

    private void setAdapters() {
        rv_friends.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        friendsAdapter = new FriendsAdapter(friendArrayList, requireActivity(), requireContext());
        rv_friends.setAdapter(friendsAdapter);
    }

    private void onClick() {

        linear_view1.setOnClickListener(view -> {
            linear_view1.setVisibility(View.GONE);
            linear_view2.setVisibility(View.VISIBLE);
            edt_search_friend.requestFocus();

            requireActivity().getWindow().getDecorView().post(() -> {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_search_friend, InputMethodManager.SHOW_IMPLICIT);
            });

        });
        txt_cancel.setOnClickListener(view -> {
            linear_view1.setVisibility(View.VISIBLE);
            linear_view2.setVisibility(View.GONE);
        });
    }

    @SuppressLint("SetTextI18n")
    private void user_id() {
        txt_number_friends.setText(user_id.size() + " / 20 người bạn đã được bổ sung");
    }

    @SuppressLint("DefaultLocale")
    private String createGetFriendsJson(String user_id) {
        return String.format(
                "{\"data\":{\"user_uid\":\"%s\"}}",
                user_id
        );
    }

    // Phương thức gọi API getMomentV2
    private void getFetchUserV2(List<String> user_id) {
        String token = "Bearer " + loginResponse.getIdToken();
        int totalUsers = user_id.size();
        int[] completedRequests = {0}; // Biến đếm số lượng yêu cầu hoàn thành

        for (String id : user_id) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), createGetFriendsJson(id));
            Call<ResponseBody> ResponseBodyCall = friendApiService.FETCH_USER_RESPONSE_CALL(token, requestBody);
            ResponseBodyCall.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            Gson gson = new Gson();
                            Friend friend = gson.fromJson(responseBody, Friend.class);
                            friendArrayList.add(friend);
                        } catch (IOException e) {
                            Log.e("Response Error", "Error reading response body", e);
                        }
                    } else {
                        Log.e("Response Error", "Failed response from getMomentV2");
                    }
                    // Tăng biến đếm khi một yêu cầu hoàn thành
                    completedRequests[0]++;
                    // Kiểm tra xem tất cả các yêu cầu đã hoàn thành chưa
                    if (completedRequests[0] == totalUsers) {
                        friendsAdapter.setFilterList(friendArrayList);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("Response Error", "Unsuccessful response: " + throwable.getMessage());
                }
            });
        }
        friendsAdapter.setFilterList(friendArrayList);
    }

}
