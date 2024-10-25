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
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 热门列表
 */
public class HotListActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tvTitle;
    private RecyclerView rvList;
    private DataAdapter dataAdapter;
    private List<Results> list;
    private int pageSize = 20;//分页大小
    private int currentPage = 1;//当前页数
    private SmartRefreshLayout srlRecord;//刷新
    private int type = TypeEnum.Movies.getCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_hot_list);
        tvTitle = findViewById(R.id.tv_name);
        rvList = findViewById(R.id.rv_list);
        srlRecord = findViewById(R.id.srl_list);
        type = getIntent().getIntExtra("type",TypeEnum.Movies.getCode());
        tvTitle.setText(String.format("热门%s",TypeEnum.getName(type)));

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
                loadData(true, 0);
            }
        });
        srlRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData(false, 0);
            }
        });

        srlRecord.autoRefresh();
    }

    private void loadData(boolean isRefresh, int typeId) {
        String url = "";
        if (type == TypeEnum.Movies.getCode()) {
            url = OkHttpTool.URL + "/movies";
        } else if (type == TypeEnum.Books.getCode()) {
            url = OkHttpTool.URL + "/books";
        }
        if (isRefresh) {
            list = new ArrayList<>();
            currentPage = 1;
        } else {
            currentPage++;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("page", currentPage);
        map.put("page_size", pageSize);
        map.put("detail", true);
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Data data = gson.fromJson(response, Data.class);
                            if (data.getResults().size() > 0) {
                                list.addAll(data.getResults());
                            }
                            dataAdapter.addItem(list);
                            if (isRefresh) {
                                srlRecord.finishRefresh();//刷新完成
                                Toast.makeText(myActivity, "加载成功", Toast.LENGTH_SHORT).show();
                            } else {
                                srlRecord.finishLoadMore();//加载更多完成
                            }

                            int totalPage = PageCalculator.calculateTotalPages(data.getCount(), pageSize);
                            if (currentPage == totalPage) {
                                srlRecord.finishLoadMoreWithNoMoreData();
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
