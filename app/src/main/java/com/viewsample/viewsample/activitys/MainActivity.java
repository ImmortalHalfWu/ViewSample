package com.viewsample.viewsample.activitys;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.viewsample.viewsample.R;

public class MainActivity extends SuperActivity {

    private ListView mListview;
    private List<ListviewItemData> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

    }

    private void initData() {
        listData = new ArrayList<>();
        listData.add(new ListviewItemData("钟表", com.viewsample.viewsample.activitys.ActivityClock.class));
        listData.add(new ListviewItemData("图文", com.viewsample.viewsample.activitys.ActivityImageWithTxt.class));
        listData.add(new ListviewItemData("简单进度条", com.viewsample.viewsample.activitys.ActivityProgress.class));
        listData.add(new ListviewItemData("音量控制",com.viewsample.viewsample.activitys. ActivitySound.class));
        listData.add(new ListviewItemData("手势锁", com.viewsample.viewsample.activitys.ActivityLock.class));
        listData.add(new ListviewItemData("侧滑", com.viewsample.viewsample.activitys.ActivitySideSlip.class));
        listData.add(new ListviewItemData("线性布局分割线", com.viewsample.viewsample.activitys.ActivityLinear.class));
//        listData.add(new ListviewItemData("表单布局", com.viewsample.viewsample.activitys.ActivityFromList.class));
        listData.add(new ListviewItemData("课程表", com.viewsample.viewsample.activitys.ActivityTimeTable.class));
        listData.add(new ListviewItemData("动态添加spinner", com.viewsample.viewsample.activitys.ActivityListViewSpinner.class));
        listData.add(new ListviewItemData("上拉加载", com.viewsample.viewsample.activitys.ActivityLoadMore.class));
        listData.add(new ListviewItemData("微信视频进度条按钮", com.viewsample.viewsample.activitys.ActivityProgressButton.class));
        listData.add(new ListviewItemData("微信视频伸缩布局", com.viewsample.viewsample.activitys.ActivityStretchOutLayout.class));
        listData.add(new ListviewItemData("微信小视频", com.viewsample.viewsample.activitys.ActivityWeChatSmallVideo.class));
        listData.add(new ListviewItemData("X5内核WebView", ActivityTencentX5WebView.class));
        listData.add(new ListviewItemData("渐入渐出TextView", ActivityFadingTextView.class));

    }

    private void initView() {
        mListview = (ListView) findViewById(R.id.listview);
        mListview.setAdapter(new MyAdapter());
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, listData.get((int) id).nextActivity));
            }
        });
    }

    final class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData == null ? 0 : listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.item_main_listview, parent, false);
            }

            ((TextView)convertView).setText(listData.get(position).itemName);

            return convertView;
        }


    }

    final class ListviewItemData{

        public final String itemName;
        public final Class<?> nextActivity;


        public ListviewItemData(String name, Class<?> activity) {
            itemName = name;
            nextActivity = activity;
        }

    }

}
