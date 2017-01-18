package com.viewsample.viewsample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;



/**
 * 微信视频录制改版后的进度条按钮。
 * <p></p>
 * <p>流程</p>
 * <P>1,不考虑横竖屏切换</P>
 * <P>2,宽高相等，默认为屏幕宽高取小的1/4，也可外部定义</P>
 * <P>3,默认为内外两个圆，外圆半径为控件宽高取小1/3，内圆半径为控件宽高取小1/4，外部圆颜色深于内部圆</P>
 * <P>4,操作为为短按跟长按，短按不考虑</P>
 * <p>5,长按，外部圆放大，内部圆缩小，在外部圆上，依边画绿色进度条，进度条宽度为控件宽度的1/5</p>
 * <p>6,抬起手指，分短按与长按，回调</p>
 * <p></p>
 * <p>状态</p>
 * <p>1,初始状态，内外两个圆</p>
 * <p>2,短按，样式不发生变化</p>
 * <p>3,长按，线程启动，外部圆放大，半径为控件宽高取小1/2，内部圆缩小，半径为控件宽高取小1/6，进度条出现</p>
 * <p>4,抬手，接口回调，短按忽略，长按抬手则重置显示为初始状态(动态)，结束后让poorThread线程等待，</p>
 * Created by immortalHalfWu on 2017/1/16.
 */
public class ProgressButtonView extends View {
    public ProgressButtonView(Context context) {
        super(context);
        init();
    }
    public ProgressButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ProgressButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static final String TAG = "ProgressButtonView";
    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;
    /**
     * 控件的宽高
     */
    private int mWidth,mHeight,mSize;
    /**
     *初始状态下内外部圆的半径
     */
    private float initiaInCircleRadius,initiaOutCircleRadius;
    /**
     * 长按状态下内外圆的半径
     */
    private float longClickInCircleRadius,longClickOutCircleRadius;
    /**
     * 真正绘制时，所用的内外圆的半径
     */
    private float drawInCircleRadius,drawOutCircleRadius;
    /**
     * 内外部圆的颜色
     */
    private int inCircleColor = 0xffFFFFFF,outCircleColor = 0xffD3D3DB;
    /**
     * 进度条的颜色，进度条的宽度，
     */
    private int progressColor = 0xff00cc33,progressWidth;
    /**
     * 画进度条时的弧度
     */
    private float progressSweep;
    /**
     * 进度条最长时间
     */
    private int progressMaxTime = 3 * 1000;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 状态机
     */
    private StateMachine mStateMachine;
    /**
     * 循环线程
     */
    private PoorThread mPoorThread;
    /**
     * 区分长短按的线程
     */
    private ClickInterValRunnable mClickIntervalRunnable;
    /**
     * 回调接口
     */
    private CallBackListener mCallBackListener;

    private RectF arcRectF;


    private void init() {

        mStateMachine = StateMachine.STATE_INITIA;
        arcRectF = new RectF();
        setClickable(true);
        initScreen();
        initPaint();
        initThread();

    }

