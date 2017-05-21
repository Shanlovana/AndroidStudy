package com.shanlovana.rcimageview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.shanlovana.rcimageview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class ChartView extends View {

    private static final String TAG = "ChartView";

    private float viewWith;
    private float viewHeight;

    private float brokenLineWith = 0.5f;

    private int brokenLineColor = 0xffff0000;
    private int straightLineColor = 0xff000000;//0xffeaeaea 0xffe2e2e2
    private int textNormalColor = 0xff00ff00;

    private int maxScore = 800;
    private int minScore = 500;

    private int monthCount = 12;
    private int selectMonth = 5;//选中的月份

    private String[] monthText = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private int[] score = new int[]{540, 640, 733, 568, 595, 732, 600, 685, 742, 653, 732, 798};


    /*******
     * set get method
     ******/

    public int[] getScore() {
        return score;
    }

    public void setScore(int[] score) {
        this.score = score;
        initData();
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    /****
     * set get method
     *****/

    private List<Point> scorePoints;

    private int textSize = dipToPx(15);

    private Paint brokenPaint;
    private Paint straightPaint;
    private Paint dottedPaint;
    private Paint textPaint;

    private Path brokenPath;


    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        maxScore = a.getInt(R.styleable.ChartView_max_score, 800);
        minScore = a.getInt(R.styleable.ChartView_min_score, 600);
        brokenLineColor = a.getColor(R.styleable.ChartView_broken_line_color, brokenLineColor);
        textNormalColor = a.getColor(R.styleable.ChartView_textColor, textNormalColor);
        textSize = a.getDimensionPixelSize(R.styleable.ChartView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                15, getResources().getDisplayMetrics()));
        straightLineColor = a.getColor(R.styleable.ChartView_dottedlineColor, straightLineColor);
        brokenLineWith = a.getDimensionPixelSize(R.styleable.ChartView_lineWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                1, getResources().getDisplayMetrics()));

        a.recycle();

        //初始化path以及Paint
        brokenPath = new Path();

        brokenPaint = new Paint();
        brokenPaint.setAntiAlias(true);
        brokenPaint.setStyle(Paint.Style.STROKE);
        brokenPaint.setStrokeWidth(dipToPx(brokenLineWith));
        brokenPaint.setStrokeCap(Paint.Cap.ROUND);

        straightPaint = new Paint();
        straightPaint.setAntiAlias(true);
        straightPaint.setStyle(Paint.Style.STROKE);
        straightPaint.setStrokeWidth(brokenLineWith);
        straightPaint.setColor((straightLineColor));
        straightPaint.setStrokeCap(Paint.Cap.ROUND);

        dottedPaint = new Paint();
        dottedPaint.setAntiAlias(true);
        dottedPaint.setStyle(Paint.Style.STROKE);
        dottedPaint.setStrokeWidth(brokenLineWith);
        dottedPaint.setColor((straightLineColor));
        dottedPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor((textNormalColor));
        textPaint.setTextSize(textSize);
    }


    //初始化数据
    private void initData() {
        scorePoints = new ArrayList<Point>();
        float maxScoreYCoordinate = viewHeight * 0.1f;
        float minScoreYCoordinate = viewHeight * 0.6f;

        Log.v(TAG, "initData: " + maxScoreYCoordinate);

        float newWith = viewWith - (viewWith * 0.15f) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        int coordinateX;

        for (int i = 0; i < score.length; i++) {
            Log.v(TAG, "initData: " + score[i]);
            Point point = new Point();
            coordinateX = (int) (newWith * ((float) (i) / (monthCount - 1)) + (viewWith * 0.15f));
            point.x = coordinateX;
            if (score[i] > maxScore) {
                score[i] = maxScore;
            } else if (score[i] < minScore) {
                score[i] = minScore;
            }
            point.y = (int) (((float) (maxScore - score[i]) / (maxScore - minScore)) * (minScoreYCoordinate - maxScoreYCoordinate) + maxScoreYCoordinate);
            scorePoints.add(point);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDottedLine(canvas, viewWith * 0.15f, viewHeight * 0.1f, viewWith, viewHeight * 0.1f);//上面一条虚线的画法，不懂看坐标系那一张图
        drawDottedLine(canvas, viewWith * 0.15f, viewHeight * 0.6f, viewWith, viewHeight * 0.6f);//下面一条虚线的画法
        drawText(canvas);//绘制文字，minScore，maxScore等等
        drawMonthLine(canvas);//月份的线及坐标点
        drawBrokenLine(canvas);//绘制折线，就是画点，moveto连接
        drawPoint(canvas);//绘制穿过折线的点
    }

    //重写ontouchevent，


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        //一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action，这个事件被消费了

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP://触摸(ACTION_DOWN操作)，滑动(ACTION_MOVE操作)和抬起(ACTION_UP)
                onActionUpEvent(event);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    private void onActionUpEvent(MotionEvent event) {


        boolean isValidTouch = validateTouch(event.getX(), event.getY());//判断是否是指定的触摸区域

        if (isValidTouch) {
            invalidate();
        }

    }


    //是否是有效的触摸范围
    private boolean validateTouch(float x, float y) {

        //曲线触摸区域
        for (int i = 0; i < scorePoints.size(); i++) {
            // dipToPx(8)乘以2为了适当增大触摸面积
            if (x > (scorePoints.get(i).x - dipToPx(8) * 2) && x < (scorePoints.get(i).x + dipToPx(8) * 2)) {
                if (y > (scorePoints.get(i).y - dipToPx(8) * 2) && y < (scorePoints.get(i).y + dipToPx(8) * 2)) {
                    selectMonth = i + 1;
                    return true;
                }
            }
        }
        //月份触摸区域
        //计算每个月份X坐标的中心点
        float monthTouchY = viewHeight * 0.7f - dipToPx(3);//减去dipToPx(3)增大触摸面积

        float newWith = viewWith - (viewWith * 0.15f) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        float validTouchX[] = new float[monthText.length];
        for (int i = 0; i < monthText.length; i++) {
            validTouchX[i] = newWith * ((float) (i) / (monthCount - 1)) + (viewWith * 0.15f);
        }

        if (y > monthTouchY) {
            for (int i = 0; i < validTouchX.length; i++) {
                Log.v(TAG, "validateTouch: validTouchX:" + validTouchX[i]);
                if (x < validTouchX[i] + dipToPx(8) && x > validTouchX[i] - dipToPx(8)) {
                    Log.v(TAG, "validateTouch: " + (i + 1));
                    selectMonth = i + 1;
                    return true;
                }
            }
        }

        return false;
    }


    //绘制折线穿过的点

    protected void drawPoint(Canvas canvas) {

        if (scorePoints == null) {
            return;
        }
        brokenPaint.setStrokeWidth(dipToPx(1));
        for (int i = 0; i < scorePoints.size(); i++) {
            brokenPaint.setColor(brokenLineColor);
            brokenPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(scorePoints.get(i).x, scorePoints.get(i).y, dipToPx(3), brokenPaint);
            brokenPaint.setColor(Color.WHITE);
            brokenPaint.setStyle(Paint.Style.FILL);
            if (i == selectMonth - 1) {
                brokenPaint.setColor(0xffd0f3f2);
                canvas.drawCircle(scorePoints.get(i).x, scorePoints.get(i).y, dipToPx(8f), brokenPaint);
                brokenPaint.setColor(0xff81dddb);
                canvas.drawCircle(scorePoints.get(i).x, scorePoints.get(i).y, dipToPx(5f), brokenPaint);

                //绘制浮动文本背景框
                drawFloatTextBackground(canvas, scorePoints.get(i).x, scorePoints.get(i).y - dipToPx(8f));

                textPaint.setColor(0xffffffff);
                //绘制浮动文字
                canvas.drawText(String.valueOf(score[i]), scorePoints.get(i).x, scorePoints.get(i).y - dipToPx(5f) - textSize, textPaint);
            }
            brokenPaint.setColor(0xffffffff);
            canvas.drawCircle(scorePoints.get(i).x, scorePoints.get(i).y, dipToPx(1.5f), brokenPaint);
            brokenPaint.setStyle(Paint.Style.STROKE);
            brokenPaint.setColor(brokenLineColor);
            canvas.drawCircle(scorePoints.get(i).x, scorePoints.get(i).y, dipToPx(2.5f), brokenPaint);
        }
    }

    private void drawFloatTextBackground(Canvas canvas, int x, int y) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.FILL);

        //P1
        Point point = new Point(x, y);
        brokenPath.moveTo(point.x, point.y);

        //P2
        point.x = point.x + dipToPx(5);
        point.y = point.y - dipToPx(5);
        brokenPath.lineTo(point.x, point.y);

        //P3
        point.x = point.x + dipToPx(12);
        brokenPath.lineTo(point.x, point.y);

        //P4
        point.y = point.y - dipToPx(17);
        brokenPath.lineTo(point.x, point.y);

        //P5
        point.x = point.x - dipToPx(34);
        brokenPath.lineTo(point.x, point.y);

        //P6
        point.y = point.y + dipToPx(17);
        brokenPath.lineTo(point.x, point.y);

        //P7
        point.x = point.x + dipToPx(12);
        brokenPath.lineTo(point.x, point.y);

        //最后一个点连接到第一个点
        brokenPath.lineTo(x, y);

        canvas.drawPath(brokenPath, brokenPaint);

    }


    //绘制折线
    private void drawBrokenLine(Canvas canvas) {
        brokenPath.reset();
        brokenPaint.setColor(brokenLineColor);
        brokenPaint.setStyle(Paint.Style.STROKE);
        if (score.length == 0) {
            return;
        }
        Log.v(TAG, "drawBrokenLine: " + scorePoints.get(0));
        brokenPath.moveTo(scorePoints.get(0).x, scorePoints.get(0).y);
        for (int i = 0; i < scorePoints.size(); i++) {
            brokenPath.lineTo(scorePoints.get(i).x, scorePoints.get(i).y);
        }
        canvas.drawPath(brokenPath, brokenPaint);

    }


    //绘制月份的直线(包括刻度)
    private void drawMonthLine(Canvas canvas) {

        straightPaint.setStrokeWidth(dipToPx(1));
        canvas.drawLine(0, viewHeight * 0.7f, viewWith, viewHeight * 0.7f, straightPaint);

        float newWith = viewWith - (viewWith * 0.15f) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        float coordinateX;//分隔线X坐标
        for (int i = 0; i < monthCount; i++) {
            coordinateX = newWith * ((float) (i) / (monthCount - 1)) + (viewWith * 0.15f);
            canvas.drawLine(coordinateX, viewHeight * 0.7f, coordinateX, viewHeight * 0.7f + dipToPx(4), straightPaint);
        }

    }

    /**
     * @param canvas
     * */
    private void drawText(Canvas canvas) {

        textPaint.setTextSize(textSize);//默认字体15
        textPaint.setColor(textNormalColor);

        canvas.drawText(String.valueOf(maxScore), viewWith * 0.1f - dipToPx(10), viewHeight * 0.1f + textSize * 0.25f, textPaint);
        canvas.drawText(String.valueOf(minScore), viewWith * 0.1f - dipToPx(10), viewHeight * 0.6f + textSize * 0.25f, textPaint);

        textPaint.setColor(0xff7c7c7c);

        float newWith = viewWith - (viewWith * 0.15f) * 2;//分隔线距离最左边和最右边的距离是0.15倍的viewWith
        float coordinateX;//分隔线X坐标
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textNormalColor);
        textSize = (int) textPaint.getTextSize();
        for (int i = 0; i < monthText.length; i++) {//这里是绘制月份，从数组中取出来，一个个的写
            coordinateX = newWith * ((float) (i) / (monthCount - 1)) + (viewWith * 0.15f);

            if (i == selectMonth - 1)//被选中的月份要单独画出来多几个圈圈
            {

                textPaint.setStyle(Paint.Style.STROKE);
                textPaint.setColor(brokenLineColor);
                RectF r2 = new RectF();
                r2.left = coordinateX - textSize - dipToPx(4);
                r2.top = viewHeight * 0.7f + dipToPx(4) + textSize / 2;
                r2.right = coordinateX + textSize + dipToPx(4);
                r2.bottom = viewHeight * 0.7f + dipToPx(4) + textSize + dipToPx(8);
                canvas.drawRoundRect(r2, 10, 10, textPaint);

            }
            //绘制月份
            canvas.drawText(monthText[i], coordinateX, viewHeight * 0.7f + dipToPx(4) + textSize + dipToPx(5), textPaint);//不是就正常的画出

            textPaint.setColor(textNormalColor);

        }


    }


    /**
     * @param canvas 画布
     * @param startX 起始点X坐标
     * @param startY 起始点Y坐标
     * @param stopX  终点X坐标
     * @param stopY  终点Y坐标
     */


    private void drawDottedLine(Canvas canvas, float startX, float startY, float stopX,
                                float stopY) {

        dottedPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 4));
        dottedPaint.setStrokeWidth(1);
        // 实例化路径
        Path mPath = new Path();
        mPath.reset();
        // 定义路径的起点
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);
        canvas.drawPath(mPath, dottedPaint);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWith = w;
        viewHeight = h;
        initData();
    }


    /**
     * @param dip
     * @return px
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
