package com.viewsample.viewsample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.viewsample.viewsample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年6月3日 下午2:13:16
 * todo:(手势锁)
 */
public class GestureLockView extends View {

	private Context mContext;
	
	private Paint mPaint;
	
	private int screenWid;
	
	private int screenHei;
	
	private int widAndHeiSize;
	
	/**  
	* @Fields defaultPadding : TODO( 默认内边距 )  
	*/  
	private int defaultPadding;
	
	/**  
	* @Fields lineNum : TODO( 圆圈的列数， 列== 行  默认3 )  
	*/  
	private int lineNum = 3;
	
	/**  
	* @Fields numList : TODO( 存储各个图形的位置，外圆，内院，大小， 坐标 ，状态)  
	*/  
	private List<Map<String, Float>> numList;
	
	/**  
	* @Fields lineList : TODO( 连接起来的圆 )  
	*/  
	private List<Map<String, Float>> lineList;
	
	/**  
	* @Fields defColor : TODO( 默认圆圈的颜色 )  
	*/  
	private int defColor = Color.GRAY;
	
	/**  
	* @Fields defaultState : TODO( 代表状态为默认 )  
	*/  
	private float defaultState = 10000;
	
	/**  
	* @Fields choiceColor : TODO( 选中圆圈的颜色 )  
	*/  
	private int choiceColor = Color.BLUE;
	
	/**  
	* @Fields choiceState : TODO( 状态为选中 )  
	*/  
	private float choiceState = 10001;
	
	/**  
	* @Fields overColor : TODO( 选择结束后的颜色 )  
	*/  
	private int overColor = Color.RED;
	
	/**  
	* @Fields overState : TODO( 状态为选择结束 )  
	*/  
	private float overState = 10002;
	
	public GestureLockView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public GestureLockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public GestureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mContext = context;
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		screenHei = manager.getDefaultDisplay().getHeight();
		screenWid = manager.getDefaultDisplay().getWidth();
		manager = null;
		defaultPadding = (screenHei < screenWid ? screenHei : screenWid) / 30;
		
		TypedArray mArray = mContext.obtainStyledAttributes(attrs, R.styleable.View5Attr);
		
		for (int i = 0; i < mArray.getIndexCount(); i++) {
			int j = mArray.getIndex(i);
			
			switch (j) {
			case R.styleable.View5Attr_choice_color:
				choiceColor = mArray.getColor(j, Color.BLUE);
				break;

			case R.styleable.View5Attr_default_color:
				defColor = mArray.getColor(j, Color.GRAY);
				break;
				
			case R.styleable.View5Attr_line_num:
				lineNum = mArray.getInt(j, 3);
				break;
				
			case R.styleable.View5Attr_over_color:
				overColor = mArray.getColor(j, Color.RED);
				break;
				
			default:
				break;
			}
			
		}
		
