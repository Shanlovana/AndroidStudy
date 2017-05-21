package com.ming.tvnetcheck.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
/*
* isConnect判断网络是否连接
* isCheck判断网络是否检测
* isPppoeConnect判断pppoe网络是否检测
* mThread新线程来检测pppoe网络，耗时操作不能放在主线程
* netconfigDetail用于返回的网络图标值
* valueEthernet 判断有线网络是否可用的返回值
* valueWifi 判断无线网络是否可用的返回值
* 根据valueEthernet和valueWifi来检测pppoe
* */

public class NetUtils {
    public static boolean isConnect = false,
            isCheck = false,
            isPppoeConnect = false;
    private static Thread mThread = null;
    int netconfigDetail, valueEthernet, valueWifi;

    public static boolean isNetworkAvailable() {
        return isConnect;
    }

    public int getNetwork(Context mContext) {
        isCheck = true;
        isConnect = false;
        WifiManager mWifiManager;
        ConnectivityManager mConnectivityManager;
        NetworkInfo ethernetNetInfo;


        mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);//获得WifiManager对象
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);//获得ConnectivityManager对象
        ethernetNetInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);//获得NetworkInfo对象


        if (ethernetNetInfo.isConnected()) {
            netconfigDetail = 4;//如果有线网连接，值为4
            valueEthernet = 1;

        } else {

            if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                if (mWifiManager.getConnectionInfo().getIpAddress() == 0) {
                    valueWifi = -1;//如果无线网可用，值为-1
                    netconfigDetail = 6;//如果无线网不可用，值为6
                } else {
                    netconfigDetail = WifiManager.calculateSignalLevel(mWifiManager
                            .getConnectionInfo().getRssi(), 4);
                    //如果无线网可用，值为0-3
                }

            } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                netconfigDetail = 5;//如果无线网不可用，值为5
                valueEthernet = -1;//如果无线网不可用，值为-1
            }

        }
        if (valueWifi < 0 && valueEthernet < 0) {
            checkPppoe();//满足判断的条件则判断是否可用，判断网络连通（包含了pppoe的检测）
        } else {
            isConnect = true;
            isCheck = false;
            isPppoeConnect = false;

        }
        Log.e("getNetwork(Context", "valueWifi   " + netconfigDetail);
        return netconfigDetail;

    }

    public static void checkPppoe() {
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();//检测前将thread置为null
            mThread = null;
        }
        if (mThread == null || mThread.isInterrupted()) {
            mThread = new Thread(new Runnable() {
                public void run() {
                    isCheck = true;
                    isConnect = false;
                    isConnect = isConnected();
                    isCheck = false;
                    isPppoeConnect = isConnect;
                }
            });
            mThread.start();
        }
    }

    private static boolean isConnected() {
        //发送一个请求用来检测是否已连接网络，并判断通畅
        //在这里你也可以ping某个IP，来判断是否已连接
        try {
            URL url = new URL("http://oavdalesp.bkt.clouddn.com/online");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "close");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            BufferedReader reader = null;

            try {
                conn.connect();
                try {
                    if (conn.getInputStream() != null)
                        reader = new BufferedReader(new InputStreamReader(
                                conn.getInputStream()), 512);
                } catch (IOException e) {
                    if (conn.getErrorStream() != null)
                        reader = new BufferedReader(new InputStreamReader(
                                conn.getErrorStream()), 512);
                }

                if (reader != null)
                    reader.close();

                if (conn != null)
                    conn.disconnect();
                return true;
            } catch (SocketException e) {
                e.printStackTrace();
                return false;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
