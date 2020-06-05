package net.edrop.edrop_user.activity;

import android.support.v7.app.AppCompatActivity;

import com.sunchen.netbus.NetStatusBus;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        NetStatusBus.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetStatusBus.getInstance().unregister(this);
    }
}