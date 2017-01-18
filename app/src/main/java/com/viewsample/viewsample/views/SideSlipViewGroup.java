package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**   
 * @TODO 侧滑容器，最多容纳左中右三个布局，至少中间一个。</br>
 * 					继承自HorizontalScrollView，外部穿入的三个布局被包裹在linearLayout中。</br>
 * 					然后得出三个布局的宽度并保留，依照mid布局的宽度，调整HorizontalScrollView滚动条的初始位置,</br>
 * .				{@link SideSlipViewGroupBuilder#onTouchUp(int, int)}}实现滚动监听，只判断左右滑动，归类为：</br>
 * 					向左滑动：</br>
 * 					1,left to mid(跳到mid)</br> 
 * 					2,mid to right（调到right）</br>
 * 					向右滑动：</br>
 * 					1,right to mid(跳到mid)</br>
 * 					2,mid to left（跳到left）</br></br>
 * 					最后分为三类</br>
 * 					1，跳到mid，跳到left的宽度，如果left为null，则跳到0</br>
 * 					2，跳到left，跳到0</br> 
 * 					3，调到right，跳到right宽度 + left宽度</br></br>
 * 					跳到XXXX的条件(n为基准)：</br>
 * 					1，如果向右滑动，并且当前坐标小于left的宽度-left宽度的n/1，跳到left</br>
 * 					2，否则如果向左滑动，并且当前坐标大于left+right宽度的n/1，调到right</br>
 * 					3，否则，跳到mid</br></br>
 * 
 * 					可以通过{@link SideSlipViewGroup #getShowingView()}获取现在的显示状态</br></br>
 * 			
 * 					=======接口部分未开放，   以后再说=====</br>
 * 					接口{@link SideSlipStatuListener}，监听leftView 与 rightView打开，不监听关闭。</br>
 * 					通过{@link SideSlipViewGroupBuilder#setSideSlipStatuListener(SideSlipStatuListener)}方法配置。
 * 					
 * @author WuImmortalHalf
 * @date 创建时间：2016年8月8日 下午2:32:14 
 * @version   3.0
 */     
public class SideSlipViewGroup extends HorizontalScrollView {

	public SideSlipViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	public SideSlipViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}
	public SideSlipViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	/**  
	* @Fields modView : TODO( 分为左中右三个模块，中是必须的 )  
	*/  
//	private View leftView,modView,rightView;
	
	/**  
	* @Fields mBuilder : TODO( 构造器 )  
	*/  
	private static SideSlipViewGroupBuilder mBuilder;
	
	/**  
	* @Fields mScrollChangedListener : TODO( 滑动监听 )  
	*/  
	private ScrollChangedListener mScrollChangedListener;
	
	
	private void initView() {
		// TODO Auto-generated method stub
		setHorizontalScrollBarEnabled(false);
	}
	
	/**  
		* @TODO 配置滑动监听，私有的
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年8月8日 下午4:15:11 * 
	 	* @param mListener  
	 */    
	private SideSlipViewGroup setScrollChangesListener(ScrollChangedListener mListener){
		if (mListener != null) {
			mScrollChangedListener = mListener;
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.HorizontalScrollView#fling(int)
	 * 调整滑动速度
	 */
	@Override
	public void fling(int velocityX) {
		// TODO Auto-generated method stub
//		super.fling(velocityX>10 ? 10 : velocityX);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//抬起时才回调
		if (ev.getAction()== MotionEvent.ACTION_UP) {
			try {
				if (mScrollChangedListener !=null) {
					mScrollChangedListener.onTouchUp(newL, oldL);
				}
			} catch (Exception e) {
				// TODO: handle exception
				mScrollChangedListener=null;
				return super.onTouchEvent(ev);
			}
		}
		
		return super.onTouchEvent(ev);
	}
	
	/**  
	* @Fields newL : TODO( 最近一次滚动距离数据 )  
	*/  
	private int newL,oldL;
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		newL = l;oldL = oldl;
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	
	/**  
		* @TODO 获取正在显示的view标识
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年8月9日 下午3:41:08 * 
	 	* @return  左边正在显示==SideSlipViewGroupBuilder.SHOWING_LEFT，
	 	* 					右边正在显示==SideSlipViewGroupBuilder.SHOWING_RIGHT，
	 	* 					左右都没有滑出==SideSlipViewGroupBuilder.SHOWING_MID，
	 */    
	public int getShowingView() {
		return mBuilder.getShowingView();
	}
	
	
	/**  
		* @TODO 返回到原始状态
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年8月9日 上午10:13:11 *   
	 */    
	public void goBackMid(){
		if (mBuilder!=null) {
			mBuilder.goBackMod(false);
		}
	}
	
	/**  
		* @TODO 滑开左边布局
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年8月9日 上午10:35:16 *   
	 */    
	public void goBackLeft() {
		if (mBuilder!=null) {
			mBuilder.goBackLeft(false);
		}
	}
	
	/**  
		* @TODO 滑开右边布局
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年8月9日 上午10:35:34 *   
	 */    
	public void goBackRight() {
		if (mBuilder!=null) {
			mBuilder.goBackRight(false);
		}
	}
	
	
	
	/**   
	 * @TODO Builder模式
	 * @author WuImmortalHalf
	 * @date 创建时间：2016年8月8日 下午2:44:05 * 
	 * @version   3.0
	 */     
	public static class SideSlipViewGroupBuilder implements ScrollChangedListener{
		
		/**  
		* @Fields lastBuilder : TODO( 在整个界面中，只有一个侧滑容器可以处于滑开状态，此对象保存这个侧滑容器 )  
		*/  
		public static SideSlipViewGroupBuilder lastBuilder;
		
		/**  
		* @Fields MODE_ONLE_MOD : TODO( 只有中 )  
		*/  
		private final int MODE_ONLY_MOD = 0x8520;
		/**  
		* @Fields MODE_LEFT_MOD : TODO( 左中 )  
		*/  
		private final int MODE_LEFT_MOD = 0x8521;
		/**  
		* @Fields MODE_RIGHT_MOD : TODO( 右中 )  
		*/  
		private final int MODE_RIGHT_MOD = 0x8523;
		/**  
		* @Fields MODE_LEFT_RIGHT_MOD : TODO( 全有 )  
		*/  
		private final int MODE_LEFT_RIGHT_MOD = 0x8524;
		
		/**  
		* @Fields choiceMod : TODO( 选中的模式,决定scrollView初始位置 )  
		*/  
		private int choiceMod = MODE_ONLY_MOD;
		
		/**  
		* @Fields SHOWING_LEFT : TODO( leftView显示中 )  
		*/  
		public static final int SHOWING_LEFT = 0x8525;
		
		/**  
		* @Fields SHOWING_RIGHT : TODO( rightView显示中 )  
		*/  
		public static final int SHOWING_RIGHT = 0x8526;
		
		/**  
		* @Fields SHOWING_MID : TODO( midView显示中 )  
		*/  
		public static final int SHOWING_MID = 0x8527;
		
		/**  
		* @Fields showingView : TODO( 正在显示的view，默认mid )  
		*/  
		private int showingView = SHOWING_MID;
		
		/**  
		* @Fields mSlipViewGroup : TODO( SideSlipViewGroup实例 )  
		*/  
		SideSlipViewGroup mSlipViewGroup;
		
		/**  
		* @Fields rightViewBuilder : TODO( 对应左中右三个布局 )  
		*/  
		private View leftViewBuilder,modViewBuilder,rightViewBuilder;
		
		
		
		/**  
		* @Fields maxWidth : TODO( 屏幕宽 )  
		*/  
		private int maxWidth;
		
		public SideSlipViewGroupBuilder(View left, View mod, View right){
			if (mod == null) {
				throw new NullPointerException("mod == null");
			}
			if (left != null) {
				leftViewBuilder = left;
				choiceMod = MODE_LEFT_MOD;
			}
			if (right != null) {
				rightViewBuilder=right;
				choiceMod = choiceMod==MODE_LEFT_MOD?MODE_LEFT_RIGHT_MOD : MODE_RIGHT_MOD;
			}
			modViewBuilder=mod;
		}

		public SideSlipViewGroupBuilder(View mod){
			this(null, mod, null);
		}
		
		/**  
			* @TODO 配置modView
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午3:32:01 * 
		 	* @param mod
		 	* @return  modView
		 */    
		private View createModView(View mod){
			mod.setLayoutParams(createLayoutParmes(maxWidth, LayoutParams.MATCH_PARENT));
			return mod;
		}
		/**  
			* @TODO 配置leftView
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午3:32:28 * 
		 	* @param left
		 	* @return  leftView
		 */    
		private View createLeftView(View left){
			left.setLayoutParams(createLayoutParmes(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			return left;
		}
		/**  
			* @TODO 配置rightView
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午3:32:38 * 
		 	* @param right
		 	* @return  rightView
		 */    
		private View createRightView(View right){
			right.setLayoutParams(createLayoutParmes(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			return right;
		}
		
		/**  
			* @TODO 创建一个layoutParams
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午3:38:37 * 
		 	* @param w 宽
		 	* @param h 高
		 	* @return  layoutParams
		 */    
		private android.view.ViewGroup.LayoutParams createLayoutParmes(int w,int h){
			return new android.view.ViewGroup.LayoutParams(w, h);
		}

		/**  
			* @TODO 初始左中右三个布局
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午3:30:54 * 
		 	* @param slipViewGroup
		 	* @return  SideSlipViewGroup实例
		 */    
		private SideSlipViewGroup configView(SideSlipViewGroup slipViewGroup){
			//不可为空
			if (slipViewGroup == null || modViewBuilder == null) {
				throw new NullPointerException("slipViewGroup == null || modViewBuilder == null");
			}
			//父布局为linearLayout，宽高自适应，水平排放，垂直居中
			LinearLayout mLinearLayout = new LinearLayout(slipViewGroup.getContext());
			mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
			mLinearLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			//再根据传入的view，进行不同的布局
			switch (choiceMod) {
			case MODE_ONLY_MOD:
				mLinearLayout.addView(createModView(modViewBuilder));
				break;
			case MODE_LEFT_MOD:
				mLinearLayout.addView(createLeftView(leftViewBuilder));
				mLinearLayout.addView(createModView(modViewBuilder));
				break;
			case MODE_RIGHT_MOD:
				mLinearLayout.addView(createModView(modViewBuilder));
				mLinearLayout.addView(createRightView(rightViewBuilder));
				break;
			case MODE_LEFT_RIGHT_MOD:
				mLinearLayout.addView(createLeftView(leftViewBuilder));
				mLinearLayout.addView(createModView(modViewBuilder));
				mLinearLayout.addView(createRightView(rightViewBuilder));
				break;

			default:
				break;
			}

			//计算组件宽高
//	        mLinearLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
	        slipViewGroup.addView(mLinearLayout);

	        mSlipViewGroup = slipViewGroup;
	        slipViewGroup = null;
			return mSlipViewGroup;
		}

		/**
			* @TODO 计算所有组件宽高，并调整初始位置
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午9:43:29 *
		 	* @param configListener
		 	* @return
		 */
		private SideSlipViewGroup measureW_H(SideSlipViewGroup SideSlipViewGroups) {
			// TODO Auto-generated method stub

			SideSlipViewGroups.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

			leftWid = leftViewBuilder == null ? 0 : leftViewBuilder.getMeasuredWidth();
	        rightWid = rightViewBuilder == null ? 0 : rightViewBuilder.getMeasuredWidth();
	        goBackMod(false);
			
			return SideSlipViewGroups;
		}
		

		/**  
			* @TODO 退回到mod，并更改标识
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午10:05:15 * 
		 	* @param hasAnimato   是否需要过度动画
		 */    
		public void goBackMod(final boolean hasAnimato){
			goBack(leftWid, hasAnimato);
			showingView = SHOWING_MID;
		}
		

		/**  
			* @TODO 滑开左边布局，并更改标识
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午10:37:04 * 
		 	* @param b  是否过度动画
		 */    
		public void goBackLeft(boolean hasAnimato) {
			// TODO Auto-generated method stub
			goBack(0, hasAnimato);
			showingView = SHOWING_LEFT;
		}
		

		/**  
			* @TODO 滑开右边布局，并更改标识
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午10:41:18 * 
		 	* @param b   是否过度动画
		 */    
		public void goBackRight(boolean b) {
			// TODO Auto-generated method stub
			goBack( leftWid + rightWid, b);
			showingView = SHOWING_RIGHT;
		}
		
		
		private Runnable goBackRunnable;
		private boolean hasAnimator;
		/**  
			* @TODO 移动到x轴指定坐标
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午10:38:09 * 
		 	* @param moveNums x轴指定坐标
		 	* @param hasAnimato  是否过渡动画
		 */    
		public void goBack(int moveNums,boolean hasAnimato){
			if (mSlipViewGroup == null) {
				return;
			}
			hasAnimator = hasAnimato;
			moveNum = moveNums;
			if (goBackRunnable == null) {
				goBackRunnable = new Runnable() {
					
					@Override
					public void run() {
						if (hasAnimator) {
							mSlipViewGroup.smoothScrollTo(moveNum, 0);
						}else {
							mSlipViewGroup.scrollTo(moveNum, 0);
						}
					}
				};
			}
			mSlipViewGroup.post(goBackRunnable);
		}
		/**  
			* @TODO 监听滑动事件
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午4:19:36 * 
		 	* @param mSideSlipViewGroup
		 	* @return  
		 */    
		private SideSlipViewGroup configListener(SideSlipViewGroup mSideSlipViewGroup){
			return mSideSlipViewGroup.setScrollChangesListener(this);
		}
		
		

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {		}
		
		private int leftWid=0,rightWid=0;
		private int moveNum = 0;//偏移量
//		private Runnable mRunnable;//smoothScrollTo偏移
					//=====================================
		@Override
		public void onTouchUp(int l, int oldl) {
			Log.i("side","l_" + l +"__" + "oldl_" + oldl);
			// TODO Auto-generated method stub
			//如果只有一个
			if (choiceMod == MODE_ONLY_MOD) {
				return;
			}
			
			if (l<=leftWid-leftWid/7 && l < oldl) {//左边滑出
				goBackLeft(true);
				
				//接口回调
//				mSideSlipStatuListener.leftViewStatu(mSlipViewGroup, leftViewBuilder);
			}else if (l>=leftWid+rightWid/7 && l > oldl) {//右边滑出
				goBackRight(true);
				
				//接口回调
//				mSideSlipStatuListener.rightViewStatu(mSlipViewGroup, rightViewBuilder);
			}else {//回到中间
				goBackMod(true);
				
				//接口回调
//				mSideSlipStatuListener.midViewStatu(mSlipViewGroup, modViewBuilder);
			}
			
			//如果有打开的，并不是自己，则归位，
			if (lastBuilder != null && lastBuilder != SideSlipViewGroupBuilder.this) {
				lastBuilder.goBackMod(true);
			}
			//并保存自己，则其他侧滑打开时，关闭自己
			lastBuilder = SideSlipViewGroupBuilder.this;
		}
		
		
		/**  
			* @TODO 返回现在滑开的View，返回SideSlipViewGroupBuilder.SHOWING_MID则说明没有滑开
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 下午3:44:41 * 
		 	* @return  leftView==SideSlipViewGroupBuilder.SHOWING_LEFT，
	 	* 					rightView==SideSlipViewGroupBuilder.SHOWING_RIGHT，
	 	* 					midView==SideSlipViewGroupBuilder.SHOWING_MID
		 */    
		public int getShowingView(){
			return showingView;
		}
		
		
		/**  
			* @TODO 生成SideSlipViewGroup对象
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 上午9:45:59 * 
		 	* @param mContext
		 	* @return  SideSlipViewGroup对象
		 */    
		public SideSlipViewGroup builder(Context mContext){
			
			mBuilder = this;
			
			maxWidth = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

			return  measureW_H(configListener(configView(new SideSlipViewGroup(mContext))));
		}
		
		
		//==================以下为接口部分，没写完=============================================
		
		/**  
		* @Fields mSideSlipStatuListener : TODO( 外部出入的滑动状态监听，默认实现 )  
		*/  
		private SideSlipStatuListener mSideSlipStatuListener = new SideSlipStatuListener() {
			
			@Override
			public void rightViewStatu(SideSlipViewGroup sideSlipViewGroup,View rightView) {}
			@Override
			public void midViewStatu(SideSlipViewGroup sideSlipViewGroup, View midView) {}
			@Override
			public void leftViewStatu(SideSlipViewGroup sideSlipViewGroup, View leftView) {}
		};
		/**  
			* @TODO 设置SideSlipStatuListener
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 下午1:43:36 * 
		 	* @param sideSlipStatuListener  
		 */    
		public SideSlipViewGroupBuilder setSideSlipStatuListener(SideSlipStatuListener sideSlipStatuListener) {
			if (sideSlipStatuListener != null) {
				mSideSlipStatuListener = sideSlipStatuListener;
			}
			return SideSlipViewGroupBuilder.this;
		}
		
	}
	
	
	/**   
	 * @TODO ScrollChanged监听事件
	 * @author WuImmortalHalf
	 * @date 创建时间：2016年8月8日 下午4:11:44 * 
	 * @version   3.0
	 */     
	public interface ScrollChangedListener{
		/**  
			* @TODO 滑动监听 ，暂无用
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午4:17:13 * 
		 	* @param l
		 	* @param t
		 	* @param oldl
		 	* @param oldt  
		 */    
		public void onScrollChanged(int l, int t, int oldl, int oldt);
		/**  
			* @TODO 前面错了，应该抬起手指时才判断，与onScrollChanged无关
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月8日 下午5:25:24 * 
		 	* @param l 滑动下标
		 	* @param oldl   上一个滑动下标
		 */    
		public void onTouchUp(int l, int oldl);
	}
	
	
	/**   
	 * @TODO 只监听哪个打开了
	 * @author WuImmortalHalf
	 * @date 创建时间：2016年8月9日 下午1:36:20 * 
	 * @version   3.0
	 */     
	public interface SideSlipStatuListener{
		
		/**  
			* @TODO 滑开左边布局
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 下午1:38:11 * 
			* @param sideSlipViewGroup SideSlipViewGroup对象
		 	* @param leftView 左边布局对象
		 	* @param hasShow   true滑开，false关闭
		 */    
		public void leftViewStatu(SideSlipViewGroup sideSlipViewGroup, View leftView);
		/**  
			* @TODO 滑开右边布局
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年8月9日 下午1:39:06 * 
			* @param sideSlipViewGroup SideSlipViewGroup对象
		 	* @param rightView 右边布局对象
		 	* @param hasShow  true滑开，false关闭
		 */    
		public void rightViewStatu(SideSlipViewGroup sideSlipViewGroup, View rightView);
		/**  
		 * @TODO 回到中间布局
		 * @author WuImmortalHalf
		 * @date 创建时间：2016年8月9日 下午1:39:06 * 
		 * @param sideSlipViewGroup SideSlipViewGroup对象
		 * @param rightView 右边布局对象
		 */    
		public void midViewStatu(SideSlipViewGroup sideSlipViewGroup, View midView);
		
	}
//	
//	/**   
//	 * @TODO 状态监听的内部实现类，外部传入的监听器会传入此类，装饰者模式.</br>
//	 * 					我需要知道当先显示的是哪个布局。</br>
//	 * 					比如从left to mid，则需要告诉外部，left隐藏了，
//	 * @author WuImmortalHalf
//	 * @date 创建时间：2016年8月9日 下午2:08:29 * 
//	 * @version   3.0
//	 */     
//	private class InSideSlipStatuListener implements SideSlipStatuListener{
//
//		/**  
//		* @Fields outListener : TODO( 外部传入的监听  )  
//		*/  
//		private SideSlipStatuListener outListener;
//		
//		/**  
//		* @Fields leftView : TODO( 对应左中右布局 )  
//		*/  
//		private View leftView,rightView,midView;
//		
//		/**
//		 * @param outSideSlipStatuListener 外部的监听器
//		 * @param leftViewBuilder 左布局
//		 * @param midViewBuilder 中布局
//		 * @param rightViewBuilder  右布局
//		 */
//		InSideSlipStatuListener(SideSlipStatuListener outSideSlipStatuListener,View leftViewBuilder,View midViewBuilder,View rightViewBuilder){
//			if (outSideSlipStatuListener == null || midViewBuilder == null) {
//				throw new NullPointerException("outSideSlipStatuListener == null || midViewBuilder == null");
//			}
//			outListener = outSideSlipStatuListener;
//			leftView = leftViewBuilder;
//			rightView = rightViewBuilder;
//			midView = midViewBuilder;
//		}
//		/**  
//		* @Fields LEFT_SHOWING : TODO( 标识左边布局正在显示 )  
//		*/  
//		private final int LEFT_SHOWING=0x3369;
//		/**  
//		* @Fields RIGHT_SHOWING : TODO( 标识右边布局正在显示 )  
//		*/  
//		private final int RIGHT_SHOWING=0x3368;
//		
//		/**  
//		* @Fields nowShowing : TODO( 现在正在显示的布局 )  
//		*/  
//		private int nowShowing = -1;
//		
//		@Override
//		public void leftViewStatu(SideSlipViewGroup sideSlipViewGroup,
//				View leftView, boolean hasShow) {
//			// TODO Auto-generated method stub
//			if (outListener!=null && leftView != null) {
//				//如果现在显示的不是左布局
//				if (nowShowing != LEFT_SHOWING) {
//					//回调
//					outListener.leftViewStatu(sideSlipViewGroup, this.leftView, hasShow);
//				}
//				//如果现在显示的是右布局
//				if (nowShowing == RIGHT_SHOWING) {
//					//回调
//					outListener.leftViewStatu(sideSlipViewGroup, this.rightView, false);
//				}
//			}
//			nowShowing = LEFT_SHOWING;
//		}
//
//		@Override
//		public void rightViewStatu(SideSlipViewGroup sideSlipViewGroup,
//				View rightView, boolean hasShow) {
//			// TODO Auto-generated method stub
//			nowShowing = RIGHT_SHOWING;
//		}
//
//		@Override
//		public void midViewStatu(SideSlipViewGroup sideSlipViewGroup,
//				View midView, boolean hasShow) {
//			// TODO Auto-generated method stub
//			nowShowing = -1;
//		}
//		
//	}
//	
}
