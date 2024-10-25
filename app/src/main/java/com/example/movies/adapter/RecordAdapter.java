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
import com.example.movies.bean.Record;
import com.example.movies.bean.Results;
import com.example.movies.enums.TypeEnum;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    private int type;
    public RecordAdapter(int type){
        this.type =type;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_record_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Record record = list.get(i);
        if (record != null) {
            Results results =record.getResults();
            viewHolder.title.setText(results.getTitle());
            if (type== TypeEnum.Movies.getCode()){
                viewHolder.tv_author.setText(results.getLanguage());
                viewHolder.tv_wd.setText(results.getCountry());
                viewHolder.tv_date.setText(results.getRuntime());
                viewHolder.tv_zj.setText(results.getRelease_date());
            }else  if (type== TypeEnum.Books.getCode()){
                viewHolder.tv_author.setText(results.getAuthor());
                viewHolder.tv_wd.setText(results.getPublisher());
                viewHolder.tv_date.setText(String.format("评分:%s",results.getRating()));
                viewHolder.tv_zj.setText(results.getPub_year());
            }
            Glide.with(mActivity)
                    .asBitmap()
                    .load(results.getPoster_url())
                    .error(R.mipmap.ic_loading)
                    .into(viewHolder.ivImg);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(record,type);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<Record> listAdd) {
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
        private TextView tv_author;
        private TextView tv_wd;
        private TextView tv_date;
        private TextView tv_zj;
        private ImageView ivImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_wd = itemView.findViewById(R.id.tv_wd);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_zj = itemView.findViewById(R.id.tv_zj);
            ivImg = itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface ItemListener{
        void ItemClick(Record record,int type);
    }
}
