package com.example.movies.dialog;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.movies.R;

/**
 * Created by gudd on 2024/9/27.
 * 自定义该view，主要就是为了实现将进度条设置为圆角
 */
public class CustomProgressBar extends ProgressBar {
    private  Paint paint;
    private  RectF rectF;
    // 这两个颜色主要用来从resource中获取颜色，不至于将色值写死在自定义view中。
    private int progressColorStart = 0;
    private int progressColorEnd = 0;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        rectF = new RectF();
        progressColorStart = ContextCompat.getColor(context, R.color.colorPrimary);
        progressColorEnd = ContextCompat.getColor(context, R.color.colorPrimaryTint);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        float radius = 40f;
        float width = getWidth();
        float height = getHeight();


        // 设置圆角矩形区域
        rectF.set(0f, 0f, width * getProgress() / getMax(), height);
        // 设置渐变色,两种方法，喜欢哪种用哪种。
        /*paint.setShader(new LinearGradient(0f, 0f, width, 0f,
            progressColorStart, progressColorEnd, Shader.TileMode.CLAMP));*/
        paint.setShader(new LinearGradient(0f, 0f, width, 0f,
                Color.parseColor("#03A9F4"), Color.parseColor("#2795B2"), Shader.TileMode.CLAMP));

        canvas.drawRoundRect(rectF, radius, radius, paint); // 绘制圆角矩形
    }

    // 设置连续动画，从左往右类似加载
    public void setProgressWithAnimation(int progress, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", this.getProgress(), progress);
        animator.setDuration(duration);
        animator.start();
    }
}