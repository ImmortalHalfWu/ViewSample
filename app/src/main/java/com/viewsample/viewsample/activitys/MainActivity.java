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
    private List<ListviewItemDate> listDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();
        initView();

    }

    private void initDate() {

        listDate = new ArrayList<MainActivity.ListviewItemDate>();
        listDate.add(new ListviewItemDate("钟表", com.viewsample.viewsample.activitys.ActivityClock.class));
        listDate.add(new ListviewItemDate("图文", com.viewsample.viewsample.activitys.ActivityImageWithTxt.class));
        listDate.add(new ListviewItemDate("简单进度条", com.viewsample.viewsample.activitys.ActivityProgress.class));
        listDate.add(new ListviewItemDate("音量控制",com.viewsample.viewsample.activitys. ActivitySound.class));
        listDate.add(new ListviewItemDate("手势锁", com.viewsample.viewsample.activitys.ActivityLock.class));
        listDate.add(new ListviewItemDate("侧滑", com.viewsample.viewsample.activitys.ActivitySideSlip.class));
        listDate.add(new ListviewItemDate("线性布局分割线", com.viewsample.viewsample.activitys.ActivityLinear.class));
//        listDate.add(new ListviewItemDate("表单布局", com.viewsample.viewsample.activitys.ActivityFromList.class));
        listDate.add(new ListviewItemDate("课程表", com.viewsample.viewsample.activitys.ActivityTimeTable.class));
        listDate.add(new ListviewItemDate("动态添加spinner", com.viewsample.viewsample.activitys.ActivityListViewSpinner.class));
        listDate.add(new ListviewItemDate("上拉加载", com.viewsample.viewsample.activitys.ActivityLoadMore.class));
        listDate.add(new ListviewItemDate("进度条按钮", com.viewsample.viewsample.activitys.ActivityProgressButton.class));
        listDate.add(new ListviewItemDate("微信小视频", com.viewsample.viewsample.activitys.ActivityWeChatSmallVideo.class));

    }

    private void initView() {
        mListview = (ListView) findViewById(R.id.listview);
        mListview.setAdapter(new MyAdapter());
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, listDate.get((int) id).nextActivity));
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listDate == null ? 0 : listDate.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listDate.get(position);
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

            ((TextView)convertView).setText(listDate.get(position).itemName);

            return convertView;
        }


    }

    class ListviewItemDate{

        public String itemName;
        public Class<?> nextActivity;


        public ListviewItemDate(String name, Class<?> activity) {
            itemName = name;
            nextActivity = activity;
        }

    }

}
