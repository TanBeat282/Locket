package com.tandev.locket.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tandev.locket.fragment.home.HomeFragment;
import com.tandev.locket.fragment.live_camera.LiveCameraFragment;
import com.tandev.locket.fragment.moment.MomentFragment;
import com.tandev.locket.fragment.view_moment.ViewMomentFragment;

public class HomeViewPager2Adapter extends FragmentStateAdapter {

    public HomeViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ViewMomentFragment();
            default:
                return new LiveCameraFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}