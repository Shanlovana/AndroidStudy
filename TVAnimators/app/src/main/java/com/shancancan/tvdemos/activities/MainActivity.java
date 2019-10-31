package com.shancancan.tvdemos.activities;


import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.shancancan.tvdemos.R;
import com.shancancan.tvdemos.views.MainRelativeLayout;
import com.shancancan.tvdemos.views.MoveFrameLayout;
import com.shancancan.tvdemos.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MainRelativeLayout mRelativeLayout;
    MoveFrameLayout mMainUpView;
    View mOldFocus;
    int currentPosition, oldPosition = 888;
    List<RoundImageView> mBackList;
    List<ImageView> mFrontList;
    List<FrameLayout> mFrameList;
    private long clickTime = System.currentTimeMillis();
    private boolean islongClick = false;


    private Handler mHandler = new Handler() {//这里最好写成静态加若引用
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    if (oldPosition < mFrameList.size()) {
                        if (mFrontList.get(oldPosition).getAnimation() != null) {
                            mFrontList.get(oldPosition).getAnimation().cancel();
                        }
                    }

                    if (currentPosition < mFrameList.size()) {
                        mFrontList.get(currentPosition).bringToFront();
                        mFrontList.get(currentPosition).animate().scaleX(1.2f).scaleY(1.2f).setInterpolator(new LinearInterpolator()).translationY(-50).setDuration(400).start(); // 让小人超出控件.
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mMoveViewsetDetail();
        initRelativeLayout();
    }

    private void initRelativeLayout() {

        mRelativeLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if ((System.currentTimeMillis() - clickTime) > 280) {
                    clickTime = System.currentTimeMillis();
                    islongClick = false;
                } else {

                    if (newFocus instanceof FrameLayout) {
                        islongClick = false;
                    } else {
                        islongClick = true;
                    }
                }
                if (newFocus != null) {
                    newFocus.bringToFront();
                    float scale = 1.0f;
                    mMainUpView.setFocusView(newFocus, mOldFocus, scale);

                    if ((newFocus instanceof FrameLayout) && !islongClick) {
                        switch (newFocus.getId()) {
                            case R.id.littleone:
                                currentPosition = 0;
                                doAnimation(currentPosition, oldPosition);
                                break;
                            case R.id.littletwo:
                                currentPosition = 1;
                                doAnimation(currentPosition, oldPosition);
                                break;
                            case R.id.littlethree:
                                currentPosition = 2;
                                doAnimation(currentPosition, oldPosition);
                                break;
                            case R.id.littlefour:
                                currentPosition = 3;
                                doAnimation(currentPosition, oldPosition);
                                break;
                        }

                    } else {
                        mMainUpView.setDrawUpRectEnabled(true);
                        currentPosition = 888;
                        if (oldPosition < mFrameList.size()) {

                            mFrontList.get(oldPosition).animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new LinearInterpolator()).translationY(0).setDuration(400).start();
                        }
                    }
                    mOldFocus = newFocus;

                }
            }
        });
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    private void mMoveViewsetDetail() {


        mMainUpView.setUpRectResource(R.drawable.bg_focus_slot_border);
        float density = getResources().getDisplayMetrics().density;
        RectF receF = new RectF(getDimension(R.dimen.w_1) * density, getDimension(R.dimen.h_1) * density,
                getDimension(R.dimen.w_1) * density, getDimension(R.dimen.h_1) * density);
        mMainUpView.setUpPaddingRect(receF);
        mMainUpView.setTranDurAnimTime(300);
    }

    private void initViews() {
        mRelativeLayout = (MainRelativeLayout) findViewById(R.id.activity_imitation_xun_ma);
        mMainUpView = (MoveFrameLayout) findViewById(R.id.xunma_mainup);
        mFrameList = new ArrayList<>();
        mBackList = new ArrayList<>();
        mFrontList = new ArrayList<>();
        mFrameList.add((FrameLayout) findViewById(R.id.littleone));
        mFrameList.add((FrameLayout) findViewById(R.id.littletwo));
        mFrameList.add((FrameLayout) findViewById(R.id.littlethree));
        mFrameList.add((FrameLayout) findViewById(R.id.littlefour));
        mBackList.add((RoundImageView) findViewById(R.id.littleone_back));
        mBackList.add((RoundImageView) findViewById(R.id.littletwo_back));
        mBackList.add((RoundImageView) findViewById(R.id.littlethree_back));
        mBackList.add((RoundImageView) findViewById(R.id.littlefour_back));
        mFrontList.add((ImageView) findViewById(R.id.littleone_front));
        mFrontList.add((ImageView) findViewById(R.id.littletwo_front));
        mFrontList.add((ImageView) findViewById(R.id.littlethree_front));
        mFrontList.add((ImageView) findViewById(R.id.littlefour_front));
    }

    private void doAnimation(int current, int old) {
        if (current < mFrameList.size()) {
            mMainUpView.setDrawUpRectEnabled(false);
            mHandler.sendEmptyMessageDelayed(0, 200);
            if (old < mFrameList.size()) {
                mFrontList.get(old).animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new LinearInterpolator()).translationY(0).setDuration(500).start();
            }
            oldPosition = currentPosition;
        }
    }
}
