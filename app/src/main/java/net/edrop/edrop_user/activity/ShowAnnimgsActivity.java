package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.CommunityPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.bboylin.universialtoast.UniversalToast;

public class ShowAnnimgsActivity extends AppCompatActivity {
    private ImageView iv;
    private ViewPager viewPager;  //声明viewpage
    private List<View> listViews = null;  //用于获取图片资源
    private int index = 0;   //获取当前点击的图片位置
    private CommunityPagerAdapter adapter;   //ViewPager的适配器
    private ImageView finishActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
        setContentView(R.layout.show_annimgs_layout);    //绑定布局
        finishActivity = findViewById(R.id.show_annimgs_finish);
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();

    }

    private void initData() {
        final Intent intent = getIntent();   //获取intent传递的信息
        Bundle bundle = intent.getExtras();
        List<Map<String, Object>> imagelist = (List<Map<String, Object>>) bundle.getSerializable("image");
        viewPager = (ViewPager) findViewById(R.id.show_view_pager);  //绑定viewpager的id
        listViews = new ArrayList<View>();   //初始化list
        for (int i = 0; i < imagelist.size(); i++) {  //for循环将试图添加到list中
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
            iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
            iv.setBackgroundResource((Integer) imagelist.get(i).get("image"));   //设置当前点击的图片
            listViews.add(view);
        }
        adapter = new CommunityPagerAdapter(listViews);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new PageChangeListener()); //设置viewpager的改变监听
        //截取intent获取传递的值
        viewPager.setCurrentItem(intent.getIntExtra("id", 0));    //viewpager显示指定的位置

    }

    /**
     * pager的滑动监听
     * */
    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            if (arg0==0){
                UniversalToast.makeText(ShowAnnimgsActivity.this, "这已经是第一张了！", UniversalToast.LENGTH_SHORT).showWarning();
            }else if (arg0==listViews.size()-1){
                UniversalToast.makeText(ShowAnnimgsActivity.this, "这已经是最后一张了！", UniversalToast.LENGTH_SHORT).showWarning();
            }
            index = arg0;
        }

    }
}

