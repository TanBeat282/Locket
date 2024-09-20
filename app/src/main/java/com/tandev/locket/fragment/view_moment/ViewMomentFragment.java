package com.tandev.locket.fragment.view_moment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tandev.locket.R;
import com.tandev.locket.adapter.ItemAdapter;
import com.tandev.locket.api.MomentApiService;
import com.tandev.locket.api.client.LoginApiClient;
import com.tandev.locket.model.login.response.LoginResponse;
import com.tandev.locket.model.moment.Moment;
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

public class ViewMomentFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<Moment> itemList = new ArrayList<>();
    private LoginResponse loginResponse;
    private MomentApiService momentApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginResponse = SharedPreferencesUser.getLoginResponse(requireContext());
        momentApiService = LoginApiClient.getCheckEmailClient().create(MomentApiService.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Thiết lập Adapter cho RecyclerView
        itemAdapter = new ItemAdapter(requireContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        // Sử dụng PagerSnapHelper để tạo hiệu ứng cuộn giống ViewPager2
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Thêm dữ liệu mẫu vào danh sách
        getMomentV2(null);
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
                            getMomentV2(finalExcludedUsers);
                            itemList.add(moment);
                        } else {
                            itemAdapter.setFilterList(itemList);
                        }
                    } catch (IOException e) {
                        Log.e("Response Error", "Error reading response body", e);
                    }
                } else {
                    itemAdapter.setFilterList(itemList);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                itemAdapter.setFilterList(itemList);
            }
        });
    }
}
