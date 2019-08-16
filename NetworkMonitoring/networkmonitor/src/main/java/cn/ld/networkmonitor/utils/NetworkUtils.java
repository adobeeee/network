package cn.ld.networkmonitor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.ConnectException;

import cn.ld.networkmonitor.NetworkManager;
import cn.ld.networkmonitor.type.NetworkType;

/**
 * Created by Administrator on 2019/8/14.
 */

public class NetworkUtils {

    /**
     * @author ld
     * created at 2019/8/14:15:19
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(){
        ConnectivityManager connMgr= (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr==null){
            return false;
        }
        NetworkInfo[] info =connMgr.getAllNetworkInfo();
        if (info!=null){
            for(NetworkInfo anInfo:info){
                if (anInfo.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author ld
     * created at 2019/8/14:15:23
     * 获取当前网络类型：<br/>-1:没有网络 ；<br/>1:wifi网络 ；<br/>2:wap网络 ；<br/>3:net 网络
     */
    @SuppressLint("MissingPermission")
    public static NetworkType getNetType(){
        ConnectivityManager connMgr= (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        if (networkInfo==null){
            return NetworkType.NONE;
        }
        int nType=networkInfo.getType();
        if (nType==ConnectivityManager.TYPE_MOBILE){
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                return NetworkType.CMNET;
            }else {
                return NetworkType.CMWAP;
            }
        }else if (nType==ConnectivityManager.TYPE_WIFI){
            return NetworkType.WIFI;
        }
        return NetworkType.NONE;
    }

}
