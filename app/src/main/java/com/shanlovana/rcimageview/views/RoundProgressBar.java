package com.shanlovana.rcimageview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.shanlovana.rcimageview.R;


/**
 * Created by Shanlovana on 2017-03-02.
 */

public class RoundProgressBar extends View {

    /*将attrs中的变量存起来，新建画笔*/
    private int roundColor;
    private int roundProgressColor;
    private int textColor;
    private int textSize;
    private int roundWidth;
    private int maxText;
    private int progress;
    private boolean textIsDisplayable;
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    public static final int FILL_UP = 2;

    private Paint mPaint;
    private int mHeight;//圆形progressbar的宽高，在本view没有必要重写，我下面的重写是为了计算一下两者都是wrapcontent的控件的大小
    private int mWidth;


    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //首先我们把在attrs里面定义的东西都给取出来，并初始化画笔。
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressBar, defStyle, 0);
        roundColor = array.getColor(R.styleable.RoundProgressBar_roundColor, 0xfffdba14);
        roundProgressColor = array.getColor(R.styleable.RoundProgressBar_roundProgressColor, 0xfffc8b0d);
        textColor = array.getColor(R.styleable.RoundProgressBar_textColor, 0xff00ff00);
        textSize = array.getDimensionPixelSize(R.styleable.RoundProgressBar_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                16, getResources().getDisplayMetrics()));
        roundWidth = array.getDimensionPixelSize(R.styleable.RoundProgressBar_roundWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                5, getResources().getDisplayMetrics()));
        maxText = array.getInt(R.styleable.RoundProgressBar_maxText, 100);
        progress = array.getInt(R.styleable.RoundProgressBar_progress, 0);
        textIsDisplayable = array.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = array.getInt(R.styleable.RoundProgressBar_style, STROKE);
        array.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //这里主要是想要设置一下wrapcontent这个问题
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        } else {
            //确定字体的大小来决定wrapcontent的最小值
            mPaint.setTextSize(textSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            int textWidth = (int) mPaint.measureText(maxText + "%") + 1;
            mWidth = textWidth + 2 * roundWidth;
        }

        int specModeH = MeasureSpec.getMode(heightMeasureSpec);
        int specSizeH = MeasureSpec.getSize(heightMeasureSpec);
        if (specModeH == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSizeH;
        } else {


        }
        if ((specMode == MeasureSpec.EXACTLY) && (specMode == specModeH)) {
            setMeasuredDimension(mWidth, mHeight);
        } else {
            int desire = Math.max(mWidth, mHeight);
            setMeasuredDimension(desire, desire);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        /*
        * 最难得就是在这里了，要绘画了，这一步和onMeasure一样，
        * 数学计算能力和空间思维能力考查的比较多
        * */

        //首先画最外层的大圆
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - roundWidth / 2;
        mPaint.setColor(roundColor);
        /*在这里判断一下类型，然后画不同类型的圆,如果不是fillup就是空心*/
        if (style == FILL_UP) {
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else if (style == FILL) {
            mPaint.setStyle(Paint.Style.FILL);
        } else if (style == STROKE) {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        /* 设置圆环的宽度
        消除锯齿*/
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setAntiAlias(true);

        canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环

        /**
         * 画圆弧 ，画圆环的进度
         */
        // 设置进度是实心还是空心
        mPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        mPaint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        //该写画圆弧的操作了

        switch (style) {
            case STROKE:

                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / maxText, false, mPaint); // 根据进度画圆弧

                break;
            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                if (progress != 0) {
                    canvas.drawArc(oval, 0, 360 * progress / maxText, true, mPaint); // 根据进度画圆弧
                }
                break;
            case FILL_UP:

                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    int angle = 360 * progress / maxText;
                    if (angle / 2 < 90) {
                        angle = Math.abs(90 - angle / 2);
                    } else {
                        angle = 360 - Math.abs(90 - angle / 2);
                    }
                    canvas.drawArc(oval, angle, 360 * progress / maxText, false, mPaint);// 根据进度画圆弧
                }
                break;
        }

        /**
         * 画进度百分比
         */
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
        int percent = (int) (((float) progress / (float) maxText) * 100); // 先转换成float在进行除法运算，不然为0
        float textWidth = mPaint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        if (textIsDisplayable && percent != 0) {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2, mPaint); // 画出进度百分比
        }


        super.onDraw(canvas);
    }
        /*从这里开始就是提供外部的一些设置方法，get方法和set方法*/
    public synchronized int getMax() {
        return maxText;
    }
    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.maxText = max;
    }
    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > maxText) {
            progress = maxText;
        }
        if (progress <= maxText) {
            this.progress = progress;
            postInvalidate();
        }

    }
    public int getCircleColor() {
        return roundColor;
    }

    /**
     * 设置圆环的颜色
     */
    public void setCircleColor(int circleColor) {
        this.roundColor = circleColor;
    }

    public int getCircleProgressColor() {
        return roundProgressColor;
    }

    /**
     * 设置圆环进度的颜色
     */
    public void setCircleProgressColor(int circleProgressColor) {
        this.roundProgressColor = circleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    /**
     * 设置中间进度百分比的字符串的颜色
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    /**
     * 设置中间进度百分比的字符串的字体大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    /**
     * 设置圆环的宽度
     */
    public void setRoundWidth(int roundWidth) {
        this.roundWidth = roundWidth;
    }

    /**
     * 设置是否显示数字百分比
     *
     * @param flag
     */
    public void setTextIsDisplayable(boolean flag) {
        textIsDisplayable = flag;
    }

    /**
     * 设置进度风格
     * 0空心，1实心，2实心从下到上
     * @param style
     */
    public void setStyle(int style) {
        this.style = style;
    }
}
