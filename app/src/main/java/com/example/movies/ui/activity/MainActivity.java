package com.example.movies.ui.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movies.ui.fragment.HomeFragment;
import com.example.movies.ui.fragment.RecordFragment;
import com.example.movies.R;
import com.example.movies.ui.fragment.UserFragment;


/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {
    private Activity myActivity;
    private LinearLayout llContent;
    private RadioButton rbHome;
    private RadioButton rbMovies;
    private RadioButton rbUser;
    private Fragment[] fragments = new Fragment[]{null, null, null};//存放Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);
        llContent = (LinearLayout) findViewById(R.id.ll_main_content);
        rbHome = (RadioButton) findViewById(R.id.rb_main_home);
        rbMovies = (RadioButton) findViewById(R.id.rb_main_bookrack);
        rbUser = (RadioButton) findViewById(R.id.rb_main_user);
        initView();
        setViewListener();
    }

    private void setViewListener() {

        rbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(0);
            }
        });

        rbMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(1);
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(2);
            }
        });
    }

    private void initView() {
        //设置导航栏图标样式
        Drawable icon1 = getResources().getDrawable(R.drawable.selector_main_rb_home);//设置主页图标样式
        icon1.setBounds(0, 0, 120, 120);//设置图标边距 大小
        rbHome.setCompoundDrawables(null, icon1, null, null);//设置图标位置
        rbHome.setCompoundDrawablePadding(10);//设置文字与图片的间距

        Drawable icon2 = getResources().getDrawable(R.drawable.selector_main_rb_bookrack);//设置主页图标样式
        icon2.setBounds(0, 0, 120, 120);//设置图标边距 大小
        rbMovies.setCompoundDrawables(null, icon2, null, null);//设置图标位置
        rbMovies.setCompoundDrawablePadding(10);//设置文字与图片的间距

        Drawable icon3 = getResources().getDrawable(R.drawable.selector_main_rb_user);//设置主页图标样式
        icon3.setBounds(0, 0, 120, 120);//设置图标边距 大小
        rbUser.setCompoundDrawables(null, icon3, null, null);//设置图标位置
        rbUser.setCompoundDrawablePadding(10);//设置文字与图片的间距
        rbHome.setChecked(true);
        switchFragment(0);
    }

    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            switch (fragmentIndex) {
                case 0:
                    fragments[fragmentIndex] = new HomeFragment();
                    break;
                case 1:
                    fragments[fragmentIndex] = new RecordFragment();
                    break;
                case 2:
                    fragments[fragmentIndex] = new UserFragment();
                    break;
            }
            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }



    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(myActivity, "再点击一次退出应用程序", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }
}
