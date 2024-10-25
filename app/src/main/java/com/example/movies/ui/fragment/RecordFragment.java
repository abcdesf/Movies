package com.example.movies.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.adapter.RecordAdapter;
import com.example.movies.bean.Record;
import com.example.movies.bean.Results;
import com.example.movies.enums.ClassifyEnum;
import com.example.movies.enums.TypeEnum;
import com.example.movies.ui.activity.PersonActivity;
import com.example.movies.utils.HttpTool;
import com.example.movies.utils.OkHttpTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电影
 */
public class RecordFragment extends Fragment {
    private Activity myActivity;
    private RadioGroup rg_classify;
    private RadioGroup rg_type;
    private RecyclerView rvList;
    private RecordAdapter recordAdapter;
    private ImageView ivSetting;
    private List<Results> list;
    private SmartRefreshLayout srlRecord;//刷新
    private int type = TypeEnum.Movies.getCode();
    private int classify = ClassifyEnum.Collect.getCode();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        rvList = view.findViewById(R.id.rv_list);
        rg_classify = view.findViewById(R.id.rg_classify);
        rg_type = view.findViewById(R.id.rg_type);
        ivSetting = view.findViewById(R.id.setting);
        srlRecord = view.findViewById(R.id.srl_list);
        //init();

        rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_collect) {
                    classify = ClassifyEnum.Collect.getCode();
                } else if (checkedId == R.id.rb_record) {
                    classify = ClassifyEnum.History.getCode();
                }
                srlRecord.autoRefresh();
            }
        });
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_type_1) {
                    type = TypeEnum.Movies.getCode();
                } else if (checkedId == R.id.rb_type_2) {
                    type = TypeEnum.Books.getCode();
                } else if (checkedId == R.id.rb_type_3) {
                    type = TypeEnum.Game.getCode();
                }
                srlRecord.autoRefresh();
            }
        });


        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"编辑资料", "切换主题", "退出登录"};//创建item
                AlertDialog alertDialog = new AlertDialog.Builder(myActivity)
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0://编辑资料
                                        Intent intent = new Intent(myActivity, PersonActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 1://切换主题

                                        break;
                                    case 2://退出登录
                                        HttpTool.logout(myActivity);
                                        break;
                                }

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        //下拉刷新
        srlRecord.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        srlRecord.autoRefresh();
        return view;
    }

    private void loadData() {
        list = new ArrayList<>();
        String url = "";
        if (classify==ClassifyEnum.Collect.getCode()){//收藏
            if (type == TypeEnum.Movies.getCode()) {
                url = OkHttpTool.URL + "/user/movies/favorite/";
            } else if (type == TypeEnum.Books.getCode()) {
                url = OkHttpTool.URL + "/user/books/favorite/";
            }
        }else  if (classify==ClassifyEnum.History.getCode()){//历史
            if (type == TypeEnum.Movies.getCode()) {
                url = OkHttpTool.URL + "/user/movies/click/";
            } else if (type == TypeEnum.Books.getCode()) {
                url = OkHttpTool.URL + "/user/books/click/";
            }
        }
        Map<String, Object> map = new HashMap<>();
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type typeBean = new TypeToken<List<Record>>() {
                            }.getType();//列表信息
                            List<Record> list = gson.fromJson(response, typeBean);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            rvList.setLayoutManager(layoutManager);
                            recordAdapter = new RecordAdapter(type);
                            rvList.setAdapter(recordAdapter);
                            recordAdapter.addItem(list);
                            recordAdapter.setItemListener(new RecordAdapter.ItemListener() {
                                @Override
                                public void ItemClick(Record record, int type) {
                                    HttpTool.goDetail(myActivity,type,record.getResults().getId());
                                }
                            });
                            srlRecord.finishRefresh();//刷新完成
                            Toast.makeText(myActivity, "加载成功", Toast.LENGTH_SHORT).show();
                        } else {
                            srlRecord.finishRefresh(false);//刷新失败
                            Toast.makeText(myActivity, "加载失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }
}
