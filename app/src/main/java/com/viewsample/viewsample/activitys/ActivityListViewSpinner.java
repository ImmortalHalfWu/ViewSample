package com.viewsample.viewsample.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewsample.viewsample.adapters.BaseExpandableListViewAdapter;
import com.viewsample.viewsample.adapters.MixExpandableListViewAdapter;
import com.viewsample.viewsample.views.MixExpandableListViewBuilder;
import com.viewsample.viewsample.R;

import java.util.ArrayList;
import java.util.List;

/**   
 * @TODO listview + spinner
 * @author WuImmortalHalf
 * @date 创建时间：2016年10月18日 上午9:12:04 * 
 * @version   3.0
 */     
public class ActivityListViewSpinner extends Activity {

	private LinearLayout groupRelativeLayout;
	private RelativeLayout itemRelativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_spinner);
		

		
		initView();
	}


	private void initView() {
		groupRelativeLayout = (LinearLayout) findViewById(R.id.relativelayout);
		
		groupRelativeLayout.addView(createBuilder());
		groupRelativeLayout.addView(createBuilder());
		groupRelativeLayout.addView(createBuilder());
		
//		groupRelativeLayout.addView(getFirstView());
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
//		groupRelativeLayout.addView(getItemView(lastViewId));
	}
	
	private View createBuilder() {
		// TODO Auto-generated method stub
		return new MixExpandableListViewBuilder<String, String>(this, new MixExpandableListViewAdapter<String, String>(this, getGroupList(), getItemList()) {

			@Override
			public View createBottomView() {
				// TODO Auto-generated method stub
				return new Button(ActivityListViewSpinner.this);
			}

			@Override
			public int getGroupCount(List<String> groupList) {
				// TODO Auto-generated method stub
				return groupList.size();
			}

			@Override
			public int getChildrenCount(int groupPosition,
					List<String> childList) {
				// TODO Auto-generated method stub
				return childList.size();
			}

			@Override
			public View getGroupView(String object, int groupPosition,
									 boolean isExpanded, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent,false);
				}
				((TextView)(convertView)).setText(object);
				return convertView;
			}

			@Override
			public View getChildView(String object, int groupPosition,
									 int childPosition, boolean isLastChild,
									 View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent,false);
				}
				((TextView)(convertView)).setText(object);
				return convertView;
			}

			@Override
			public void configExpandableListView(
					final ExpandableListView expandableListView) {
				// TODO Auto-generated method stub
				expandableListView.setChildDivider(getResources().getDrawable(R.drawable.shape_liner));
//				expandableListView.setDivider(getResources().getDrawable(R.drawable.shape_liner));
				expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					
					@Override
					public void onGroupCollapse(int groupPosition) {
						// TODO Auto-generated method stub
						lastExpandableListView = null;
						setListViewHeightBasedOnChildren(expandableListView, false);
					}
				});
				expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					
					@Override
					public void onGroupExpand(int groupPosition) {
						// TODO Auto-generated method stub
						if (lastExpandableListView!= null) {
							lastExpandableListView.collapseGroup(0);
						}
						lastExpandableListView = expandableListView;
						setListViewHeightBasedOnChildren(expandableListView, true);
					}
				});
			}
		}).Builder();
	}


	private View getFirstView() {
		// TODO Auto-generated method stub
		itemRelativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_listview_spinner, null);
		itemRelativeLayout.setId(lastViewId);
		LayoutParams mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		itemRelativeLayout.setLayoutParams(mLayoutParams);
		ExpandableListView mExpandableListView = (ExpandableListView) itemRelativeLayout.findViewById(R.id.expandableListview);
		mExpandableListView.setAdapter(getAdapter());
		return itemRelativeLayout;
	}

	int lastViewId = 1;
	int lastExpandPositon = -1;
	ExpandableListView lastExpandableListView = null;
	private View getItemView(int lastViewId) {
		// TODO Auto-generated method stub
		itemRelativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_listview_spinner, null);
		itemRelativeLayout.setId(lastViewId++);
		LayoutParams mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		itemRelativeLayout.setLayoutParams(mLayoutParams);
		final ExpandableListView mExpandableListView = (ExpandableListView) itemRelativeLayout.findViewById(R.id.expandableListview);
		mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				if (lastExpandableListView!= null) {
					lastExpandableListView.collapseGroup(0);
				}
				lastExpandableListView = mExpandableListView;
				setListViewHeightBasedOnChildren(mExpandableListView, true);
			}
		});
		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				lastExpandableListView = null;
				setListViewHeightBasedOnChildren(mExpandableListView, false);
			}
		});
		mExpandableListView.setAdapter(getAdapter());
		return itemRelativeLayout;
	}

	private class ListViewModel{
		String group = "group",item = "item";
	}
	

	private ExpandableListAdapter getAdapter() {
		// TODO Auto-generated method stub
		return new BaseExpandableListViewAdapter<String, String>(ActivityListViewSpinner.this,getGroupList(),getItemList()){

		@Override
		public int getGroupCount(List<String> groupList) {
			// TODO Auto-generated method stub
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition,
				List<String> childList) {
			// TODO Auto-generated method stub
			return childList.size();
		}

		@Override
		public View getGroupView(String object, int groupPosition,
								 boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent,false);
			}
			((TextView)(convertView)).setText(object);
			return convertView;
		}

		@Override
		public View getChildView(String object, int groupPosition,
								 int childPosition, boolean isLastChild,
								 View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent,false);
			}
			((TextView)(convertView)).setText(object);
			return convertView;
		}
		};
	}
	
	private List<String> getItemList() {
		// TODO Auto-generated method stub
		
		List<String> itemList = new ArrayList<String>();
		itemList.add("item");
		itemList.add("item");
		itemList.add("item");
		itemList.add("item");
		return itemList;
	}

	private List<String> getGroupList() {
		// TODO Auto-generated method stub
		List<String> groupList = new ArrayList<String>();
		groupList.add("group");
		return groupList;
	}
	
	
	
	
	public static synchronized void setListViewHeightBasedOnChildren(ExpandableListView listView, boolean Expand ) {
        // 获取ListView对应的Adapter
       ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
              // pre -condition
              return;
       }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
             View listgroupItem = listAdapter .getGroupView(i, true, null, listView );
              listgroupItem.measure(0, 0); // 计算子项View 的宽高
              totalHeight += listgroupItem .getMeasuredHeight(); // 统计所有子项的总高度
             System. out.println("height : group" +i +"次" +totalHeight );
              for (int j = 0;Expand &&j < listAdapter.getChildrenCount( i); j++) {
                   View listchildItem = listAdapter .getChildView(i, j, false , null, listView);
                    listchildItem.measure(0, 0); // 计算子项View 的宽高
                    totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                   System. out.println("height :" +"group:" +i +" child:"+j+"次"+ totalHeight);
             }
       }

       ViewGroup.LayoutParams params = listView .getLayoutParams();
        params.height = totalHeight + ( listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params );
 }
	
	
}
