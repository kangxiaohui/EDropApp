package net.edrop.edrop_user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mob.MobSDK;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.QQUser;
import net.edrop.edrop_user.utils.QQConfig;
import net.edrop.edrop_user.utils.SystemTransUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PhoneLoginActivity extends Activity implements View.OnClickListener{
    private String APPKEY = "2d3bde91c4a25";
    private String APPSECRET = "0f4a5150f9707ef7423d60cf7aaf3ae8";
    private Button btnPwdLogin;
    // 手机号输入框
    private EditText inputPhoneEt;
    // 验证码输入框
    private EditText inputCodeEt;
    // 获取验证码按钮
    private Button requestCodeBtn;
    // 登录按钮
    private Button btnPhoneLogin;
    //倒计时显示   可以手动更改。
    int i = 60;
    //qq
    private ImageView qqLogin;
    private Tencent mTencent;
    private UserInfo userInfo;
    private static PhoneLoginActivity.BaseUiListener listener = null;
    private String QQ_uid;//qq_openid
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(PhoneLoginActivity.this);
        super.onCreate(savedInstanceState);
        listener = new BaseUiListener();
        mTencent = Tencent.createInstance(QQConfig.QQ_LOGIN_APP_ID, this.getApplicationContext());
        setContentView(R.layout.activity_phone_login);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }

    private void initView() {
        btnPwdLogin=findViewById(R.id.btn_pwd_login);
        inputPhoneEt = (EditText) findViewById(R.id.et_phoneNum);
        inputCodeEt = (EditText) findViewById(R.id.et_request_Code);
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);
        btnPhoneLogin = (Button) findViewById(R.id.btn_request_login);
        qqLogin=findViewById(R.id.qq);

        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
        setListener();
    }
    private void setListener() {
        btnPwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        requestCodeBtn.setOnClickListener(this);
        btnPhoneLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();  //取出输入的手机号
        switch (v.getId()) {
            case R.id.qq:
                Log.e("qq","开始QQ登录..");
                if (!mTencent.isSessionValid()) {
                    //注销登录 mTencent.logout(this);
                    mTencent.login(PhoneLoginActivity.this, "all", listener);
                }
                break;
            case R.id.login_request_code_btn:
                // 1. 判断手机号是不是11位并且看格式是否合理
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setBackgroundResource(R.drawable.btn_login_gray_background);
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.btn_request_login:
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt.getText().toString());
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
//                requestCodeBtn.setBackgroundColor(R.drawable.btn_login_gray_background);
                requestCodeBtn.setText("重新发送(" + i + ")");

            } else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setBackgroundResource(R.drawable.btn_login_green_background);
                requestCodeBtn.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Log.e("test", "1");
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
//                        Toast.makeText(getApplicationContext(), "提交验证码成功",
//                                Toast.LENGTH_SHORT).show();
                        Log.e("test", "2");
                        String phoneNums = inputPhoneEt.getText().toString();
//                        OkHttpPhoneLogin(phoneNums);

                        Intent intent = new Intent(PhoneLoginActivity.this,
                                TestPhoneNumActivity.class);
                        intent.putExtra("phoneNum",phoneNums);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("phoneNum", inputPhoneEt.getText().toString().trim());
//                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码，请及时接收登录",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    private class BaseUiListener  implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.e("qq","授权:"+o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            }catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateUserInfo() {
            if (mTencent != null && mTencent.isSessionValid()) {
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onError(UiError e) {
                    }
                    @Override
                    public void onComplete(final Object response) {
                        Message msg = new Message();
                        msg.obj = response;
                        Log.e("qq","................"+response.toString());
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onCancel() {
                        Log.e("qq","登录取消..");
                    }
                };
                userInfo = new UserInfo(PhoneLoginActivity.this, mTencent.getQQToken());
                userInfo.getUserInfo(listener);
            }
        }

        private void initOpenidAndToken(org.json.JSONObject jsonObject) {
            try {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                        && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                    QQ_uid = openId;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    com.alibaba.fastjson.JSONObject response = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(msg.obj));
                    Log.e("qq","UserInfo:"+ JSON.toJSONString(response));
                    QQUser user= com.alibaba.fastjson.JSONObject.parseObject(response.toJSONString(),QQUser.class);
                    if (user!=null) {
                        Log.e("qq","userInfo:昵称："+user.getNickname()+"  性别:"+user.getGender()+"  地址："+user.getProvince()+user.getCity());
                        Log.e("qq","头像路径："+user.getFigureurl_qq_2());
//                        Glide.with(QQLoginActivity.this).load(user.getFigureurl_qq_2()).into(ivHead);
                    }
                }
            }
        };

        @Override
        public void onError(UiError e) {
            Log.e("qq","onError:code:" + e.errorCode +
                    ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }
        @Override
        public void onCancel() {
            Log.e("qq","onCancel");
        }

    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
