package com.cookandroid.blogappproject.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.blogappproject.Models.Comment;
import com.cookandroid.blogappproject.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context mContext;
    private final List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.commentUserImage);
        holder.tvName.setText(mData.get(position).getUserName());
        holder.tvContent.setText(mData.get(position).getContent());
        holder.tvDate.setText(timestampToString((Long)mData.get(position).getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView commentUserImage;
        TextView tvName, tvContent, tvDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserImage = itemView.findViewById(R.id.comment_user_image);
            tvName = itemView.findViewById(R.id.comment_user_name);
            tvContent = itemView.findViewById(R.id.comment_content);
            tvDate = itemView.findViewById(R.id.comment_date);
        }
    }

    // 시간 정보 변환 및 반환
    private String timestampToString(long postDate) {
        Calendar calendar = Calendar.getInstance(Locale.KOREAN);
        calendar.setTimeInMillis(postDate);
        return DateFormat.format("HH:mm",calendar).toString();
    }
}
