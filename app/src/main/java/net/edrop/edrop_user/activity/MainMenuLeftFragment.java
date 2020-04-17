package net.edrop.edrop_user.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.Wallet;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.ShareAppToOther;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;

public class MainMenuLeftFragment extends Fragment {
    //popupWindow
    private PopupWindow popupWindow = null;
    private View popupView = null;
    private View myView;
    private ImageView userSex;
    private ImageView userImg;
    private TextView userName;
    private TextView tvMoney;
    private LinearLayout myMoney;
    private LinearLayout myAddress;
    private LinearLayout myOrder;
    private LinearLayout inviteFriends;
    private LinearLayout businessCooperation;
    private LinearLayout aboutEDrop;
    private LinearLayout setting;
    private LinearLayout feedback;
    private OkHttpClient okHttpClient;
    private SharedPreferencesUtils sharedPreferences;
    private Intent intent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 888) {
                RequestOptions options = new RequestOptions().centerCrop();
                Glide.with(myView.getContext())
                        .load(msg.obj)
                        .apply(options)
                        .into(userImg);
            } else if (msg.what == 1) {
                Intent intent = new Intent(myView.getContext(), ShowOrdersActivity.class);
                String str = (String) msg.obj;
                intent.putExtra("orderjson", str);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (msg.what == 666) {
                String json = (String) msg.obj;
                Wallet wallet = new Gson().fromJson(json, Wallet.class);
                tvMoney.setText("￥" + wallet.getMoney() + "");

            }
        }
    };

    //重写
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_home_left_menu, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件以及设置
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化监听事件
        initEvent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        userImg = myView.findViewById(R.id.iv_userImg);
        userName = myView.findViewById(R.id.tv_userName);
        myMoney = myView.findViewById(R.id.myMoney);
        myAddress = myView.findViewById(R.id.myAddress);
        myOrder = myView.findViewById(R.id.myOrder);
        inviteFriends = myView.findViewById(R.id.inviteFriends);
        businessCooperation = myView.findViewById(R.id.businessCooperation);
        aboutEDrop = myView.findViewById(R.id.aboutEDrop);
        setting = myView.findViewById(R.id.setting);
        feedback = myView.findViewById(R.id.feedback);
        userSex = myView.findViewById(R.id.iv_head_img_main);
        tvMoney = myView.findViewById(R.id.tv_myMoney);
    }

    /**
     * 初始化默认数据【这个需要在activity中执行，原因是：在布局文件中通过<fragment>的方式引用Fragment，打开Activity的时候，Fragment的生命周期函数均执行了】
     * 那么就存在一个问题，初始化fragment中的数据，可能会在activity数据初始化之前执行
     */
    public void setDefaultDatas() {
        //修改首页左边数据
        sharedPreferences = new SharedPreferencesUtils(myView.getContext(), "loginInfo");
        String username = sharedPreferences.getString("username", "");
        String gender = sharedPreferences.getString("gender", "");
        userName.setText(username);
        switch (gender) {
            case "boy":
                userSex.setImageDrawable(getResources().getDrawable(R.drawable.gender_boy));
                break;
            case "girl":
                userSex.setImageDrawable(getResources().getDrawable(R.drawable.gender_girl));
                break;
            case "secret":
                userSex.setImageDrawable(getResources().getDrawable(R.drawable.gender_secret));
            default:
                break;
        }
        okHttpClient = new OkHttpClient();
        getImg();
        getMoney();
    }

    private void getMoney() {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "getSurplus?uid=" + sharedPreferences.getInt("userId")).build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = new Message();
                message.what = 666;
                message.obj = str;
                handler.sendMessage(message);
            }
        });


    }

    private void getImg() {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "getUserInfoById?id=" + sharedPreferences.getInt("userId")).build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                String imgPath = "";
                String imgName = "";
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    imgPath = jsonObject.getString("imgpath");
                    imgName = jsonObject.getString("imgname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 888;
                message.obj = BASE_URL.substring(0, BASE_URL.length() - 1) + imgPath + "/" + imgName;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        userName.setOnClickListener(new MyListener());
        userImg.setOnClickListener(new MyListener());
        myMoney.setOnClickListener(new MyListener());
        myAddress.setOnClickListener(new MyListener());
        myOrder.setOnClickListener(new MyListener());
        inviteFriends.setOnClickListener(new MyListener());
        businessCooperation.setOnClickListener(new MyListener());
        aboutEDrop.setOnClickListener(new MyListener());
        setting.setOnClickListener(new MyListener());
        feedback.setOnClickListener(new MyListener());
    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_userName:
                    intent = new Intent(getContext(), PersonalCenterManagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.iv_userImg:
                    intent = new Intent(getContext(), PersonalCenterManagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.myMoney:
                    Intent intent = new Intent(myView.getContext(), MyMoneyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.myAddress:
                    Toast.makeText(getActivity(), "我的地址", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.myOrder:
                    SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(myView.getContext(), "loginInfo");
                    int userId = loginInfo.getInt("userId");
                    FormBody formBody = new FormBody.Builder()
                            .add("userId", userId + "").build();
                    Request request = new Request.Builder()
                            .url(Constant.BASE_URL + "getOrderById")
                            .post(formBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            Message message = new Message();
                            message.what = 1;
                            message.obj = string;
                            handler.sendMessage(message);
                        }
                    });

                    break;
                case R.id.inviteFriends:
                    // 显示PopupWindow
                    if (popupWindow == null || !popupWindow.isShowing()) {
                        showPopupWindow();
                    }
                    break;
                case R.id.businessCooperation:
                    //跳转到商务合作的详细页面
                    intent = new Intent(getContext(), CooperationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.aboutEDrop:
                    //跳转到详细介绍页面
                    MainMenuLeftFragment.this.intent = new Intent(getContext(), AboutEDropActivity.class);
                    MainMenuLeftFragment.this.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(MainMenuLeftFragment.this.intent);
                    break;
                case R.id.setting:
                    MainMenuLeftFragment.this.intent = new Intent(getContext(), SettingActivity.class);
                    MainMenuLeftFragment.this.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(MainMenuLeftFragment.this.intent);
                    break;
                case R.id.feedback:
                    MainMenuLeftFragment.this.intent = new Intent(getContext(), FeedBackActivity.class);
                    MainMenuLeftFragment.this.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(MainMenuLeftFragment.this.intent);
                    break;
            }
        }
    }

    public void shareQQ(View view) {
        ShareAppToOther shareAppToOther = new ShareAppToOther(myView.getContext());
        shareAppToOther.shareQQFriend("EDrop", "EDrop邀请您的参与,下载地址为：https://www.lanzous.com/b0aqeodib \n" +
                "密码:90rv", ShareAppToOther.TEXT, drawableToBitmap(getResources().getDrawable(R.drawable.logo)));
    }

    public void shareWechat(View view) {
        ShareAppToOther shareAppToOther = new ShareAppToOther(myView.getContext());
        shareAppToOther.shareWeChatFriend("EDrop", "EDrop邀请您的参与,下载地址为：https://www.lanzous.com/b0aqeodib \n" +
                "密码:90rv", ShareAppToOther.TEXT, drawableToBitmap(getResources().getDrawable(R.drawable.logo)));
    }

    public void shareWechatQzene(View view) {
        ShareAppToOther shareAppToOther = new ShareAppToOther(myView.getContext());
        shareAppToOther.shareWeChatFriendCircle("EDrop", "EDrop邀请您的参与,下载地址为：https://www.lanzous.com/b0aqeodib \n" +
                "密码:90rv", drawableToBitmap(getResources().getDrawable(R.drawable.logo)));
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void showPopupWindow() {
        // 创建popupWindow对象
        setBackgroundAlpha(0.5f, myView.getContext());
        popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 通过布局填充器创建View
        popupView = getLayoutInflater().inflate(R.layout.item_shared_app, null);
        // 设置PopupWindow显示的内容视图
        popupWindow.setContentView(popupView);
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否相应点击事件
        popupWindow.setTouchable(true);
        popupView.setFocusable(true);
        View view_list = View.inflate(myView.getContext(), R.layout.item_shared_app, null);
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindow.showAtLocation(view_list.findViewById(R.id.share_app), Gravity.CENTER, 0, 0);

        // 获取按钮并添加监听器
        Button btnQQFriend = popupView.findViewById(R.id.share_qq_friend);
        Button btnWeChat = popupView.findViewById(R.id.share_wechat);
        Button btnWeChatQzene = popupView.findViewById(R.id.share_wechat_qzene);
        btnQQFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQQ(view);
            }
        });
        btnWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWechat(view);
            }
        });
        btnWeChatQzene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWechatQzene(view);
            }
        });
    }

    protected class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f, myView.getContext());
        }
    }

    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