		mArray.recycle();
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		numList = new ArrayList<Map<String,Float>>(lineNum*lineNum);
		lineList = new ArrayList<Map<String,Float>>(lineNum*lineNum);
		
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		
		drawCircle(canvas);
		drawLine(canvas);
		drawTriangle(canvas);
		
	}
	


	/**  
		* @TODO 计算数值
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年6月3日 下午2:54:11 *   
	 */    
	private void NumericalValue() {
		if (numList!= null && numList.size() == lineNum *lineNum) {
			return;
		}
		int rectSize = widAndHeiSize / lineNum;
		float y=0;
		for (int i = 0; i < lineNum; ++i,y+=rectSize) {
			float x=0;
			for (int j = 0; j < lineNum; ++j,x+=rectSize) {
				Map<String, Float> map = new HashMap<String, Float>();
				map.put("left", x); 
				map.put("right", x+rectSize);
				map.put("top", y);
				map.put("bottom", y+rectSize);
				map.put("circleCenterX",x+rectSize/2);
				map.put("circleCenterY", y+rectSize/2);
				map.put("outCircleRadii", rectSize/2f - defaultPadding );
				map.put("inCircleRadii", map.get("outCircleRadii") / 4 );
				map.put("inCircleRadiiLeft", map.get("circleCenterX") - map.get("inCircleRadii"));
				map.put("inCircleRadiiRight", map.get("circleCenterX") + map.get("inCircleRadii"));
				map.put("inCircleRadiiTop", map.get("circleCenterY") - map.get("inCircleRadii"));
				map.put("inCircleRadiiBottom", map.get("circleCenterY") + map.get("inCircleRadii"));
//				map.put("state", defaultState);
				numList.add(map);
//				lineList.add(map);
			}
			
		}
		
	}

	private void drawCircle(Canvas canvas) {
		// TODO Auto-generated method stub
		
		
		for (Map<String, Float> map : numList) {
			
			int painColor = defColor;
			//如果是选中状态
//			if (map.get("state") == choiceState) {
//				//并且还处在滑动过程中
//				if (isMoveing) {
//					//则颜色设置为选中状态
//					painColor = choiceColor;
//				}else {
//					//否则标识抬起手指，颜色设置为结束状态
//					painColor = overColor;
//				}
//			}
			//如果选中列表中有此对象，  则说明此对象为选中状态
			if (lineList.contains(map)) {
				//如果处于移动中
				if (isMoveing) {
					//则颜色设置为选中状态
					painColor = choiceColor;
				}else {
					//否则标识抬起手指，颜色设置为结束状态
					painColor = overColor;
				}
			}
			
			mPaint.setColor(painColor);
			mPaint.setStrokeWidth(map.get("outCircleRadii")/20);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(
					map.get("circleCenterX"),
					map.get("circleCenterY"),
					map.get("outCircleRadii"),
					mPaint);
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setStrokeWidth(1);
			canvas.drawCircle(
					map.get("circleCenterX"),
					map.get("circleCenterY"),
					map.get("inCircleRadii"),
					mPaint);
			
		}
		
	}
	
	
	
	private Path mPath = new Path();
	private Map<String, Float> moveLineMap = new HashMap<String, Float>();
	private void drawLine(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if (lineList.size()==0 ) {
			return;
		}
//		if ( lineList.size() <2) {
//			mPath.moveTo( lineList.get(0).get("circleCenterX"),  lineList.get(0).get("circleCenterY"));
//			return;
//		}
//		float[] mf = new float[lineList.size()*4-2];
		mPaint.setColor(isMoveing?choiceColor:overColor);
		mPaint.setAlpha(50);
		mPaint.setStrokeWidth(lineList.get(0).get("inCircleRadii")*2);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStyle(Paint.Style.STROKE);
	    
	    
	    for (Map<String, Float> f : lineList) {
			if (lineList.indexOf(f) == 0) {
				mPath.moveTo( f.get("circleCenterX"),  f.get("circleCenterY"));
			}else {
				mPath.lineTo(f.get("circleCenterX"), f.get("circleCenterY"));
			}
		    if (lineList.indexOf(f)+1 < lineList.size()) {

			}
		}
	    
	    if (moveLineMap.get("circleCenterX") != null && moveLineMap.get("circleCenterY") != null) {
			mPath.lineTo(moveLineMap.get("circleCenterX") , moveLineMap.get("circleCenterY"));
		}


	    canvas.drawPath(mPath, mPaint);
		
//		for (int i = 0,j=0; i < mf.length-3; i+=4,j++) {
////			if (i == 0) {
//				mf[i] = lineList.get(j).get("circleCenterX");
//				mf[i+1] = lineList.get(j).get("circleCenterY");
//				mf[i+2] = lineList.get(j+1).get("circleCenterX");
//				mf[i+3] = lineList.get(j+1).get("circleCenterY");
////			}
////			else {
////				mf[i] = lineList.get(j).get("circleCenterX");
////				mf[i+1] = lineList.get(j).get("circleCenterY");
////				mf[i+2] = lineList.get(j).get("circleCenterY");
////			}
//		}
		
//		canvas.drawLines(mf, mPaint);
		
	}

	private void drawPath(Canvas canvas, float x, float y){
		
//	    mPaint.setAntiAlias(true);                       //设置画笔为无锯齿  
//	    mPaint.setAlpha(50);
	    
	      
//	    Path path = mPath;                     //Path对象  
//	    mPath.moveTo(50, 100);                           //起始点  
	    mPath.lineTo(50, 300);                           //连线到下一点  
	    mPath.lineTo(100, 500);                      //连线到下一点  
	    mPath.lineTo(400, 500);                      //连线到下一点  
	    mPath.lineTo(300, 300);                      //连线到下一点  
	    mPath.lineTo(450, 50);                           //连线到下一点  
	    mPath.lineTo(200, 200);                      //连线到下一点  
	    canvas.drawPath(mPath, mPaint);                   //绘制任意多边形  
		
	}
	
	
	
	Path path = new Path();
	private void drawTriangle(Canvas canvas) {
		// TODO Auto-generated method stub
		if (lineList == null || lineList.size() <2) {
			return;
		}
		for (Map<String, Float> i : lineList) {
			
			if (lineList.indexOf(i) + 1 == lineList.size()) {
				return;
			}
			
			path.reset();
			path.moveTo(i.get("circleCenterX"), i.get("circleCenterY") - i.get("outCircleRadii")*0.95f);
			path.lineTo(i.get("circleCenterX") + i.get("inCircleRadii"),  i.get("circleCenterY") - i.get("outCircleRadii")+ i.get("inCircleRadii"));
			path.lineTo(i.get("circleCenterX") - i.get("inCircleRadii"),  i.get("circleCenterY") - i.get("outCircleRadii")+ i.get("inCircleRadii"));
			path.close();
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setStrokeWidth(2);
			mPaint.setStrokeCap(Paint.Cap.BUTT);
			mPaint.setStrokeJoin(Paint.Join.BEVEL);
			
	    	float dx = lineList.get(lineList.indexOf(i)+1).get("circleCenterX")- i.get("circleCenterX");  
	    	float dy = lineList.get(lineList.indexOf(i)+1).get("circleCenterY") -  i.get("circleCenterY");  
	    	// 计算角度  
	    	int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
	    	canvas.save();
			canvas.rotate(angle, i.get("circleCenterX"), i.get("circleCenterY"));
	    	
			canvas.drawPath(path, mPaint);
			canvas.restore();
			
		}
		
	}
	
	private boolean isMoveing = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		if (lineList.size()==lineNum*lineNum) {
//			return false;
//		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			lineList.clear();
			isMoveing = false;
			isChoiceOfXY(event.getX(), event.getY());
			
			return true;
			
		case MotionEvent.ACTION_MOVE:
			isMoveing = true;
			mPath.reset();
			int back;
			if (( back = isChoiceOfXY(event.getX(), event.getY())) == NOT_FOUND) {
				moveLineMap.put("circleCenterY", event.getY());
				moveLineMap.put("circleCenterX", event.getX());
			}else if (back == OUT_OF_INDEX) {
				//应该在此处判断手势密码是否正确，如果正确则跳转到下一界面，否则清空lineList
				
			}
			
			break;
			
		case MotionEvent.ACTION_UP:
			isMoveing = false;
			mPath.reset();
//			lineList.clear();
			moveLineMap.put("circleCenterY", null);
			moveLineMap.put("circleCenterX", null);
			break;
			
		default:
			
			break;
		}
		postInvalidate();
		return super.onTouchEvent(event);
	}
	
	private int OUT_OF_SIZE =  -1;
	private int OUT_OF_INDEX = -4;
	private int NOT_FOUND = -2;
	private int TRUE = -3;
	private int isChoiceOfXY(float x,float y){
		
		if (lineList.size() == lineNum * lineNum) {
			return OUT_OF_INDEX;
		}
		
		if (x<0 || x > getWidth() || y < 0 || y>getHeight()) {
			return OUT_OF_SIZE;
		}
//		int z = (int) (x / (getWidth() / lineNum));
//		
//		int c =  (int) (y/(getHeight() / lineNum));
		
		int j  = (int) ((int) (y/(getHeight() / lineNum)) * lineNum 
				+ x/(getWidth() / lineNum));
		
		Map<String, Float> hMap = numList.get(j);
		
		if (
				x >= hMap.get("inCircleRadiiLeft") &&
				x <= hMap.get("inCircleRadiiRight")&&
				y >= hMap.get("inCircleRadiiTop")&&
				y <= hMap.get("inCircleRadiiBottom") 
				) {
			
			if (lineList.contains(hMap)) {
				return NOT_FOUND;
			}
			
//			hMap.put("state", choiceState);
			lineList.add(hMap);
			return TRUE;
		}
		
//		for (Map<String, Float> i : numList) {
//			if (
//					x >= i.get("inCircleRadiiLeft") &&
//					x <= i.get("inCircleRadiiRight")&&
//					y >= i.get("inCircleRadiiTop")&&
//					y <= i.get("inCircleRadiiBottom") 
//					) {
//				
//				if (lineList.contains(i)) {
//					return NOT_FOUND;
//				}
//				
//				i.put("state", choiceState);
//				lineList.add(i);
//				return TRUE;
//			}
//		}
		
		return NOT_FOUND;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int widSize = screenWid ,heiSize = screenHei ;
		
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			widSize = MeasureSpec.getSize(widthMeasureSpec);
		}

		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
			heiSize = MeasureSpec.getSize(heightMeasureSpec);
		}
		
		widAndHeiSize = widSize > heiSize ? heiSize : widSize;
		setMeasuredDimension(widAndHeiSize , widAndHeiSize);
		NumericalValue();
	}
	

}
