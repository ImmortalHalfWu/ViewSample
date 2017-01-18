package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 上拉下拉与{@link PullRefreshFooterView}相反。
 * 其他不变
 * <p>Created by Administrator on 2016/12/14.
 */

public class PullRefreshHeaderView extends PullRefreshFooterView implements PullRefreshHelp.HeaderViewChangeParamesInterface{
    public PullRefreshHeaderView(Context context) {
        super(context);
    }
    public PullRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PullRefreshHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private PullRefreshHelp.RefreshListener mRefreshListener;


    @Override
    public boolean pullDown(int moveValue) {
        return super.pullUp(moveValue*-1);
    }

    @Override
    public boolean pullUp(int moveValue) {
        return super.pullDown(moveValue*-1);
    }

    @Override
    protected void callBackListener() {
        if (mRefreshListener !=null){
            mRefreshListener.refresh();
        }
//        if (changeContentViewStateInterface != null){
//            changeContentViewStateInterface.refresh(this,getChildAt(0));
//        }
    }

    @Override
    public void setRefreshListener(PullRefreshHelp.RefreshListener listener) {
        if (listener == null) throw new NullPointerException("RefreshListener listener == null");
        mRefreshListener = listener;
    }

}
