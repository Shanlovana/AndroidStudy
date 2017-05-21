package com.shanlovana.rcimageview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Shanlovana on 2017-03-26.
 */

public class PathView extends View {
    // 实例化画笔
    private Paint mPaint = null;
    private Path mPath;// 路径对象
    private Context mContext;


    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        initPaint();
        //初始化path
        initPath();


    }

    private void initPath() {
        // 实例化路径
        mPath = new Path();
        // 定义路径的起点
        mPath.moveTo(10, 50);

        // 定义路径的各个点
        for (int i = 0; i <= 20; i++) {
            mPath.lineTo(i * 20, (float) (Math.random() * 100));
        }
    }


    private void initPaint() {
        // 实例化画笔并打开抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.DKGRAY);
    }

    /*
   * 绘制view时调用的方法，可能会出现多次调用，所以不建议在这里面实例化对象，也就是不要出现new
   *
   * @param canvas 一个画布对象，我们可以用paint在上面画画
   */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int phase = 1;

        /*
         * 绘制路径
         */
        // 没有做处理，还没有写代码
        mPaint.setPathEffect(null);//什么都不设置，没有做处理，显示生硬
        canvas.drawPath(mPath, mPaint);

        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        mPaint.setPathEffect(new CornerPathEffect(10));
        canvas.drawPath(mPath, mPaint);
        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        mPaint.setPathEffect(new DiscretePathEffect(3.0F, 5.0F));
        canvas.drawPath(mPath, mPaint);
        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        mPaint.setPathEffect(new DiscretePathEffect(10.0F, 2.0F));
        canvas.drawPath(mPath, mPaint);
        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        mPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 1));
        canvas.drawPath(mPath, mPaint);

        //这个绘制需拉出去单独写，才会有动态效果，此处就是为了统一展示  ---start
        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        mPaint.setPathEffect(new DashPathEffect(new float[]{20, 10, 50, 5, 100, 30, 10, 5}, phase));
        canvas.drawPath(mPath, mPaint);
        phase++;
        invalidate();
        //这个绘制需拉出去单独写，才会有动态效果，此处就是为了统一展示  ---end


        //2，这个绘制需拉出去单独写，才会有动态效果，此处就是为了统一展示  ---start
        canvas.translate(0, 100);//下移100dp
        /*
         * 绘制路径
         */
        //
        Path path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CCW);
        PathEffect pathEffect = new PathDashPathEffect(path, 12, phase, PathDashPathEffect.Style.ROTATE);

        mPaint.setPathEffect(pathEffect);
        canvas.drawPath(mPath, mPaint);
        // 改变偏移值
        phase++;
        // 重绘，产生动画效果
        invalidate();
        //2，这个绘制需拉出去单独写，才会有动态效果，此处就是为了统一展示  ---end


    }

}
