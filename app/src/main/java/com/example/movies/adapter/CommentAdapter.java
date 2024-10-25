package com.example.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.bean.Comments;
import com.example.movies.enums.CommentEnum;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comments> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_comment_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comments comment = list.get(i);
        if (comment != null) {
            viewHolder.tvName.setText(comment.getUsername());
            viewHolder.tvDate.setText(comment.getTime());
            viewHolder.tvContent.setText(comment.getContent());
            viewHolder.ratingBar.setRating(CommentEnum.getCode(comment.getRating()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<Comments> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvContent;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public interface ItemListener{
        void ItemClick(Comments comment);
    }
}
