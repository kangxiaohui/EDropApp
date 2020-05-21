package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import net.edrop.edrop_user.activity.LoginActivity;

import java.util.ArrayList;



public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;
    private Context context;

    public MyPagerAdapter(ArrayList<View> viewLists, Context context) {
        super();
        this.viewLists = viewLists;
        this.context = context;
    }


    @Override
    public int getCount() {
        return viewLists.size();
    }

    /**
     * 用来判断instantiateItem返回的Object与当前的View是否是同一对象
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 该方法两个作用：一、把要被显示的视图加载到ViewGroup,二、返回要加载的View
     *
     * @param container 被加载的View的父布局
     * @param position  要加载的View在数组或集合中的位置
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewLists.get(position));
        viewLists.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        return viewLists.get(position);
    }

    /**
     * 该方法在View被移除时调用,移除对应的View
     *
     * @param container 要被移除的View的父布局
     * @param position  表示要被移除的View在数组或集合中的位置
     * @param object    代表的就是要被移除的对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}
