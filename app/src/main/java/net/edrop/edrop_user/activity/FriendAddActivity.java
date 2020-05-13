package net.edrop.edrop_user.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.model.bean.IMUserInfo;
import net.edrop.edrop_user.model.Model;
import net.edrop.edrop_user.utils.SystemTransUtil;

import xyz.bboylin.universialtoast.UniversalToast;

public class FriendAddActivity extends AppCompatActivity {
    private TextView tv_add_find;
    private EditText et_add_name;
    private RelativeLayout rl_add;
    private TextView tv_add_name;
    private Button bt_add_add;
    private IMUserInfo userInfo;
    private TextView tvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(FriendAddActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        initView();
        initListener();
    }
    private void initView() {
        tvFinish = findViewById(R.id.add_friend_finish);
        tv_add_find = findViewById(R.id.tv_add_find);
        et_add_name = findViewById(R.id.et_add_name);
        rl_add = findViewById(R.id.rl_add);
        tv_add_name = findViewById(R.id.tv_add_name);
        bt_add_add = findViewById(R.id.bt_add_add);
    }
    private void initListener() {
        //查找按钮的点击事件处理
        tv_add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
        //添加按钮的点击事件处理
        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //查找按钮处理
    private void find() {
        //获取输入用户的名称
        final String name = et_add_name.getText().toString();

        //校验输入的名称
        if (TextUtils.isEmpty(name)) {
            UniversalToast.makeText(FriendAddActivity.this, "输入的用户名不能为空", UniversalToast.LENGTH_SHORT).showWarning();
            return;
        }else {
            //去服务器判断当前用户是否存在
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    //去服务器判断当前的用户是否存在
                    userInfo = new IMUserInfo(name);//默认有---到服务器端判断
                    //更新UI显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rl_add.setVisibility(View.VISIBLE);
                            tv_add_name.setText(userInfo.getName());
                        }
                    });
                }
            });
        }
    }
    //添加按钮处理
    private void add() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(), "添加好友");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UniversalToast.makeText(FriendAddActivity.this, "发送添加好友邀请成功", UniversalToast.LENGTH_SHORT,
                                    UniversalToast.EMPHASIZE)
                                    .setLeftIconRes(R.drawable.ic_check_circle_white_24dp)
                                    .show();
                        }
                    });
                    finish();
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UniversalToast.makeText(FriendAddActivity.this, "发送添加好友邀请失败", UniversalToast.LENGTH_SHORT).showWarning();
                        }
                    });
                }

            }
        });
    }
}
