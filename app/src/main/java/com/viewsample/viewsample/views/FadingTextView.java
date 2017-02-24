package com.viewsample.viewsample.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import static android.graphics.Color.blue;

/**   
 * 
 * Name:    FadingTextView
 * 
 * User:    WuImmortalHalf
 * Data:    2017/2/24 16:02
 *
 * Todo:    ( 渐入渐出更替显示文本 )
 * 
*/ 
public class FadingTextView extends TextView {

    public FadingTextView(Context context) {
        super(context);
        init();
    }

    public FadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setAlpha();
//        getCurrentTextColor()
//        Color color = new Color();
//        int textcolor = getCurrentTextColor();
//        int blue = Color.blue(textcolor);
//        ColorDrawable c = new ColorDrawable(getCurrentTextColor());
    }


    private enum StateMachine{
        /**
         * 渐出
         */
        FADING,
        /**
         * 渐出结束
         */
        FADING_OVER,
        /**
         * 渐入
         */
        VIGOROUS,
        /**
         * 渐入结束
         */
        VIGOROUS_OVER
    }

    private final static class ViewModel{
        private int nowAlpha;
        private int nowTextColor;
        private int isLoop;
    }

    private final static class ViewController extends Thread{

        private static final String TAG = "FadingTextView:"+"ViewController";

        private final TextView textView;
        private boolean threadSwitch = true;
        private boolean isWaiting = false;

        public ViewController(@NonNull TextView textView) {
            setName(TAG);
            this.textView = textView;
        }

        @Override
        public void run() {
            while (threadSwitch){

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
            synchronized (ViewController.this){
                try {
                    isWaiting = true;
                    ViewController.this.wait();
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
            synchronized (ViewController.this){
                ViewController.this.notify();
                isWaiting = false;
            }

        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
}
