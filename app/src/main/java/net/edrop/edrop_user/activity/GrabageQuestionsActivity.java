package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.Competition;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<Competition> lists = new ArrayList<>();
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().transform(GrabageQuestionsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabage_questions);
        okHttpClient = new OkHttpClient();
    }

    public void buttonOnclicked(View view) {
        switch (view.getId()) {
            case R.id.btn_problems:
                lists.clear();
//        for (int i = 0; i < 10; i++) {
//            Problems bean = new Problems(i, 2, "我是第" + i);
//            lists.add(bean);
//        }
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
                        Log.e("response", responseJson);
                        try {
                            List<Competition> competitions = new Gson().fromJson(responseJson, new TypeToken<List<Competition>>() {}.getType());
                            for (int i = 0; i < competitions.size(); i++) {
                                Competition competition = new Competition(competitions.get(i).getId(),competitions.get(i).getQuestion(),competitions.get(i).getTypeId(),competitions.get(i).getType());
                                lists.add(competition);
                            }
                            Log.e("ddd", lists.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Intent intent = new Intent(GrabageQuestionsActivity.this, Answer2Activity.class);
                intent.putExtra("lists", (Serializable) lists);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
        }
    }
}
