package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.CustomAdapter;
import net.edrop.edrop_user.entity.Competition;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.util.ArrayList;
import java.util.List;

public class Answer3Activity extends AppCompatActivity {
    private List<Competition> list2 = new ArrayList<>();
    private List listHisAnswer = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ListView listView;
    private Button btnBack;
    private Button btnAgain;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().transform(Answer3Activity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer3);
        initdata();
        findViews();
        setMethod();

    }

    //设置对应的点击事件
    private void setMethod() {
        customAdapter = new CustomAdapter(list2, listHisAnswer, this, R.layout.listview_item);
        listView.setAdapter(customAdapter);
        //listview失去焦点
        listView.setFocusable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Answer3Activity.this, Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Answer3Activity.this, GrabageQuestionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initdata() {
        list2 = (ArrayList<Competition>) getIntent().getSerializableExtra("lists");
        listHisAnswer = (List) getIntent().getSerializableExtra("listHis");
    }

    private void findViews() {
        listView = findViewById(R.id.list_itemViewId);
        btnBack = findViewById(R.id.btn_answer_back);
        btnAgain = findViewById(R.id.btn_answer_again);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent(Answer3Activity.this, Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

}
