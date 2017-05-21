package com.ming.tvnetcheck.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.ethernet.EthernetManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static com.ming.tvnetcheck.utils.ConFigs.NET;

/**
 * Created by ChangMingShan on 2016/11/3.
 * <p>
 * 设想返回值是一个int值，根据值设置图片
 * 将广的注册，处理与销毁完全从activity剥离。
 * 在构造函数时，将context和mainactivity的Handler传入，用来刷新主页面的UI
 * 调用这个类，构造方法传参，与activity解耦
 */

public class NetCheckUtils {
    private Context mContext;
    private ConnectivityManager mConnectivityManager;
    private WifiManager mWifiManager;
    private BroadcastReceiver mReceiver;
    private Handler mHandler;
    public Boolean isReceiver = false;
    public Boolean isConnect = false;

    private int netCondition;



    public NetCheckUtils(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }


    public void getNetwork() {//获得网络的具体返回值

        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //广播的action有四种，包含了有线网络变化，无线网络变化和网络连接变化
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(EthernetManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);




        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                handleEvent(mContext, intent);
            }
        };
        if (!isReceiver) {//注册广播
            mContext.registerReceiver(mReceiver, mFilter);
            isReceiver = true;
        }

    }
   //注销广播
    public void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    private void handleEvent(Context context, Intent intent) {
        //处理广播不同action事件
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)
                || WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)
                || ConnectivityManager.CONNECTIVITY_ACTION.equals(action)
                || EthernetManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            netCondition = new NetUtils().getNetwork(mContext);
            Log.e("changmingshan", "i是NetCheckUtils " + netCondition);
            Message msg = new Message();
            msg.obj = netCondition;
            msg.what =NET;
            mHandler.sendMessage(msg);//发送消息，通知主页面更新UI

        }
    }

}
