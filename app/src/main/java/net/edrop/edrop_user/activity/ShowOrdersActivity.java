package net.edrop.edrop_user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.ShowOrderAdapter;
import net.edrop.edrop_user.entity.Order;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowOrdersActivity extends Activity {
    private SmartRefreshLayout refreshLayout;
    private List<Order> orderList = null;
    private ListView listView;
    private int userId;
    private OkHttpClient okHttpClient;
    private ImageView imvBack;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String json = (String) msg.obj;
                orderList = new Gson().fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                setMyAdapter();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ShowOrdersActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);
        SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(ShowOrdersActivity.this, "loginInfo");
        userId = loginInfo.getInt("userId");

        findView();
        json2objectList();
        setMyAdapter();
        setListeners();
    }

    private void findView() {
        imvBack = findViewById(R.id.iv_showorders_back);
        refreshLayout = findViewById(R.id.smart_layout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        listView = findViewById(R.id.lv_showOrders);
        okHttpClient = new OkHttpClient();
    }

    private void setMyAdapter() {
        ShowOrderAdapter adapter = new ShowOrderAdapter(orderList, ShowOrdersActivity.this, R.layout.item_showorders_layout);
        listView.setAdapter(adapter);
    }

    private void json2objectList() {
        Intent intent = getIntent();
        String orderjson = intent.getStringExtra("orderjson");
//        Log.e("test", orderjson);
        orderList = new Gson().fromJson(orderjson, new TypeToken<List<Order>>() {
        }.getType());
    }

    private void setListeners() {
        //监听下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                FormBody formBody = new FormBody.Builder()
                        .add("userId", userId + "").build();
                Request request = new Request.Builder()
                        .url(Constant.BASE_URL + "getOrderById")
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
                        refreshLayout.finishRefresh();
                        Message message = new Message();
                        message.what = 1;
                        message.obj = string;
                        handler.sendMessage(message);

                    }
                });
            }
        });
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent intent = new Intent(ShowOrdersActivity.this, Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
