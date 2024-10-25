package com.example.movies.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.adapter.CustomBannerAdapter;
import com.example.movies.adapter.Data2Adapter;
import com.example.movies.adapter.GenresAdapter;
import com.example.movies.bean.BannerBean;
import com.example.movies.bean.Data;
import com.example.movies.bean.GenresVo;
import com.example.movies.bean.Results;
import com.example.movies.enums.TypeEnum;
import com.example.movies.ui.activity.HotListActivity;
import com.example.movies.ui.activity.PersonActivity;
import com.example.movies.ui.activity.PreferenceListActivity;
import com.example.movies.ui.activity.TypeListActivity;
import com.example.movies.utils.HttpTool;
import com.example.movies.utils.OkHttpTool;
import com.example.movies.utils.RecyclerViewSpaces;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */
public class HomeFragment extends Fragment {
    private Activity myActivity;
    private RadioGroup rgType;
    private RadioButton rbMovies;
    private RadioButton rbFiction;
    private RadioButton rbGame;
    private Banner mBanner;//轮播顶部
    private ImageView ivSetting;
    private RecyclerView rvHotList;
    private TextView tvHotShow;
    private TextView tvHotChange;

    private RecyclerView rvTypeList;
    private TextView tvTypeShow;
    private TextView tvTypeChange;

    private RecyclerView rvTagList;

    private RecyclerView rvPreferenceList;
    private TextView tvPreferenceShow;
    private TextView tvPreferenceChange;

    private GenresAdapter genresAdapter;
    private Data2Adapter hotAdapter;
    private Data2Adapter typeAdapter;
    private Data2Adapter preferenceAdapter;


    private int cureType = TypeEnum.Movies.getCode();
    private int hotPage = 1;
    private int typePage = 1;
    private List<GenresVo> genresVoList;

    private String genres = "";
    private String typeName = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ivSetting = view.findViewById(R.id.setting);
        rgType = view.findViewById(R.id.rg_type);
        rbMovies = view.findViewById(R.id.rb_main_movies);
        rbFiction = view.findViewById(R.id.rb_main_fiction);
        rbGame = view.findViewById(R.id.rb_main_game);
        mBanner = view.findViewById(R.id.banner);
        rvHotList = view.findViewById(R.id.rv_hot_list);
        tvHotShow = view.findViewById(R.id.tv_hot_show);
        tvHotChange = view.findViewById(R.id.tv_hot_change);

        rvTagList = view.findViewById(R.id.rv_tag_list);
        rvTypeList = view.findViewById(R.id.rv_type_list);
        tvTypeShow = view.findViewById(R.id.tv_type_show);
        tvTypeChange = view.findViewById(R.id.tv_type_change);

