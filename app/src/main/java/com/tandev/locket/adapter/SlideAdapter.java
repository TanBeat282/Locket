package com.tandev.locket.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.locket.R;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private int[][] slides; // Mảng chứa các mảng ảnh
    private Handler handler = new Handler();
    private OnSlideChangeListener onSlideChangeListener;

    public SlideAdapter(int[][] slides, OnSlideChangeListener listener) {
        this.slides = slides;
        this.onSlideChangeListener = listener;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImages(slides[position], position);
    }

    @Override
    public int getItemCount() {
        return slides.length;
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private int currentImageIndex = 0;
        private Runnable imageSwitcher;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void setImages(int[] images, int slidePosition) {
            // Đảm bảo hủy bỏ bất kỳ runnable nào trước đó để tránh xung đột
            handler.removeCallbacksAndMessages(null);

            currentImageIndex = 0;
            imageView.setImageResource(images[currentImageIndex]); // Hiển thị ngay lập tức ảnh đầu tiên

            imageSwitcher = new Runnable() {
                @Override
                public void run() {
                    if (currentImageIndex < images.length - 1) {
                        currentImageIndex++;
                        imageView.setImageResource(images[currentImageIndex]);
                        handler.postDelayed(this, 3000); // Chuyển ảnh mỗi 1 giây
                    } else {
                        onSlideChangeListener.onSlideComplete(slidePosition);
                    }
                }
            };

            // Bắt đầu chuyển ảnh tiếp theo sau 1 giây
            handler.postDelayed(imageSwitcher, 3000);
        }
    }

    public interface OnSlideChangeListener {
        void onSlideComplete(int position);
    }
}