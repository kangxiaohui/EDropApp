package net.edrop.edrop_user.jpush;

import android.content.Context;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;

public class JPushMessageReceiver extends cn.jpush.android.service.JPushMessageReceiver {
    @Override
    //通知消息到达时回调
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
//        Log.e("接收到通知","通知标题："+notificationMessage.notificationTitle+
//                "---通知内容："+notificationMessage.notificationContent+
//                "---附加信息："+notificationMessage.notificationExtras);
    }

    @Override
    //用户点击通知时回调
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //启动固定的某一个界面
//        Intent intent = new Intent(context, NewActivity.class);
//        intent.putExtra("extra",notificationMessage.notificationExtras);//（json格式字符串）
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//解决栈的问题
//        context.startActivity(intent);
    }

    @Override
    //程序接收到自定义消息时回调
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
//        Intent intent = new Intent(context, NewActivity.class);
//        intent.putExtra("content",customMessage.message);//自定义消息内容
//        intent.putExtra("extras",customMessage.extra);//自定义消息的额外字段（json格式字符串）
//        context.startActivity(intent);
    }
}
