package com.shancancan.tvdemos.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.shancancan.tvdemos.views.MoveFrameLayout;

/**
 * Created by ShanCanCan on 2016/4/3 0003.
 */

public class MoveAnimationHelperImplement implements MoveAnimationHelper {

    private static final String TAG = "MoveAnimationHelperImplement";
    private static int DEFAULT_TRAN_DUR_ANIM = 300;
    private static final float DEFUALT_SCALE = 1.0f;
    private boolean isDrawUpRect = true;
    private boolean mIsHide = false;
    private boolean isDrawing = false;

    private View mFocusView;

    private MoveFrameLayout moveView;

    private AnimatorSet mCombineAnimatorSet;


    @Override
    public void drawMoveView(Canvas canvas) {
        canvas.save();

        if (!isDrawUpRect) {

            onDrawShadow(canvas);

            onDrawUpRect(canvas);
        }
        // 绘制焦点子控件.
        if (mFocusView != null && (!isDrawUpRect && isDrawing)) {
            onDrawFocusView(canvas);
        }
        //
        if (isDrawUpRect) {

            onDrawShadow(canvas);

            onDrawUpRect(canvas);
        }
        canvas.restore();

    }

    public void setVisibleWidget(boolean isHide) {
        this.mIsHide = isHide;
        getMoveView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
    }


    public boolean isVisibleWidget() {
        return this.mIsHide;
    }

    @Override
    public void setFocusView(View currentView, View oldView, float scale) {

        mFocusView = currentView;
        int getScale = (int) (scale * 10);
        if (getScale > 10) {
            if (currentView != null) {

                currentView.animate().scaleX(scale).scaleY(scale).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
                if (oldView != null) {
                    oldView.animate().scaleX(DEFUALT_SCALE).scaleY(DEFUALT_SCALE).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
                }
            }
        }
        rectMoveAnimation(currentView, scale, scale);

    }

    @Override
    public void rectMoveAnimation(View currentView, float scaleX, float scaleY) {
        Rect fromRect = findLocationWithView(getMoveView());
        Rect toRect = findLocationWithView(currentView);
        int disX = toRect.left - fromRect.left;
        int disY = toRect.top - fromRect.top;
        rectMoveMainLogic(currentView, disX, disY, scaleX, scaleY);
    }

    private Rect findLocationWithView(View view) {
        ViewGroup root = (ViewGroup) getMoveView().getParent();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    private void rectMoveMainLogic(final View focusView, float x, float y, float scaleX, float scaleY) {
        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;
        if (focusView != null) {
            newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
            newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
            x = x + (focusView.getMeasuredWidth() - newWidth) / 2;
            y = y + (focusView.getMeasuredHeight() - newHeight) / 2;
        }

        // 取消之前的动画.
        if (mCombineAnimatorSet != null)
            mCombineAnimatorSet.cancel();

        oldWidth = getMoveView().getMeasuredWidth();
        oldHeight = getMoveView().getMeasuredHeight();

        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMoveView(), "translationX", x);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMoveView(), "translationY", y);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleTool(getMoveView()), "width", oldWidth,
                (int) newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleTool(getMoveView()), "height", oldHeight,
                (int) newHeight);
        //
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(DEFAULT_TRAN_DUR_ANIM);
        getMoveView().setVisibility(View.VISIBLE);

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isDrawUpRect)
                    isDrawing = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (!isDrawUpRect)
                    isDrawing = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isDrawUpRect)
                    isDrawing = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (!isDrawUpRect)
                    isDrawing = false;
            }
        });

        mAnimatorSet.start();
        mCombineAnimatorSet = mAnimatorSet;

    }

    /**
     * 绘制外部阴影.
     */
    public void onDrawShadow(Canvas canvas) {
        Drawable drawableShadow = getMoveView().getShadowDrawable();
        if (drawableShadow != null) {
            RectF shadowPaddingRect = getMoveView().getDrawShadowRect();
            int width = getMoveView().getWidth();
            int height = getMoveView().getHeight();
            Rect padding = new Rect();
            drawableShadow.getPadding(padding);
            drawableShadow.setBounds((int) (-padding.left + (shadowPaddingRect.left)), (int) (-padding.top + (shadowPaddingRect.top)),
                    (int) (width + padding.right - (shadowPaddingRect.right)),
                    (int) (height + padding.bottom - (shadowPaddingRect.bottom)));
            drawableShadow.draw(canvas);
        }
    }

    /**
     * 绘制最上层的移动边框.
     */
    public void onDrawUpRect(Canvas canvas) {
        Drawable drawableUp = getMoveView().getUpRectDrawable();
        if (drawableUp != null) {
            RectF paddingRect = getMoveView().getDrawUpRect();
            int width = getMoveView().getWidth();
            int height = getMoveView().getHeight();
            Rect padding = new Rect();
            // 边框的绘制.
            drawableUp.getPadding(padding);
            drawableUp.setBounds((int) (-padding.left + (paddingRect.left)), (int) (-padding.top + (paddingRect.top)),
                    (int) (width + padding.right - (paddingRect.right)), (int) (height + padding.bottom - (paddingRect.bottom)));
            drawableUp.draw(canvas);
        }
    }

    public void onDrawFocusView(Canvas canvas) {
        View view = mFocusView;
        canvas.save();
        float scaleX = (float) (getMoveView().getWidth()) / (float) view.getWidth();
        float scaleY = (float) (getMoveView().getHeight()) / (float) view.getHeight();
        canvas.scale(scaleX, scaleY);
        view.draw(canvas);
        canvas.restore();
    }


    @Override
    public MoveFrameLayout getMoveView() {
        return this.moveView;
    }

    @Override
    public void setMoveView(MoveFrameLayout moveView) {
        this.moveView = moveView;
    }

    @Override
    public void setTranDurAnimTime(int time) {
        DEFAULT_TRAN_DUR_ANIM = time;
    }

    @Override
    public void setDrawUpRectEnabled(boolean isDrawUpRect) {
        this.isDrawUpRect = isDrawUpRect;
        getMoveView().invalidate();
    }


}
