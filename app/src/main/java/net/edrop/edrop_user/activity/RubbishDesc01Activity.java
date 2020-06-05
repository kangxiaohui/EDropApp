package net.edrop.edrop_user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sunchen.netbus.annotation.NetSubscribe;
import com.sunchen.netbus.type.Mode;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.fragment.MessageFragment;
import net.edrop.edrop_user.utils.SystemTransUtil;

import xyz.bboylin.universialtoast.UniversalToast;

public class RubbishDesc01Activity extends BaseActivity {

    private MessageFragment mMessageFragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(RubbishDesc01Activity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish_desc01);
        ImageView iv = findViewById(R.id.iv_rubbish_1);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @NetSubscribe(mode = Mode.NONE)
    public void noneNet() {
        showdialog();
    }

    private void showdialog() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("content", "系统检测到您尚未连接网络，请到设置中进行网络设置，是否跳转？");
        bundle.putString("title", "警告");
        mMessageFragment.setOnResultListener(new OnItemMsgResultListener());
        mMessageFragment.setArguments(bundle);
        // 显示提示窗口
        mMessageFragment.show(getFragmentManager(), "");
    }

    class OnItemMsgResultListener implements MessageFragment.OnMsgResultListener {

        public OnItemMsgResultListener() {
        }

        @Override
        public void onResultFun(int resultCode) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        }
    }
}
