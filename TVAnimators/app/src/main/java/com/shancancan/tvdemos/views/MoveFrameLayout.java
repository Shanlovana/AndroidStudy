package com.shancancan.tvdemos.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shancancan.tvdemos.tools.MoveAnimationHelper;
import com.shancancan.tvdemos.tools.MoveAnimationHelperImplement;

/**
 *
 * Created by ShanCanCan on 2016/4/3 0003.
 */

public class MoveFrameLayout extends FrameLayout {

    private static final String TAG = "MoveFramLayout";
    private Context mContext;
    private Drawable mRectUpDrawable;
    private Drawable mRectUpShade;
    private MoveAnimationHelper mMoveAnimationHelper;

    private RectF mShadowPaddingRect = new RectF();
    private RectF mUpPaddingRect = new RectF();


    public MoveFrameLayout(Context context) {
        super(context);
        init(context);
    }


    public MoveFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setWillNotDraw(false);
        mMoveAnimationHelper = new MoveAnimationHelperImplement();
        mMoveAnimationHelper.setMoveView(this);

    }


    public void setFocusView(View currentView, View oldView, float scale) {
        mMoveAnimationHelper.setFocusView(currentView, oldView, scale);

    }

    public View getUpView() {
        return this;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (mMoveAnimationHelper != null) {
            mMoveAnimationHelper.drawMoveView(canvas);
            return;
        }
        super.onDraw(canvas);

    }

    public void setUpRectResource(int id) {
        try {
            this.mRectUpDrawable = mContext.getResources().getDrawable(id); // 移动的边框.
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpRectShadeResource(int id) {

        this.mRectUpShade = mContext.getResources().getDrawable(id); // 移动的边框.
        invalidate();

    }
    public Drawable getShadowDrawable() {
        return this.mRectUpShade;
    }


    public Drawable getUpRectDrawable() {
        return this.mRectUpDrawable;
    }

    public RectF getDrawShadowRect() {
        return this.mShadowPaddingRect;
    }

    public RectF getDrawUpRect() {
        return this.mUpPaddingRect;
    }

    public void setUpPaddingRect(RectF upPaddingRect) {
        mUpPaddingRect = upPaddingRect;
    }

    public void setShadowPaddingRect(RectF shadowPaddingRect) {
        mShadowPaddingRect = shadowPaddingRect;
    }

    public  void setTranDurAnimTime(int defaultTranDurAnim) {
        mMoveAnimationHelper.setTranDurAnimTime(defaultTranDurAnim);
    }

    public void setDrawUpRectEnabled(boolean isDrawUpRect) {
        mMoveAnimationHelper.setDrawUpRectEnabled(isDrawUpRect);
    }
}
