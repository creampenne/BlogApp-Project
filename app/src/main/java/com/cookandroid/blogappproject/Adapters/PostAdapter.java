package com.cookandroid.blogappproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.blogappproject.Activities.PostDetailActivity;
import com.cookandroid.blogappproject.Models.Post;
import com.cookandroid.blogappproject.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.postImage);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.postUserImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView postImage;
        ImageView postUserImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            postImage = itemView.findViewById(R.id.row_post_image);
            postUserImage = itemView.findViewById(R.id.row_post_user_image);

            // Lambda 변경
            itemView.setOnClickListener(view -> {
                Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                int position = getAdapterPosition();
                long timestamp = (long) mData.get(position).getTimeStamp();

                postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                postDetailActivity.putExtra("title", mData.get(position).getTitle());
                postDetailActivity.putExtra("description", mData.get(position).getDescription());
                postDetailActivity.putExtra("postKey", mData.get(position).getDescription());
                postDetailActivity.putExtra("userPhoto", mData.get(position).getUserPhoto());
                // postDetailActivity.putExtra("userName", mData.get(position).getUsername());
                postDetailActivity.putExtra("postDate", timestamp);
                mContext.startActivity(postDetailActivity);


            });

        }
    }
}
