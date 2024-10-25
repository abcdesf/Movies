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
import com.example.movies.bean.Results;

import java.util.ArrayList;
import java.util.List;

public class Data2Adapter extends RecyclerView.Adapter<Data2Adapter.ViewHolder> {
    private List<Results> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private int type;
    public Data2Adapter(int type){
        this.type =type;
    }
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_data_list2,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Results results = list.get(i);
        if (results != null) {
            viewHolder.title.setText(results.getTitle());
            Glide.with(mActivity)
                    .asBitmap()
                    .load(results.getPoster_url())
                    .error(R.mipmap.ic_loading)
                    .into(viewHolder.ivImg);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(results,type);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<Results> listAdd) {
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
        private TextView title;
        private ImageView ivImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            ivImg = itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface ItemListener{
        void ItemClick(Results results,int type);
    }
}
