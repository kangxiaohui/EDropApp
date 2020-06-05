package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.model.Model;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import xyz.bboylin.universialtoast.UniversalToast;

public class SettingActivity extends AppCompatActivity {
    private ImageView imageView;
    private RelativeLayout aboutlayout;
    private RelativeLayout updatelayout;
    private RelativeLayout cachelayout;
    private RelativeLayout feedbacklayout;
    private RelativeLayout thanklayout;
    private Button button;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(SettingActivity.this);
        super.onCreate(savedInstanceState);
        //去掉顶部标题
        setContentView(R.layout.activity_setting_main);
        findViews();
        initEvent();
    }


    /**
     * 获取控件对象
     */
    private void findViews() {
        thanklayout = findViewById(R.id.rl_setting_thank);
        imageView = findViewById(R.id.iv_setting_back);
        aboutlayout = findViewById(R.id.rl_setting_about);
        updatelayout = findViewById(R.id.rl_setting_update);
        feedbacklayout = findViewById(R.id.rl_setting_feedback);
        cachelayout = findViewById(R.id.rl_setting_cache);
        button = findViewById(R.id.btn_setting_quit);
    }

    /**
     * 绑定监听器
     */
    private void initEvent() {
        imageView.setOnClickListener(new MyListener());
        aboutlayout.setOnClickListener(new MyListener());
        updatelayout.setOnClickListener(new MyListener());
        feedbacklayout.setOnClickListener(new MyListener());
        cachelayout.setOnClickListener(new MyListener());
        button.setOnClickListener(new MyListener());
        thanklayout.setOnClickListener(new MyListener());
    }

    /**
     * 设置监听处理方法
     */
    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_setting_back://返回
                    finish();
                    break;
                case R.id.rl_setting_about://关于
                    intent = new Intent(SettingActivity.this, AboutEDropActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.rl_setting_update://更新信息
                    UniversalToast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).showSuccess();
                    break;
                case R.id.rl_setting_feedback://反馈消息
                    intent = new Intent(SettingActivity.this, FeedBackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.rl_setting_cache://清除缓存
                    intent = new Intent(SettingActivity.this, ClearCacheActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.rl_setting_thank:
                    intent = new Intent(SettingActivity.this, CopyrightInformationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.btn_setting_quit://退出账号
                    SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(SettingActivity.this, "loginInfo");
                    sharedPreferences.removeValues("username");
                    sharedPreferences.removeValues("password");
                    sharedPreferences.removeValues("userId");
                    SharedPreferences.Editor editor2 = sharedPreferences.getEditor();
                    editor2.putBoolean("isAuto", false);
                    editor2.commit();
                    toLoginOutIM();
                    Intent intent2 = new Intent(SettingActivity.this, LoginActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    finish();
                    break;
            }
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
                                //关闭DBHelper
                                Model.getInstance().getDBManager().close();
                                UniversalToast.makeText(SettingActivity.this, "注销成功", UniversalToast.LENGTH_SHORT,
                                        UniversalToast.EMPHASIZE).showSuccess();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        ZLoadingDialog dialog = new ZLoadingDialog(SettingActivity.this);
                        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                                .setLoadingColor(Color.parseColor("#00FF7F"))
                                .setHintText("注销中，请稍后...")
                                .setHintTextColor(Color.GRAY)
                                .setDialogBackgroundColor(Color.parseColor("#cc111111"))
                                .show();
                    }

                    @Override
                    public void onError(int code, String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UniversalToast.makeText(SettingActivity.this,"注销失败",Toast.LENGTH_SHORT).showError();
                            }
                        });
                    }
                });
            }
        });
    }
}