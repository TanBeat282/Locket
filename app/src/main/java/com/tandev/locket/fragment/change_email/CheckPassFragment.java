package com.tandev.locket.fragment.change_email;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tandev.locket.R;

public class CheckPassFragment extends Fragment {
    private EditText edt_password;
    private OnPasswordChangedListener passwordChangedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_password = view.findViewById(R.id.edt_password);
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordChangedListener != null) {
                    passwordChangedListener.onPasswordChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.passwordChangedListener = listener;
    }

    public interface OnPasswordChangedListener {
        void onPasswordChanged(String email);
    }

    public String getPassword() {
        return edt_password.getText().toString().trim();
    }
}