        rvPreferenceList = view.findViewById(R.id.rv_preference_list);
        tvPreferenceShow = view.findViewById(R.id.tv_preference_show);
        tvPreferenceChange = view.findViewById(R.id.tv_preference_change);
        initView();
        setViewListener();
        return view;
    }


    private void setViewListener() {

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
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_main_movies) {
                    cureType = TypeEnum.Movies.getCode();
                } else if (checkedId == R.id.rb_main_fiction) {
                    cureType = TypeEnum.Books.getCode();
                } else if (checkedId == R.id.rb_main_game) {
                    cureType = TypeEnum.Game.getCode();
                }
                hotPage = 1;
                typePage = 1;
                loadHotData();
                loadGenresData();
                //  loadTypeData();
                loadPreferenceData();
            }
        });
        tvHotShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, HotListActivity.class);
                intent.putExtra("type", cureType);
                startActivity(intent);
            }
        });
        tvHotChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotPage += 1;
                loadHotData();
            }
        });
        tvTypeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, TypeListActivity.class);
                intent.putExtra("type", cureType);
                intent.putExtra("genres", genres);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
            }
        });
        tvTypeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePage += 1;
                loadTypeData();
            }
        });
        tvPreferenceShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, PreferenceListActivity.class);
                intent.putExtra("type", cureType);
                String url = "";
                if (cureType == TypeEnum.Movies.getCode()) {
                    url = OkHttpTool.URL + "/movies/recommendations/";
                } else if (cureType == TypeEnum.Books.getCode()) {
                    url = OkHttpTool.URL + "/books/recommendations/";
                }
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        tvPreferenceChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(myActivity, "暂无更多数据", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        //图片资源
        int[] imageResourceID = new int[]{R.mipmap.lb_1, R.mipmap.lb_2, R.mipmap.lb_3, R.mipmap.lb_4, R.mipmap.lb_5, R.mipmap.lb_6, R.mipmap.lb_7, R.mipmap.lb_8, R.mipmap.lb_9};
        List<BannerBean> list = new ArrayList<>();
        for (Integer image : imageResourceID) {
            list.add(new BannerBean(image));
        }
        //轮播
        mBanner.addBannerLifecycleObserver(this)
                .setAdapter(new CustomBannerAdapter(list))
                .setIndicator(new CircleIndicator(myActivity));
        mBanner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
            }
        });

        mBanner.setClipToOutline(true);


        //设置导航栏图标样式
        Drawable icon1 = getResources().getDrawable(R.drawable.selector_main_rb_movies);//设置主页图标样式
        icon1.setBounds(0, 0, 80, 80);//设置图标边距 大小
        rbMovies.setCompoundDrawables(null, icon1, null, null);//设置图标位置
        rbMovies.setCompoundDrawablePadding(5);//设置文字与图片的间距

        Drawable icon2 = getResources().getDrawable(R.drawable.selector_main_rb_fiction);//设置主页图标样式
        icon2.setBounds(0, 0, 80, 80);//设置图标边距 大小
        rbFiction.setCompoundDrawables(null, icon2, null, null);//设置图标位置
        rbFiction.setCompoundDrawablePadding(5);//设置文字与图片的间距

        Drawable icon3 = getResources().getDrawable(R.drawable.selector_main_rb_game);//设置主页图标样式
        icon3.setBounds(0, 0, 80, 80);//设置图标边距 大小
        rbGame.setCompoundDrawables(null, icon3, null, null);//设置图标位置
        rbGame.setCompoundDrawablePadding(5);//设置文字与图片的间距
        rbMovies.setChecked(true);


        init();
        loadHotData();
        loadGenresData();
        loadPreferenceData();
    }

    private void init(){

        LinearLayoutManager hotLayoutManager = new LinearLayoutManager(myActivity);
        hotLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHotList.setLayoutManager(hotLayoutManager);
        hotAdapter = new Data2Adapter(TypeEnum.Movies.getCode());
        rvHotList.setAdapter(hotAdapter);
        hotAdapter.setItemListener(new Data2Adapter.ItemListener() {
            @Override
            public void ItemClick(Results results, int type) {
                HttpTool.goDetail(myActivity,cureType,results.getId());
            }

        });


        LinearLayoutManager genresLayoutManager = new LinearLayoutManager(myActivity);
        genresLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTagList.setLayoutManager(genresLayoutManager);
        genresAdapter = new GenresAdapter();
        rvTagList.setAdapter(genresAdapter);

        LinearLayoutManager typeLayoutManager = new LinearLayoutManager(myActivity);
        typeLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTypeList.setLayoutManager(typeLayoutManager);
        typeAdapter = new Data2Adapter(TypeEnum.Movies.getCode());
        rvTypeList.setAdapter(typeAdapter);
        typeAdapter.setItemListener(new Data2Adapter.ItemListener() {
            @Override
            public void ItemClick(Results results, int type) {
                HttpTool.goDetail(myActivity,cureType,results.getId());
            }

        });

        GridLayoutManager layoutManager = new GridLayoutManager(myActivity, 5);//两列
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvPreferenceList.setLayoutManager(layoutManager);
        HashMap<String, Integer> mapSpaces = new HashMap<>();//间距
        mapSpaces.put(RecyclerViewSpaces.TOP_DECORATION, 20);//上间距
        mapSpaces.put(RecyclerViewSpaces.BOTTOM_DECORATION, 20);//下间距
        mapSpaces.put(RecyclerViewSpaces.LEFT_DECORATION, 20);//左间距
        mapSpaces.put(RecyclerViewSpaces.RIGHT_DECORATION, 20);//右间距
        rvPreferenceList.addItemDecoration(new RecyclerViewSpaces(mapSpaces));//设置间距
        preferenceAdapter = new Data2Adapter(TypeEnum.Movies.getCode());
        rvPreferenceList.setAdapter(preferenceAdapter);
        preferenceAdapter.setItemListener(new Data2Adapter.ItemListener() {
            @Override
            public void ItemClick(Results results, int type) {

                HttpTool.goDetail(myActivity,cureType,results.getId());
            }

        });
    }


    //获取热门列表
    private void loadHotData() {
        String url = "";
        if (cureType == TypeEnum.Movies.getCode()) {
            url = OkHttpTool.URL + "/movies";
        } else if (cureType == TypeEnum.Books.getCode()) {
            url = OkHttpTool.URL + "/books";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("page", hotPage);
        map.put("page_size", 5);
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Data data = gson.fromJson(response, Data.class);
                            hotAdapter.addItem(data.getResults());
                        }
                    }
                });
            }
        });

    }


    //加载分类标签
    private void loadGenresData() {
        genresVoList = new ArrayList<>();
        if (cureType == TypeEnum.Movies.getCode()) {
            genresVoList.add(new GenresVo(1, R.mipmap.m_comedy, "喜剧"));
            genresVoList.add(new GenresVo(2, R.mipmap.m_drama, "剧情"));
            genresVoList.add(new GenresVo(3, R.mipmap.m_action, "动作"));
            genresVoList.add(new GenresVo(4, R.mipmap.m_adventure, "冒险"));
            genresVoList.add(new GenresVo(5, R.mipmap.m_fantasy, "奇幻"));
            genresVoList.add(new GenresVo(6, R.mipmap.m_sci_fi, "科幻"));
            genresVoList.add(new GenresVo(7, R.mipmap.m_war, "战争"));
            genresVoList.add(new GenresVo(8, R.mipmap.m_romance, "爱情"));
            genresVoList.add(new GenresVo(9, R.mipmap.m_thriller, "惊悚"));
            genresVoList.add(new GenresVo(10, R.mipmap.m_crime, "犯罪"));
            genresVoList.add(new GenresVo(11, R.mipmap.m_film_noir, "黑色电影"));
            genresVoList.add(new GenresVo(12, R.mipmap.m_mystery, "悬疑"));
            genresVoList.add(new GenresVo(13, R.mipmap.m_children, "儿童"));
            genresVoList.add(new GenresVo(14, R.mipmap.m_horror, "恐怖"));
            genresVoList.add(new GenresVo(15, R.mipmap.m_animation, "动画"));
            genresVoList.add(new GenresVo(16, R.mipmap.m_musical, "音乐"));
            genresVoList.add(new GenresVo(17, R.mipmap.m_western, "西部片"));
            genresVoList.add(new GenresVo(18, R.mipmap.m_documentary, "纪录片"));
        } else if (cureType == TypeEnum.Books.getCode()) {
            genresVoList.add(new GenresVo(67, R.mipmap.x_foreign_literature, "外国文学"));
            genresVoList.add(new GenresVo(58, R.mipmap.x_china, "中国"));
            genresVoList.add(new GenresVo(36, R.mipmap.x_literature, "文学"));
            genresVoList.add(new GenresVo(1, R.mipmap.x_chinese_literature, "中国文学"));
            genresVoList.add(new GenresVo(132, R.mipmap.x_japan, "日本"));
            genresVoList.add(new GenresVo(26, R.mipmap.x_classics, "经典"));
            genresVoList.add(new GenresVo(176, R.mipmap.x_romance, "爱情"));
            genresVoList.add(new GenresVo(184, R.mipmap.x_essays, "随笔"));
            genresVoList.add(new GenresVo(135, R.mipmap.x_youth, "青春"));
        }
        genresAdapter.addItem(genresVoList);
        if (cureType == TypeEnum.Movies.getCode()) {
            genres = String.valueOf(genresVoList.get(0).getId());
        } else if (cureType == TypeEnum.Books.getCode()) {
            genres = genresVoList.get(0).getName();
        }
        typeName = genresVoList.get(0).getName();
        genresAdapter.setItemListener(new GenresAdapter.ItemListener() {
            @Override
            public void ItemClick(GenresVo genresVo) {
                if (cureType == TypeEnum.Movies.getCode()) {
                    genres = String.valueOf(genresVo.getId());
                } else if (cureType == TypeEnum.Books.getCode()) {
                    genres = genresVo.getName();
                }
                typeName = genresVo.getName();
                typePage = 1;
                loadTypeData();
            }

        });
        loadTypeData();
    }


    //获取分类列表
    private void loadTypeData() {
        String url = "";
        Map<String, Object> map = new HashMap<>();
        if (cureType == TypeEnum.Movies.getCode()) {
            url = OkHttpTool.URL + "/movies";
            map.put("genre_id", Integer.valueOf(genres));
        } else if (cureType == TypeEnum.Books.getCode()) {
            url = OkHttpTool.URL + "/books";
            map.put("tag", genres);
        }
        map.put("page", typePage);
        map.put("page_size", 5);
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Data data = gson.fromJson(response, Data.class);
                            typeAdapter.addItem(data.getResults());
                        }
                    }
                });
            }
        });
    }

    //获取个性化推荐列表
    private void loadPreferenceData() {
        String url = "";
        if (cureType == TypeEnum.Movies.getCode()) {
            url = OkHttpTool.URL + "/movies/recommendations/";
        } else if (cureType == TypeEnum.Books.getCode()) {
            url = OkHttpTool.URL + "/books/recommendations/";
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
                            Type type = new TypeToken<List<Results>>() {
                            }.getType();//列表信息
                            List<Results> list = gson.fromJson(response, type);
                            preferenceAdapter.addItem(list);
                        }
                    }
                });
            }
        });
    }
}
