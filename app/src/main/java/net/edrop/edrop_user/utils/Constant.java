package net.edrop.edrop_user.utils;

/**
 * Created by Android Studio.
 * User: zhanghaoyu
 * Date: 2019/11/28
 * Time: 17:18
 */
public class Constant {
    //    public static final String BASE_URL = "http://10.7.88.157:8080/EDropService/";
    public static final String BASE_URL = "http://122.51.69.212:8080/EDropService/";
    public static final String NEWS_URL = "http://122.51.69.212:8080/EDropAdminService/";

    //聊天发送消息kxh
    public static final int RECEVIED_MSG = -3;
    //表单提交状态码
    public static final int UPDATE_USER_FAIL = 7;
    public static final int UPDATE_USER_SUCCESS = 8;
    //登录相关
    public static final int USER_NO_EXISTS = 1;
    public static final int LOGIN_SUCCESS = 2;
    public static final int PASSWORD_WRONG = 3;
    public static final int LOGIN_FAIL = 5;
    public static final int NEW_USER = 4;
    //注册
    public static final int REGISTER_FAIL = 9;
    public static final int REGISTER_SUCCESS = 10;
    //信息完善
    public static final int BASE_SUCCESS = -4;
    public static final int BASE_FAIL = -5;
    public static final int IMG_SUCCESS = -6;
    public static final int IMG_FAIL = -7;
    public static final int PSD_SUCCESS = -8;
    public static final int PSD_FAIL = -9;
    public static final int SEARCH_SUCCESS = 6;

    //订单相关
    public static final int ORDER_STATE_NO_RECEIVE = -1;    //未接单
    public static final int ORDER_STATE_NO_FINISH = 0;    //未完成
    public static final int ORDER_STATE_FINISH = 1;    //已完成
    public static final int ORDER_STATE_USER_DELETED = 1;    //删除
    public static final int ORDER_STATE_USER_NO_DELETE = 0;    //未删除
    public static final int ORDER_STATE_EMPLOYEE_DELETED = 1;    //删除
    public static final int ORDER_STATE_EMPLOYEE_NO_DELETE = 0;    //未删除

    public static final String CONTACT_CHANGED = "contact_changed";//发送联系人变化的广播
    public static final String CONTACT_INVITE_CHANGED = "contact_invite_changed";//联系人邀请信息变化的广播
    public static final String GROUP_ID = "group_id";  //群id
    public static final String EXIT_GROUP = "exit_group"; //退群广播

    //图片路径
    public static final String path = "http://122.51.69.212:8080/img/";
}
