package com.viewsample.viewsample.activitys;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.views.FadingTextViewBuilder;

public class ActivityFadingTextView extends SuperActivity {

    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fading_textview);

        initView();

    }

    private void initView() {
        viewGroup = (ViewGroup) findViewById(R.id.LinearLayout);
        int childCount = viewGroup.getChildCount();

        for (int i = 0 ; i < childCount ; i++){
            TextView textView = (TextView) viewGroup.getChildAt(i);
            new FadingTextViewBuilder(textView,new String[]{"SEPTEMBER 21ST","1945","THAT WAS THE NIGHT I DIED"}, (int) (Math.random()*1000%1000+1000)).builder().start();
        }


    }
}
