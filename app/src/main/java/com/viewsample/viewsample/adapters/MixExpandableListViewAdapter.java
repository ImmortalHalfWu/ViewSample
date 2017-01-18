package com.viewsample.viewsample.adapters;

import android.content.Context;

import com.viewsample.viewsample.views.MixExpandableListViewBuilder.MixExpandableListViewInterface;

import java.util.List;

/** 
 * @author  WuImmortalHalf 
 * @date 创建时间：2016年10月18日 下午2:35:06 
 * @version 3.0 
 *  @return  
 */
public abstract  class  MixExpandableListViewAdapter<Group,Child> extends BaseExpandableListViewAdapter<Group, Child> implements MixExpandableListViewInterface{

	public MixExpandableListViewAdapter(Context mContext,
										List<Group> groupList, List<Child> childList) {
		super(mContext, groupList, childList);
		// TODO Auto-generated constructor stub
	}
	
}
