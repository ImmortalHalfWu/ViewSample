package com.viewsample.viewsample.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**   
 * @TODO 可折叠listview适配器父类，Group是父组件数据类型吗，Child是子组件数据类型
 * @author WuImmortalHalf
 * @date 创建时间：2016年10月9日 下午3:32:01 * 
 * @version   3.0
 */     
public abstract class  BaseExpandableListViewAdapter<Group,Child> extends BaseExpandableListAdapter {

    protected Context mContext;

    private List<Group> groupList;

    private List<Child> childList;

    public BaseExpandableListViewAdapter(Context mContext, List<Group> groupList, List<Child> childList){
        this.mContext = mContext;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return getGroupCount(groupList);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getChildrenCount(groupPosition,childList);
    }

    public abstract int getGroupCount(List<Group> groupList);

    public abstract int getChildrenCount(int groupPosition, List<Child> childList);

    public abstract View getGroupView(Group object, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    public abstract View getChildView(Child object, int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getGroupView(groupList.get(groupPosition),groupPosition,isExpanded,convertView,parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getChildView(childList.get(groupPosition),groupPosition,childPosition,isLastChild,convertView,parent);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}