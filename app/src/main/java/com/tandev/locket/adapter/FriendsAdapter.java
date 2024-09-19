package com.tandev.locket.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.locket.R;
import com.tandev.locket.model.firend.Friend;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private ArrayList<Friend> friendArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Friend> filterList) {
        this.friendArrayList = filterList;
        notifyDataSetChanged();
    }

    public FriendsAdapter(ArrayList<Friend> friendArrayList, Activity activity, Context context) {
        this.friendArrayList = friendArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Friend friend = friendArrayList.get(position);


        holder.txt_full_name.setText(friend.getResult().getData().getFirst_name() + " " + friend.getResult().getData().getLast_name());
        Glide.with(context)
                .load(friend.getResult().getData().getProfile_picture_url())
                .placeholder(R.color.bg_btn)
                .into(holder.img_avatar);
        holder.img_un_friend.setOnClickListener(view -> {
            ///
        });
    }


    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView img_avatar;
        private TextView txt_full_name;
        private ImageView img_un_friend;

        public ViewHolder(View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            txt_full_name = itemView.findViewById(R.id.txt_full_name);
            img_un_friend = itemView.findViewById(R.id.img_un_friend);
        }
    }

}
