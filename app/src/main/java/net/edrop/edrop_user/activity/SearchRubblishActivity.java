package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.HotSearchAdapter;
import net.edrop.edrop_user.adapter.SearchAdapter;
import net.edrop.edrop_user.entity.HotItem;
import net.edrop.edrop_user.entity.Rubbish;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;
import static net.edrop.edrop_user.utils.Constant.SEARCH_SUCCESS;

public class SearchRubblishActivity extends AppCompatActivity {
    //搜索框控件
    private SearchView searchView;
    private AutoCompleteTextView mAutoCompleteTextView;//搜索输入框
    private ImageView mDeleteButton;//搜索框中的删除按钮
    private RecyclerView searchRes;//搜索的结果
    private List<String> findList = new ArrayList<>();//部分结果
    private SearchAdapter searchAdapter;
    //历史记录
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout container;
    private List<String> history = new ArrayList<>();
    private ImageView ivDelete;
    //热搜榜
    private List<HotItem> hotItems = new ArrayList<>();
    private ListView hotItemListView;
    private HotSearchAdapter hotSearchAdapter;
    private OkHttpClient okHttpClient;
    private List<Rubbish> rubbishList = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SEARCH_SUCCESS) {
                searchRes.setLayoutManager(new LinearLayoutManager(SearchRubblishActivity.this));
                searchAdapter = new SearchAdapter(findList,SearchRubblishActivity.this,SearchRubblishActivity.this,rubbishList);
                searchAdapter.notifyDataSetChanged();
                searchRes.setAdapter(searchAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(SearchRubblishActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rubblish);
        initView();
        initData();
        bindHZSWData();
        setListener();
    }

    private void initView() {
        //搜索框
        searchView = findViewById(R.id.view_search);
        mAutoCompleteTextView = searchView.findViewById(R.id.search_src_text);
        mDeleteButton = searchView.findViewById(R.id.search_close_btn);
        searchRes = findViewById(R.id.search_result);
        //历史记录：横向布局
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        container = (LinearLayout) findViewById(R.id.horizontalScrollViewItemContainer);
        ivDelete = findViewById(R.id.iv_delete);
        //热搜榜
        hotItemListView = findViewById(R.id.lv_hot_search);
        hotSearchAdapter = new HotSearchAdapter(SearchRubblishActivity.this, R.layout.item_hot_search, hotItems);
        hotItemListView.setAdapter(hotSearchAdapter);
        okHttpClient = new OkHttpClient();
    }

    private void initData() {
        hotItems.add(new HotItem("1", "便签纸"));
        hotItems.add(new HotItem("2", "报纸"));

        searchView.setIconifiedByDefault(false);//设置搜索图标是否显示在搜索框内
        //1:回车2:前往3:搜索4:发送5:下一項6:完成
        searchView.setImeOptions(2);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
//        mSearchView.setInputType(1);//设置输入类型
//        mSearchView.setMaxWidth(200);//设置最大宽度
        searchView.setQueryHint("查找分类");//设置查询提示字符串
//        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        setUnderLinetransparent(searchView);
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置搜索文本监听
     **/
    private void setListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    findList.clear();
                    searchRes.setAdapter(searchAdapter);
                } else {
                    findList.clear();
                    OkHttpQuery(newText);

                }
                return true;
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history.clear();
                SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(SearchRubblishActivity.this,"searchHistory");
                sharedPreferences.removeValues("history");
                Toast.makeText(SearchRubblishActivity.this, "历史记录已清空", Toast.LENGTH_SHORT).show();
            }
        });
        hotItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(SearchRubblishActivity.this,"searchHistory");
                SharedPreferences.Editor editor = sharedPreferences.getEditor();
                String history = sharedPreferences.getString("history", "");
                if (history.equals("")){
                    editor.putString("history",String.valueOf(parent.getItemAtPosition(position)));
                }else {
                    editor.putString("history",history+","+parent.getItemAtPosition(position)+"");
                }
                editor.commit();
                Toast.makeText(SearchRubblishActivity.this, String.valueOf(hotItemListView.getItemAtPosition(position)), Toast.LENGTH_SHORT).show();
                OkHttpQuery(parent.getItemAtPosition(position).toString());
            }
        });
    }

    /**
     * 查询垃圾种类
     *
     * @param query
     */
    private void OkHttpQuery(String query) {
        final Request request = new Request.Builder().url(BASE_URL + "searchRubbishByName?name=" + query).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
//                Log.e("test", jsonStr);
                if (jsonStr.length()==0){
                    Toast.makeText(SearchRubblishActivity.this,"系统正在升级，该垃圾不能识别",Toast.LENGTH_SHORT).show();
                }
                rubbishList = new Gson().fromJson(jsonStr, new TypeToken<List<Rubbish>>() {
                }.getType());
//                Log.e("test", rubbishList.toString());
                int count = 0;
                for (Rubbish rubbish : rubbishList) {
                    if (count < 5) {
                        Rubbish rubbish1 = new Rubbish(rubbish.getId(), rubbish.getName(), rubbish.getTypeId(), rubbish.getType());
                        findList.add(rubbish1.getName());
                        count++;
                    } else {
                        break;
                    }
                }
                Message msg = new Message();
                msg.obj = response;
                msg.what = SEARCH_SUCCESS;
                mHandler.sendMessage(msg);
            }
        });

    }


    /**
     * 历史记录的绑定
     **/
    private void bindHZSWData() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20, 10, 20, 10);
        SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(SearchRubblishActivity.this,"searchHistory");
        String h = sharedPreferences.getString("history", "");
        String[] historys = h.split(",");
        for (int i = 0; i < historys.length; i++) {
            history.add(historys[i]);
        }
        for (int i = 0; i < history.size(); i++) {
            final Button button = new Button(this);
            button.setText(history.get(i));
            button.setTextSize(10);
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_search_gray_background));
            button.setLayoutParams(layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(SearchRubblishActivity.this,"searchHistory");
                    SharedPreferences.Editor editor = sharedPreferences.getEditor();
                    String history = sharedPreferences.getString("history", "");
                    if (history.equals("")){
                        editor.putString("history",button.getText().toString());
                    }else {
                        editor.putString("history",history+","+button.getText().toString());
                    }
                    editor.commit();
                    mAutoCompleteTextView.setText(button.getText().toString());
                }
            });
            container.addView(button);
            container.invalidate();
        }
    }

    private void performItemClick(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;

        int scrollX = (view.getLeft() - (screenWidth / 2)) + (view.getWidth() / 2);

        //smooth scrolling horizontalScrollView
        horizontalScrollView.smoothScrollTo(scrollX, 0);

        String s = "CenterLocked Item: " + ((TextView) view).getText();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(SearchRubblishActivity.this,Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
