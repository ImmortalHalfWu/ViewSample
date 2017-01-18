package com.viewsample.viewsample.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.views.PullRefreshHelp;

public class ActivityLoadMore extends Activity {

	private ExpandableListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_more);

		initView();

	}

	int i = 1;

	private void initView() {

		final PullRefreshHelp help = new PullRefreshHelp(this, new PullRefreshHelp.PullRefreshListener() {
			@Override
			public void loadMore() {
				Toast.makeText(ActivityLoadMore.this,"loadMore", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void refresh() {
				Toast.makeText(ActivityLoadMore.this,"refresh", Toast.LENGTH_SHORT).show();
			}
		}).setFooterContentView(getLayoutInflater().inflate(R.layout.item_main_listview, null), new PullRefreshHelp.ChangeContentViewStateInterface() {
			@Override
			public void pullUp(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("上拉加载更多");
			}

			@Override
			public void pullDown(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("上拉加载更多");
			}

			@Override
			public void refresh(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("加载中");
			}
		}).setHeaderContentView(getLayoutInflater().inflate(R.layout.item_main_listview, null), new PullRefreshHelp.ChangeContentViewStateInterface() {
			@Override
			public void pullUp(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("下拉刷新");
			}

			@Override
			public void pullDown(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("下拉刷新");
			}

			@Override
			public void refresh(View contentViewParent, View cententView) {
				((TextView)(cententView)).setText("刷新中");
			}
		});

		listview = (ExpandableListView) findViewById(R.id.expandableListview);
		listview.setOnTouchListener(help);
		listview.addFooterView(help.getFooterView());
		listview.addHeaderView(help.getHeaderView());

		listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (groupPosition % 2 == 0){
					Toast.makeText(ActivityLoadMore.this,"stopLoadMore", Toast.LENGTH_SHORT).show();
					help.stopLoadMore();
				}else {
					Toast.makeText(ActivityLoadMore.this,"stoprefresh", Toast.LENGTH_SHORT).show();
					help.stopRefresh();
				}
				return false;
			}
		});

		listview.setAdapter(new BaseExpandableListAdapter() {

			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
									 View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView textview = (TextView) getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
				textview.setText(groupPosition + "");
				return textview;
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return 15;
			}

			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
									 boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}
		});

	}

}
