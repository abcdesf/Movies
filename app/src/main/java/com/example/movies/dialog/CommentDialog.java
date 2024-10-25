package com.example.movies.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.enums.CommentEnum;

public class CommentDialog extends DialogFragment {
    private Activity activity;//上下文
    private Dialog dialog;//弹框
    private View view;
    private RatingBar ratingBar;
    private EditText etContent;
    private LinearLayout llSave;//保存
    private OnItemClickListener onItemClickListener;

    public CommentDialog(@NonNull Activity activity, OnItemClickListener onItemClickListener) {
        this.activity=activity;
        this.onItemClickListener=onItemClickListener;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog=new Dialog(activity, R.style.dialog);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//软键盘就会把dialog弹起，有的手机则会遮住dialog布局。
        view = View.inflate(getActivity(), R.layout.dialog_comment,null);
        dialog.setContentView(view);
        etContent =view.findViewById(R.id.et_content);
        ratingBar =view.findViewById(R.id.ratingBar);
        llSave =view.findViewById(R.id.ll_save);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.windowAnimations= R.style.dialog_bottom_top;//设置弹窗动画
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setViewListener();
        return dialog;
    }

    /**
     * 事件监听
     */
    private void setViewListener(){
        //保存
        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                if ("".equals(content)) {
                    Toast.makeText(activity, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                float rating = ratingBar.getRating();
                if (onItemClickListener!=null){
                    onItemClickListener.onSave(dialog,content, CommentEnum.getName(rating));
                }
            }
        });
    }



    public interface OnItemClickListener{
        void onSave(Dialog dialog,String content,String rating);
    }
}
