package com.halloandroid.downloadfiles;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import com.halloandroid.downloadfiles.adapter.MainAdapter;

import com.halloandroid.downloadfiles.services.DownloadService;

import com.halloandroid.downloadfiles.utils.GetJsonAndReadUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShanCanCan on 2017/3/6 0006.
 */
public class MainActivity extends Activity implements ServiceConnection {

    private ListView listView;
    private DownloadService mDownloadService;
    private MainAdapter adapter;
    List<GetJsonAndReadUtil.EntryBean> mBeanList = new ArrayList<>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Button button = (Button) msg.obj;
                button.setClickable(true);
                button.setText("下载队列中");
            } else if (msg.what == 2) {
                Button button = (Button) msg.obj;
                button.setClickable(true);
                Toast.makeText(MainActivity.this, "初始化失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

        bindService(intent, this, Context.BIND_AUTO_CREATE);


    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        mDownloadService = ((DownloadService.MyBinder) service).getService();
        if (service != null) {
            mBeanList = new GetJsonAndReadUtil(getAssets()).getJsonDetail();
            Log.e("onServiceConnected", "mBeanList  " + mBeanList.size());

            adapter = new MainAdapter(this, mBeanList, mDownloadService, mHandler);
            listView.setAdapter(adapter);
        }


    }

    public void goDownLoadList(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DownLoadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}
