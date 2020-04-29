package net.edrop.edrop_user.model;

import android.content.Context;
import com.tencent.connect.UserInfo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层全局类
public class Model {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();
    //创建对象
    private static Model model=new Model();

    //私有化构造
    private Model(){

    }
    //获取单例对象
    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        mContext = context;

        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }
    //获取全局线程池
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

}
