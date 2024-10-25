package com.example.movies.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.bean.Data;
import com.example.movies.utils.OkHttpTool;
import com.example.movies.utils.PageCalculator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText etAccount;//账号
    private EditText etEmail;//地址
    private EditText etPassword;//密码
    private EditText etPasswordSure;//确认密码
    private Button btnRegister;//注册按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etAccount = findViewById(R.id.et_account);//获取账号
        etEmail = findViewById(R.id.et_email);//获取地址
        etPassword = findViewById(R.id.et_password);//获取密码
        etPasswordSure = findViewById(R.id.et_password_sure);//获取确认密码
        btnRegister = (Button) findViewById(R.id.btn_register);//获取注册按钮

        //设置注册点击按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取请求参数
                String username = etAccount.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordSure = etPasswordSure.getText().toString();

                if ("".equals(username)) {//用户名不能为空
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(email)) {//邮箱地址不能为空
                    Toast.makeText(RegisterActivity.this, "邮箱地址不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)) {//密码为空
                    Toast.makeText(RegisterActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(passwordSure)) {//密码不一致
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = OkHttpTool.URL + "/user/auth/register/";
                String jsonStr = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"password2\":\"" + passwordSure + "\",\"email\":\"" + email + "\"}";
                OkHttpTool.httpRegisterPostJson(url, jsonStr, new OkHttpTool.ResponseCallback() {
                    @Override
                    public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("RegisterActivity", "Response Code: " + responseCode); // 打印 responseCode
                                if (responseCode == 201) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Log.e("RegisterActivity", "注册失败：" + response);
                                    Toast.makeText(RegisterActivity.this, "注册失败：" + response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    public void back(View view) {
        finish();
    }
}
