package com.viewsample.viewsample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * <p>自定义控件，一个空心粗边框的圆，圆中有文本</p>
 * <p>1，控件默认宽高相等，为屏幕宽高取小后1/3</p>
 * <p>2，圆的直径为控件宽高取小后的值</p>
 * <p>3，默认控件无内边距</p>
 * <p>4，圆的边框为控件宽高取小后的值得1/10，边框颜色默认为{@link Color#GREEN}</p>
 * <p>5，中心字体样式为粗体，颜色默认为{@link Color#GREEN}，大小取决于圆的直径</p>
 * Created by Administrator on 2016/11/25.
 */
public class WeChatVideoButton extends View {

    /**
     *圆边框的宽度为半径的百分之几
     */
    private int ArcStrockWidScale = 5;

    private Paint mPaint;

    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;

    /**
     * 控件X，Y中心
     */
    private int viewCententX,viewCententY;

    /**
     * 圆的边框的宽度（应为圆的半径的{@link #ArcStrockWidScale}/100）
     */
    private int arcStrockWid;

    /**
     * 圆的半径（应为控件的宽度-Max(内边距)-边框的宽度）
     */
    private int arcRadius;

    /**
     * 圆的边框的颜色
     */
    private int arcColor = Color.GREEN;

    /**
     * 文本
     */
    private String textString = "开始";

    /**
     * 字体颜色
     */
    private int textColot = arcColor;

    /**
     * 字体大小（直径/4*3/文本长度）
     */
    private float textSize;

    /**
     * 背景色
     */
    private int backgroupColor = Color.BLACK;

    public WeChatVideoButton(Context context) {
        super(context);
        init();
    }

    public WeChatVideoButton(Context context, AttributeSet attrs) {
        super(context, attrs );
        init();
    }
    public WeChatVideoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        WindowManager m = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenHei = m.getDefaultDisplay().getHeight();
        screenWid = m.getDefaultDisplay().getWidth();

        mPaint = new Paint();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景色
        canvas.drawColor(backgroupColor);

        //画笔调为边框模式
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔变框的宽度为圆的边框宽度
        mPaint.setStrokeWidth(arcStrockWid);
        //设置画笔颜色为圆的颜色
        mPaint.setColor(arcColor);
        //画圆
        canvas.drawCircle(
                viewCententX,
                viewCententY,
                arcRadius,
                mPaint
        );


        //设置字体颜色
        mPaint.setColor(textColot);
        //设置字体大小
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置文本水平居中
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获得文本高度
//        fontMetrics.descent-fontMetrics.ascent //字符串高度
//        mPaint.measureText(textString);   //字符串宽度
        //画文本
        try {
            canvas.drawText(
                    textString,
                    viewCententX,
                    //垂直居中
                    viewCententY - (fontMetrics.bottom - fontMetrics.top)/2 -fontMetrics.top,
                    mPaint
            );
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minScreen = Math.min(screenHei,screenWid);

        //控件宽高
        int viewWid,viewHei;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY){
            viewWid = MeasureSpec.getSize(widthMeasureSpec);
        }else {
            viewWid = Math.round(minScreen / 3.0f * 2);
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY){
            viewHei = MeasureSpec.getSize(heightMeasureSpec);
        }else{
            viewHei = Math.round(minScreen / 3.0f * 2);
        }

        setMeasuredDimension(viewWid,viewHei);

        //圆心X
        viewCententX = Math.round(viewWid/2 );
        //圆心Y
        viewCententY = Math.round(viewHei/2 );

        //宽高取小
        int minView = Math.min(viewHei,viewWid);

        //无边框下的圆的半径
        arcRadius = (minView - Math.max(Math.max(getPaddingLeft(),getPaddingRight()),Math.max(getPaddingTop(),getPaddingBottom())))/2;
        //边框宽度为圆的半径的ArcStrockWidScale/100
        arcStrockWid = Math.round(arcRadius*1.0f/100*ArcStrockWidScale);
        //有边框下的圆的半径
        arcRadius -= Math.round(arcStrockWid/2.0f);
        //字体大小
        textSize = arcRadius*2f/(textString.length()*1.5f);
    }



    public void setArcStrockWid(int arcStrockWid) {
        this.arcStrockWid = arcStrockWid;
    }

    public void setArcStrockWidScale(int ArcStrockWidScale) {
        this.ArcStrockWidScale = ArcStrockWidScale;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }

    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public void setTextColot(int textColot) {
        this.textColot = textColot;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setBackgroupColor(int backgroupColor) {
        this.backgroupColor = backgroupColor;
    }


}
