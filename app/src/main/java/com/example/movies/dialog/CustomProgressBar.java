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
import android.view.MotionEvent;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.movies.R;

/**
 * 自定义该view，主要是实现将进度条设置为圆角并支持拖动效果。
 */
public class CustomProgressBar extends ProgressBar {
    private Paint paint;
    private RectF rectF;
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

        // 设置渐变色
        paint.setShader(new LinearGradient(0f, 0f, width, 0f,
                Color.parseColor("#03A9F4"), Color.parseColor("#2795B2"), Shader.TileMode.CLAMP));

        // 绘制圆角矩形进度条
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    // 设置连续动画，从左往右类似加载
    public void setProgressWithAnimation(int progress, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", this.getProgress(), progress);
        animator.setDuration(duration);
        animator.start();
    }

    // 实现拖动效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX(); // 获取触摸的X坐标
            int width = getWidth();
            int newProgress = (int) ((x / width) * getMax()); // 将触摸位置转换为进度值
            setProgress(newProgress); // 更新进度条显示
            return true;
        }
        return super.onTouchEvent(event);
    }


}
