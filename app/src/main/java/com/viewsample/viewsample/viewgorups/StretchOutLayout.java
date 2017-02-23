package com.viewsample.viewsample.viewgorups;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 *
 * Name:    StretchOutLayout
 *
 * User:    WuImmortalHalf
 * Data:    2017/1/23 9:18
 *
 * Todo:    ( 新版微信大视频，录制结束或拍照后，动态展开两个按钮，首先两个按钮重叠，之后两个按钮以动画形式像两边展开 )
 * <p></p>
 * <P>我想要一个布局有这样的效果：</P>
 * <p>1,它是个容器，宽高可以自定义。</p>
 * <p>2,像linearLayout一样线性摆放控件，并且可以指定垂直或水平。</p>
 * <p>3,</p>
 * <p></p>
 * <p></p>
 * <p></p>
 * <p></p>
*/
public class StretchOutLayout extends LinearLayout{

    public StretchOutLayout(Context context) {
        super(context);
    }
    public StretchOutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public StretchOutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
        View childView = getChildAt(0);
    }
}