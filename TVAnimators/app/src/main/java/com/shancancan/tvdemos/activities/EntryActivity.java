package com.shancancan.tvdemos.activities;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;


import com.shancancan.tvdemos.R;
import com.shancancan.tvdemos.views.MainRelativeLayout;
import com.shancancan.tvdemos.views.MoveFrameLayout;

public class EntryActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MainRelativeLayout mRelativeLayout;
    MoveFrameLayout mMoveView;
    View mOldFocus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        mRelativeLayout = (MainRelativeLayout) findViewById(R.id.activity_entry);
        mMoveView = (MoveFrameLayout) findViewById(R.id.entrymove);
        mMoveViewsetDetail();
        initRelativeLayout();
    }
    private void mMoveViewsetDetail() {
        mMoveView.setUpRectResource(R.drawable.conner);//这里也可以设置shape或者是.9图片
        float density = getResources().getDisplayMetrics().density;//调整大小，如果你的边框大了就修改w_或者h_这两个参数
        RectF receF = new RectF(-getDimension(R.dimen.w_5) * density, -getDimension(R.dimen.h_5) * density,
                -getDimension(R.dimen.w_5) * density, -getDimension(R.dimen.h_5) * density);
        mMoveView.setUpPaddingRect(receF);//重新为mMoveView设置大小
        mMoveView.setTranDurAnimTime(400);
    }
    public float getDimension(int id) {
        return getResources().getDimension(id);
    }
    private void initRelativeLayout() {//这是焦点的全局监听方法，与OnFocusChangeListener不同，这个方法长安不执行。

        mRelativeLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

                if (newFocus != null) {
                   // newFocus.bringToFront();
                    mMoveView.setDrawUpRectEnabled(true);//设置居于放大的view之上。
                    float scale = 1.1f;
                    mMoveView.setFocusView(newFocus, mOldFocus, scale);
                    mMoveView.bringToFront();//将mMoveView的位置bringToFront()
                    mOldFocus = newFocus;//自己将移动后的View进行保存，

                }
            }
        });
    }
}
