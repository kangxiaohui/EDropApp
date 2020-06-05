package net.edrop.edrop_user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.SimpleIntroduceAdapter;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.util.ArrayList;
import java.util.List;

public class IntroductionEDropActivity extends AppCompatActivity{
    private SimpleIntroduceAdapter mAdapter;
    private List<String> mDatas;
    private RecyclerView rvToDoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new SystemTransUtil().transform(IntroductionEDropActivity.this);
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_introduction_edrop);
        findViewById(R.id.iv_introduction_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbar.setTitle("     EDrop简介");
        collapsingToolbar.setExpandedTitleMarginTop(10);
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.color_green_32BA88));
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);//设置展开后标题的颜色
        rvToDoList = (RecyclerView) findViewById(R.id.rvToDoList);
        recycleView();
    }
    private void recycleView() {
        mDatas = new ArrayList<String>();
        mDatas.add(getResources().getString(R.string.introduction_text));
        mAdapter = new SimpleIntroduceAdapter(IntroductionEDropActivity.this, mDatas);
        rvToDoList.setAdapter(mAdapter);
        //设置布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvToDoList.setLayoutManager(linearLayoutManager);
        rvToDoList.setItemAnimator(new DefaultItemAnimator());
    }
}
