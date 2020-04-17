package net.edrop.edrop_user.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.TestData;
import net.edrop.edrop_user.adapter.ChatAdapter;
import net.edrop.edrop_user.entity.ChatModel;
import net.edrop.edrop_user.entity.ItemModel;
import net.edrop.edrop_user.fragment.EmotionMainFragment;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static net.edrop.edrop_user.utils.Constant.RECEVIED_MSG;

public class ChatViewActivity extends AppCompatActivity implements EMMessageListener {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private String content;
    private ImageView ivBack;
    private String employeeName;
    private String employeeHeadImg;
    private String userHeadImg;
    private TextView chatNav;
    private EmotionMainFragment emotionMainFragment;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==RECEVIED_MSG){
                ArrayList<ItemModel> data = new ArrayList<>();
                ChatModel model = new ChatModel();
                model.setContent(new Gson().fromJson((String) msg.obj,String.class));
                model.setIcon(employeeHeadImg);
                data.add(new ItemModel(ItemModel.CHAT_A, model));
                adapter.notifyDataSetChanged();
                adapter.addAll(data);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ChatViewActivity.this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_chat_view);
        initView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter());
        adapter.replaceAll(TestData.getTestAdData(employeeHeadImg,userHeadImg));// 测试用的

        employeeName = getIntent().getExtras().getString("employeeName");
        userHeadImg = getIntent().getExtras().getString("userHeadImg");
        employeeHeadImg = getIntent().getExtras().getString("employeeHeadImg");
        chatNav.setText(employeeName);
        EMClient.getInstance().chatManager().addMessageListener(this);

        initData();
        setLinster();
        initEmotionMainFragment();
    }

    private void setLinster() {
//        tvSend.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                EMMessage messagelay = EMMessage.createTxtSendMessage(content,employeeName);
//                EMClient.getInstance().chatManager().sendMessage(messagelay);
//                ArrayList<ItemModel> data = new ArrayList<>();
//                ChatModel model = new ChatModel();
//                model.setIcon(userHeadImg);
//                model.setContent(content);
//                data.add(new ItemModel(ItemModel.CHAT_B, model));
//                adapter.notifyDataSetChanged();
//                adapter.addAll(data);
//                etContent.setText("");
//                hideKeyBorad(etContent);
//            }
//        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        chatNav=findViewById(R.id.chat_nav);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
//        etContent = (EditText) findViewById(R.id.etContent);
//        tvSend = (TextView) findViewById(R.id.tvSend);
    }

    private void initData() {
//        etContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                content = s.toString().trim();
//            }
//        });
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(recyclerView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    //隐藏键盘
    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //环信:接收消息
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        Observable.from(messages).subscribe(new Action1<EMMessage>() {
            @Override
            public void call(EMMessage emMessage) {
                EMTextMessageBody emtextmessage = (EMTextMessageBody) emMessage.getBody();
                Message msg = new Message();
                msg.obj = new Gson().toJson(emtextmessage.getMessage());
                msg.what = RECEVIED_MSG;
                mHandler.sendMessage(msg);
//                Log.e("message",emtextmessage.getMessage());//获取信息
                chatNav.setText(emMessage.getFrom());//设置标题栏
            }
        });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 判断是否拦截返回键操作
     */
    @Override
    public void onBackPressed() {
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }
}
