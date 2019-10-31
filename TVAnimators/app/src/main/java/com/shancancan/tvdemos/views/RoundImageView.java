package com.shancancan.tvdemos.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.shancancan.tvdemos.R;


/**
 * Created by Shanlovana on 2017-02-19.
 */

public class RoundImageView extends ImageView {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;


    private int type; //picture type

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    private static final int BODER_RADIUS_DEFAULT = 10;  //default rectange border radius

    private int mBorderRadius;


    private Paint mBitmapPaint;

    private int mRadius;   //circle radius

    private Matrix mMatrix; //matrix  for scale

    private BitmapShader mBitmapShader; //bitmapshader

    private int mWidth; //views width

    private RectF mRoundRect;

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);

        mBorderRadius = array.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BODER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));// default is 10
        type = array.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// circle default

        array.recycle();
        // init paint and matrix
        mMatrix = new Matrix();

        mBitmapPaint = new Paint();

        mBitmapPaint.setAntiAlias(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * if the shape is circle ,choose the min in width and height
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    /**
     * @param drawable used for convert Bitmap to Drawable
     */
    private Bitmap bitmapToDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // init bitmapshader
    private void initBitmapShader() {

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bitmap = bitmapToDrawable(drawable);

        if (bitmap == null) {
            invalidate();
            return;
        }
        // use bitmap Aas a shader, is drawn in the specified area
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;


        if (type == TYPE_CIRCLE) {
            // get the min of bitmap width or height
            int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_ROUND) {

            if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
                /*If the width of the picture or the width of the view does not match
                the width of the need to calculate the need to scale the scale; zoom picture
                 after the width and height, must be greater than the width of our view;
                 so we take a large value*/

                scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(),
                        getHeight() * 1.0f / bitmap.getHeight());
            }

        }

        // shader transformation matrix, we mainly used here to zoom in or out
        mMatrix.setScale(scale, scale);
        // set the transformation matrix
        mBitmapShader.setLocalMatrix(mMatrix);
        // set shader
        mBitmapPaint.setShader(mBitmapShader);


    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (getDrawable() == null) {
            return;
        }
        initBitmapShader();

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else  // circle
        {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldwidth, int oldheight) {
        super.onSizeChanged(w, h, oldwidth, oldheight);

        // The range of fillet
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, w, h);
    }



     /*Save the status of the image start*/

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }

    }
     /*Save the status of the image  end*/



    /*Provide the usual setting method*/

    public void setBorderRadius(int borderRadius)
    {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal)
        {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type)
    {
        if (this.type != type)
        {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE)
            {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}
