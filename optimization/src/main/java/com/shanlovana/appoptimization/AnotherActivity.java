package com.shanlovana.appoptimization;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class AnotherActivity extends AppCompatActivity {
    ViewStub mMainContain;
    SplashFragment fragment;
    ImageView mImageView;
    Handler mHandler=new MyHandler(this);
    private static final int REMOVE=1;
    private static final int INITMAIN=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
         fragment = new SplashFragment();
         mMainContain = (ViewStub) findViewById(R.id.main_contain);
        //加载启动页面的SplashFragment
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_contain, fragment);

                fragmentTransaction.commit();
            }
        }
        mHandler.sendEmptyMessageDelayed(INITMAIN,5000);//5s后加载主页面，纯粹为了效果明显
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<AnotherActivity> mReference;

        public MyHandler(AnotherActivity mainReference) {
            mReference = new WeakReference<AnotherActivity>(mainReference);
        }

        @Override
        public void handleMessage(Message msg) {
            final AnotherActivity anotherActivity = mReference.get();
            switch (msg.what) {
                case REMOVE:
                    if (anotherActivity != null) {
                        FragmentManager manager = anotherActivity.getSupportFragmentManager();
                        if (manager != null) {
                            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                            if (fragmentTransaction != null) {
                                fragmentTransaction.remove(anotherActivity.fragment);

                                fragmentTransaction.commit();
                            }
                        }
                    }

                    break;
                case INITMAIN:
                    anotherActivity.getWindow().getDecorView().post(new Runnable() {
                        @Override
                        public void run() {
                            View mian=anotherActivity.mMainContain.inflate();
                            anotherActivity.mImageView= (ImageView) mian.findViewById(R.id.viewstub_img);

                        }
                    });
                    anotherActivity.mHandler.sendEmptyMessage(REMOVE);//通知删除Fragment
                    break;
            }
        }
    }


}
