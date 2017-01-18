package com.viewsample.viewsample.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年6月7日 上午9:50:45 
 * @version 3.0 
 *  @return  
 */
public class FirstViewGroup extends ViewGroup {

	public FirstViewGroup(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}
	public FirstViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public FirstViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

		
		
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new MarginLayoutParams(getContext(), attrs);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		int widSize,heiSize,childCount = getChildCount();
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			 widSize = MeasureSpec.getSize(widthMeasureSpec);
		}else {
			
			for (int i = 0; i < childCount; i++) {
			
				View v = getChildAt(i);
				MarginLayoutParams layoutParams = (MarginLayoutParams) v.getLayoutParams();
			
				switch (i) {
				case 0:
					
					
					
					break;

				case 1:
					
					break;
					
				case 2:
					
					break;
					
				case 3:
					
					break;
					
				default:
					break;
				}
				
			}
			
		}
		
	}

}
