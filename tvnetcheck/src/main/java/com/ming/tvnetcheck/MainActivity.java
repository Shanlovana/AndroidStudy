package com.ming.tvnetcheck;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.ming.tvnetcheck.utils.NetCheckUtils;

import static com.ming.tvnetcheck.utils.ConFigs.NET;

public class MainActivity extends AppCompatActivity {
    //网络监听图片数组
    public static int[] mNetLogo = new int[]{R.drawable.wifi0, R.drawable.wifi1,
            R.drawable.wifi2, R.drawable.wifi3, R.drawable.connect,
            R.drawable.etnerror, R.drawable.wifinotnormal};

    ImageView mImageView;
    NetCheckUtils mNetCheckUtils;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NET:
                    int obj = (int) msg.obj;
                    Log.e("handleMessage","obj"+obj);

                    mImageView.setImageResource(mNetLogo[obj]);

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.netImage);
        checkNet();
    }

    private void checkNet() {//开启新线程，防止阻塞UI主线程
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("handleMessagecheckNet","obj51");
                mNetCheckUtils = new NetCheckUtils(MainActivity.this, mHandler);//网络
                mNetCheckUtils.getNetwork();
            }
        });

        mThread.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetCheckUtils.unregisterReceiver();
    }
}
