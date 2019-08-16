package cn.ld.networkmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.ld.networkmonitor.annotation.Network;
import cn.ld.networkmonitor.bean.MethodManager;
import cn.ld.networkmonitor.type.NetworkType;
import cn.ld.networkmonitor.utils.Constants;
import cn.ld.networkmonitor.utils.NetworkUtils;

/**
 * Created by Administrator on 2019/8/14.
 */

public class NetStateReceiver extends BroadcastReceiver {

    private NetworkType networkType;
    //key:activity/fragment ,value:该容器内所有订阅监听网络的方法的集合
    private Map<Object, List<MethodManager>> networkList;


    public NetStateReceiver() {
        networkType = NetworkType.NONE;
        networkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            networkType = NetworkUtils.getNetType();
            post(networkType);
        }
    }

    /**
     * @author ld
     * created at 2019/8/15:11:30
     * 网络匹配过程
     */
    private void post(NetworkType networkType) {
        if (networkList.isEmpty()) return;
        Set<Object> set = networkList.keySet();
        for (Object o : set) {
            List<MethodManager> methodManagers = networkList.get(o);
            if (methodManagers != null) {
                for (MethodManager methodManager : methodManagers) {
                    if (methodManager.getType().isAssignableFrom(networkType.getClass())) {
                        switch (methodManager.getNetworkType()) {
                            case AUTO:
                                invoke(methodManager, o, networkType);
                                break;
                            case WIFI:
                                if (networkType == NetworkType.WIFI || networkType == NetworkType.NONE) {
                                    invoke(methodManager, o, networkType);
                                }
                                break;
                            case CMNET:
                            case CMWAP:
                                if (networkType == NetworkType.CMNET || networkType == NetworkType.CMWAP || networkType == NetworkType.NONE) {
                                    invoke(methodManager, o, networkType);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }


    private void invoke(MethodManager methodManager, Object o, NetworkType networkType) {
        try {
            methodManager.getMethod().invoke(o, networkType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author ld
     * created at 2019/8/15:10:49
     * 进行注册，收集订阅方法
     */
    public void register(Object object) {
        List<MethodManager> methodList = networkList.get(object);
        if (methodList == null) {
            //利用反射进行收集该容器内所有方法集合
            methodList = findAnnotationMethod(object);
            networkList.put(object, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> methodList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        //订阅方法的收集
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }
            //获取方法的返回值，校验一
//            Type genericReturnType = method.getGenericReturnType();
            //获取方法的参数，校验二
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法的参数有且只有一个");
            }
            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodList.add(manager);
        }


        return methodList;
    }
}
