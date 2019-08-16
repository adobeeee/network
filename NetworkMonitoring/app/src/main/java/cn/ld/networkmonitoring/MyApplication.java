package cn.ld.networkmonitoring;

import android.app.Application;

import cn.ld.networkmonitor.NetworkManager;

/**
 * Created by Administrator on 2019/8/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
