package com.viewsample.viewsample.views;


import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 *
 * Name:    FadingTextViewBuilder
 *
 * User:    WuImmortalHalf
 * Data:    2017/3/2 17:09
 *
 * Todo:    ( FadingTextView构造器，将TextView转为FadingTextView )
 *
*/
public class FadingTextViewBuilder{

    private ViewController viewController;

    public FadingTextViewBuilder(@NonNull TextView textView, @NonNull String[] sources, int timeOut)   {
        if (sources.length == 0){
            throw new NullPointerException("sources.length == 0");
        }
        viewController = new ViewController(textView,new ViewModel(sources,timeOut,textView.getCurrentTextColor()));
    }

    public ViewController builder(){
        return viewController;
    }

    public final static class ViewController extends Thread{

        private final TextView textView;
        private final ViewModel viewModel;
        private StateMachine stateMachine;
        private boolean threadSwitch = true;
        private boolean isWaiting = false;

        private final ChangTextViewRunnable runnable;

        public ViewController(@NonNull TextView textView,@NonNull ViewModel viewModel) {
            this.textView = textView;
            this.viewModel = viewModel;
            stateMachine = StateMachine.RESET;
            runnable = new ChangTextViewRunnable();
        }

        @Override
        public void run() {

            while (threadSwitch){

                switch (stateMachine){
                    case RESET:
                        // TODO: 2017/3/3 初始化，文本赋值，下标重置，状态重置，字体颜色重置
                        viewModel.showingPosition = 0;
                        viewModel.showingAlpha = viewModel.textColorAlpha;
                        stateMachine = StateMachine.SHOWING;
                        runnable.text = viewModel.sources[viewModel.showingPosition];
                        runnable.textColor = viewModel.textColor;
//                        textView.post(runnable);
                        break;
                    case SHOWING:
                        stateMachine = StateMachine.FADING;
                        break;
                    case FADING:
                        if (viewModel.showingAlpha - viewModel.offsetAlpha < viewModel.minAlpha){
                            viewModel.showingAlpha = viewModel.minAlpha;
                            runnable.textColor = viewModel.textMinAlphaColor;
                            stateMachine = StateMachine.FADING_OVER;
                        }else {
                            viewModel.showingAlpha -= viewModel.offsetAlpha;
                            runnable.textColor = changeAlpha(textView.getCurrentTextColor(),viewModel.offsetAlpha *-1);
                        }
//                        textView.post(runnable);
                        break;
                    case FADING_OVER:
                        stateMachine = StateMachine.VIGOROUS;
                        runnable.text = viewModel.sources[viewModel.showingPosition+1>viewModel.maxPostion ? viewModel.showingPosition = 0 : ++viewModel.showingPosition];
//                        textView.post(runnable);
                        break;
                    case VIGOROUS:
                        if (viewModel.showingAlpha + viewModel.offsetAlpha > viewModel.maxAlpha){
                            viewModel.showingAlpha = viewModel.maxAlpha;
                            runnable.textColor = viewModel.textMaxAlphaColor;
                            stateMachine = StateMachine.VIGOROUS_OVER;
                        }else {
                            viewModel.showingAlpha += viewModel.offsetAlpha;
                            runnable.textColor = changeAlpha(textView.getCurrentTextColor(),viewModel.offsetAlpha);
                        }
//                        textView.post(runnable);
                        break;
                    case VIGOROUS_OVER:
                        stateMachine = StateMachine.SHOWING;
                        break;
                }
                textView.post(runnable);
                try {
                    sleep(viewModel.sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

        /**
         *<p> User:    WuImmortalHalf
         *<p> Data:    2017/3/3 10:52
         *<p> Todo:    ( 将指定颜色alpha值与偏移值相加，并返回 )
        */
        private @ColorInt int changeAlpha(@ColorInt int textViewColor, int offsetAlpha){
            return Color.argb(
                Color.alpha(textViewColor) + offsetAlpha,
                    Color.red(textViewColor),
                    Color.green(textViewColor),
                    Color.blue(textViewColor)
            );
        }


        /**
         *<p> User:    WuImmortalHalf
         *<p> Data:    2017/3/2 9:54
         *<p> Todo:    ( 销毁线程 )
         */
        public void finishPoorThread(){
            threadSwitch = false;
            noitfyPoorThread();
        }


        /**
         *<p> User:    WuImmortalHalf
         *<p> Data:    2017/3/2 9:55
         *<p> Todo:    ( 等待线程 )
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
         *<p> User:    WuImmortalHalf
         *<p> Data:    2017/3/2 9:55
         *<p> Todo:    ( 唤醒线程 )
         */
        public void noitfyPoorThread(){

            if (!isWaiting) return;
            synchronized (ViewController.this){
                ViewController.this.notify();
                isWaiting = false;
            }

        }

        public void startFading(){
            noitfyPoorThread();
        }

        public void stopFading(){
            waitPoorThread();
        }

        public boolean isFading(){
            return isWaiting;
        }

        private class ChangTextViewRunnable implements Runnable{
            private int textColor;
            private String text;
            @Override
            public void run() {
                textView.setText(text);
                textView.setTextColor(textColor);
            }
        }

    }

    public final static class ViewModel{

        private final @ColorInt int textColor;
        private final @ColorInt int textMinAlphaColor;
        private final @ColorInt int textMaxAlphaColor;
        private final int textColorAlpha;

        private final int maxAlpha = Color.alpha(0xffffffff);
        private final int minAlpha = Color.alpha(0x00ffffff);

        private final String[] sources;

        private final int timeOut;
        private final int sleepTime = 33;

        private final int maxPostion;
        private int showingPosition = 0;

        private final int offsetAlpha;
        private int showingAlpha;

        public ViewModel(@NonNull String[] sources,int timeOut,@ColorInt int textColor) {
            this.sources = sources;
            this.timeOut = timeOut;
            this.textColor = textColor;
            this.maxPostion = sources.length - 1;

            textColorAlpha = Color.alpha(textColor);
            showingAlpha = textColorAlpha;

            offsetAlpha = (maxAlpha - minAlpha)/(timeOut / sleepTime);

            textMaxAlphaColor = Color.argb(
                    maxAlpha,
                    Color.red(textColor),
                    Color.green(textColor),
                    Color.blue(textColor)
            );
            textMinAlphaColor = Color.argb(
                    minAlpha,
                    Color.red(textColor),
                    Color.green(textColor),
                    Color.blue(textColor)
            );

        }

    }


    /**
     *<p> User:    WuImmortalHalf
     *<p> Data:    2017/3/2 10:01
     *<p> Todo:    ( 状态机，描述控件所有状态与当下所处状态 )
     */
    private enum StateMachine{
        /**
         * 重置以及初始化状态
         */
        RESET,
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
        VIGOROUS_OVER,
        /**
         * 显示中（介于渐入与渐出之间）
         */
        SHOWING
    }

}
