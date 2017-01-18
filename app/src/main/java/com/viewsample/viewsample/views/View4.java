package com.viewsample.viewsample.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.viewsample.viewsample.R;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年6月2日 下午12:56:49 
 * @version 3.0 
 *  @return  
 */
@SuppressLint("DrawAllocation")
public class View4 extends View implements OnTouchListener {

	private Context mContext;
	/**  
	* @Fields screenWidth : TODO( 屏幕宽度 )  
	*/  
	private int screenWidth;
	/**  
	* @Fields screenHeight : TODO( 屏幕高度 )  
	*/  
	private int screenHeight;
	
	/**  
	* @Fields minSize : TODO( 屏幕高宽取小 )  
	*/  
	private int minSize;
	
	/**  
	* @Fields mPdding : TODO( 需要的边距 )  
	*/  
	private int mPdding = 10*4;
	
	/**  
	* @Fields tagNum : TODO( 音量标识的数量,默认13 )  
	*/  
	private int tagNum= 13;
	
	/**  
	* @Fields choiceTagNum : TODO( 选中的内饰 )  
	*/  
	private int choiceTagNum = 5;
	
	/**  
	* @Fields tagColor : TODO( 标识的颜色 ,默认白色)  
	*/  
	private int tagColor = Color.WHITE;
	
	/**  
	* @Fields ResId : TODO( 中心的图标 )  
	*/  
	private int ResId;
	
	private Paint mPaint;
	
	public View4(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}
	public View4(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public View4(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mContext = context;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenHeight = manager.getDefaultDisplay().getHeight();
		screenWidth = manager.getDefaultDisplay().getWidth();
		minSize = screenHeight > screenWidth ? screenWidth : screenHeight;
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		if (attrs == null) {
			return;
		}
		
		TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.VIew4Attr);
		
		for (int i = 0; i < mArray.getIndexCount(); i++) {
			int j = mArray.getIndex(i);
			switch (j) {
			case R.styleable.VIew4Attr_tag_num:
				tagNum = mArray.getInt(j, 13);
				break;

			case R.styleable.VIew4Attr_tag_color:
				tagColor = mArray.getColor(j, Color.WHITE);
				break;
				
			case R.styleable.VIew4Attr_image_src:
				ResId = mArray.getResourceId(j, 0);
				break;
				
			default:
				break;
			}
		}
		mArray.recycle();
		
		mPaint = new Paint();
		mPaint.setColor(tagColor);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		
		setOnTouchListener(this);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		float tagWid = getWidth()/19f; //条饰的长度
		float tagHei = getHeight()/32.4f;//条饰的高度
		//条饰围绕的圆的周长，周长越大，条饰越多
		double zhouchang = 2 * Math.PI * ((getHeight() / 2) - (mPdding + getPaddingTop() + tagHei));
		//条饰的外边距基数，边距越大，条饰越少
		int tagMargin = (int) (tagWid*1.5);
		//条饰的个数，不一定，由周长与外边距决定，与周长成正比，与外边距成反比
//		int tagNum;
		//条饰分布的角度，360就是围绕一圈，180就是半圈（9点钟顺时针到3点钟），120就是1/3圈（10点顺时针到2点）
		float tagRotateAngle = 240;
		
		
		drawBackGroup(canvas);
		drawImage(canvas,(getHeight() / 2) - (mPdding + getPaddingTop() + tagHei));
		
		
		//饰条的范围大小
		RectF mF = new RectF(
				getWidth()/2-tagWid/2, 
				mPdding + getPaddingTop(), 
				getWidth()/2 + tagWid, 
				mPdding + getPaddingTop() + tagHei);
		
		//计算饰条数量
		while (tagRotateAngle / (tagNum =(int)(zhouchang /(mF.width()+(++tagMargin)))) *10 % 10 != 0);
			
		//调整画布
		canvas.rotate(tagRotateAngle/-2, getWidth()/2, getHeight()/2);
		//画饰条
		for (int i = 0; i <= tagNum; i++) {
			if (choiceTagNum == i) {
				mPaint.setStyle(Paint.Style.STROKE);
			}
			canvas.drawRoundRect(mF, tagHei/2, tagHei/2, mPaint);
			canvas.rotate((tagRotateAngle/tagNum), getWidth()/2, getHeight()/2);
		}
		mPaint.setStyle(Paint.Style.FILL);
	}
	
	
	/**  
		* @TODO 画背景
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年6月2日 下午4:41:47 * 
	 	* @param canvas  
	 */    
	private void drawBackGroup(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.argb(0, 30, 30, 30));
		mPaint.setColor(Color.argb(50, 0, 0, 0));
		canvas.drawRoundRect(
				new RectF(0, 0, getWidth(), getHeight()), getWidth()/6, getWidth()/6, mPaint);
		mPaint.setColor(tagColor);
	}
	/**  
		* @TODO 画中心的图片，画在饰品的中心
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年6月2日 下午3:58:22 * 
	 	* @param canvas 画布
	 	* @param f  外圈饰品围绕的圆的半径
	 */    
	private void drawImage(Canvas canvas, float f) {
		// TODO Auto-generated method stub
		if (ResId == 0) {
			return;
		}
		
		int canvasMinSize = getWidth() > getHeight() ? getHeight() : getWidth();
		
		canvas.drawBitmap(
				BitmapFactory.decodeResource(getResources(), ResId),
//				new Rect(0, 0, (int)f, (int)f),
				null,
				new RectF(getWidth()/2-f/2, getHeight()/2-f/2, getWidth()/2+f/2, getHeight()/2+f/2),
				mPaint);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int wSize;
		int hSize;
		
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			wSize = MeasureSpec.getSize(widthMeasureSpec);
		}else {
			wSize = minSize /2 + getPaddingLeft() + getPaddingRight() + mPdding;
		}
		
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
			hSize = MeasureSpec.getSize(heightMeasureSpec);
		}else {
			hSize = wSize /5*4 + getPaddingBottom() + getPaddingTop() + mPdding;
		}		
		
		setMeasuredDimension(wSize, hSize);
		
	}
	private float startY,downY,moveY,upY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = event.getY();
			startY = event.getY();
			return true;
			
		case MotionEvent.ACTION_MOVE:
			moveY = event.getY();
			if (moveY > getHeight() || moveY < 0) {
				return false;
			}
			up();
			break;
			
		case MotionEvent.ACTION_UP:
			upY = event.getY();
			break;

		default:
			break;
		}
		Log.i("", moveY + "");
		return false;
	}
	

	private void up() {
		// TODO Auto-generated method stub
		if (moveY < startY) {
			if (Math.abs(moveY - startY) > getHeight() / 5) {
				if (choiceTagNum <= tagNum) {
					++choiceTagNum;
					startY = moveY;
				}
			}
		}else {
			if (moveY - startY > getHeight() / 5) {
				if (choiceTagNum > 0) {
					--choiceTagNum;
					startY = moveY;
				}
			}
		}
		postInvalidate();
	}

}