    private void initScreen() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenHei = manager.getDefaultDisplay().getHeight();
        screenWid = manager.getDefaultDisplay().getWidth();
    }


    private void initValue() {

        initiaOutCircleRadius = mSize/3;
        longClickOutCircleRadius = mSize/2;

        initiaInCircleRadius = mSize/4;
        longClickInCircleRadius = mSize/6;

        drawInCircleRadius = initiaInCircleRadius;
        drawOutCircleRadius = initiaOutCircleRadius;

        progressWidth = mSize/25;
        progressSweep = 0;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    private void initThread() {
        mPoorThread = new PoorThread();
        mClickIntervalRunnable = new ClickInterValRunnable();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                //如果不是初始状态，则直接返回，因为动画需要时间，避免冲突
                if (mStateMachine != StateMachine.STATE_INITIA){
                    return  false;
                }
                //将状态切换为短按
                mStateMachine = StateMachine.STATE_SHORT_CLICK;
                //调用线程，250毫秒后，如果状态还是短按而没有抬手，则将状态切换为长按
                postDelayed(mClickIntervalRunnable,250);

                return true;

            case MotionEvent.ACTION_MOVE:

                if(mCallBackListener!= null && mStateMachine == StateMachine.STATE_LONG_CLICKING){
                    mCallBackListener.move(event);
                }
                break;

            case MotionEvent.ACTION_UP:
                //如果状态为短按，
                if (mStateMachine == StateMachine.STATE_SHORT_CLICK){
                    //则更改状态为短按抬起
                    mStateMachine = StateMachine.STATE_SHORT_UP;
                    //如果传入回调接口
                    if (mCallBackListener != null){
                        //回调
                        mCallBackListener.shortClick();
                    }
                    //回调结束后，将状态切换位初始化
                    mStateMachine = StateMachine.STATE_INITIA;
                }
                //如果状态为长按，
                if (mStateMachine == StateMachine.STATE_LONG_CLICKING){
                    //则更改状态为长按抬起
                    mStateMachine = StateMachine.STATE_LONG_UP;
                    //回调结束
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*
        MeasureSpec.EXACTLY：父视图希望子视图的大小应该是specSize中指定的。

        MeasureSpec.AT_MOST：子视图的大小最多是specSize中指定的值，也就是说不建议子视图的大小超过specSize中给定的值。

        MeasureSpec.UNSPECIFIED：我们可以随意指定视图的大小。
        */

        int widMod = MeasureSpec.getMode(widthMeasureSpec);
        int heiMod = MeasureSpec.getMode(heightMeasureSpec);

        if (widMod == MeasureSpec.EXACTLY){
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        }else if (widMod == MeasureSpec.AT_MOST){
            mWidth = MeasureSpec.getSize(widthMeasureSpec) < screenWid / 3 ? MeasureSpec.getSize(widthMeasureSpec) : screenWid / 3;
        }else if (widMod == MeasureSpec.UNSPECIFIED){
            mWidth = screenWid / 3;
        }

        if (heiMod == MeasureSpec.EXACTLY){
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
        }else if (heiMod == MeasureSpec.AT_MOST){
            mHeight = MeasureSpec.getSize(heightMeasureSpec) < screenHei / 3 ? MeasureSpec.getSize(heightMeasureSpec) : screenHei / 3;
        }else if (heiMod == MeasureSpec.UNSPECIFIED){
            mHeight = screenHei / 3;
        }

        mSize = mHeight = mWidth = Math.min(mHeight + getPaddingTop() + getPaddingBottom(),mWidth + getPaddingLeft() + getPaddingRight());

        setMeasuredDimension(mWidth,mHeight);
        initValue();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        //画外圆
        mPaint.setColor(outCircleColor);
        canvas.drawCircle(canvas.getWidth() / 2,canvas.getHeight() /2,drawOutCircleRadius,mPaint);

        //如果当前状态为长按中，并且进图条得弧度大于0
        if (mStateMachine == StateMachine.STATE_LONG_CLICKING && progressSweep > 0){
            arcRectF.set(canvas.getWidth()/2-drawOutCircleRadius,
                    canvas.getHeight()/2-drawOutCircleRadius,
                    canvas.getWidth()/2+drawOutCircleRadius,
                    canvas.getHeight()/2+drawOutCircleRadius);
            //画弧
            mPaint.setColor(progressColor);
            canvas.drawArc(
                    arcRectF,
                    -90.0f,
                    progressSweep*1.0f,
                    true,
                    mPaint
            );
            //画遮盖弧线的圆
            mPaint.setColor(outCircleColor);
            canvas.drawCircle(canvas.getWidth() / 2,canvas.getHeight() /2,longClickOutCircleRadius - progressWidth,mPaint);
        }

        //画内圆
        mPaint.setColor(inCircleColor);
        canvas.drawCircle(canvas.getWidth() / 2,canvas.getHeight() /2,drawInCircleRadius,mPaint);

    }

    /**
     * 控制数据，重复刷新控件
     */
    private final class PoorThread extends Thread{

        private static final String TAG = ProgressButtonView.TAG+".PoorThread";
        private boolean threadSwitch = true;
        private boolean isWaiting = false;
        /**
         * 刷新间隔ms，状态不同会有变化
         */
        private int sleepTime = 13;

        PoorThread(){
            setName(TAG);
            start();
        }

        @Override
        public void run() {

            //默认死循环，控件销毁时停止
            while (threadSwitch){
                switch (mStateMachine){

                    case STATE_INITIA:
                    case STATE_SHORT_CLICK:
                    case STATE_SHORT_UP:
                        //如果是初始化、按下、短按抬起三种状态，则wait线程
                        waitPoorThread();
                        break;

                    //长按ing
                    case STATE_LONG_CLICKING:
                        //长按后，拉伸外圈，收缩内圈，如果外圈半径小于指定最大半径，或内圈半径大于指定最小半径，则修改半径数值，并刷新界面
                        if (drawOutCircleRadius < longClickOutCircleRadius || drawInCircleRadius > longClickInCircleRadius){
                            //增加外圈半径，每次更改的数值相同
                            drawOutCircleRadius += (longClickOutCircleRadius - initiaOutCircleRadius) / sleepTime;
                            //确保更改后的数值<=指定最大半径，如果大于，则赋值外圆最大半径
                            drawOutCircleRadius = drawOutCircleRadius > longClickOutCircleRadius ?  longClickOutCircleRadius : drawOutCircleRadius;
                            //减小内圈半径，每次更改的数值相同
                            drawInCircleRadius += (longClickInCircleRadius - initiaInCircleRadius) / sleepTime;
                            //确保更改后的数值>=指定最小半径，如果小于，则赋值内圈最小半径
                            drawInCircleRadius = drawInCircleRadius < longClickInCircleRadius ? longClickInCircleRadius: drawInCircleRadius;
                        }
                        //如果当前外圈半径==最大外圈半径，并且内圈半径==最小内圈半径，则说明伸缩动画结束
                        //如果弧线的角度小于360，则进度条还没加载结束，继续加载进度条
                        else if (progressSweep < 360){
                            //则增加弧线角度，确保每次更改数值相同
                            progressSweep += 360.0f / progressMaxTime * sleepTime;
                            //确保角度<=360
                            progressSweep = progressSweep>360 ? 360 : progressSweep;
                        }
                        //拉伸动画结束，弧线角度>=360，则进度条加载结束
                        else if (progressSweep >= 360){
                            //状态切换为进度条加载结束
                            mStateMachine = StateMachine.STATE_PROGRESS_OVER;
                        }

                        break;

                    //长按抬手与进度条结束两种状态的处理方式一样，收缩外圈，拉伸内圈，以动画的形式过度到初始状态
                    case STATE_PROGRESS_OVER://进度条加载结束
                    case STATE_LONG_UP://长按抬手

                        //进度条宽度为0，也就相当于不绘制
                        progressSweep = 0;

                        //抬手或进度条结束，外圆收缩，内圆拉伸，判断外圆是否大于初始值，内院是否小于初始值，如果是，则更改数据
                        if (drawOutCircleRadius > initiaOutCircleRadius || drawInCircleRadius < initiaInCircleRadius){
                            //确保外圆半径每次更改的数值相同
                            drawOutCircleRadius += (initiaOutCircleRadius - longClickOutCircleRadius) / sleepTime;
                            //确保外圆半径更改后的数值<=指定数值
                            drawOutCircleRadius = drawOutCircleRadius < initiaOutCircleRadius ?  initiaOutCircleRadius : drawOutCircleRadius;
                            //确保每次更改的数值相同
                            drawInCircleRadius += (initiaInCircleRadius - longClickInCircleRadius) / sleepTime;
                            //确保更改后的数值>=指定数值
                            drawInCircleRadius = drawInCircleRadius > initiaInCircleRadius ? initiaInCircleRadius: drawInCircleRadius;
                        }
                        //伸缩动画结束
                        else{
                            //如果接口不为空
                            if (mCallBackListener != null){
                                //如果当前状态为进度条结束
                                if (mStateMachine == StateMachine.STATE_PROGRESS_OVER){
                                    //状态为进度条结束，回调
                                    mCallBackListener.progressOver();
                                }else{
                                    //否则就是长按抬手，回调
                                    mCallBackListener.longClickUp();
                                }

                            }
                            //如果内外圆都恢复为初始大小，则将状态切换为初始状态
                            mStateMachine = StateMachine.STATE_INITIA;
                        }

                        break;

                }

//                try {
//                    sleep(sleepTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    break;
//                }

                synchronized (PoorThread.this){
                    try {
                        Log.i("PoorThread", "sleepPoorThread : " + true);
                        PoorThread.this.wait(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("PoorThread", "sleepPoorThread : " + false);
                        break;
                    }
                }

                postInvalidate();

            }


        }

        /**
         * 销毁线程
         */
        public void finishPoorThread(){
            threadSwitch = false;
            noitfyPoorThread();
        }


        /**
         * 线程等待
         */
        public void waitPoorThread(){
            if (isWaiting) return;
            synchronized (PoorThread.this){
                try {
                    isWaiting = true;
                    PoorThread.this.wait();
                } catch (InterruptedException e) {
                    isWaiting = false;
                    e.printStackTrace();
                }
            }

        }

        /**
         * 唤醒线程
         */
        public void noitfyPoorThread(){

            if (!isWaiting) return;
            synchronized (PoorThread.this){
                PoorThread.this.notify();
                isWaiting = false;
            }

        }

    }

    /**
     * 长短按的区别，在指定时间间隔后运行，如果转台还是短按，则切换为长按
     */
    private final class ClickInterValRunnable implements Runnable{

        @Override
        public void run() {
            //如果当前状态为短按
            if (mStateMachine == StateMachine.STATE_SHORT_CLICK){
                //切换为长按
                mStateMachine = StateMachine.STATE_LONG_CLICKING;
                //唤醒数据处理线程
                if (mPoorThread!= null){
                    mPoorThread.noitfyPoorThread();
                }
                //如果回调接口不为空，回调接口
                if (mCallBackListener != null){
                    mCallBackListener.longClick();
                }
            }
        }
    }

    /**
     * 状态机，描述不同的状态
     */
    private enum StateMachine{

        /**
         * 初始化状态
         */
        STATE_INITIA,
        /**
         * 短按
         */
        STATE_SHORT_CLICK,
        /**
         * 短按抬手
         */
        STATE_SHORT_UP,
        /**
         * 长按中
         */
        STATE_LONG_CLICKING,
        /**
         * 长按抬手
         */
        STATE_LONG_UP,
        /**
         * 进度条加载结束
         */
        STATE_PROGRESS_OVER,

    }


    /**
     * 事件回调接口
     */
    public interface CallBackListener{

        /**
         * 短按，抬手时回调
         */
        void shortClick();

        /**
         * 长按，判断为长按后回调
         */
        void longClick();

        /**
         * 长按抬起，与{@link #progressOver()}只有一个会回调
         */
        void longClickUp();

        /**
         * 进度条结束 ，与{@link #longClickUp()}只有一个会回调
         */
        void progressOver();

        /**
         * 手指滑动，只有在长按中滑动才会调用
         * @param event
         */
        void move(MotionEvent event);

    }
    public void setCallBackListener(CallBackListener mCallBackListener) {
        this.mCallBackListener = mCallBackListener;
    }


    @Override
    protected void onAttachedToWindow() {
        //展现
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        //销毁
        super.onDetachedFromWindow();

        mStateMachine = null;
        if (mPaint != null){
            mPaint.reset();
            mPaint = null;
        }
        if (mClickIntervalRunnable!=null){
            mClickIntervalRunnable = null;
        }
        if (mCallBackListener != null){
            mCallBackListener = null;
        }
        if (mPoorThread != null){
            mPoorThread.finishPoorThread();
        }
    }
}
