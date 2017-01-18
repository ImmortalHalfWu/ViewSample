package com.viewsample.viewsample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.viewsample.viewsample.R;


/**   
 * @TODO 显示一个图片，并添加边框+标题
 * @author WuImmortalHalf
 * @date 创建时间：2016年6月1日 下午2:30:49 * 
 * @version   3.0
 */     
public class View2 extends View {

	private Context mContext;
	private int screenWidth,screenHeight;
	
	
	
	
	public View2(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public View2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	/**  
	* @Fields BKcolor : TODO( 边框颜色 )  
	*/  
	private int BKcolor;
	/**  
	* @Fields image : TODO( 图片 )  
	*/  
	private Bitmap image;
	/**  
	* @Fields title : TODO( 图片说明 )  
	*/  
	private String title;
	/**  
	* @Fields titleColor : TODO( 说明颜色 )  
	*/  
	private int titleColor;
	/**  
	* @Fields titleSize : TODO( 标题字体大小 )  
	*/  
	private int titleSize;
	
	/**  
	* @Fields titleImageMargin : TODO( 图片与标题的间距 )  
	*/  
	private int titleImageMargin;
//    <declare-styleable name="View2Attr">
//    <attr name="view2_biankuangyanse" format="color"></attr>
//    <attr name="view2_tupian" format="reference"></attr>
//    <attr name="view2_biaoti" format="string"></attr>
//    <attr name="view2_biaotiyanse" format="color"></attr>
//    <attr name="view2_biaotidaxiao" format="dimension"></attr>
//		<attr name="view2_wentujianju" format="dimension"></attr>
//    </declare-styleable>
	
	public View2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		screenHeight = manager.getDefaultDisplay().getHeight();
		screenWidth = manager.getDefaultDisplay().getWidth();
		
		
		TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.View2Attr);
		
		for (int i = 0; i < mArray.getIndexCount(); i++) {
			
			int j = mArray.getIndex(i);
			
			switch (j) {
			case R.styleable.View2Attr_view2_biankuangyanse:
				BKcolor = mArray.getColor(j, Color.BLACK);
				break;
				
			case R.styleable.View2Attr_view2_biaoti:
				title = mArray.getString(j);
				break;
				
			case R.styleable.View2Attr_view2_biaotidaxiao:
				titleSize = mArray.getDimensionPixelSize(j, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
				break;
				
			case R.styleable.View2Attr_view2_biaotiyanse:
				titleColor = mArray.getColor(j, Color.BLACK);
				break;
				
			case R.styleable.View2Attr_view2_tupian:
				image = BitmapFactory.decodeResource(getResources(), mArray.getResourceId(j, 0));
				break;

			case R.styleable.View2Attr_view2_wentujianju:
				titleImageMargin = mArray.getDimensionPixelSize(j, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_PX, 50, getResources().getDisplayMetrics()));
				break;
			default:
				break;
			}
			
		}
		
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		super.onDraw(canvas);
		
		drawCanvas(canvas);
		drawFrame(canvas);
		drawBitmap(canvas);
		drawTitle(canvas);
		
	}
	
	private void drawTitle(Canvas canvas) {
		// TODO Auto-generated method stub
		
		Paint mPaint  = new Paint();
		Rect mRect = new Rect();
		mPaint.setStrokeWidth(3);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(titleSize);
		mPaint.getTextBounds(title == null ? "" : title, 0, title == null ? 0 :title.length(), mRect);
		canvas.drawText(
				title == null ? "" : title, 
				canvas.getWidth()/2-mRect.width()/2,
				canvas.getHeight() - getPaddingBottom(),
				mPaint);
		
	}
	private void drawBitmap(Canvas canvas) {
		// TODO Auto-generated method stub
		if (image == null) {
			return;
		}
		canvas.drawBitmap(
				image, 
				new Rect(0, 0, image.getWidth(),image.getHeight()),
				new RectF(
						(canvas.getWidth())/2 - image.getWidth()/2, 
						getPaddingTop(), 
						(canvas.getWidth())/2 - image.getWidth()/2 + image.getWidth(), 
						getPaddingTop() + image.getHeight()),
				new Paint());
	}
	
	private void drawFrame(Canvas canvas) {
		// TODO Auto-generated method stub
		RectF mF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
		Paint mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(BKcolor);
		mPaint.setStrokeWidth(3);
		canvas.drawRoundRect(mF, 25, 25, mPaint);
	}
	
	private void drawCanvas(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.WHITE);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode,widthSize;
		int heightMode,heightSize;
		int width,height;
		
		widthMode = MeasureSpec.getMode(widthMeasureSpec);
		widthSize = MeasureSpec.getSize(widthMeasureSpec);
		heightMode = MeasureSpec.getMode(heightMeasureSpec);
		heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		}else {
			int titleWid = 
					title == null ? 0 : title.length()
					* titleSize 
					+ getPaddingLeft()  
					+ getPaddingRight();
			int imageWid = image == null ? 0 : image.getWidth() + getPaddingLeft() + getPaddingRight();
			width = titleWid > imageWid ? titleWid : imageWid;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		}else {
			height = titleSize + getPaddingBottom() + (image == null ? 0 : image.getHeight())+getPaddingTop() + titleImageMargin;
		}
		
		if (width > height) {
			if (width > screenWidth) {
				width = screenWidth;
				height -=screenWidth-width;
			}
		}else if(width<height){
			if (height > screenHeight) {
				height = screenHeight;
				width -= screenHeight -height;
			}
		}
		
		//宽高相等
		setMeasuredDimension(
//				width < height ? width : height, 
//				width < height ? width : height
				width,height
						);
	}
	

}
