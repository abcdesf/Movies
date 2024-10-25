package com.example.movies.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.utils.GetImagePath;
import com.example.movies.utils.MPermissionUtils;
import com.example.movies.utils.OkHttpTool;
import com.example.movies.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人资料页面
 */
public class PersonActivity extends AppCompatActivity {
    private Activity myActivity;//上下文
    private ImageView ivAvatar;//头像
    private EditText etNickName;//昵称
    private TextView tvUserName;//用户名
    private TextView tvEmail;//邮箱
    private LinearLayout llAvatar;
    private int userId;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final int IMAGE_REQUEST_CODE = 100;
    private static final int IMAGE_REQUEST_CODE_GE7 = 101;
    private static final int CAMERA_REQUEST_CODE = 104;
    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 200;
    private File mGalleryFile;//存放图库选择是返回的图片
    private File mCropFile;//存放图像裁剪的图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;
        setContentView(R.layout.activity_person);
        ivAvatar=findViewById(R.id.iv_user_avatar);
        etNickName =findViewById(R.id.et_nickname);
        tvUserName =findViewById(R.id.tv_username);
        tvEmail =findViewById(R.id.tv_email);
        llAvatar=findViewById(R.id.ll_user_avatar);
        initView();//初始化页面
        setViewListener();//设置监听事件
    }


    /**
     * 初始化页面
     */
    private void initView() {
        String path = getCacheDir().getAbsolutePath();
        //相册的File对象
        mGalleryFile = new File(path, "IMAGE_GALLERY_NAME.jpg");
        //裁剪后的File对象
        mCropFile = new File(path, "PHOTO_FILE_NAME.jpg");
        loadUser();
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
                                String avatar = OkHttpTool.URL+jsonObject.getString("avatar");
                                etNickName.setText(nickname);
                                tvEmail.setText(email);
                                tvUserName.setText(username);
                                Glide.with(myActivity)
                                        .load(avatar)
                                        .apply(headerRO.error(R.mipmap.ic_acatar))
                                        .into(ivAvatar);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            // 打印错误信息
                            Log.e("profile", "数据读取失败，响应码：" + responseCode, exception);
                            Log.e("profile", "数据读取失败：" + response);
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 监听事件
     */
    private void setViewListener() {
        //头像
        llAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions=new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};//写入存储权限
                if (MPermissionUtils.checkPermissions(myActivity, permissions)) {//检查是否有权限
                    //访问系统图库
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);//设置打开文件的模式 读取
                    intent.setType("image/*");//告诉系统我要获取图片
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //Android7.0及以上
                        Uri uriForFile = FileProvider.getUriForFile(myActivity,
                                "com.example.movies.fileprovider", mGalleryFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //启动页面，设置请求码
                        startActivityForResult(intent, IMAGE_REQUEST_CODE_GE7);
                    } else {
                        //Android7.0一下
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGalleryFile));
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                    }
                }else {//没有权限
                    if (!MPermissionUtils.checkPermissions(myActivity, permissions)) {//检查是否有权限  //没有权限
                        //申请权限
                        MPermissionUtils.requestPermissionsResult(myActivity,200, permissions,
                                new MPermissionUtils.OnPermissionListener() {
                                    //权限通过的回调
                                    @Override
                                    public void onPermissionGranted() {
                                    }
                                    //权限不通过的回调
                                    @Override
                                    public void onPermissionDenied() {
                                        //判断是否有永久拒绝的权限
                                        if (MPermissionUtils.hasAlwaysDeniedPermission(myActivity, permissions)) {
                                        } else {
                                            Toast.makeText(myActivity, "您拒绝了应用需要的权限，部分功能将无法使用"
                                                    , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    /**
     * 返回图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (data != null || requestCode == CAMERA_REQUEST_CODE)) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE://版本<7.0  图库返回
                    //获取图片的全路径
                    Uri uri = data.getData();
                    //进行图像裁剪
                    startPhotoZoom(uri);
                    break;
                case IMAGE_REQUEST_CODE_GE7://版本>= 7.0 图库返回
                    //获取文件路径
                    String strPath = GetImagePath.getPath(myActivity, data.getData());
                    if (Tools.isNotNull(strPath)) {
                        File imgFile = new File(strPath);
                        //通过FileProvider创建一个content类型的Uri
                        Uri dataUri = FileProvider.getUriForFile(myActivity, "com.example.movies.fileprovider", imgFile);
                        //进行图像裁剪
                        startPhotoZoom(dataUri);
                    } else {
                        Toast.makeText(myActivity, "选择图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.REQUEST_CROP://Ucrop裁剪返回
                    Uri resultUri = UCrop.getOutput(data);
                    if (resultUri != null) {
                        //uri转文件路径
                        String strPathCrop = GetImagePath.getPath(myActivity, resultUri);
                        if (Tools.isNotNull(strPathCrop)) {
                            File fileUp = new File(strPathCrop);
                            //保存到服务器
                            if (fileUp.exists()) {
                                //=====上传文件

                                String url = OkHttpTool.URL + "/user/profile/";
                                //参数map
                                Map<String, Object> map = new HashMap<>();
                                //文件map
                                Map<String, File> fileMap = new HashMap<>();
                                fileMap.put("avatar", fileUp);
                                //发送请求
                                OkHttpTool.httpPostWithFile(url, map, fileMap, new OkHttpTool.ResponseCallback() {
                                    @Override
                                    public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                                        myActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (isSuccess && responseCode == 200) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        Glide.with(myActivity)
                                                                .load(strPathCrop)
                                                                .apply(headerRO.error(R.mipmap.ic_acatar))
                                                                .into(ivAvatar);
                                                        Toast.makeText(myActivity,"上传成功",Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                                return;
                            }
                        }
                    }
                    Toast.makeText(myActivity, "图片裁剪失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * 开始图片裁剪 使用UCrop
     *
     * @param uri uri地址
     */
    private void startPhotoZoom(Uri uri) {
        //裁剪后保存到文件中
        Uri cropFileUri = Uri.fromFile(mCropFile);
        UCrop uCrop = UCrop.of(uri, cropFileUri);//源文件url,裁剪后输出文件uri
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        uCrop.withOptions(options);
        //设置比例为1:1
        uCrop.withAspectRatio(1, 1);
        //注意！！！！Fragment中使用uCrop 必须这样，否则Fragment的onActivityResult接收不到回调
        uCrop.start(this);
    }

    public void save(View view){
        String nickname = etNickName.getText().toString();
        if ("".equals(nickname)) {
            Toast.makeText(myActivity, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = OkHttpTool.URL + "/user/profile/";
        //参数map
        Map<String, Object> map = new HashMap<>();

        map.put("nickname", nickname);
        //发送请求
        OkHttpTool.httpPostWithFile(url, map, null, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            Toast.makeText(myActivity,"保存成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void back(View view){
        finish();
    }


}
