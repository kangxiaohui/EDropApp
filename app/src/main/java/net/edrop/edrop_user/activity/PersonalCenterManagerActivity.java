package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.User;
import net.edrop.edrop_user.model.Model;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;

public class PersonalCenterManagerActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvPhone;
    private TextView tvAddress;
    private ImageView userImg;
    private ImageView ivGender;
    private ImageView personalBack;
    private Button cancelLogin;
    private TextView tvEditInfo;
    private OkHttpClient okHttpClient;
    private SharedPreferencesUtils sharedPreferences;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 888) {
                RequestOptions options = new RequestOptions().centerCrop();
                Glide.with(PersonalCenterManagerActivity.this)
                        .load(msg.obj)
                        .apply(options)
                        .into(userImg);
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(PersonalCenterManagerActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        sharedPreferences = new SharedPreferencesUtils(PersonalCenterManagerActivity.this, "loginInfo");
        initView();
        setListener();
        initData();
    }

    private void initData() {
        tvUsername.setText(sharedPreferences.getString("username", ""));
        tvPhone.setText(sharedPreferences.getString("phone", ""));
        tvAddress.setText(sharedPreferences.getString("address", ""));
        String gender = sharedPreferences.getString("gender", "");
        switch (gender) {
            case "boy":
                ivGender.setImageDrawable(getResources().getDrawable(R.drawable.gender_boy));
                break;
            case "girl":
                ivGender.setImageDrawable(getResources().getDrawable(R.drawable.gender_girl));
                break;
            case "secret":
                ivGender.setImageDrawable(getResources().getDrawable(R.drawable.gender_secret));
            default:
                break;
        }
        okHttpClient = new OkHttpClient();
        getImg();
    }

    private void getImg() {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "getUserInfoById?id=" + sharedPreferences.getInt("userId")).build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                String imgPath = "";
                String imgName = "";
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    imgName = jsonObject.getString("imgname");
                    imgPath = jsonObject.getString("imgpath");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 888;
                message.obj = BASE_URL.substring(0,BASE_URL.length()-1)+imgPath +"/"+ imgName;
                handler.sendMessage(message);
            }
        });
    }

    private void setListener() {
        cancelLogin.setOnClickListener(new MyListener());
        personalBack.setOnClickListener(new MyListener());
        tvEditInfo.setOnClickListener(new MyListener());
    }

    private void initView() {
        ivGender = findViewById(R.id.change_gender);
        tvAddress = findViewById(R.id.change_address);
        tvPhone = findViewById(R.id.change_phone);
        tvUsername = findViewById(R.id.change_username);
        tvEditInfo = findViewById(R.id.tv_edit_info);
        cancelLogin = findViewById(R.id.btn_cancel_login);
        personalBack = findViewById(R.id.personal_back);
        userImg = findViewById(R.id.change_head_img);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_cancel_login:
                    SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(PersonalCenterManagerActivity.this, "loginInfo");
                    sharedPreferences.removeValues("username");
                    sharedPreferences.removeValues("password");
                    sharedPreferences.removeValues("userId");
                    SharedPreferences.Editor editor2 = sharedPreferences.getEditor();
                    editor2.putBoolean("isAuto", false);
                    editor2.commit();
                    toLoginOutIM();
                    Intent intent2 = new Intent(PersonalCenterManagerActivity.this, LoginActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.personal_back:
                    finish();
                    break;
                case R.id.tv_edit_info:
                    Intent intent = new Intent(PersonalCenterManagerActivity.this, FillPersonalInforActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    break;
            }
        }

        /**
         * 退出环信登录
         */
        private void toLoginOutIM() {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    EMClient.getInstance().logout(true, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UniversalToast.makeText(PersonalCenterManagerActivity.this, "注销成功", UniversalToast.LENGTH_SHORT,
                                            UniversalToast.EMPHASIZE).showSuccess();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(PersonalCenterManagerActivity.this)
                                    .setMessage("注销中，请稍后...")
                                    .setCancelable(true)
                                    .setCancelOutside(true);
                            LoadingDailog dialog=loadBuilder.create();
                            dialog.show();
                        }

                        @Override
                        public void onError(int code, String message) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PersonalCenterManagerActivity.this,"注销失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}
