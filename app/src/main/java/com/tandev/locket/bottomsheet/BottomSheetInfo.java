package com.tandev.locket.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.fragment.login.LoginOrRegisterFragment;
import com.tandev.locket.model.login.reponse.LoginResponse;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

public class BottomSheetInfo extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;

    private RoundedImageView img_capture;
    private RoundedImageView img_avatar_2;
    private TextView txt_full_name;
    private LinearLayout linear_logout;

    public BottomSheetInfo(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_info, null);
        bottomSheetDialog.setContentView(view);
        // Customize BottomSheet
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        initViews(bottomSheetDialog);
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        img_capture = bottomSheetDialog.findViewById(R.id.img_capture);
        img_avatar_2 = bottomSheetDialog.findViewById(R.id.img_avatar_2);
        txt_full_name = bottomSheetDialog.findViewById(R.id.txt_full_name);
        linear_logout = bottomSheetDialog.findViewById(R.id.linear_logout);

        LoginResponse  loginResponse = SharedPreferencesUser.getUserProfile(requireContext());
        txt_full_name.setText(loginResponse.getDisplayName());
        Glide.with(this).load(loginResponse.getProfilePicture()).into(img_avatar_2);
        Glide.with(this).load(loginResponse.getProfilePicture()).into(img_capture);
    }

    private void onClick() {

        linear_logout.setOnClickListener(view -> {
            SharedPreferencesUser.clearAll(requireContext());
            releaseFragment();
            bottomSheetDialog.dismiss();
        });
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
