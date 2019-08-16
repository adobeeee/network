package cn.ld.networkmonitor;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import java.util.logging.Filter;

import cn.ld.networkmonitor.core.NetworkCallbackImpl;
import cn.ld.networkmonitor.utils.Constants;

/**
 * Created by Administrator on 2019/8/14.
 * 网络管理类
 */

public class NetworkManager {
    public static volatile NetworkManager instance;//跨进程中防止序列化重排序的作用
    private Application application;//整个进程都需要监听
    private NetStateReceiver receiver;

    private NetworkManager() {
        receiver = new NetStateReceiver();
    }


    /**
     * @author ld
     * created at 2019/8/14:15:58
     * 设置单例模式
     */
    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    /**
     * @author ld
     * created at 2019/8/15:16:34
     * 获取上下文对象
     */
    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("application异常...");
        }
        return application;
    }


    /**
     * @author ld
     * created at 2019/8/14:16:17
     * 初始化注册监听，并获取application对象
     */
    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager.NetworkCallback networkCallback=new NetworkCallbackImpl();
            NetworkRequest.Builder builder=new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr= (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmgr!=null){
                cmgr.registerNetworkCallback(request,networkCallback);
            }
        }else {
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            application.registerReceiver(receiver,intentFilter);
        }
    }

    public void register(Object object){
        receiver.register(object);
    }
}
