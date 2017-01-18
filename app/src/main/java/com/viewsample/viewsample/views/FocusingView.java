package com.viewsample.viewsample.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;


/**
 * <p></p>
 * 绘制一个正方形，在12点、3点、6点、9点四个方向上，有向内延伸的一条短短的线。
 * <p>当此控件显示后，会有两个连续动作：</p>
 * <p>1，缩小（需要知道原始大小，缩放后的大小，缩放动画的持续时间）</p>
 * <p>2，三次连续渐变，gradientStart--->gradientEnd--->gradientStart--->gradientEnd--->gradientStart--->gradientEnd---gradientStart，（渐变的持续时间）</p>
 * <p></p>
 * Created by immortalHalfWu on 2016/11/28.
 */
public class FocusingView extends View {
    public FocusingView(Context context) {
        super(context);
        init(context);
    }
    public FocusingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FocusingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Paint mPaint;
    /**
     * 默认背景色为全透明
     */
    private int backgroupColor = 0x00000000;
    /**
     * 正方形的颜色，默认绿色
     */
    private int squareColor = Color.GREEN;
    /**
     * 线条宽度，默认为
     */
    private int paintStrockWid = 2;
    /**
     * 画矩形时的rect对象
     */
    private Rect mRect;
    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;
    /**
     * 屏幕宽高取小，作为计算宽高的基准
     */
    private int screenMin;

    /**
     * 控件的宽高px
     */
    private int viewWid = -1,viewHei = -1;

    /**
     * 缩放后的宽高px
     */
    private int scalingWid = -1,scalingHei =-1;
    /**
     * 缩放动画的持续时间，单位ms
     */
    private int scalingTime = 200;

    /**
     * 渐变的开始值与结束值
     */
    private int gradientStart = 254,gradientEnd = 100;
    /**
     * 单次渐变动画持续时间ms
     */
    private int gradientTime = 100;

    /**
     * 刷新界面的循环线程
     */
    private PoorThread mPoorThread;

    private void init(Context context) {
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenHei = m.getDefaultDisplay().getHeight();
        screenWid = m.getDefaultDisplay().getWidth();
        //屏幕宽高取小
        screenMin = Math.min(screenHei,screenWid);
        m = null;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(squareColor);
        mPaint.setStrokeWidth(paintStrockWid);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(backgroupColor);

        if (mPoorThread == null ){
            mPoorThread = new PoorThread();
            mPoorThread.start();
            return;
        }
//        if (!mPoorThread.isRunning){
//            mPoorThread.start();
//            return;
//        }

        switch (mPoorThread.nowStatue){

            case PoorThread.STATUE_SCALING:

//                canvas.drawRect(mRect,mPaint);
//
//                break;
            case PoorThread.STATUE_GRADIENT:
                canvas.drawRect(mRect,mPaint);
                for (int i = 0;i<360;i+=90){
                    canvas.rotate(i,viewWid/2,viewHei/2);
                    canvas.drawLine(mRect.width()/2+mRect.left,mRect.top,mRect.width()/2+mRect.left,mRect.top+mRect.width()/12,mPaint);
                }
                break;
            case PoorThread.STATUE_OVER:
//                mPoorThread =
                if (mCallback != null){
                    mCallback.over(this);
                }
                break;

         }

    }

    /**
     * 是否一定要是正方形？默认正方形
     * <P></P>
     * 计算分三种情况：
     * <P>1，宽高明确</P>
     * <P>2，宽高只有一个明确</P>
     * <P>3，宽高都不明确</P>
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST){
            //宽不明确
            if (viewWid == -1){
                //宽为屏幕宽高取小/4
                viewWid = screenMin/3;
            }else{
                //宽为外部传入
            }
        }else{
            //直接获取
            viewWid = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST){
            //高不明确
            if (viewHei == -1){
                //默认为屏幕3/1
                viewHei = screenMin/3;
            }
        } else{
              //取高
            viewHei = MeasureSpec.getSize(heightMeasureSpec);
        }


        //控件宽高取小，并相等
        viewHei = Math.min(viewWid,viewHei);
        viewWid = viewHei;


        //计算缩放后的宽高，默认为控件宽高 - 控件宽高取小的三分之二
        if (scalingHei == -1 || screenWid == -1){
//            int viewMin = Math.min(viewWid,viewHei);
            //初始值为-1
            if (scalingHei == -1){
                //未设置，则设为默认值
                scalingHei = viewWid/2;
            }
            //初始值为-1
            if (scalingWid == -1){
                //未设置，则设为默认
                scalingWid = viewWid/2;
            }
        }

        resetRect();

        //加上内边距的宽度
        viewWid+=getPaddingLeft()+getPaddingRight();
        viewHei+=getPaddingTop()+getPaddingBottom();

        //加上画笔的宽度
        viewHei+=paintStrockWid;
        viewWid+=paintStrockWid;


        setMeasuredDimension(viewWid,viewHei);
    }


    public void reset(){
        resetRect();
        resetPoorThread();
    }

    private void resetPoorThread() {
        if (mPoorThread == null){
            mPoorThread = new PoorThread();
            return;
        }
        mPoorThread.resetValue();
        if (!mPoorThread.isRunning){
            mPoorThread.start();
        }
    }

    /**
     * 重置矩形框
     */
    private void resetRect() {
        //设置矩形的宽高
        if (mRect == null){
            mRect = new Rect();
        }

        //矩形初始的起点为内边距
        mRect.left =getPaddingLeft();
        mRect.top = getPaddingTop();
        //矩形初始为控件的宽高
        mRect.right = viewWid;
        mRect.bottom = viewHei;
    }


