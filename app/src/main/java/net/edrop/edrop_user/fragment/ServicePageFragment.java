package net.edrop.edrop_user.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.ImmediateAppointmentActivity;
import net.edrop.edrop_user.activity.IntroductionEDropActivity;
import net.edrop.edrop_user.activity.MyMoneyActivity;
import net.edrop.edrop_user.activity.ShowOrdersActivity;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.ShareAppToOther;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/25
 * Time: 16:39
 */
public class ServicePageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private TextView textView;
    private View view;
    private Button btnService;
    private LinearLayout myOrders;
    private LinearLayout myWallet;
    private LinearLayout myTicket;
    private LinearLayout myKefu;
    private LinearLayout myShare;
    private LinearLayout myInfo;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Intent intent = new Intent(view.getContext(), ShowOrdersActivity.class);
                String str = (String) msg.obj;
                intent.putExtra("orderjson", str);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    public static ServicePageFragment newInstance(String sectionNumber) {
        ServicePageFragment fragment = new ServicePageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_page, container, false);

        findView();
        setListener();
        return view;
    }

    //什么是易扔？的点击事件
    private void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转新页面
                Intent intent = new Intent(getActivity(), IntroductionEDropActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImmediateAppointmentActivity.class);
                startActivity(intent);
            }
        });
        myOrders.setOnClickListener(new MyListener());
        myWallet.setOnClickListener(new MyListener());
        myTicket.setOnClickListener(new MyListener());
        myKefu.setOnClickListener(new MyListener());
        myShare.setOnClickListener(new MyListener());
        myInfo.setOnClickListener(new MyListener());
    }

    //找到控件对象
    private void findView() {
        okHttpClient = new OkHttpClient();
        textView = view.findViewById(R.id.tv_service_what);
        btnService = view.findViewById(R.id.btn_service_reservation);
        myOrders = view.findViewById(R.id.ll_service_myorders);
        myWallet = view.findViewById(R.id.ll_service_wallet);
        myTicket = view.findViewById(R.id.ll_service_ticket);
        myKefu = view.findViewById(R.id.ll_service_kefu);
        myShare = view.findViewById(R.id.ll_service_share);
        myInfo = view.findViewById(R.id.ll_service_info);

    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_service_myorders:
                    SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(view.getContext(), "loginInfo");
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
                            e.printStackTrace();
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
                case R.id.ll_service_wallet:
                    Intent intent3 = new Intent(view.getContext(), MyMoneyActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent3);
                    break;
                case R.id.ll_service_ticket:
                    UniversalToast.makeText(view.getContext(), "暂无可用优惠券", Toast.LENGTH_SHORT).showWarning();
                    break;
                case R.id.ll_service_kefu:
                    UniversalToast.makeText(view.getContext(), "联系客服", Toast.LENGTH_SHORT).showSuccess();
                    break;
                case R.id.ll_service_share:
                    ShareAppToOther shareAppToOther = new ShareAppToOther(view.getContext());
                    shareAppToOther.shareWeChatFriend("EDrop", "EDrop邀请您参与，下载地址：https://www.lanzous.com/b0aqeodib \n" +
                            "密码:90rv", ShareAppToOther.TEXT, drawableToBitmap(getResources().getDrawable(R.drawable.logo)));
                    break;
                case R.id.ll_service_info:
                    Intent intent2 = new Intent(getActivity(), IntroductionEDropActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
            }
        }
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
}
