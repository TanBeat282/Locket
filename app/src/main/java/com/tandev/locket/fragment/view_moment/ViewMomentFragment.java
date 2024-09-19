package com.tandev.locket.fragment.view_moment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.tandev.locket.R;

import java.util.ArrayList;
import java.util.List;

public class ViewMomentFragment extends Fragment {

    private ViewPager2 smallViewPager;
    private ImagePagerAdapter imagePagerAdapter;
    private List<String> imageList = new ArrayList<>();
    private boolean hasMoreImages = true;

    private float initialXValue, initialYValue;  // Giá trị tọa độ bắt đầu khi bắt đầu vuốt

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smallViewPager = view.findViewById(R.id.smallViewPager);

        imagePagerAdapter = new ImagePagerAdapter(requireContext(), imageList);
        smallViewPager.setAdapter(imagePagerAdapter);

        // Đăng ký listener để bắt sự kiện vuốt
        smallViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialXValue = event.getX();
                        initialYValue = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float diffX = event.getX() - initialXValue;
                        float diffY = event.getY() - initialYValue;

                        if (Math.abs(diffY) > Math.abs(diffX)) {
                            // Vuốt theo chiều dọc - ngăn ViewPager lớn can thiệp
                            smallViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            // Vuốt theo chiều ngang - cho phép ViewPager lớn nhận sự kiện
                            smallViewPager.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                }
                return false;  // Để các sự kiện cảm ứng khác tiếp tục được xử lý
            }
        });

        smallViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == imageList.size() - 1 && hasMoreImages) {
                    getNextImagesFromApi();
                }
            }
        });

        loadImages();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadImages() {
        imageList.add("https://picsum.photos/200/300");
        imagePagerAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getNextImagesFromApi() {
        imageList.add("https://picsum.photos/200/301");
        imagePagerAdapter.notifyDataSetChanged();
    }

    private class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
        private Context context;
        private List<String> images;

        public ImagePagerAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_view_moment, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            String imageUrl = images.get(position);
            Glide.with(context).load(imageUrl).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}
