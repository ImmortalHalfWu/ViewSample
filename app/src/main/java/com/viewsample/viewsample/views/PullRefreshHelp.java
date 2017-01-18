package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**<p>
 * <p>下拉刷新，上拉加载帮助类。
 * <p>
 * <p>手指在屏幕上上下滑动，带动顶部与底部视图隐藏--展现--更改--隐藏的显示状态的切换，
 *    显示状态的切换带动视图中核心控件对用户的显性提示的更改，可以是文字，可以只是图像。
 *    这是我所认为的PullRefresh。
 * <p>分为三个模块：
 * <p>1，逻辑层，{@link android.view.View.OnTouchListener},
 *       只负责将手指的滑动分解为按下，上拉，下拉，抬起四个状态，并传递给状态更改层
 * <p>2，显示状态更改层，{@link ChangeParamesInterface},
 *       通过不同的滑动状态，对视图进行显示状态的切换，隐藏、展现或更改，并传递给提示更改层
 * <p>3，显性提示更改层，{@link ChangeContentViewStateInterface},
 *       通过不同的显示状态，修改对用户的显性提示（上拉加载更多，松手刷新，刷新中..）
 * <p>
 * <p>思路是:
 * <p>1，{@link android.view.View.OnTouchListener}手势分发，
 * <p>2，{@link android.view.ViewGroup}实现{@link ChangeParamesInterface}改变显示状态，
 * <p>3，{@link android.view.ViewGroup} or {@link View}实现{@link ChangeContentViewStateInterface}改变提示状态
 * <p>Created by Administrator on 2016/12/13.
 */
public class PullRefreshHelp implements View.OnTouchListener {

    public static final ExecutorService mExecutors = Executors.newSingleThreadExecutor();
    private FootViewChangeParamesInterface footerViewInterface;
    private HeaderViewChangeParamesInterface headerViewInterface;

    public PullRefreshHelp(Context context, PullRefreshListener listener){
        this(listener,new PullRefreshHeaderView(context),new PullRefreshFooterView(context));
    }

    public PullRefreshHelp(PullRefreshListener listener,HeaderViewChangeParamesInterface headerView,FootViewChangeParamesInterface footView){
        if (listener == null){
            throw new NullPointerException("listener == null");
        }
        headerViewInterface = headerView;
        footerViewInterface = footView;
        headerViewInterface.setRefreshListener(listener);
        footerViewInterface.setLoadMoreListener(listener);
    }

    private float lastY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //如果不是listview，则直接返回
        if (!(v instanceof AbsListView)){
            return false;
        }

        ChangeParamesInterface paramesInterface;
        //如果是底部
        if (((AbsListView)v).getLastVisiblePosition() == ((AbsListView)v).getCount()-1) {
            Log.i("=======","底部");
            paramesInterface = footerViewInterface;
        }

        //如果是顶部
        else if (((AbsListView)v).getFirstVisiblePosition() == 0){
            Log.i("=======","顶部");
            paramesInterface = headerViewInterface;
        }

        //否则退出
        else{
            return false;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                lastY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                //偏移值 =上一次坐标 - 本次坐标，向上为正，向下为负
                int moveValue = Math.round(lastY - event.getY());
                //保留本次坐标
                lastY = event.getY();

                if (moveValue > 0){//上拉

                    return paramesInterface.pullUp(moveValue);

                }else{//下拉

                    return paramesInterface.pullDown(moveValue);

                }

            case MotionEvent.ACTION_UP:

                return paramesInterface.moveUp();

        }

        return false;

    }

    public PullRefreshHelp setHeaderContentView(View contentView, ChangeContentViewStateInterface statuInterface){
        headerViewInterface.setContentView(contentView,statuInterface);
        return this;
    }

    public PullRefreshHelp setFooterContentView(View contentView, ChangeContentViewStateInterface statuInterface){
        footerViewInterface.setContentView(contentView,statuInterface);
        return this;
    }

    public View getHeaderView(){
        return headerViewInterface.getView();
    }

    public View getFooterView(){
        return footerViewInterface.getView();
    }

    public void stopRefresh(){
        headerViewInterface.reset();
    }

    public void stopLoadMore(){ footerViewInterface.reset(); }

    /**
     *显示状态更改层通用接口
     */
    public static interface ChangeParamesInterface{
        /**
         * 手指按下时调用
         * @return 是否消费，false就可以
         */
        public boolean moveDown();

        /**
         * 获取headerView  or  footerView
         * @return 顶部或底部视图
         */
        public View getView();

        /**
         * 传入显性提示视图与显性提示视图状态更改接口
         * @param contentView 显性提示视图
         * @param statuInterface 显性提示视图状态更改接口
         */
        public void setContentView(View contentView, ChangeContentViewStateInterface statuInterface);

        /**
         * 上啦时调用
         * @param moveValue 移动的值
         * @return 是否消费
         */
        public boolean pullUp(int moveValue);

        /**
         * 下拉时调用
         * @param moveValue 移动的值
         * @return 是否消费此次触屏事件
         */
        public boolean pullDown(int moveValue);

        /**
         * 手指抬起时调用
         * @return 是否消费此次触屏事件
         */
        public boolean moveUp();

        /**
         * 重置为隐藏状态
         */
        public void reset();
    }

    /**
     * 底部视图状态显示更改层
     */
    public static interface FootViewChangeParamesInterface extends  ChangeParamesInterface{
        /**
         * 当显示状态为“展现”时（也就是加载中..），则回调外部
         * @param listener 回调接口
         */
        public void setLoadMoreListener(LoadMoreListener listener);
    }

    /**
     * 顶部视图状态显示更改层
     */
    public static interface HeaderViewChangeParamesInterface extends ChangeParamesInterface{
        /**
         * 当显示状态为“展现”时（也就是刷新中..），则回调外部
         * @param listener
         */
        public void setRefreshListener(RefreshListener listener);
    }

    /**
     * 显性提示更改层，这一层由显示状态更改层调用
     */
    public static interface ChangeContentViewStateInterface{

        /**
         * 上拉中，修改对用户的提示
         * @param contentViewParent 显示状态更改层的视图
         * @param cententView 显性提示更改层的视图
         */
        public void pullUp(View contentViewParent, View cententView);

        /**
         * 下拉中，修改对用户的提示
         * @param contentViewParent 显示状态更改层的视图
         * @param cententView 显性提示更改层的视图
         */
        public void pullDown(View contentViewParent, View cententView);

        /**
         * 当显示状态更改层处于“刷新”状态时调用，修改对用户的提示
         * @param contentViewParent 显示状态更改层的视图
         * @param cententView 显性提示更改层的视图
         */
        public void refresh(View contentViewParent, View cententView);
    }

    /**
     * 外部回调接口，加载更多
     */
    public static interface LoadMoreListener{
        public void loadMore();
    }

    /**
     * 外部回调接口，刷新
     */
    public static interface RefreshListener{
        public void refresh();
    }

    /**
     * 外部回调接口，刷新加载状态回调
     */
    public static interface PullRefreshListener extends LoadMoreListener,RefreshListener{}

}