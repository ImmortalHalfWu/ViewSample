package com.viewsample.viewsample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.R.styleable;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年5月30日 下午2:28:00 
 * @version 3.0 
 *  @return  
 */
public class View1 extends View {

	private Context mContext;
	private Paint mPaint;
	private int canvesBackgroudColor;
	private int paintColor;
	
	public View1(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public View1(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public View1(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub.
		
		mContext = context;
		
		TypedArray mArray = context.obtainStyledAttributes(attrs, R.styleable.View1Attr);
		for (int i = 0; i < mArray.getIndexCount() ; i++) {
			int attr = mArray.getIndex(i);
			switch (attr) {
			//铅笔颜色
			case R.styleable.View1Attr_view_paint_colcr:
				paintColor = mArray.getColor(attr, Color.WHITE);
				break;
			//背景颜色，也就是画布颜色
			case R.styleable.View1Attr_view_backgroud:
				canvesBackgroudColor = mArray.getColor(attr, Color.argb(1000, 65, 184, 217));
				break;
				
			default:
				break;
			}
		}
		
		mArray.recycle();
		String[] mStrings =  new SimpleDateFormat("HH:mm:ss").format(new Date()).split(":");
		sAngle = Float.parseFloat(mStrings[2])*6;
		mAngle = Float.parseFloat(mStrings[1])*6 +  (Float.parseFloat(mStrings[2]) >= 10? Float.parseFloat(mStrings[2])/10*0.3f : 0);
		hAngle = Float.parseFloat(mStrings[0])*30 + (mAngle >= 12? mAngle/12 : 0);
//		hAngle = Float.parseFloat(mStrings[0])*30 + (Float.parseFloat(mStrings[1]) >= 12? Float.parseFloat(mStrings[1])/12*0.0053f : 0);
		
		mPaint  = new Paint();
		mPaint.setColor(paintColor);
		mPaint.setStrokeWidth(3);
		
		new Thread(){
			
			public void run() {
				
				while (true) {
					
					//sAngle<360 时，每次+0.3度，否则回到初始0.3度
					sAngle = sAngle==360 ? 0.3f : sAngle+0.3f;
					//分针旋转角度为秒针除以60取整，也就是1,2,3,4,5,6六种可能
					mAngle = (sAngle >= 60 && (int)sAngle%60 == 0? mAngle+0.3f : mAngle);
					
					hAngle =  (mAngle >= 12 && (int)mAngle%12 == 0? hAngle+0.00530f : hAngle);
					if (mAngle == 360) {
						mAngle = 0;
					}
					
					if (hAngle ==360) {
						
					}
					
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					postInvalidate();
					
				}
				
			};
			
		}.start();
		
	}
	
	/**  
	* @Fields sAngle : TODO( 秒针旋转的角度 = 次数 * 6 ，最大360)  
	*/  
	private float sAngle ;
	
	/**  
	* @Fields mAngle : TODO( 分针旋转角度 )  
	*/  
	private float mAngle = 0;
	
	/**  
	* @Fields hAngle : TODO( 时针旋转角度 )  
	*/  
	private float hAngle = 0;
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		mPaint.setStyle(Paint.Style.STROKE);
		//画布景色
		canvas.drawColor(canvesBackgroudColor);
//		canvas.drawCircle(manager.getDefaultDisplay().getWidth()/2, manager.getDefaultDisplay().getHeight()/2, 100, mPaint);
		//先把表盘画出来
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3, mPaint);
		
		//画刻度表，共60个，当刻度为5或5的倍数，为整点，并画上数字
		for (int i = 1; i < 61; i++) {
			
			canvas.rotate(6,canvas.getWidth()/2, canvas.getHeight()/2);
			int j = 10;
			//当刻度为5或5的倍数，为整点
			if (i%5 == 0) {
				j = 18;
				mPaint.setStyle(Paint.Style.FILL);
				mPaint.setStrokeWidth(2);
				mPaint.setTextSize((canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/28);
				canvas.drawText(
						i/5 + "",
						canvas.getWidth()/2 - 10, 
						canvas.getHeight()/2 - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3 - 30, 
						mPaint
						);
				mPaint.setStrokeWidth(3);
				mPaint.setStyle(Paint.Style.STROKE);
			}
			
			canvas.drawLine(
					canvas.getWidth()/2, 
					canvas.getHeight()/2 - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3,
					canvas.getWidth()/2, 
					canvas.getHeight()/2 - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3 -j,
					mPaint
					);
			
		}
		
		
		
		//画分针时先旋转角度
		canvas.rotate(mAngle, canvas.getWidth()/2, canvas.getHeight()/2);
		//分针
		canvas.drawLine(
				canvas.getWidth()/2, 
				canvas.getHeight()/2, 
				canvas.getWidth()/2  , 
				canvas.getHeight()/2 - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3 + 20, 
				mPaint);
		canvas.rotate(mAngle*-1, canvas.getWidth()/2, canvas.getHeight()/2);
		//画时针时先旋转角度
		canvas.rotate(hAngle, canvas.getWidth()/2, canvas.getHeight()/2);
		//时针
		canvas.drawLine(
				canvas.getWidth()/2, 
				canvas.getHeight()/2, 
				canvas.getWidth()/2 , 
				canvas.getHeight()/2  - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/4, 
				mPaint);
		canvas.rotate(hAngle*-1, canvas.getWidth()/2, canvas.getHeight()/2);
		
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/90, mPaint);
		//旋转画布，读数为1至60 * 6
		canvas.rotate(sAngle,canvas.getWidth()/2, canvas.getHeight()/2);
		//秒针
		canvas.drawCircle(
				canvas.getWidth()/2  , 
				canvas.getHeight()/2 - (canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/3 + 10, 
				(canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight())/90, 
				mPaint);
	}


	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int width = manager.getDefaultDisplay().getWidth(),height = manager.getDefaultDisplay().getHeight();
		
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		}
		
		if (widthMode == MeasureSpec.AT_MOST) {
			width = widthSize > heightSize ? heightSize : widthSize;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		}
		
		if (heightMode == MeasureSpec.AT_MOST) {
			height = width = widthSize > heightSize ? heightSize : widthSize;
		}
		
		setMeasuredDimension(width, height);
		
	}
	
	
}
