package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleDetailsActivity extends AppCompatActivity {
    private ImageView finishActivity;
    private ZLoadingDialog dialog;
    private WebView webView;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ArticleDetailsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        dialog = new ZLoadingDialog(ArticleDetailsActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#00FF7F"))
                .setHintText("正在加载中...")
                .setHintTextColor(Color.GRAY)
                .setDialogBackgroundColor(Color.parseColor("#cc111111"))
                .show();
        initView();
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        setListener();
    }

    private void setListener() {
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        okHttpClient = new OkHttpClient();
        webView = findViewById(R.id.web_view);
        finishActivity = findViewById(R.id.iv_article_details_finish);
    }

    private void initData() {
        Intent intent = getIntent();
        int articleId = intent.getIntExtra("articleId", 0);
        sendMessageByArticleId(articleId);
    }

    private void sendMessageByArticleId(int articleId) {
        FormBody formBody = new FormBody.Builder()
                .add("articleId", String.valueOf(articleId))
                .build();
        Request request = new Request.Builder()
                .url(Constant.NEWS_URL + "community/get_article_detail_info")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String content = response.body().string();
                final String[] markdown = {null};
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String html5 = null;
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            markdown[0] = jsonObject.getString("content");
                            html5 = new Markdown4jProcessor().process(markdown[0]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        webView.loadDataWithBaseURL(null, html5, "text/html", "UTF-8", null);
                    }
                });
            }
        });
    }
}
