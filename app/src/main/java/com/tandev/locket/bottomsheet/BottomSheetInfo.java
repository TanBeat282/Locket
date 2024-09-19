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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.model.user.AccountInfo;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

public class BottomSheetInfo extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;

    private TextView txt_edit_info;
    private LinearLayout linear_logout, linear_new, linear_change_email;

    public BottomSheetInfo(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialogTheme; // Áp dụng theme tùy chỉnh
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_info, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        initViews(bottomSheetDialog);
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        txt_edit_info = bottomSheetDialog.findViewById(R.id.txt_edit_info);
        linear_new = bottomSheetDialog.findViewById(R.id.linear_new);
        RoundedImageView img_capture = bottomSheetDialog.findViewById(R.id.img_capture);
        RoundedImageView img_avatar_2 = bottomSheetDialog.findViewById(R.id.img_avatar_2);
        TextView txt_full_name = bottomSheetDialog.findViewById(R.id.txt_full_name);
        linear_change_email = bottomSheetDialog.findViewById(R.id.linear_change_email);
        linear_logout = bottomSheetDialog.findViewById(R.id.linear_logout);

        AccountInfo accountInfo = SharedPreferencesUser.getAccountInfo(requireContext());
        txt_full_name.setText(accountInfo.getUsers().get(0).getDisplayName());
        Glide.with(this).load(accountInfo.getUsers().get(0).getPhotoUrl()).into(img_avatar_2);
        Glide.with(this).load(accountInfo.getUsers().get(0).getPhotoUrl()).into(img_capture);
    }

    private void onClick() {

        linear_logout.setOnClickListener(view -> {
            openBottomSheetLogout();
        });
        linear_new.setOnClickListener(view -> openBottomSheetRegisterUserName());

        txt_edit_info.setOnClickListener(view -> openBottomSheetChangeName());
        linear_change_email.setOnClickListener(view -> openBottomSheetChangeEmail());
    }

    private void openBottomSheetRegisterUserName() {
        dismiss();
        BottomSheetRegisterUserName bottomSheetRegisterUserName = new BottomSheetRegisterUserName(context, activity);
        bottomSheetRegisterUserName.show(getActivity().getSupportFragmentManager(), bottomSheetRegisterUserName.getTag());
    }

    private void openBottomSheetChangeEmail() {
        dismiss();
        BottomSheetChangeEmail bottomSheetChangeEmail = new BottomSheetChangeEmail(context, activity);
        bottomSheetChangeEmail.show(getActivity().getSupportFragmentManager(), bottomSheetChangeEmail.getTag());
    }

    private void openBottomSheetLogout() {
        dismiss();
        BottomSheetLogout bottomSheetLogout = new BottomSheetLogout(context, activity);
        bottomSheetLogout.show(getActivity().getSupportFragmentManager(), bottomSheetLogout.getTag());
    }

    private void openBottomSheetChangeName() {
        dismiss();
        BottomSheetChangeName bottomSheetChangeName = new BottomSheetChangeName(context, activity);
        bottomSheetChangeName.show(getActivity().getSupportFragmentManager(), bottomSheetChangeName.getTag());
    }
}
