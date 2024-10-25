package com.example.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.bean.Celebrities;

import java.util.ArrayList;
import java.util.List;

public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.ViewHolder> {
    private List<Celebrities> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_celebrities_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Celebrities celebrities = list.get(i);
        if (celebrities != null) {
            viewHolder.title.setText(celebrities.getCelebrity().getName());
            viewHolder.role.setText(celebrities.getRole());
            Glide.with(mActivity)
                    .asBitmap()
                    .load(celebrities.getCelebrity().getAvatar())
                    .error(R.mipmap.ic_loading)
                    .into(viewHolder.ivImg);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(celebrities);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<Celebrities> listAdd) {
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
        private TextView role;
        private TextView title;
        private ImageView ivImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            role = itemView.findViewById(R.id.tv_role);
            title = itemView.findViewById(R.id.tv_name);
            ivImg = itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface ItemListener{
        void ItemClick(Celebrities celebrities);
    }
}
