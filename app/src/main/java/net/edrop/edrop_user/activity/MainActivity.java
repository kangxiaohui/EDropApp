package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.MyPagerAdapter;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager vpGuiding;
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<View> viewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(MainActivity.this);
        super.onCreate(savedInstanceState);
        //判断是不是第一次登陆
        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isFirstRun){
            //第一次运行
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            setContentView(R.layout.activity_main);
            vpGuiding = findViewById(R.id.main_vpGuiding);
            viewPagerNormalLookLike();
        }else{
            //不是第一次运行
            vpGuiding = findViewById(R.id.main_vpGuiding);
            setContentView(R.layout.activity_main_several);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
                    intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
                    startActivity(intent);
                }
            }).start();
        }
    }

    //默认效果的
    public void viewPagerNormalLookLike() {
        //List集合赋值，用于给适配器传参数
        viewArrayList = new ArrayList<View>();
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        viewArrayList.add(layoutInflater.inflate(R.layout.item_viewpaper_one, null, false));
        viewArrayList.add(layoutInflater.inflate(R.layout.item_viewpaper_two, null, false));
        viewArrayList.add(layoutInflater.inflate(R.layout.item_viewpaper_three, null, false));

        //适配器赋值
        myPagerAdapter = new MyPagerAdapter(viewArrayList,this);
        //绑定数据适配器
        vpGuiding.setAdapter(myPagerAdapter);
    }

}
