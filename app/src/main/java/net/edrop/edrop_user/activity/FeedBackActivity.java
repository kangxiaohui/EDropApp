package net.edrop.edrop_user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.GridImageAdapter;
import net.edrop.edrop_user.entity.FullyGridLayoutManager;
import net.edrop.edrop_user.utils.SystemTransUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.reactivex.functions.Consumer;

/**
 * Created by 李诗凡.
 * User: sifannnn
 * Date: 2019/12/9
 * Time: 14:24
 * TODO：反馈的页面
 */
public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener,
        EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    // EmojiconTextView继承自AppCompatTextView，而AppCompatTextView继承自TextView
    private EmojiconEditText mEditEmojicon;
    private EditText etQQ;
    private EditText etPhone;
    private TextView textView;
    private ImageView imageView;
    private ImageView imageViewback;
    private Button button;
    private boolean hasClick;
    private String string;
    private String qq;
    private String phone;
    private Intent intent;
    private LinearLayout linearLayout;
    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private PopupWindow pop;
    private RecyclerView mRecyclerView;
    private EmojiconEditText emojiconEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new SystemTransUtil().trans(FeedBackActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);
        findViews();
        setLisner();
        setEmojiconFragment(false);
        intent = getIntent();
        if (intent != null) {
            string = intent.getStringExtra("msg");
            textView.setText(string);
        }
        initWidget();
    }

    //设置监听器
    private void setLisner() {
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
        mEditEmojicon.setOnClickListener(this);
        imageViewback.setOnClickListener(this);
    }

    private void findViews() {
        mEditEmojicon = findViewById(R.id.et_feedback);
        etPhone = findViewById(R.id.et_feedback_phone);
        textView = findViewById(R.id.tv_feedback);
        etQQ = findViewById(R.id.et_feedback_qq);
        button = findViewById(R.id.btn_feedback);
        imageView = findViewById(R.id.im_feedback_smile);
        linearLayout = findViewById(R.id.ll_feedback);
        imageViewback = findViewById(R.id.iv_feedback_back);
        mRecyclerView = findViewById(R.id.rl_feedback_recycler);
        emojiconEditText=findViewById(R.id.et_feedback);
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选项选择
            case R.id.ll_feedback:
                intent = new Intent(FeedBackActivity.this, FeedBackOption.class);
                startActivity(intent);
                break;
            case R.id.im_feedback_smile: // 表情按钮
                if (hasClick) {
                    findViewById(R.id.emojicons).setVisibility(View.GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                    findViewById(R.id.emojicons).setVisibility(View.VISIBLE);
                }
                hasClick = !hasClick;
                break;
            case R.id.et_feedback: // 输入框
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                break;
            case R.id.btn_feedback:
                emojiconEditText.setText("");
                textView.setText("");
                Toast.makeText(this, "反馈已提交，请耐心等待结果！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_feedback_back:
                finish();
                break;
        }
    }

    //发送消息的异步类
    private class sendMessage extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //获取控件值
            qq = etQQ.getText().toString();
            phone = etPhone.getText().toString();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            try {
                url = new URL((String) objects[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //设置Http请求方式 GET,POST,DELETE
                //默认get请求
                connection.setRequestMethod("POST");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }

    @Override
    public void onBackPressed() {
        if (hasClick) {
            findViewById(R.id.emojicons).setVisibility(View.GONE);
            hasClick = !hasClick;
        } else {
            super.onBackPressed();
        }
    }

    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(FeedBackActivity.this).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(FeedBackActivity.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                showPop();
                            } else {
                                Toast.makeText(FeedBackActivity.this, "未获得权限", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };


    private void showPop() {
        View bottomView = View.inflate(FeedBackActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(FeedBackActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(FeedBackActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };
        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
