package com.viewsample.viewsample.views;

import android.content.Context;
import android.util.Log;

import com.viewsample.viewsample.views.TimeTableAdapter.TimeTableBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @TODO 创建一个课程表的adapter，指定8列，月份+周一至周日，
 * 	行数，也就是一天最多几门课，由外部传入，
 * @author WuImmortalHalf
 * @date 创建时间：2016年9月9日 上午9:30:36 * 
 * @version   3.0
 */     
public class TimeTableBuidler {
	
	/**  
	* @Fields maxRojws : TODO( 最大行数，也就是一天最多几门课 )  
	*/  
	private int maxRows;
	
	private List<TimeTableBean> sources;
	
	private  TimeTableBuidler(int maxRow) {
		maxRows = maxRow;
		initSources();
	}
	
	private void initSources() {
		// TODO Auto-generated method stub
		sources = new ArrayList<>(TimeTableAdapter.columnsCount * maxRows);
		sources.add(TimeTableBean.newInstance("九月"));
		sources.add(TimeTableBean.newInstance("周一"));
		sources.add(TimeTableBean.newInstance("周二"));
		sources.add(TimeTableBean.newInstance("周三"));
		sources.add(TimeTableBean.newInstance("周四"));
		sources.add(TimeTableBean.newInstance("周五"));
		sources.add(TimeTableBean.newInstance("周六"));
		sources.add(TimeTableBean.newInstance("周日"));
		for (int i = 0; i < TimeTableAdapter.columnsCount * maxRows; i++) {
			if (i%TimeTableAdapter.columnsCount ==0) {
				sources.add(TimeTableBean.newInstance(i/TimeTableAdapter.columnsCount +1+""));
			}else {
				sources.add(TimeTableBean.newInstance("q"));
			}
		}
		Log.i("", sources.toString());
	}

	public static TimeTableBuidler create(int maxRow) {
		return new TimeTableBuidler(maxRow);
	}
	
	/**  
		* @TODO
	 	* @author WuImmortalHalf
		* @date 创建时间：2016年9月9日 上午10:05:30 * 
	 	* @param courseName 课程名称
	 	* @param fewWeeks 周几
	 	* @param start 第几节课开始
	 	* @param end 第几节课结束
	 	* @return  
	 */    
	public TimeTableBuidler addCourse(String courseName, int fewWeeks, int start, int end) {
		int index = (start) * TimeTableAdapter.columnsCount +fewWeeks;
		sources.set(index,TimeTableBean.newInstance(courseName,end - start+1,start));
//		int index = (start) * TimeTableAdapter.columnsCount +fewWeeks+1;
//		sources.set(index,TimeTableBean.newInstance(courseName,end - start+1,start));
		return this;
	}
	
	public TimeTableAdapter Builder(Context context){
		return new TimeTableAdapter(context, sources, sources.size()/TimeTableAdapter.columnsCount );
	}
	
}
