package net.edrop.edrop_user.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.CommunityViewAdapter;
import net.edrop.edrop_user.entity.Article;
import net.edrop.edrop_user.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommunityPageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private RecyclerView articlesRecyclerView;    //定义listview
    private CommunityViewAdapter listViewAdapter;   //给listview的适配器
    private View myView;
    private List<Article> articles=new ArrayList<>();
    private SmartRefreshLayout refreshLayout;
    private OkHttpClient okHttpClient;
    private int currentPage=1;
    private int pageSize=2;
    private int[] image = {R.drawable.default_head_img6, R.drawable.default_head_img4, R.drawable.default_head_img1,
            R.drawable.default_head_img2, R.drawable.default_head_img3 , R.drawable.default_head_img5,
            R.drawable.logo, R.drawable.logo};
    private List<Map<String, Object>> imagelist = new ArrayList<Map<String, Object>>();

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
        okHttpClient=new OkHttpClient();
        initView();
        initData();
        return myView;
    }

    private void initView() {
        articlesRecyclerView = myView.findViewById(R.id.lv_articles);
        refreshLayout=myView.findViewById(R.id.srl);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            Article article = new Article();
            article.setId(i);
            article.setDatetime("发布于2020-5-21");
            article.setContent("第一种就是检查你的数据有没有更新。\n" +
                    "第二种你可以先清空mList，然后调用addAll()方法添加新的数据。\n" +
                    "你也可以用新的list数据源再new一个新的adapter设置给ListView，但是不推荐这样做了。\n" +
                    "如果你发现你的数据源更新了，但是页面的数据并没有更新，你也可以检查一下是否忘记调用notifyDataSetChanged了\n");
            articles.add(article);
        }
        for (int i = 0; i < 5; i++) {  //for循环添加
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            imagelist.add(map);
        }
        listViewAdapter = new CommunityViewAdapter(getActivity(), R.layout.item_list_announcement, articles,imagelist);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(myView.getContext(), LinearLayoutManager.VERTICAL, false));
        articlesRecyclerView.setAdapter(listViewAdapter);

        //禁用下拉刷新
        refreshLayout.setEnableRefresh(false);
        //监听上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(++currentPage,pageSize);
            }
        });
    }

    private void requestData(int pageNum,int pageSize){
        Request request=new Request.Builder()
                .url(Constant.BASE_URL+"BookListServlet?pageNum="+pageNum+"&pageSize="+pageSize)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bookListStr = response.body().string();
                Type type = new TypeToken<List<Article>>() {}.getType();//调用TypeToken的子类对象的方法
//                books=new Gson().fromJson(bookListStr,type);//因为List<Book>.class不可用 会解析为List.class
                articles.addAll((List<Article>) new Gson().fromJson(bookListStr,type));
                //发送消息
                EventBus.getDefault().post("列表");//通知所有的订阅者（包括主线程）:发布事件
            }
        });
    }

    //消息的处理方法：形参类型同消息一致
    @Subscribe(threadMode = ThreadMode.MAIN)//在主线程中进行
    public void updateUI(String  msg){
        if (msg.equals("列表")){
            //更新界面
            listViewAdapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();//结束上拉加载更多动画
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
