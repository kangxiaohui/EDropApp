package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
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
import xyz.bboylin.universialtoast.UniversalToast;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ImageView finishActivity;
    private ZLoadingDialog dialog;
    private WebView webView;
    private String html;
    private OkHttpClient okHttpClient;
    private String markdown = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ArticleDetailsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        initView();
        initData();
        initOption();
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

    private void initOption() {
        try {
            html = new Markdown4jProcessor().process(markdown);
            webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
            WebSettings webSettings = webView.getSettings();
            webSettings.setDomStorageEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        dialog = new ZLoadingDialog(ArticleDetailsActivity.this);
        okHttpClient = new OkHttpClient();
        webView = findViewById(R.id.web_view);
        finishActivity = findViewById(R.id.iv_article_details_finish);
    }

    private void initData() {
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#00FF7F"))
                .setHintText("正在加载中...")
                .setHintTextColor(Color.GRAY)
                .setDialogBackgroundColor(Color.parseColor("#cc111111"))
                .show();
        Intent intent = getIntent();
        int articleId = intent.getIntExtra("articleId",0);
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
            public void onResponse(Call call, Response response) throws IOException {
                markdown = response.body().string();
                Log.e("markdown",markdown);
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                });
            }
        });
    }
}
