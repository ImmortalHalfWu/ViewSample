package com.viewsample.viewsample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.viewsample.viewsample.R;

/**   
 * @TODO 简单的进度条
 * @author WuImmortalHalf
 * @date 创建时间：2016年6月2日 上午9:16:50 * 
 * @version   3.0
 */     
public class SimpleProgressBar extends View {

	private Context mContext;
	
	/**  
	* @Fields screenWidth : TODO( 屏幕宽度 )  
	*/  
	private int screenWidth;
	
	/**  
	* @Fields maxSize : TODO( 宽与高小的那一个 )  
	*/  
	private int minSize;
	
	/**  
	* @Fields screenHeight : TODO( 屏幕高度 )  
	*/  
	private int screenHeight;
	
	/**  
	* @Fields arcWidth : TODO( 弧线的宽度 )  
	*/  
	private int arcWidth;
	
	/**  
	* @Fields arcColorFirst : TODO( 第一次展现的颜色 )  
	*/  
	private int arcColorFirst;
	
	/**  
	* @Fields arcColorNext : TODO( 第二次展现的颜色 )  
	*/  
	private int arcColorNext;
	
	/**  
	* @Fields circleRadii : TODO( 这个圆的半径 )  
	*/  
	private int circleRadii;
	
	/**  
	* @Fields rotateSpeed : TODO( 旋转速度,1 - 99 )  
	*/  
	private int rotateSpeed;
	
	/**  
	* @Fields mPadding : TODO( 原始内边距为20/1 )  
	*/  
	private int mPadding;
	
	private Paint mPaint ;
	
	public SimpleProgressBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public SimpleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public SimpleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mContext = context;
		WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		screenHeight = manager.getDefaultDisplay().getHeight();
		screenWidth = manager.getDefaultDisplay().getWidth();
		minSize = screenHeight > screenWidth ? screenWidth : screenHeight;
		mPadding  = minSize / 20;
		
		if (attrs == null) {
			return;
		}
		
		TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.View3Attr);
		for (int i = 0; i < mArray.getIndexCount(); i++) {
			
			int j = mArray.getIndex(i);
			switch (j) {
			case R.styleable.View3Attr_circle_color_first:
				arcColorFirst = mArray.getColor(j, Color.argb(255, 0, 255, 179));
				break;

			case R.styleable.View3Attr_circle_color_next:
				arcColorNext = mArray.getColor(j, Color.argb(255, 247, 247, 179));
				break;
				
			case R.styleable.View3Attr_circle_rotate_speed:
				rotateSpeed = mArray.getInt(j, 50);
				break;
				
			case R.styleable.View3Attr_circle_width:
				arcWidth = mArray.getDimensionPixelSize(j, minSize / 20);
				break;
				
			case R.styleable.View3Attr_circle_radii:
				 circleRadii = mArray.getDimensionPixelSize(j, minSize/2);
				break;
				
			default:
				break;
			}
			
		}
		
		mArray.recycle();
		
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(arcWidth);
		mPaint.setAntiAlias(true);
		
		new Thread(){
			public void run() {
				int i = 1;
				while (true) {
					try {
						sleep(100 - (rotateSpeed > 99 ? 99
								: rotateSpeed < 1 ? 1 : rotateSpeed));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (arcAngle >= 360) {
						arcAngle = 0;
						i*=-1;
						drawLastColor = drawColor;
						arcAngleFirst= (arcAngleFirst+20>360 ? -90:arcAngleFirst+20);
					}
					
					if (i > 0) {
						drawColor = arcColorFirst;
					}else {
						drawColor = arcColorNext;
					}
					
					arcAngle += 2;
					
					postInvalidate();
					
				}
				
			};
		}.start();
	}
	private int drawLastColor = 0x00000000;
	private float arcLastAngle = 360;
	private int drawColor = 0x00000000;
	//开始角度
	private int arcAngleFirst = -90;
	//截止角度
	private float arcAngle = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		drawArc(canvas, drawLastColor, arcLastAngle);
		drawArc(canvas, drawColor, arcAngle);
	}
	
	/**  
		* @TODO 画一条弧线
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年6月2日 上午10:41:40 * 
	 	* @param canvas 画布
	 	* @param color 弧线的颜色
	 	* @param angle  弧线从0度到angle度
	 */    
	private void drawArc(Canvas canvas, int color, float angle){
		if (canvas == null) {
			return;
		} 
		
		mPaint.setColor(color);
		
		RectF mF = new RectF(
				mPadding + getPaddingLeft(),
				mPadding + getPaddingTop(), 
				getWidth() - mPadding - getPaddingRight(),
				getHeight() - mPadding - getPaddingBottom());
		canvas.drawArc(mF,(arcAngleFirst) , angle, false, mPaint);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		int widSize;
		int heiSize;
		int size;
		
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			widSize = MeasureSpec.getSize(widthMeasureSpec);
		}else {
			if (circleRadii == 0 ||circleRadii == minSize/2) {
				widSize = arcWidth * 15 + mPadding + getPaddingLeft() + getPaddingRight();
			}else {
				widSize = circleRadii + mPadding + getPaddingLeft() + getPaddingRight();
			}
		}
		
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
			heiSize = MeasureSpec.getSize(heightMeasureSpec);
		}else {
			if (circleRadii == 0 || circleRadii == minSize/2) {
				heiSize = arcWidth * 15 + mPadding + getPaddingTop() + getPaddingBottom();
			}
			else {
				heiSize = circleRadii+mPadding + getPaddingTop() + getPaddingBottom();
			}
		}
		size = widSize > heiSize ? heiSize : widSize;
		//宽高相等
		setMeasuredDimension(size, size);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
	}
	
}
