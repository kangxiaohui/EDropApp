package net.edrop.edrop_user;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

public class MyApplication extends Application {
    private static Application instance2;

    @Override
    public void onCreate() {
        super.onCreate();
        instance2 = this;
        Fresco.initialize(this);
        //环信
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        int pid = Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
    }

    public static Application getInstance2(){
        return instance2;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
