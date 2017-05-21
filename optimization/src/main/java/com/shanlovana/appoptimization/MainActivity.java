package com.shanlovana.appoptimization;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper allFlipper;
    RelativeLayout mSplashRelativeLayout;
    Button mButton;
    private static final int REMOVE=1;

    Handler mHandler = new MyHandler(this);//创建一个静态Handler内部类，然后对Handler持有的对象使用弱引用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allFlipper = (ViewFlipper) findViewById(R.id.allFlipper);
        mSplashRelativeLayout = (RelativeLayout) findViewById(R.id.splashsplash);
        mButton = (Button) findViewById(R.id.toFragment);
        mHandler.sendEmptyMessageDelayed(REMOVE, 2000);
        //在这两秒的时间，你可以做很多事情，数据处理，网络请求。。。。
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                startActivity(intent);
                //进入另一个优化的方法
            }
        });
    }


    private static class MyHandler extends Handler {

        private WeakReference<MainActivity> mReference;

        public MyHandler(MainActivity mainReference) {
            mReference = new WeakReference<MainActivity>(mainReference);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mReference.get();
            switch (msg.what) {
                case REMOVE:
                    if (mainActivity != null) {
                        mainActivity.allFlipper.setDisplayedChild(1);
                        mainActivity.allFlipper.removeView(mainActivity.mSplashRelativeLayout);
                    }

                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
