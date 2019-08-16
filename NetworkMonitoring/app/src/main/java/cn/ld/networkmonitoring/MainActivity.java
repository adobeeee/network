package cn.ld.networkmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.ld.networkmonitor.NetworkManager;
import cn.ld.networkmonitor.annotation.Network;
import cn.ld.networkmonitor.type.NetworkType;
import cn.ld.networkmonitor.utils.Constants;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getDefault().register(this);
    }


    @Network(netType = NetworkType.WIFI)
    public void network(NetworkType networkType){
        switch (networkType){
            case WIFI:

                break;
            case CMNET:
            case CMWAP:

                break;
            case NONE:
                break;
            default:
                break;
        }
    }


}
