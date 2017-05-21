package com.shanlovana.rcimageview.touchviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class GrandPaViewGroup extends RelativeLayout {

    public static String TAG = "ShanCanCan";

    public GrandPaViewGroup(Context context) {
        super(context);
    }

    public GrandPaViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrandPaViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "GrandPaViewGroup  "+ "onInterceptTouchEvent  "+ "Event "+ev.getAction());
        if (ev.getAction()==MotionEvent.ACTION_UP){
            return  true;
        }//例四测试
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "GrandPaViewGroup  "+ "dispatchTouchEvent  "+ "Event "+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "GrandPaViewGroup  "+ "onTouchEvent  "+ "Event "+event.getAction());

        return super.onTouchEvent(event);
    }
}
