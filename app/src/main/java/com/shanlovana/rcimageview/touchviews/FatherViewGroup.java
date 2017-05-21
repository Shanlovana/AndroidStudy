package com.shanlovana.rcimageview.touchviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class FatherViewGroup extends RelativeLayout {

    public static String TAG = "ShanCanCan";

    public FatherViewGroup(Context context) {
        super(context);
    }

    public FatherViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FatherViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "FatherViewGroup  "+ "onInterceptTouchEvent  "+ "Event "+ev.getAction());
        return super.onInterceptTouchEvent(ev);
        //return  true;//测试拦截事件
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "FatherViewGroup  "+ "dispatchTouchEvent  "+ "Event "+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "FatherViewGroup  "+ "onTouchEvent  "+ "Event "+event.getAction());

       // return super.onTouchEvent(event);
        return true;//测试消费Target的touch事件有哪些。
    }
}
