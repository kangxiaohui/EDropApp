package net.edrop.edrop_user;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import net.edrop.edrop_user.model.Model;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Application instance2;
    private static MyApplication mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        //极光推送
        JPushInterface.setDebugMode(true);//打开调试模式
        JPushInterface.init(this);

        instance2 = this;
        Fresco.initialize(this);

        //初始化环信easeui
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        EMClient.getInstance().init(this, options);
        //初始化数据模型层类
        Model.getInstance().init(this);
        //初始化全局上下文
        mContext = this;
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

    //获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
