package net.edrop.edrop_user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.edrop.edrop_user.model.Model;
import net.edrop.edrop_user.model.bean.IMUserInfo;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.QQConfig;
import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.QQUser;
import net.edrop.edrop_user.entity.User;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;
import static net.edrop.edrop_user.utils.Constant.LOGIN_SUCCESS;
import static net.edrop.edrop_user.utils.Constant.PASSWORD_WRONG;
import static net.edrop.edrop_user.utils.Constant.USER_NO_EXISTS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    private Button btnLogin;
    private Button btnPhoneLogin;
    private Button btnRegister;
    private EditText edUserName;
    private EditText edPwd;
    private TextView tvForgetPsd;
    public boolean isSelected = false;
    private ImageView qqLogin;
    //qq
    private Tencent mTencent;
    private UserInfo userInfo;
    private static BaseUiListener listener = null;
    private String QQ_uid;//qq_openid
    private SharedPreferencesUtils sharedPreferences;
    private OkHttpClient okHttpClient;
    private String username;
    private String password;
    private static final int REQUEST_PERMISSION_CODE = 123;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 9) {
                Toast.makeText(LoginActivity.this, "QQ登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            if (msg.what == PASSWORD_WRONG) {
                Toast.makeText(LoginActivity.this, "密码错误，请重试!", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == USER_NO_EXISTS) {
                Toast.makeText(LoginActivity.this, "该账号不存在，请先注册！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(LoginActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = new SharedPreferencesUtils(LoginActivity.this, "loginInfo");
        isAuto();
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        listener = new BaseUiListener();
        mTencent = Tencent.createInstance(QQConfig.QQ_LOGIN_APP_ID, this.getApplicationContext());
        initView();
        setListener();
        okHttpClient = new OkHttpClient();
    }

    //判断是否是第一次登陆
    private void isAuto() {
        boolean isAuto = sharedPreferences.getBoolean("isAuto");
        String loginName=sharedPreferences.getString("username","");
        if (isAuto) {
            //登陆环信
            //判断当前账号是否已经登陆过环信
            if(EMClient.getInstance().isLoggedInBefore()){//登陆过
                //获取当前登陆用户的信息
                IMUserInfo account = Model.getInstance().getUSerAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                if (account!=null){
                    //登录成功后的方法
                    Model.getInstance().loginSuccess(account);
                    //提示登录成功
                    UniversalToast.makeText(LoginActivity.this, "登录成功", UniversalToast.LENGTH_SHORT,
                            UniversalToast.EMPHASIZE)
                            .setLeftIconRes(R.drawable.ic_check_circle_white_24dp)
                            .show();
                    //跳转到主页面
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"请登录",Toast.LENGTH_SHORT).show();
                }
            }
                finish();
//            loginIMByDB(loginName);
        }
    }

    private void loginIMByDB(final String loginName) {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(loginName, loginName, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //对模型层数据处理
                                Model.getInstance().loginSuccess( new IMUserInfo( loginName ) );
                                //提示登录成功
                                UniversalToast.makeText(LoginActivity.this, "登录成功", UniversalToast.LENGTH_SHORT,
                                        UniversalToast.EMPHASIZE)
                                        .setLeftIconRes(R.drawable.ic_check_circle_white_24dp)
                                        .show();
                                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } );
                    }

                    @Override
                    public void onError(int i, final String s) {
                        //提示登录失败
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( LoginActivity.this, "登录失败" + s, Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(LoginActivity.this)
                                .setMessage("登陆中，请稍后...")
                                .setCancelable(true)
                                .setCancelOutside(true);
                        LoadingDailog dialog=loadBuilder.create();
                        dialog.show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_PERMISSION_CODE) {
            String text = Settings.canDrawOverlays(this) ? "已获取悬浮窗权限" : "请打开悬浮窗权限";
            UniversalToast.makeText(this, text, UniversalToast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        tvForgetPsd = findViewById(R.id.tv_forget_psd);
        usernameWrapper = findViewById(R.id.usernameWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        edPwd = findViewById(R.id.password);
        edUserName = findViewById(R.id.username);
        btnPhoneLogin = findViewById(R.id.btn_phone_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        qqLogin = findViewById(R.id.qq_name);
        usernameWrapper.setHint("请输入用户名");
        passwordWrapper.setHint("请输入密码");
        edPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!validatePassword(edPwd.getText().toString())) {
                    passwordWrapper.setError("请输入6位数有效的密码哦!");
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login_gray_background));
                } else {
                    btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login_background));
                    isSelected = true;
                    passwordWrapper.setErrorEnabled(false);
                    hideKeyboard();
                }
            }
        });
    }

    private void setListener() {
        tvForgetPsd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        usernameWrapper.setOnClickListener(this);
        passwordWrapper.setOnClickListener(this);
        btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!requestPermission()) {
                    return;
                }
                username = edUserName.getText().toString().trim();
                password = edPwd.getText().toString().trim();
                if (isSelected) {
                    username = edUserName.getText().toString();
                    password = edPwd.getText().toString();
                    OkHttpLogin(username, password);
                    loginIMByUserNameAndPassword(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "请检查用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.qq_name:
                Log.e("qq", "开始QQ登录..");
                if (!mTencent.isSessionValid()) {
                    //注销登录 mTencent.logout(this);
                    mTencent.login(LoginActivity.this, "all", listener);
                }
                break;
            case R.id.tv_forget_psd:
                Intent intent = new Intent(LoginActivity.this, ForgetPsdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private void loginIMByUserNameAndPassword(final String username, String password) {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(username, username, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //对模型层数据处理
                                Model.getInstance().loginSuccess( new IMUserInfo( username ) );
                                //提示登录成功
                                UniversalToast.makeText(LoginActivity.this, "登录成功", UniversalToast.LENGTH_SHORT,
                                        UniversalToast.EMPHASIZE)
                                        .setLeftIconRes(R.drawable.ic_check_circle_white_24dp)
                                        .show();
                                //保存用户账号信息到本地数据库
                                Model.getInstance().getUSerAccountDao().addAccount( new IMUserInfo( username ) );
                                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        } );
                    }

                    @Override
                    public void onError(int i, final String s) {
                        //提示登录失败
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( LoginActivity.this, "登录失败" + s, Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(LoginActivity.this)
                                .setMessage("登陆中，请稍后...")
                                .setCancelable(true)
                                .setCancelOutside(true);
                        LoadingDailog dialog=loadBuilder.create();
                        dialog.show();
                    }
                });
            }
        });
    }

    /***
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 自动弹出软键盘
     */
    public static void showSoftkeyboard(final EditText etID, final Context mContext) {
        etID.post(new Runnable() {
            @Override
            public void run() {
                etID.requestFocus(etID.getText().length());
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etID, 0);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
    }

    /***
     * 校验密码
     * @param password
     * @return
     */
    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.e("qq", "授权:" + o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateUserInfo() {
            if (mTencent != null && mTencent.isSessionValid()) {
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onError(UiError e) {
                        String errorMessage = e.errorMessage;
                        Log.e("更新用户失败信息：",errorMessage);
                    }

                    @Override
                    public void onComplete(final Object response) {
                        com.alibaba.fastjson.JSONObject response1 = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(response));
                        Log.e("qq", "UserInfo:" + JSON.toJSONString(response));
                        QQUser user = com.alibaba.fastjson.JSONObject.parseObject(response1.toJSONString(), QQUser.class);
                        if (user != null) {
                            Log.e("qq", "userInfo:昵称：" + user.getNickname() + "  性别:" + user.getGender() + "  地址：" + user.getProvince() + user.getCity());
                            Log.e("qq", "头像路径：" + user.getFigureurl_qq_2());
                            Log.e("qq", "qquid：" + QQ_uid);
                            FormBody formBody = new FormBody.Builder()
                                    .add("username", user.getNickname())
                                    .add("password", "123456")
                                    .add("qq", QQ_uid + "")
                                    .add("gender", user.getGender())
                                    .add("address", user.getProvince() + user.getCity())
                                    .build();
                            Request request = new Request.Builder()
                                    .url(Constant.BASE_URL + "loginByQq")
                                    .post(formBody)
                                    .build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String string = response.body().string();
                                    String user1 = null;
                                    try {
                                        JSONObject jsonObject= new JSONObject(string);
                                        user1 = jsonObject.getString("user");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    User u = new Gson().fromJson(user1, User.class);
                                    SharedPreferences.Editor editor = sharedPreferences.getEditor();
                                    editor.putInt("userId", u.getId());
                                    editor.putString("gender", u.getGender());
                                    editor.putString("phone", u.getPhone());
                                    editor.putString("username", u.getUsername());
                                    editor.putString("password", u.getPassword());
                                    editor.putString("imgName", u.getImgname());
                                    editor.putString("imgPath", u.getImgpath());
                                    editor.putString("address", u.getAddress());
                                    editor.putString("detailAddress", u.getDetailAddress());
                                    editor.putBoolean("isAuto", true);
                                    editor.commit();
                                    Message msg = new Message();
                                    msg.obj = response;
                                    msg.what = 9;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancel() {
                        Log.e("qq", "登录取消..");
                    }
                };
                userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onError(UiError e) {
            Log.e("qq", "onError:code:" + e.errorCode +
                    ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.e("qq", "onCancel");
        }

    }

    /**
     * 用户名密码登录
     */
    private void OkHttpLogin(final String username, String password) {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "loginByUsernameAndPassword?username=" + username + "&password=" + password).build();
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
                String responseJson = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseJson);
                    String state = jsonObject.getString("state");
                    if (Integer.valueOf(state) == LOGIN_SUCCESS) {
                        //登录成功
                        String userJson = jsonObject.getString("user");
                        User user = new Gson().fromJson(userJson, User.class);
                        SharedPreferences.Editor editor = sharedPreferences.getEditor();
                        editor.putInt("userId", user.getId());
                        editor.putString("gender", user.getGender());
                        editor.putString("phone", user.getPhone());
                        editor.putString("username", user.getUsername());
                        editor.putString("password", user.getPassword());
                        editor.putString("imgName", user.getImgname());
                        editor.putString("imgPath", user.getImgpath());
                        editor.putString("address", user.getAddress());
                        editor.putString("detailAddress", user.getDetailAddress());
                        editor.putBoolean("isAuto", true);
                        editor.commit();
                    } else if (Integer.valueOf(state) == PASSWORD_WRONG) {
                        //密码错误
                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = PASSWORD_WRONG;
                        mHandler.sendMessage(msg);
                    } else if (Integer.valueOf(state) == USER_NO_EXISTS) {
                        //用户不存在
                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = USER_NO_EXISTS;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent intent = new Intent();// 创建Intent对象
            intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
            intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
            startActivity(intent);// 将Intent传递给Activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(this)) {
                UniversalToast.makeText(this, "请允许悬浮窗权限", UniversalToast.LENGTH_SHORT).showWarning();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +
                        getPackageName()));
                startActivityForResult(intent, REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

}
