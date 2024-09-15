package com.tandev.locket.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tandev.locket.fragment.home.HomeFragment;
import com.tandev.locket.fragment.moment.MomentFragment;

public class HomeStateAdapter extends FragmentStateAdapter {

    public HomeStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new MomentFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}