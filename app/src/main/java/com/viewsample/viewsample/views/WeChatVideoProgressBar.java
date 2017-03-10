package com.viewsample.viewsample.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;



/**
 * 自定义进度条，从两边向中心缩进知道消失，缩进的速度由时间决定，颜色分两部分，进度条颜色与背景色
 * Created by Administrator on 2016/11/23.
 */

public class WeChatVideoProgressBar extends View {
    public WeChatVideoProgressBar(Context context) {
        super(context);
        init();
    }

    public WeChatVideoProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public WeChatVideoProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint mPaint;

    /**
     * 开始状态
     */
    private final short STATUE_START = 0x10;
    /**
     * 暂停状态
     */
    private final short STATUE_STOP = 0x20;
    /**
     * 重置状态
     */
    private final short STATUE_RESET = 0x30;

    /**
     * 结束状态
     */
    private final short STATUE_OVER = 0x40;

    /**
     * 当下所处的状态，默认重置状态
     */
    private short nowStatue = STATUE_RESET;

    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;
    /**
     * 空间宽高
     */
    private int viewWid=-1,viewHei=-1;
    /**
     * 进度条从两边缩进到中间需要多少时间，单位ms
     */
    private int maxTime = 6*1000;
    /**
     * 进度条前置色，背景色
     */
    private int progressColor = Color.GREEN,backgroupColor = Color.BLACK;

    /**
     * 我决定将画笔宽度调成View高度，然后在画布上画线就行了
     */
    private float lineStartX,lineStopX;

    /**
     * 我决定将画笔宽度调成View高度，然后在画布上画线就行了
     */
    private float lineStartY,lineStopY;

    /**
     * 循环线程，刷新界面
     */
    private PoorThread mPoorThread;

    /**
     * 循环线程的停止
     */
    private boolean isPoor = true;

    /**
     *  界面刷新频率，单位ms
     */
    private final int  SLEEP_TIME= 5;

    /**
     * drawLine时，x轴偏移量
     */
    private float changeX;

    /**
     * 回调接口
     */
    private Callback mCallback;


    /**
     * 获取屏幕宽高，初始化画笔
     */
    private void init() {
        WindowManager m  = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenHei = m.getDefaultDisplay().getHeight();
        screenWid = m.getDefaultDisplay().getWidth();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }


        @Override
        protected void onDraw(Canvas canvas) {

            //先绘制背景色
            canvas.drawColor(backgroupColor);

            //根据运行时的状态判断该做什么
            switch (nowStatue){
                //重置
                case STATUE_RESET:
                    break;
                //运行
                case STATUE_START:
                //暂停
                case STATUE_STOP:
                    canvas.drawLine(lineStartX,lineStartY,lineStopX,lineStopY,mPaint);
                    break;
                //结束
                case STATUE_OVER:
                    break;
            }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY){
            viewWid = MeasureSpec.getSize(widthMeasureSpec);
        }else{
            //默认宽度为屏幕宽度
            viewWid = screenWid;
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY){
            viewHei = MeasureSpec.getSize(heightMeasureSpec);
        }else {
            //默认高度为屏幕高度的110分之1
            viewHei = screenHei / 200;
        }

        setMeasuredDimension(viewWid,viewHei);
        //将画笔宽度设置为组件高度
        mPaint.setStrokeWidth(viewHei);
        //重置状态
        reset();
    }


    /**
     * 设置进度条的颜色，在{@link #STATUE_START}状态下会立即生效
     * @param progressColor 进度条的颜色
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        //将画笔颜色设置为进度条颜色
        mPaint.setColor(progressColor);
    }

    /**
     * 设置背景色，在{@link #STATUE_START}状态下会立即生效
     * @param backgroupColor
     */
    public void setBackgroupColor(int backgroupColor) {
        this.backgroupColor = backgroupColor;
    }

    /**
     * 设置进度条持续时间，此进度条是由两边向中间缩进，这个过程持续的时间
     * @param maxTime
     */
    public void setMaxTime(int maxTime) {
        if (STATUE_RESET == nowStatue){
            this.maxTime = maxTime;
        }
    }

    public void setCallback(Callback mCallback){
        if (mCallback!=null){
            this.mCallback = mCallback;
        }
    }

    /**
     * 进度条开始缩进，此方法只能在调用了{@link #reset()} 或 {@link #stop()}后调用才会开始缩进
     */
    public void start(){
        //重置--->开始 //暂停--->开始
        if (nowStatue==STATUE_RESET || nowStatue==STATUE_STOP){
            nowStatue = STATUE_START;

            mPoorThread.notifyPoorThread();

        }
    }

    /**
     * 此方法只有在调用{@link #start()}后调用才有效
     * 停止缩进
     */
    public void stop(){
        //start ---> stop;
        if (nowStatue == STATUE_START){
            nowStatue = STATUE_STOP;
        }
    }

    /**
     * 进度条缩进结束
     */
    private void over(){
        //start ---> over;
        if (nowStatue == STATUE_START){
            nowStatue = STATUE_OVER;
            mPoorThread = null;
            isPoor = false;
            if (mCallback!=null){
                mCallback.overCallBack();
            }
        }

    }

    /**
     * 重置进度条到初始状态
     */
    public void reset(){
        nowStatue = STATUE_RESET;
        lineStartX = 0;
        lineStopX = viewWid;
        lineStartY=lineStopY=viewHei/2;
        mPaint.setColor(progressColor);
        changeX = viewWid/2.0f/maxTime*SLEEP_TIME;
        if (mPoorThread == null){
            isPoor = true;
            mPoorThread= new PoorThread();
            mPoorThread.start();
        }
        mPoorThread.notifyPoorThread();
    }


    /**
     * 循环线程，计算绘画线条时的偏移量，并重复刷新界面
     */
    private class PoorThread extends Thread{

        private boolean isWait = false;

        public PoorThread(){
            setName(getClass().getSimpleName());
        }

        @Override
        public void run() {
            //循环刷新界面，直到进度条缩进完毕
            while (isPoor){
                try {
                    //如果当前状态为暂停或重置状态
                    if (nowStatue == STATUE_STOP || nowStatue == STATUE_RESET){
                        //则线程等待
                        waitPoorThread();
                    }
                    //更改drawLine时X的起点
                    lineStartX += changeX;
                    //更改drawLine时X的终点
                    lineStopX -= changeX;

                    //如果起点或超过了宽度的一般，或终点小于宽度的一般
                    if (lineStartX > viewWid/2 || lineStopX <viewWid/2){
                        //就将起点终点设置为宽度的一般
                        lineStartX = viewWid/2;
                        lineStopX = viewWid/2;
                        //并认为缩进结束
                        over();
                    }
                    //重绘的间隔
                    sleep(SLEEP_TIME);
                    //重绘
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //sleep崩溃则重置界面
                    reset();
                    return;
                }
            }
        }

        /**
         * 让循环线程切换为wait状态
         */
        synchronized void notifyPoorThread(){
            synchronized(mPoorThread){
                if (mPoorThread!=null && isPoor && isWait){
                    mPoorThread.notify();
                    isWait = false;
                }
            }
        }

        /**
         * 唤醒循环线程继续刷新界面
         */
        synchronized void waitPoorThread(){
            synchronized(mPoorThread){
                if (mPoorThread!=null && isPoor && !isWait){
                    try {
                        isWait = true;
                        mPoorThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isWait = false;
                    }
                }
            }
        }

    }


    /**
     * 回调接口
     */
    public static interface Callback{

        /**
         * 当进度条缩进结束，回调
         */
        public void overCallBack();

    }


}
