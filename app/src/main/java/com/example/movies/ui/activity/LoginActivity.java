package com.example.movies.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movies.R;
import com.example.movies.utils.OkHttpTool;
import com.example.movies.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etAccount;//账号
    private EditText etPassword;//密码
    private TextView tvRegister;//注册
    private Button btnLogin;//登录按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = findViewById(R.id.et_account);//获取手机号
        etPassword= findViewById(R.id.et_password);//获取密码
        tvRegister= findViewById(R.id.tv_register);//获取注册
        btnLogin= findViewById(R.id.btn_login);//获取登录
        //手机号注册
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //设置点击按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取请求参数
                String username= etAccount.getText().toString();
                String password=etPassword.getText().toString();
                if ("".equals(username)){//用户名不能为空
                    Toast.makeText(LoginActivity.this,"用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(LoginActivity.this,"密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = OkHttpTool.URL + "/user/auth/login/";
                String jsonStr = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
                OkHttpTool.httpPostJson(url, jsonStr, new OkHttpTool.ResponseCallback() {
                    @Override
                    public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSuccess && responseCode == 200) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String refresh = jsonObject.getString("refresh");
                                        String access = jsonObject.getString("access");
                                        SPUtils.put(LoginActivity.this,SPUtils.ACCESS,access);
                                        SPUtils.put(LoginActivity.this,SPUtils.REFRESH,refresh);
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        OpenActivity.access=access;
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
