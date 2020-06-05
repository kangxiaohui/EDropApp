package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.ArticleDetailCommentAdapter;
import net.edrop.edrop_user.entity.ArticleComment;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

public class ArticleDetailCommentActivity extends AppCompatActivity {
    private ZLoadingDialog dialog;
    private List<ArticleComment> articleComments = new ArrayList<>();
    private ListView commentListView;
    private ArticleDetailCommentAdapter articleDetailCommentAdapter;
    private OkHttpClient okHttpClient;
    private EditText etActicleComment;
    private Button btnActicleComment;
    private int articleId;
    private ImageView finishActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ArticleDetailCommentActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail_comment);
        //将主线程注册成为订阅者
        EventBus.getDefault().register(this);
        okHttpClient = new OkHttpClient();
        dialog = new ZLoadingDialog(ArticleDetailCommentActivity.this);
        Intent intent = getIntent();
        articleId = intent.getIntExtra("articleId", 0);
        initView();
        setListener();
        requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        articleDetailCommentAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        btnActicleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etActicleComment.getText().toString())) {
                    sendComment(etActicleComment.getText().toString());
                } else {
                    UniversalToast.makeText(ArticleDetailCommentActivity.this, "评论内容不能为空！", UniversalToast.LENGTH_SHORT).showWarning();
                }
            }
        });
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendComment(String articleContent) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", String.valueOf(new SharedPreferencesUtils(ArticleDetailCommentActivity.this, "loginInfo").getInt("userId")))
                .add("articleId", String.valueOf(articleId))
                .add("articleContent", articleContent)
                .build();
        Request request = new Request.Builder()
                .url(Constant.NEWS_URL + "community/comment_article ")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etActicleComment.setText("");
                        UniversalToast.makeText(ArticleDetailCommentActivity.this, "发送成功", Toast.LENGTH_SHORT).showSuccess();
                        finish();
                        Intent intent = new Intent(ArticleDetailCommentActivity.this, ArticleDetailCommentActivity.class);
                        intent.putExtra("articleId", articleId);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    //消息的处理方法：形参类型同消息一致
    @Subscribe(threadMode = ThreadMode.MAIN)//在主线程中进行
    public void updateUI(String msg) {
        if (msg.equals("评论列表")) {
            articleDetailCommentAdapter.notifyDataSetChanged();
        }
    }

    private void requestData() {
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#00FF7F"))
                .setHintText("正在加载中...")
                .setHintTextColor(Color.GRAY)
                .setDialogBackgroundColor(Color.parseColor("#cc111111"))
                .show();
        FormBody formBody = new FormBody.Builder()
                .add("articleId", String.valueOf(articleId))
                .build();
        Request request = new Request.Builder()
                .url(Constant.NEWS_URL + "community/load_comment")
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
                String str = response.body().string();
                dialog.dismiss();
                Type type = new TypeToken<List<ArticleComment>>() {
                }.getType();
                articleComments.addAll((List<ArticleComment>) new Gson().fromJson(str, type));
                //发送消息
                EventBus.getDefault().post("评论列表");
            }
        });
    }

    private void initView() {
        finishActivity = findViewById(R.id.article_comment_back);
        etActicleComment = findViewById(R.id.et_article_comment);
        btnActicleComment = findViewById(R.id.btn_article_comment);
        commentListView = findViewById(R.id.article_comment_lv);
        articleDetailCommentAdapter = new ArticleDetailCommentAdapter(ArticleDetailCommentActivity.this, R.layout.item_detail_comment, articleComments);
        commentListView.setAdapter(articleDetailCommentAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
