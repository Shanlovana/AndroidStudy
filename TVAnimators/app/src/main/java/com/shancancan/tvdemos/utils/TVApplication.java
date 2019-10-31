package com.shancancan.tvdemos.utils;

import android.app.Application;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class TVApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.initTag(true);
    }
}
