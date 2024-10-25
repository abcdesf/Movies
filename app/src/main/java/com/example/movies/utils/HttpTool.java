package com.example.movies.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.movies.bean.Results;
import com.example.movies.enums.TypeEnum;
import com.example.movies.ui.activity.BooksDetailActivity;
import com.example.movies.ui.activity.MoviesDetailActivity;
import com.example.movies.ui.activity.OpenActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpTool {
    public static void goDetail(Activity myActivity, int type, int id) {
        String url = "";
        if (type == TypeEnum.Movies.getCode()) {
            url = OkHttpTool.URL + "/movies/" + id;
        } else if (type == TypeEnum.Books.getCode()) {
            url = OkHttpTool.URL + "/books/" + id;
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
                            Results results = gson.fromJson(response, Results.class);
                            if (type == TypeEnum.Movies.getCode()) {
                                Intent intent = new Intent(myActivity, MoviesDetailActivity.class);
                                intent.putExtra("results", results);
                                myActivity.startActivity(intent);
                            } else if (type == TypeEnum.Books.getCode()) {
                                Intent intent = new Intent(myActivity, BooksDetailActivity.class);
                                intent.putExtra("results", results);
                                myActivity.startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

    }


    public static void logout(Activity myActivity){
        SPUtils.clear(myActivity);
        Intent intent = new Intent(myActivity, OpenActivity.class);
        myActivity.startActivity(intent);
        Toast.makeText(myActivity, "退出成功", Toast.LENGTH_SHORT).show();
    }
}