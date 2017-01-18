package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/13.
 */

public class LoadMoreView2 extends RelativeLayout implements View.OnTouchListener {

    public LoadMoreView2(Context context) {
        super(context );
        // TODO Auto-generated constructor stub
        init();
    }
    public LoadMoreView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }
    public LoadMoreView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;

    /**
     * 下拉时，增加高度，增加的量
     */
    private int addHeight;

    /**
     * 松手时，缩减高度，缩减的量
     */
    private int subHeight;

    /**
     * 核心布局
     */
    private View contentView;

    /**
     * 线程池，单一线程
     */
    private ExecutorService mExecutors;

    /**
     * 抬手时，通过线程缩减高度
     */
    private PoorThread poorThread;

    /**
     * 是否显示在屏幕中
     */
    private boolean isVisible = false;

    /**
     * 上拉时粘稠度，数值越大，则上拉越灵活
     */
    private final short PULL_VISCOSITY = 100;

    /**
     * 恢复隐藏时的粘稠度，数值越大，恢复速度越迟钝
     */
    private final short PULL_DOWN_VISCOSITY = 8;

    private final short STATUE_DOWN = 0x1;
    private final short STATUE_PULL_UP = 0x2;
    private final short STATUE_PULL_DOWN = 0x3;
    private final short STATUE_UP = 0x4;
    private short NOW_STATUE=STATUE_UP;

    private void init() {
        // TODO Auto-generated method stub
        initValue();
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        TextView textview = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_expandable_list_item_1, this,false);
        textview.setId(textview.hashCode());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textview.setLayoutParams(layoutParams);
        addView(textview,layoutParams);
    }

    private void initValue() {
        // TODO Auto-generated method stub
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenHei = manager.getDefaultDisplay().getHeight();
        screenWid = manager.getDefaultDisplay().getWidth();

        addHeight = screenHei / 130;
        subHeight = 5;

        mExecutors = Executors.newSingleThreadExecutor();
        poorThread = new PoorThread();

    }


    public LoadMoreView2 setContentView(View ContentView) {
        if (contentView == null) {
            contentView = ContentView;
        }
        return this;
    }


    /**
     * onTouch中上一次的y轴坐标
     */
    private float lastY;
    private boolean hasSelect;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("=======",((AbsListView)v).getFirstVisiblePosition()+"");
        //如果不是底部则直接返回
        if (((AbsListView)v).getLastVisiblePosition() != ((AbsListView)v).getCount()-1) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //只有up能转为down
                if (NOW_STATUE != STATUE_UP){
                    break;
                }

                NOW_STATUE = STATUE_DOWN;
                lastY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                //down转为move，或者pullUp转为pullDown，或者pullDown转为pullUp
                if (NOW_STATUE != STATUE_DOWN && NOW_STATUE != STATUE_PULL_DOWN && NOW_STATUE != STATUE_PULL_UP){
                    break;
                }

                //偏移值 =上一次坐标 - 本次坐标，向上为正，向下为负
                int moveValue = Math.round(lastY - event.getY());
                //保留本次坐标
                lastY = event.getY();

                //向上滑动
                if (moveValue>0) {
                    NOW_STATUE = STATUE_PULL_UP;


//					Log.i("=====",2+getHeight()/PULL_VISCOSITY+"");
                    //增加高度
                    addHeight(moveValue/(2+getHeight()/PULL_VISCOSITY));
                    hasSelect = false;

                //向下滑动
                }else{
                    NOW_STATUE = STATUE_PULL_DOWN;

                    //如果已经看不到
                    if (getHeight() == 0 && !hasSelect) {
                        ((AbsListView)v).setSelection(AbsListView.FOCUS_DOWN);
                        hasSelect = true;
                        break;
                    }
                    //getHeight() > 0，则缩减高度
                    subHeight(moveValue);
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                NOW_STATUE = STATUE_UP;

                moveUp();
                break;

            default:
                break;
        }

        return false;
    }

    /**
     * 上拉时，增加高度
     * @param f 手指移动的距离
     */
    private synchronized void addHeight(int f) {
//		Log.i("======", "hei:"+getHeight()+"增加_"+f);
        changeLayoutParam(getWidth(), f+getHeight());
    }

    /**
     * 下拉或松手时，缩减高度
     */
    private synchronized void subHeight(int f){
//		Log.i("======", "hei:"+getHeight()+"_减少_"+f);
        changeLayoutParam(getWidth(), getHeight() + f);
    }

    private void moveUp() {
        if (!poorThread.isRunning) {
            mExecutors.execute(poorThread);
        }
    }

    /**
     * 修改控件大小
     * @param wid
     * @param hei
     */
    private void changeLayoutParam(int wid,int hei) {
        android.view.ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.width = wid;
        layoutParams.height = hei;
        setLayoutParams(layoutParams);

    }

    private ChangeParamesThread mChangeParamesThread =  new ChangeParamesThread();
    private class ChangeParamesThread implements Runnable {
        public int wid,hei;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            subHeight(hei);
        }

    }

    private class PoorThread implements Runnable {
        public boolean isRunning = false;
        @Override
        public void run() {
            isRunning = true;
            while (isRunning) {

                if (getHeight() <= 0) {
                    break;
                }

                mChangeParamesThread.wid = getWidth();
                mChangeParamesThread.hei = -1* Math.round(getHeight()/PULL_DOWN_VISCOSITY<1?1:getHeight()/PULL_DOWN_VISCOSITY);
                if (NOW_STATUE != STATUE_UP){
                    break;
                }
                post(mChangeParamesThread);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }

            }
            isRunning = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), MeasureSpec.getSize(heightMeasureSpec)==0 ? 0 : MeasureSpec.getSize(heightMeasureSpec));
    }


}
