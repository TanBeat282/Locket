package com.tandev.locket.fragment.change_email;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tandev.locket.R;
import com.tandev.locket.sharedfreferences.SharedPreferencesUser;

public class ChangeEmailFragment extends Fragment {
    private EditText edt_email;
    private OnEmailChangedListener emailChangedListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_email = view.findViewById(R.id.edt_email);
        edt_email.setText(SharedPreferencesUser.getLoginRequest(requireContext()).getEmail());
        edt_email.requestFocus();

        requireActivity().getWindow().getDecorView().post(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edt_email, InputMethodManager.SHOW_IMPLICIT);
        });
        // Set up TextWatcher to notify when the email changes
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailChangedListener != null) {
                    emailChangedListener.onEmailChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    // Method to set the listener for email changes
    public void setOnEmailChangedListener(OnEmailChangedListener listener) {
        this.emailChangedListener = listener;
    }

    // Interface to notify email changes
    public interface OnEmailChangedListener {
        void onEmailChanged(String email);
    }
    public String getPassword() {
        return edt_email.getText().toString().trim();
    }
}