package com.tandev.locket.fragment.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tandev.locket.MainActivity;
import com.tandev.locket.R;
import com.tandev.locket.adapter.SlideAdapter;

public class LoginOrRegisterFragment extends Fragment implements SlideAdapter.OnSlideChangeListener {
    private ViewPager2 view_pager2;
    private TextView txt_register, txt_login;
    private SlideAdapter slideAdapter;
    private int[][] slides = {
            {R.drawable.assets_phones_01, R.drawable.assets_phones_02, R.drawable.assets_phones_03, R.drawable.assets_phones_04},
            {R.drawable.assets_phones_05, R.drawable.assets_phones_06, R.drawable.assets_phones_07, R.drawable.assets_phones_08},
            {R.drawable.assets_phones_09, R.drawable.assets_phones_10, R.drawable.assets_phones_11}
    };
    private int currentSlide = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_or_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        slideAdapter = new SlideAdapter(slides, this);
        view_pager2.setAdapter(slideAdapter);

        view_pager2.setUserInputEnabled(false); // Vô hiệu hóa thao tác tay để chuyển slide
    }

    private void initViews(View view) {
        view_pager2 = view.findViewById(R.id.view_pager2);
        txt_register = view.findViewById(R.id.txt_register);
        txt_login = view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                );
                transaction.replace(R.id.frame_layout, new LoginEmailFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onSlideComplete(int position) {
        // Kiểm tra xem slide hiện tại có phải là slide cuối cùng hay không
        if (currentSlide < slides.length - 1) {
            currentSlide++;
        } else {
            currentSlide = 0; // Quay lại slide đầu tiên
        }
        view_pager2.setCurrentItem(currentSlide, true);
    }
}