    /**
     * 数值计算线程+刷新控件。
     * <p>根据当前状态的不同，进行不同的数值计算，sleep的时间相同==10</p>
     */
    class PoorThread extends Thread{
        //是否处在运行中
        boolean isRunning = false;
        //sleep时间ms
        int sleepTime = 10;
        //缩放状态
        static final int STATUE_SCALING = 0x1001;
        //渐变状态
        static final int STATUE_GRADIENT = 0x1002;
        //结束状态
        static final int STATUE_OVER = 0x1003;
        //目前的状态，缩放--->渐变--->结束
        int nowStatue;
        //每次缩放的值，值是固定的
        private int widScaleValue,heiScaleValue;
        //每次渐变的改变值
        private int alphaValue;
        //渐变循环了几次
        private int gradientIndex;

        PoorThread(){
            resetValue();
        }

        public void resetValue() {
            setName(getClass().getSimpleName());
            nowStatue = STATUE_SCALING;
            //计算每次缩放的值，缩放的总距离/缩放的总时间*沉睡的时间
            widScaleValue = Math.round(1.0f*(viewWid - scalingWid)/scalingTime*sleepTime/2);
            heiScaleValue = Math.round(1.0f*(viewHei - scalingHei)/scalingTime*sleepTime/2);
            alphaValue = Math.round(1.0f*(gradientStart - gradientEnd)/gradientTime*sleepTime);
            gradientIndex = 0;
            //画笔透明度设置为255
//            mPaint.setAlpha(255);
        }

        @Override
        public void run() {
            isRunning = true;
            //如果当前状态不是结束状态
            while (nowStatue != STATUE_OVER){

                //根据状态不同，进行不同的操作
                switch (nowStatue){

                    case STATUE_SCALING:
//                        viewWid-=widScaleValue;
//                        viewHei-=heiScaleValue;
                        mRect.left+=widScaleValue;
                        mRect.right-=widScaleValue;
                        mRect.top+=heiScaleValue;
                        mRect.bottom-=heiScaleValue;
                        if (mRect.height() <= scalingHei || mRect.width() <= scalingWid) {
                            mRect.left = viewWid/2 - scalingWid/2;
                            mRect.right = viewWid /2 + scalingWid/2;
                            mRect.top = viewHei /2 - scalingHei/2;
                            mRect.bottom = viewHei /2 + scalingHei/2;
                            nowStatue = STATUE_GRADIENT;
                        }
                        break;

                    case STATUE_GRADIENT:
//                        if (gradientIndex >= 4)
                        int paintAlpha = mPaint.getAlpha();
                        if (paintAlpha >= gradientStart || paintAlpha <= gradientEnd){
                            if (gradientIndex >= 4){
                                nowStatue = STATUE_OVER;
                                break;
                            }
                            alphaValue*=-1;
                            gradientIndex += alphaValue<0? 1:0;
                        }

                        mPaint.setAlpha(paintAlpha + alphaValue);
                        break;
                    default:
                        return;
                }

                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                if (nowStatue != STATUE_OVER){
                    postInvalidate();
                }
            }

            isRunning = false;

        }
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //大小发成转变时，宽高取小
        screenMin = Math.min(w,h);
    }


    public void setBackgroupColor(int backgroupColor) {
        this.backgroupColor = backgroupColor;
    }

    public void setSquareColor(int squareColor) {
        this.squareColor = squareColor;
    }

    public void setViewWid(int viewWid) {
        this.viewWid = viewWid;
    }

    public void setViewHei(int viewHei) {
        this.viewHei = viewHei;
    }

    public void setScalingWid(int scalingWid) {
        this.scalingWid = scalingWid;
    }

    public void setScalingHei(int scalingHei) {
        this.scalingHei = scalingHei;
    }

    public void setScalingTime(int scalingTime) {
        this.scalingTime = scalingTime;
    }

    public void setGradientStart(int gradientStart) {
        this.gradientStart = gradientStart;
    }

    public void setGradientEnd(int gradientEnd) {
        this.gradientEnd = gradientEnd;
    }

    public void setGradientTime(int gradientTime) {
        this.gradientTime = gradientTime;
    }

    public int getScreenWid() {
        return screenWid;
    }

    public int getScreenHei() {
        return screenHei;
    }

    private CallBack mCallback;
    public FocusingView setCallBack(CallBack callback){
        if (callback != null){
            mCallback = callback;
        }
        return this;
    }
    public static interface CallBack{
        public void over(FocusingView v);
    }


}

