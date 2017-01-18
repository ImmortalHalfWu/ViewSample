package com.viewsample.viewsample.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.viewsample.viewsample.adapters.MixExpandableListViewAdapter;
import com.viewsample.viewsample.R;


/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年10月18日 下午2:21:12 
 * @version 3.0 
 *  @return  
 */
public class MixExpandableListViewBuilder<Group, Child> {

	private Context context;
	
	private MixExpandableListViewAdapter<Group, Child> mixExpandableListViewAdapter;
	
	private ViewGroup mViewGroup;
	
	private ExpandableListView mExpandableListView;
	
	private RelativeLayout mRelativeLayout;
	
	public MixExpandableListViewBuilder(Context mContext, MixExpandableListViewAdapter<Group,Child> expandableListViewAdapter){
		if (expandableListViewAdapter == null || mContext == null) {
			throw new NullPointerException("expandableListViewAdapter == null || mContext == null");
		}
		mixExpandableListViewAdapter = expandableListViewAdapter;
		context = mContext;
		initView();
		
	}
	
	
	private void initView() {
		// TODO Auto-generated method stub
		mViewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item_listview_spinner, null);
		mExpandableListView = (ExpandableListView) mViewGroup.findViewById(R.id.expandableListview);
		mRelativeLayout = (RelativeLayout) mViewGroup.findViewById(R.id.relativelayout);
		mExpandableListView.setAdapter(mixExpandableListViewAdapter);
		mixExpandableListViewAdapter.configExpandableListView(mExpandableListView);
		
		if (mixExpandableListViewAdapter.createBottomView() != null) {
			View mView = mixExpandableListViewAdapter.createBottomView();
			mView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mRelativeLayout.addView(mView);
		}
	}

	public View Builder() {
		return mViewGroup;
	}

	public interface MixExpandableListViewInterface{
		public View createBottomView();
		public void configExpandableListView(ExpandableListView expandableListView);
	}
}
