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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.adapter.RecommendAdapter;
import com.example.movies.bean.Results;
import com.example.movies.enums.ClassifyEnum;
import com.example.movies.enums.TypeEnum;
import com.example.movies.ui.activity.LoginActivity;
import com.example.movies.ui.activity.PersonActivity;
import com.example.movies.ui.activity.RecordActivity;
import com.example.movies.ui.activity.StatisticsActivity;
import com.example.movies.utils.HttpTool;
import com.example.movies.utils.OkHttpTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户页面
 */
public class UserFragment extends Fragment {
    private Activity myActivity;
    private ImageView ivPhoto;
    private ImageView ivSetting;
    private ImageView ivGo;
    private TextView tvName;
    private LinearLayout ll_collect;
    private LinearLayout ll_history;
    private LinearLayout ll_recommendation;
    private LinearLayout ll_statistics;
    private RecyclerView rv_like_list;
    private RecommendAdapter recommendAdapter;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private RequestOptions backgroundRO = new RequestOptions().centerCrop();//会缩放图片让图片充满整个ImageView的边框,然后裁掉超出的部分

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ivSetting = view.findViewById(R.id.setting);
        ivPhoto = view.findViewById(R.id.iv_photo);
        ivGo = view.findViewById(R.id.iv_go);
        tvName = view.findViewById(R.id.tv_name);
        ll_collect = view.findViewById(R.id.ll_collect);
        ll_history = view.findViewById(R.id.ll_history);
        ll_recommendation = view.findViewById(R.id.ll_recommendation);
        ll_statistics = view.findViewById(R.id.ll_statistics);
        rv_like_list = view.findViewById(R.id.rv_like_list);
        initView();
        loadUser();
        loadPreferenceData();
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("立即登录".equals(tvName)){
                    Intent intent = new Intent(myActivity, LoginActivity.class);
                    startActivity(intent);
                    myActivity.finish();
                }
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
        ivGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, RecordActivity.class);
                intent.putExtra("classify", ClassifyEnum.Collect.getCode());
                startActivity(intent);
            }
        });
        ll_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, RecordActivity.class);
                intent.putExtra("classify", ClassifyEnum.History.getCode());
                startActivity(intent);
            }
        });
        ll_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ll_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, StatisticsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);//两列
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //=1.3、设置recyclerView的布局管理器
        rv_like_list.setLayoutManager(layoutManager);
        recommendAdapter = new RecommendAdapter();
        rv_like_list.setAdapter(recommendAdapter);
        recommendAdapter.setItemListener(new RecommendAdapter.ItemListener() {
            @Override
            public void ItemClick(Results results) {

                HttpTool.goDetail(myActivity,TypeEnum.Movies.getCode(),results.getId());
            }

        });

    }

    private void loadUser(){
        String url = OkHttpTool.URL + "/user/profile/";
        OkHttpTool.httpGet(url, new HashMap<>(), new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String username = jsonObject.getString("username");
                                String email = jsonObject.getString("email");
                                String nickname = jsonObject.getString("nickname");
                                String description = jsonObject.getString("description");
                                String avatar = OkHttpTool.URL+ jsonObject.getString("avatar");
                                tvName.setText(nickname);
                                Glide.with(myActivity)
                                        .load(avatar)
                                        .apply(headerRO.error(R.mipmap.ic_acatar))
                                        .into(ivPhoto);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                        }
                    }
                });
            }
        });
    }


    //获取心悦推荐列表
    private void loadPreferenceData() {
        String url = OkHttpTool.URL + "/movies/recommendations/";
        Map<String, Object> map = new HashMap<>();
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type type = new TypeToken<List<Results>>() {}.getType();//列表信息
                            List<Results> list = gson.fromJson(response, type);
                            recommendAdapter.addItem(list);

                            // 打印部分数据到控制台
                            Log.d("loadPreferenceData", "获取到的数据：" + list.toString());
                        } else {
                            // 打印错误信息
                            Log.e("loadPreferenceData", "数据读取失败，响应码：" + responseCode, exception);
                            Log.e("loadPreferenceData", "数据读取失败：" + response);
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
