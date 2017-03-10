package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.viewsample.viewsample.R;


public class WeChatVideoSurfaceView extends SurfaceView {

    private PopupWindow mPopupWindow;
    private View popupWindowView;

    public WeChatVideoSurfaceView(Context context) {
        super(context);
    }
    public WeChatVideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public WeChatVideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }

            popupWindowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popuowindow,null);
            mPopupWindow =new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,false);
            mPopupWindow.setFocusable(false);
            popupWindowView.measure(0,0);
            FocusingView v = (FocusingView) popupWindowView.findViewById(R.id.focusingview);
            v.setCallBack(new FocusingViewCallBack(mPopupWindow));
            mPopupWindow.setTouchable(false);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAtLocation(popupWindowView, Gravity.NO_GRAVITY,Math.round(event.getX()-popupWindowView.getMeasuredWidth()/2.0f),Math.round(event.getY()-popupWindowView.getMeasuredHeight()/2.5f));
//            mPopupWindow.showAtLocation(popupWindowView, Gravity.NO_GRAVITY,Math.round(event.getX()-popupWindowView.getMeasuredWidth()/2.0f),Math.round(event.getY()-popupWindowView.getMeasuredHeight()/2.0f));

        }

        return super.onTouchEvent(event);
    }

    public static class FocusingViewCallBack implements FocusingView.CallBack{

        PopupWindow mPopupWindow;

        public FocusingViewCallBack(PopupWindow popupWindow){
            mPopupWindow = popupWindow;
        }

        @Override
        public void over(FocusingView v) {
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.setContentView(null);
                mPopupWindow.dismiss();
            }
        }
    }

}
