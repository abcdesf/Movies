package com.example.movies.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movies.R;
import com.example.movies.bean.Record;
import com.example.movies.bean.Results;
import com.example.movies.bean.Statistics;
import com.example.movies.utils.OkHttpTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 个人报告
 */
public class StatisticsActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tv_date;
    private TextView books_read;
    private TextView most_watched_movie_count;
    private TextView movies_watched;
    private TextView percentage;
    private TextView favorite_book_genres;
    private TextView favorite_movie_genres;
    private TextView most_watched_movie;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_statistics);
        tv_date = findViewById(R.id.tv_date);
        books_read = findViewById(R.id.books_read);
        movies_watched = findViewById(R.id.movies_watched);
        percentage = findViewById(R.id.percentage);
        favorite_book_genres = findViewById(R.id.favorite_book_genres);
        favorite_movie_genres = findViewById(R.id.favorite_movie_genres);
        most_watched_movie = findViewById(R.id.most_watched_movie);
        most_watched_movie_count = findViewById(R.id.most_watched_movie_count);
        loadData();
    }


    private void loadData() {
        String url = OkHttpTool.URL + "/user/statistics/";
        Map<String, Object> map = new HashMap<>();
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Statistics statistics = gson.fromJson(response, Statistics.class);
                            if (statistics != null) {
                                tv_date.setText(sf.format(new Date()));
                                books_read.setText(String.valueOf(statistics.getBooks_read()));
                                most_watched_movie_count.setText(String.valueOf(statistics.getMost_watched_movie_count()));
                                movies_watched.setText(String.valueOf(statistics.getMovies_watched()));
                                percentage.setText(String.format("%d%%",statistics.getBooks_read()+statistics.getMovies_watched()
                                <=99?statistics.getBooks_read()+statistics.getMovies_watched():99));
                                if (statistics.getFavorite_book_genres() != null) {
                                    favorite_book_genres.setText(statistics.getFavoriteBookGenreNames().get(0));
                                }
                                if (statistics.getFavorite_movie_genres() != null) {
                                    favorite_movie_genres.setText(statistics.getFavoriteMovieGenreNames().get(0));
                                }
                                most_watched_movie.setText(String.valueOf(statistics.getMost_watched_movie()));
                            }
                        }
                        else {
                            // 打印错误信息
                            Log.e("statistics", "数据读取失败，响应码：" + responseCode, exception);
                            Log.e("statistics", "数据读取失败：" + response);
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }


    public void back(View view) {
        finish();
    }

}
