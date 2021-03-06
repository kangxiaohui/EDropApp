package net.edrop.edrop_user.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.CommunityViewAdapter;
import net.edrop.edrop_user.entity.Article;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

public class CommunityPageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private RecyclerView articlesRecyclerView;
    private CommunityViewAdapter listViewAdapter;
    private View myView;
    private Integer userId;
    private List<Article> articles = new ArrayList<>();
    private SmartRefreshLayout refreshLayout;
    private OkHttpClient okHttpClient;
    private int currentPage = 1;
    private int pageSize = 5;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                if ((int)msg.obj==0){
                    UniversalToast.makeText(myView.getContext(),"亲，已经没有更多了",UniversalToast.LENGTH_SHORT).showSuccess();
                }else {
                    UniversalToast.makeText(myView.getContext(),"更新了"+msg.obj+"条数据",UniversalToast.LENGTH_SHORT).showSuccess();
                }
            }
        }
    };

    public static CommunityPageFragment newInstance(String sectionNumber) {
        CommunityPageFragment fragment = new CommunityPageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_community_page, container, false);
        //将主线程注册成为订阅者
        EventBus.getDefault().register(this);//注册时必须在activity启动时注册
        okHttpClient = new OkHttpClient();
        initView();
        initData();
        requestData(currentPage, pageSize);
        setListener();
        return myView;
    }

    private void setListener() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                initData();
                requestData(1, 5);
            }
        });
        //监听上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(++currentPage, pageSize);
            }
        });
    }

    private void initView() {
        articlesRecyclerView = myView.findViewById(R.id.lv_articles);
        refreshLayout = myView.findViewById(R.id.srl);
    }

    private void initData() {
        SharedPreferencesUtils sp = new SharedPreferencesUtils(myView.getContext(), "loginInfo");
        userId = sp.getInt("userId");
        listViewAdapter = new CommunityViewAdapter(getActivity(), R.layout.item_list_announcement, articles);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(myView.getContext(), LinearLayoutManager.VERTICAL, false));
        articlesRecyclerView.setAdapter(listViewAdapter);
    }

    private void requestData(int pageNum, int pageSize) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", String.valueOf(userId))
                .add("pageNum", String.valueOf(pageNum))
                .add("pageSize", String.valueOf(pageSize))
                .build();
        Request request = new Request.Builder()
                .url(Constant.NEWS_URL + "community/get_article_brief_info")
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
                String articleList = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(new JSONObject(articleList).getString("articles"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        Log.e("article", jsonObject.toString());
                        Article article = new Article();
                        article.setId(Integer.parseInt(jsonObject.getString("article_id")));
                        article.setDiscussCount(jsonObject.getString("comment_counts"));
                        article.setPraiseCount(jsonObject.getString("like_counts"));
                        article.setContent(jsonObject.getString("article_description"));
                        if (Integer.parseInt(jsonObject.getString("is_like")) == 1) {
                            article.setPraise(true);
                        } else {
                            article.setPraise(false);
                        }
                        article.setDatetime(getTime(jsonObject.getString("publish_date")));
                        articles.add(article);
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = jsonArray.length();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post("列表");
            }

            private String getTime(String publish_date) {
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH) + 1;
                int day = now.get(Calendar.DAY_OF_MONTH);
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                int second = now.get(Calendar.SECOND);
                String[] date = publish_date.substring(0, publish_date.length() - 2).split(" ");
                String[] ymd = date[0].split("-");
                if (year > Integer.parseInt(ymd[0])) {
                    return "发布于" + Math.abs(year - Integer.parseInt(ymd[0])) + "年前";
                } else if (month > Integer.parseInt(ymd[1])) {
                    return "发布于" + Math.abs(month - Integer.parseInt(ymd[1])) + "月前";
                } else if (day > Integer.parseInt(ymd[2])) {
                    return "发布于" + Math.abs(day - Integer.parseInt(ymd[2])) + "天前";
                }
                String[] hms = date[1].split(":");
                if (hour > Integer.parseInt(ymd[0])) {
                    return "发布于" + Math.abs(hour - Integer.parseInt(hms[0])) + "时前";
                } else if (minute > Integer.parseInt(ymd[1])) {
                    return "发布于" + Math.abs(minute - Integer.parseInt(hms[1])) + "分前";
                } else if (second > Integer.parseInt(ymd[2])) {
                    return "发布于" + Math.abs(second - Integer.parseInt(hms[2])) + "秒前";
                }
                return null;
            }
        });
    }

    //消息的处理方法：形参类型同消息一致
    @Subscribe(threadMode = ThreadMode.MAIN)//在主线程中进行
    public void updateUI(String msg) {
        if (msg.equals("列表")) {
            //更新界面
            listViewAdapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
