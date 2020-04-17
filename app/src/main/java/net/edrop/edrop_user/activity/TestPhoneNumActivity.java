package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.User;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;
import static net.edrop.edrop_user.utils.Constant.LOGIN_FAIL;
import static net.edrop.edrop_user.utils.Constant.LOGIN_SUCCESS;
import static net.edrop.edrop_user.utils.Constant.NEW_USER;

public class TestPhoneNumActivity extends Activity {

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phone_num);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phoneNum");
        initView();
        OkHttpPhoneLogin(phone);
//        Intent intent = new Intent(TestPhoneNumActivity.this, Main2Activity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
    }

    private void initView() {
        okHttpClient = new OkHttpClient();


    }

    //通过okhttp发送通过验证的手机号
    private void OkHttpPhoneLogin(final String phoneNum) {

        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "loginByPhone?phone=" + phoneNum).build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);
        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJson = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseJson);
                    String state = jsonObject.getString("state");
                    String userjson = jsonObject.getString("user");

                    User user = new Gson().fromJson(userjson, User.class);
                    int userId = user.getId();
                    if (Integer.valueOf(state) == LOGIN_FAIL) {
                        Toast.makeText(TestPhoneNumActivity.this, "软件运行异常，请退出重试", Toast.LENGTH_SHORT).show();


                    } else if (Integer.valueOf(state) == NEW_USER) {
                        //新用户登录
//                        Toast.makeText(TestPhoneNumActivity.this,"新用户",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TestPhoneNumActivity.this, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(TestPhoneNumActivity.this, "loginInfo");
                        SharedPreferences.Editor editor = sharedPreferencesUtils.getEditor();

                        editor.putInt("userId",userId);
                        editor.putString("userType", "new");
                        editor.commit();
                        overridePendingTransition(0, 0);
                        startActivity(intent);

                    } else if (Integer.valueOf(state) == LOGIN_SUCCESS) {
                        //老用户登录
//                        Toast.makeText(TestPhoneNumActivity.this,"老用户",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TestPhoneNumActivity.this, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(TestPhoneNumActivity.this, "loginInfo");
                        SharedPreferences.Editor editor = sharedPreferencesUtils.getEditor();
                        editor.putString("userType", "old");
                        editor.putInt("userId",userId);
                        editor.putString("username",user.getUsername() );
                        editor.putString("password", user.getPassword());
                        editor.commit();
                        overridePendingTransition(0, 0);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.e("response", responseJson);
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
