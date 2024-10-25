package com.example.movies.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.adapter.DataAdapter;
import com.example.movies.bean.Data;
import com.example.movies.bean.Results;
import com.example.movies.enums.TypeEnum;
import com.example.movies.utils.HttpTool;
import com.example.movies.utils.OkHttpTool;
import com.example.movies.utils.PageCalculator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个性列表
 */
public class PreferenceListActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tvTitle;
    private RecyclerView rvList;
    private DataAdapter dataAdapter;
    private SmartRefreshLayout srlRecord;//刷新
    private int type = TypeEnum.Movies.getCode();
    private String url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_hot_list);
        tvTitle = findViewById(R.id.tv_name);
        rvList = findViewById(R.id.rv_list);
        srlRecord = findViewById(R.id.srl_list);
        url = getIntent().getStringExtra("url");
        type = getIntent().getIntExtra("type",TypeEnum.Movies.getCode());
        tvTitle.setText(String.format("%s个性推荐",TypeEnum.getName(type)));

        //init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        dataAdapter = new DataAdapter(type);
        rvList.setAdapter(dataAdapter);
        dataAdapter.setItemListener(new DataAdapter.ItemListener() {
            @Override
            public void ItemClick(Results results, int type) {
                HttpTool.goDetail(myActivity,type,results.getId());
            }
        });
        //下拉刷新
        srlRecord.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData(true);
            }
        });
        srlRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData(false);
            }
        });

        srlRecord.autoRefresh();
    }

    private void loadData(boolean isRefresh) {
        Map<String, Object> map = new HashMap<>();
        map.put("detail", true);
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type type = new TypeToken<List<Results>>() {
                            }.getType();//列表信息
                            List<Results> list =gson.fromJson(response,type);
                            dataAdapter.addItem(list);
                            if (isRefresh) {
                                srlRecord.finishRefresh();//刷新完成
                                Toast.makeText(myActivity, "加载成功", Toast.LENGTH_SHORT).show();
                            } else {
                                srlRecord.finishLoadMore();//加载更多完成
                            }
                        } else {
                            if (isRefresh) {
                                srlRecord.finishRefresh(false);//刷新失败
                                Toast.makeText(myActivity, "加载失败", Toast.LENGTH_SHORT).show();
                            } else {
                                srlRecord.finishLoadMore(false);//加载更多失败
                            }
                        }
                    }
                });
            }
        });

    }

    public void back(View view){
        finish();
    }
}
