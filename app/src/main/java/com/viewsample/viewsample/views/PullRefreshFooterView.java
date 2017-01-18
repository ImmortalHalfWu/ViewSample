package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * <p>1，按下，停止动画线程{@link PoorThread#stopThread()},因为手指抬起后，收缩动画需要一定时间才能结束，这段时间内如果上下拖动，会产生冲突
 * <p>2，上拉，只需要增加高度
 * <p>3，下拉，如果并不处于刷新状态，则减少高度
 * <p>4，抬手，如果并不处于刷新状态 并且 如果当前高度大于“刷新”要求的高度，
 * 则将高度设置为“刷新”要求的高度，否则将高度设置为0，不是一次性设置，而是由{@link PoorThread}分多次进行
 * <p>5，动画线程{@link PoorThread}，负责松手后，将高度设置为指定高度，分多次进行，形成收缩动画
 * <p>Created by Administrator on 2016/12/13.
 */
public class PullRefreshFooterView extends RelativeLayout implements PullRefreshHelp.FootViewChangeParamesInterface{


    public PullRefreshFooterView(Context context) {
        super(context);
        init();
    }
    public PullRefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PullRefreshFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    /**
     * 宽为屏宽，高取固定值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), MeasureSpec.getSize(heightMeasureSpec)==0 ? 0 : MeasureSpec.getSize(heightMeasureSpec));
    }


//======================================================================================
    protected PullRefreshHelp.ChangeContentViewStateInterface changeContentViewStateInterface;
    private PullRefreshHelp.LoadMoreListener loadMoreListener;
    private PoorThread mPoorThread = new PoorThread();
    private ChangeParamesThread mChangeParamesThread= new ChangeParamesThread();
    private final int HEIGHT_MIN = 1,HEIGHT_MAX = 150;
    private int STATE_DOWN = 0x1;
    private int STATE_PULL_UP = 0x2;
    private int STATE_PULL_DOWN = 0x3;
    private int STATE_UP = 0x4;
    private int STATE_REFRHSHING = 0x5;
    private int now_state;


    @Override
    public void setLoadMoreListener(PullRefreshHelp.LoadMoreListener listener) {
        if (listener == null) throw new NullPointerException("LoadMoreListener == null");
        loadMoreListener = listener;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setContentView(View contentView, PullRefreshHelp.ChangeContentViewStateInterface statuInterface) {
        if (contentView == null || statuInterface == null) throw new NullPointerException("contentView == null || statuInterface == null");

        if (contentView.getLayoutParams() == null){
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            contentView.setLayoutParams(layoutParams);
        }

        addView(contentView);
        changeContentViewStateInterface = statuInterface;

    }


//======================================================================================
    /**
     * 上拉
     * @param moveValue
     * @return
     */
    @Override
    public boolean pullUp(int moveValue) {
        //避免在poorThread线程运行中修改高度，会有冲突
        if (!mPoorThread.isRuning){
            //上拉，增加高度，无论何时都是可以增加高度的
            changeParamesHeight(moveValue/(2+getHeight()/100));
            if (changeContentViewStateInterface != null){
                changeContentViewStateInterface.pullUp(this,getChildAt(0));
            }
        }
        return false;
    }

    /**
     * 下拉
     * @param moveValue
     * @return
     */
    @Override
    public boolean pullDown(int moveValue) {
        //当前状态不处于刷新中时，才减少高度
        //避免在poorThread线程运行中修改高度，会有冲突
        if (now_state != STATE_REFRHSHING && !mPoorThread.isRuning){
            //如果当前高度修改后小于等于最小高度
            if (getLayoutParams().height + moveValue <= HEIGHT_MIN){
                //则将高度设置为最小高度
                setParamesHeight(HEIGHT_MIN);
            }else{
                //否则，修改高度
                changeParamesHeight(moveValue);
            }
            if (changeContentViewStateInterface != null){
                changeContentViewStateInterface.pullDown(this,getChildAt(0));
            }
        }

        return false;
    }

    /**
     * 手指按下
     * @return
     */
    @Override
    public boolean moveDown() {

        //因为手指抬起后，收缩动画需要一定时间才能结束，这段时间内如果上下拖动，会产生冲突
        //所以如果手指按下，则停止动画线程
        if (mPoorThread.isRuning){
            mPoorThread.stopThread();
        }

        return false;
    }

    /**
     * 手指抬起，意味着要么将当前高度移动到0，要么将当前高度移动到为“刷新”要求的高度，取决于当前高度是否大于等于“刷新”的高度要求。
     * <p>肯定的是，调用动画线程，修改高度
     * <p>
     * @return
     */
    @Override
    public boolean moveUp() {
        //如果动画线程处于闲置
        if (!mPoorThread.isRuning ){

            //如果高度满足“刷新”要求的高度
            if (getHeight() >= HEIGHT_MAX){
                //则将目标高度设置为“刷新”要求的高度
                mPoorThread.minHei = HEIGHT_MAX;
                //调用显性提示视图的状态更改
                changeContentViewStateInterface.refresh(this,getChildAt(0));
                //为了避免重复回调刷新接口，如果当前状态并不处于刷新中状态才回调
                if (now_state != STATE_REFRHSHING){
                    //回调刷新接口
                    callBackListener();
                    //将当前状态设置为刷新中
                    now_state = STATE_REFRHSHING;
                }
                //将动画线程扔进线程池
                PullRefreshHelp.mExecutors.execute(mPoorThread);
            }else{
                //高度小于“刷新”要求的高度，则重置
                reset();
            }

        }

        return false;
    }

    /**
     * 抬手后，当高度达到刷新的高度，则通过回调通知外部
     * 其实这个是为了{@link PullRefreshHeaderView}写的
     */
    protected void callBackListener(){
        if (changeContentViewStateInterface != null){
            changeContentViewStateInterface.refresh(this,getChildAt(0));
        }
        if (loadMoreListener != null){
            loadMoreListener.loadMore();
        }
    }

    /**
     * 外部或抬手后高度小于“刷新”要求的高度时调用，从“刷新中”转换为“结束，恢复为隐藏”
     */
    @Override
    public void reset() {
        //将目标高度设置为最小高度
        mPoorThread.minHei = HEIGHT_MIN;
        //如果动画线程不在运行中
        if (!mPoorThread.isRuning){
            //将动画线程扔进线程池
            PullRefreshHelp.mExecutors.execute(mPoorThread);
            //将状态设置为down状态
            now_state = STATE_DOWN;
        }

    }

//===========================下面两个方法用于修改高度===========================================================

    /**
     * 修改高度
     * @param changeValue 修改的值，正为增加，负为减少
     */
    protected synchronized void changeParamesHeight(int changeValue){
        setParamesHeight(getLayoutParams().height += changeValue);
    }

    /**
     * 指定高度
     * @param height 高度
     */
    protected synchronized void setParamesHeight(int height){
        android.view.ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }


//========================下面两个线程用于松手时，对view高度进行动态更改==============================================================
    /**
     *此线程，记录目标高度，并分多次更改，形成动画效果，
     * 当目前高度小于等于目标高度时退出，或外部调用{@link PoorThread#stopThread()}
     */
    private class PoorThread implements Runnable {
        //运行标识
        public boolean isRuning = false;
        //目标高度
        public int minHei = 0;
        @Override
        public void run() {

            //如果运行中则直接返回
            if (isRuning) return;
            //将状态设置为运行中
            isRuning = true;
            //循环开始
            while (isRuning){
                try {
                    //每次更改间隔，此值与流畅度成反比
                    Thread.sleep(15);
                    //锁
                    synchronized (PoorThread.this){
                        //如果当先高度大于目标高度，并且是运行状态
                        if (getLayoutParams().height > minHei && isRuning){
                            //计算高度便宜量并传给 mChangeParamesThread.hei
                            mChangeParamesThread.hei =-1* Math.round(getHeight()/8<1?1:getHeight()/8);
                            //传给主线程
                            post(mChangeParamesThread);
                        }else{
                            //否则，修改结束
                            break;
                        }

                    }

                }catch (InterruptedException e ) {
                    e.printStackTrace();
                    break;
                }catch (NullPointerException e){
                    e.printStackTrace();
                    break;
                }
            }
            isRuning = false;

        }
        public void stopThread(){
            isRuning = false;
        }
    }

    /**
     * 此类的出现是因为，
     * 1，高度的更改必须在主线程中进行。
     * 2，记录本次高度更改的值。
     * 3，对更改值进行判断，如果更改后小于目标高度，则直接将高度设置为目标高度，并停止{@link #mPoorThread}线程
     */
    private class ChangeParamesThread implements Runnable {
        /**
         * 每次高度更改的值，由{@link PoorThread#run()}计算后赋值
         */
        public int hei;
        @Override
        public void run() {

            //如果当前高度更改后小于目标高度
            if (getLayoutParams().height + hei < mPoorThread.minHei){
                //则直接将高度设置为目标高度
                setParamesHeight(mPoorThread.minHei);
                //停止循环线程
                mPoorThread.stopThread();
                //返回
                return;
            }
            //否则，修改高度
            changeParamesHeight(hei);

        }

    }
//======================================================================================
}
