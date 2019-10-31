package com.shancancan.tvdemos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 *
 * Created by ShanCanCan on 2016/4/3 0003.
 */

public class MainRelativeLayout extends RelativeLayout {

    private int position;



    public MainRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public MainRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



        private void init(Context context){
            setClipChildren(false); //是否现限制其他控件在它周围绘制选择false
            setClipToPadding(false); //是否限制控件区域在padding里面
            setChildrenDrawingOrderEnabled(true);//用于改变控件的绘制顺序

            getViewTreeObserver()
                    .addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                        @Override
                        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                            position = indexOfChild(newFocus);
                            if (position != -1) {
                                bringChildToFront(newFocus);
                                newFocus.postInvalidate();// 然后让控件重画，这样会好点。
                            }
                        }
                    });
        }


    /**
     * 此函数 dispatchDraw 中调用.
     * 原理就是和最后一个要绘制的view，交换了位置.
     * 因为dispatchDraw最后一个绘制的view是在最上层的.
     * 这样就避免了使用 bringToFront 导致焦点错乱问题.
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (position != -1) {
            if (i == childCount - 1){
                return position;
            }
            if (i == position)
                return childCount - 1;
        }
        return i;
    }
}
