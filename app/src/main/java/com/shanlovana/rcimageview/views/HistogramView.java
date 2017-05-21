package com.shanlovana.rcimageview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.shanlovana.rcimageview.R;

import java.util.ArrayList;

/**
 * Created by Shanlovana on 201numList.size()-04-18.
 */

public class HistogramView extends View {


    private ArrayList<String> nameList; //每条柱状图的名字

    private ArrayList<Float> numList;//设置每条柱状图的目标值，除以设置的最大值即为比例

    private int histogramrNum; //设置一共有几条柱状图

    private int currentVerticalLineProgress;//每条竖线的当前比例

    private int currentHorizentalLineProgress; //最上面一条横线的比例

    private int verticalLineNum = 4;//一共有几条竖线

    private String unit = "";//单位

    private float numPerUnit;   //每条竖线之间相差的值

    private Float[] currentBarProgress; //设置每条柱状图的当前比例


    private Paint mLinePaint;//画线的画笔

    private Paint mBarPaint;//画柱状图的画笔

    private Paint mTextPaint; //写字的画笔

    private int cylinderColor;//主题颜色

    private int axesColor;//坐标线颜色

    private int textColor;//文字颜色


    private int startX; //开始X坐标

    private int startY;//开始Y坐标

    private int stopX; //结束X坐标


    private int measuredWidth;//测量值 宽度

    private int measuredHeight;//测量值 高度

    private int cylinderWidth;//每条柱状图的宽度

    private float maxSize;//设置最大值，用于计算比例

    private int textSize;//设字体大小

    private int deltaX; //每条竖线之间的间距

    private int deltaY; //每条柱状图之间的间距


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
        maxSize = a.getFloat(R.styleable.HistogramView_maxSize, 100f);
        cylinderWidth = a.getInt(R.styleable.HistogramView_cylinderWith, 50);
        textSize = a.getDimensionPixelSize(R.styleable.RoundProgressBar_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                12, getResources().getDisplayMetrics()));
        cylinderColor = a.getColor(R.styleable.HistogramView_cylinderColor, 0xff40E0D0);
        axesColor = a.getColor(R.styleable.HistogramView_axesColor, 0xffcdcdcd);
        textColor = a.getColor(R.styleable.HistogramView_textColor, 0xffababab);
        a.recycle();
        initColorPaint();
    }

    private void initColorPaint() {
        //设开始X坐标为0
        startX = 0;
        //设开始Y坐标为50
        startY = 50;
        //初始化柱状图画笔
        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setColor(cylinderColor);
        mBarPaint.setStyle(Paint.Style.FILL);
        //初始化线的画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(axesColor);
        mLinePaint.setStrokeWidth(2);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(textColor);
    }


    /**
     * 这里主要是想要处理一下wrapcontent这个问题，EXACTLY和AT_MOST问题
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {//都是wrapcontent
            setMeasuredDimension((int) maxSize, startY + 10 + histogramrNum * (cylinderWidth + 2 * 10));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {//如果宽度为wrapcontent  高度为数值的时候
            setMeasuredDimension((int) maxSize, heightSpecSize);// 宽度设置为maxSize
        } else if (heightSpecMode == MeasureSpec.AT_MOST) { //如果宽度为数值的时候，高度为wrapcontent
            setMeasuredDimension(widthSpecSize, startY + 10 + histogramrNum * (cylinderWidth + 2 * 10)); //高度为每条柱状图的宽度加上间距再乘以柱状图条数再加上开始Y值后得到的值
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        measuredWidth = getMeasuredWidth();//获得测量后的宽度

        measuredHeight = getMeasuredHeight();//获得测量后的高度

        stopX = measuredWidth - cylinderWidth; //计算结束X的值

        deltaX = (stopX - (startX + numList.size() * cylinderWidth / 5)) / verticalLineNum; //计算每条竖线之间的间距

        deltaY = (measuredHeight - startY - cylinderWidth * histogramrNum) / histogramrNum; //计算每条柱状图之间的间距

        numPerUnit = maxSize / verticalLineNum; //计算出每条竖线所代表的数值

        currentHorizentalLineProgress = stopX;//初始化最上面横线的初始进度
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * 画柱状图
         */
        for (int i = 0; i < histogramrNum; i++) {
            if (currentBarProgress[i] < (numList.get(i) / maxSize) * stopX) {
                currentBarProgress[i] += 5;
                postInvalidateDelayed(10);
            }
            canvas.drawText(nameList.get(i), startX, startY + deltaY + i * (deltaY + cylinderWidth) + 3 * cylinderWidth / 4, mTextPaint);
            canvas.drawRect(startX + numList.size() * cylinderWidth / 5, startY + deltaY + i * (deltaY + cylinderWidth), currentBarProgress[i], startY + deltaY + i * (deltaY + cylinderWidth) + cylinderWidth, mBarPaint);
        }
        /**
         * 画竖线
         */
        for (int i = 0; i < verticalLineNum; i++) {
            if (currentVerticalLineProgress < measuredHeight) {
                currentVerticalLineProgress += 3;
                postInvalidateDelayed(10);
            }
            canvas.drawLine((startX + 7 * cylinderWidth / 5) + (i + 1) * deltaX, startY, (startX + 7 * cylinderWidth / 5) + (i + 1) * deltaX, currentVerticalLineProgress, mLinePaint);
            canvas.drawText(numPerUnit * (i + 1) + unit, (startX + 7 * cylinderWidth / 5) + (i + 1) * deltaX - cylinderWidth, startY - cylinderWidth / 5, mTextPaint);
        }
        /**
         * 画最上面的横线
         */
        if (currentHorizentalLineProgress > startX + numList.size() * cylinderWidth / 5) {
            currentHorizentalLineProgress -= 10;
            postInvalidateDelayed(10);
        }
        canvas.drawLine(stopX, startY, currentHorizentalLineProgress, startY, mLinePaint);
        super.onDraw(canvas);
    }

    /**
     * 设置每个柱状图的宽度
     *
     * @param width
     */
    public void setcylinderWidth(int width) {
        this.cylinderWidth = width;
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(float max) {
        this.maxSize = max;
    }


    /**
     * 分别设置每个柱状图的目标值
     *
     * @param numList
     */
    public void setNumListForEvery(ArrayList<Float> numList) {
        this.numList = numList;

    }

    /**
     * 分别设置每个柱状图的名字
     *
     * @param nameList
     */
    public void settNameListForEvery(ArrayList<String> nameList) {
        this.nameList = nameList;
        this.histogramrNum = nameList.size();
        currentBarProgress = new Float[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            currentBarProgress[i] = 0.0f;
        }
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 设置有几条竖线
     *
     * @param num
     */
    public void setVerticalLineNum(int num) {
        this.verticalLineNum = num;
    }


}

