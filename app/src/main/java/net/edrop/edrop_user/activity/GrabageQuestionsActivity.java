package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.Competition;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GrabageQuestionsActivity extends AppCompatActivity {
    private List<Competition> lists = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                List<Competition> competitions = new Gson().fromJson((String) msg.obj, new TypeToken<List<Competition>>() {
                }.getType());
//                Log.e("obj", (String) msg.obj);
                for (int i = 0; i < competitions.size(); i++) {
                    Competition competition = competitions.get(i);
                    lists.add(competition);
//                    Log.e("添加：", i + "" + competition.toString());
                }
                Intent intent = new Intent(GrabageQuestionsActivity.this, Answer2Activity.class);
                intent.putExtra("lists", (Serializable) lists);
//                Log.e("===================", lists.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(GrabageQuestionsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabage_questions);
        okHttpClient = new OkHttpClient();
    }

    public void buttonOnclicked(View view) {
        switch (view.getId()) {
            case R.id.btn_problems:
                lists.clear();
                Request request = new Request.Builder()
                        .url(Constant.BASE_URL + "getRandData")
                        .method("GET", null)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseJson = response.body().string();
//                        Log.e("response", responseJson);
                        Message message = new Message();
                        message.what = 10;
                        message.obj = responseJson;
                        handler.sendMessage(message);
                    }
                });
                break;
        }
    }
}
