package net.edrop.edrop_user;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import net.edrop.edrop_user.model.Model;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Application instance2;
    private static MyApplication mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送
        JPushInterface.setDebugMode(true);//打开调试模式
        JPushInterface.init(this);

        instance2 = this;
        Fresco.initialize(this);

        //初始化环信easeui
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false); //设置需要同意后才能接受群邀请
        //初始化数据模型层类
        Model.getInstance().init(this);
        EMClient.getInstance().init(this, options);
    }

    public static Application getInstance2(){
        return instance2;
    }


    /**
     * 获取context
     * @return
     */
    public static Context getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }
}
