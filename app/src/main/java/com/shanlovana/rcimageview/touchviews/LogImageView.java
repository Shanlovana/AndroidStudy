package com.shanlovana.rcimageview.touchviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class LogImageView extends ImageView {

    public static String TAG = "ShanCanCan";

    public LogImageView(Context context) {
        super(context);
    }

    public LogImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "LogImageView  "+ "dispatchTouchEvent  "+ "Event "+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "LogImageView  "+ "onTouchEvent  "+ "Event "+event.getAction());

        return super.onTouchEvent(event);
    }
}
