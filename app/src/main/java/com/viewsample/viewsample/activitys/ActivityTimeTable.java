package com.viewsample.viewsample.activitys;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.views.TimeTableBuidler;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年9月8日 下午2:59:27 
 * @version 3.0 
 *  @return  
 */
public class ActivityTimeTable extends SuperActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);
		initView();
	}

	private void initView() {
		configRecyclerView();
	}

	private void configRecyclerView() {
		// TODO Auto-generated method stub
		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerView.setAdapter(
				TimeTableBuidler.create(10)
				.addCourse("高数", 2, 1, 3)
				.addCourse("数电", 1, 4, 6)
				.addCourse("JAVA", 1, 7, 9)
				.addCourse("SQL", 3, 6, 6)
				.Builder(this)
				);
//		mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
	}

	private void configTopLinearlayout() {

		// TODO Auto-generated method stub
		LinearLayout mLayout = (LinearLayout) findViewById(R.id.LinearLayout);
		for (int i = 0; i < 9; i++) {
//			TextView mTextView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_time_table_top_text, null);
			TextView mTextView =new TextView(this);
			android.widget.LinearLayout.LayoutParams mLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			
			mTextView.setGravity(Gravity.CENTER);
			if (i == 0 || i == 8) {
				mLayoutParams.weight=1;
				mTextView.setText(i == 0 ? "<" : ">");
			}else {
				mLayoutParams.weight=5;
				mTextView.setText("周"+ i);
			}
			mTextView.setLayoutParams(mLayoutParams);
			mLayout.addView(mTextView);
		}
	
	}
	
}
