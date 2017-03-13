package com.viewsample.viewsample.views;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewsample.viewsample.R;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年9月8日 下午2:43:03 
 * @version 3.0 
 *  @return  
 */
public class TimeTableAdapter extends Adapter<TimeTableAdapter.TimeTableViewHold>{
	
	/**  
	* @Fields rowsCount : TODO( 13行 )  
	*/  
	public static int rowsCount;
	/**  
	* @Fields columnsCount : TODO( 8列 )  
	*/  
	public final static int columnsCount = 8;
	
	public  int itemHeigh;
	
	/**  
	* @Fields itemCount : TODO( 显示的总个数 )  
	*/  
	private int itemCount = rowsCount * columnsCount;
	
	private int screenHei;
	
	private Context mContext;
	

	private List<TimeTableBean> mList = new ArrayList<TimeTableBean>();
	
	/**
	 * @param mContext 上下文
	 * @param sources 数据源
	 * @param rowsCount 几行，相当于一天几节课，比如上午四节，下午四节，晚上自修两节，则一共10节，就把10传进来，
	 */
	public TimeTableAdapter(Context mContext, List<TimeTableBean> sources, int rowsCount){
		this.mContext = mContext;
		mList = sources;
		TimeTableAdapter.rowsCount = rowsCount;
		initValue();
	}

	
	private void initValue() {
		// TODO Auto-generated method stub
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		screenHei = manager.getDefaultDisplay().getHeight();
		itemHeigh = screenHei / 10;
	}


	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}


	@Override
	public void onBindViewHolder(TimeTableViewHold arg0,
			int arg1) {
		// TODO Auto-generated method stub
		TimeTableBean mBean = mList.get(arg1);
		arg0.mTextView.setText(mBean.courseName);
		arg0.mTextView.setBackgroundColor(0x00000000);
		arg0.imageView.setVisibility(View.GONE);
		if (mBean.startRows!=-1) {
			arg0.mTextView.setLayoutParams(new RelativeLayout.LayoutParams(arg0.mTextView.getLayoutParams().width, itemHeigh*mBean.rowsCount));
			arg0.mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.orangee));
			arg0.imageView.setVisibility(View.VISIBLE);
			for (int i = 1; i < mBean.rowsCount; i++) {
				if (!mList.get(arg1+i*columnsCount).isDelete) {
					mList.remove(arg1 + i*columnsCount);
					mList.get(arg1+i*columnsCount).isDelete = true;
				}
			}
			
		}else if (arg1 < columnsCount) {
			arg0.mTextView.setLayoutParams(new RelativeLayout.LayoutParams(arg0.mTextView.getLayoutParams().width, itemHeigh/2));
		}else{
			arg0.mTextView.setLayoutParams(new RelativeLayout.LayoutParams(arg0.mTextView.getLayoutParams().width, itemHeigh));
		}
		
	}
	


	@Override
	public com.viewsample.viewsample.views.TimeTableAdapter.TimeTableViewHold onCreateViewHolder(
			ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return new TimeTableViewHold(LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_time_table_text, arg0,false));
	}

	
	 class TimeTableViewHold extends ViewHolder{

		 private final TextView mTextView;
		 private final ImageView imageView;

		public TimeTableViewHold(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			mTextView = (TextView) itemView.findViewById(R.id.textView1);
			mTextView.setBackgroundColor(0x00000000);
			imageView = (ImageView) itemView.findViewById(R.id.imageView);
		}
		
	}
	 
	 /**   
	 * @TODO
	 * @author WuImmortalHalf
	 * @date 创建时间：2016年9月9日 上午9:20:36 * 
	 * @version   3.0
	 */     
	public static class TimeTableBean{
		 /**  
		* @Fields courseName : TODO( 课程名称 )  
		*/  
		String courseName ="";
		/**  
		* @Fields rowsCount : TODO( 课长，1表示占用一行，就是一节课，2表示占用两行 )  
		*/  
		int rowsCount = -1;
		/**  
		* @Fields startRows : TODO( 从第几节课开始 )  
		*/  
		int startRows = -1;
		
		/**  
		* @Fields isDelete : TODO( 如果这节课占用多节课时，要删除垂直数据，占用几节删几个，这个字段标识是否已经做过这样的操作 )  
		*/  
		boolean isDelete = false;
		
		private TimeTableBean(String courseName, int rowCount, int startRows){
			this.courseName = courseName;
			this.rowsCount = rowCount;
			this.startRows = startRows;
		}
		private TimeTableBean(){}
		
		/**  
			* @TODO
		 	* @author WuImmortalHalf
			* @date 创建时间：2016年9月9日 上午10:11:17 * 
		 	* @param courseName 课程名称
		 	* @param rowCount 课长
		 	* @param startRows 开始
		 	* @return  
		 */    
		public static TimeTableBean newInstance(String courseName, int rowCount, int startRows){
			return new TimeTableBean(courseName, rowCount, startRows);
		}
		public static TimeTableBean newInstance(String courseName){
			return new TimeTableBean(courseName, -1,-1);
		}
		public static TimeTableBean newInstance(){
			return new TimeTableBean();
		}
		
		@Override
		public String toString() {
			return courseName+"\t";
		}
		
	 }
	 
}
