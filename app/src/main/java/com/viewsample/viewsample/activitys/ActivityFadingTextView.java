package com.viewsample.viewsample.activitys;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.viewsample.viewsample.R;

public class ActivityFadingTextView extends SuperActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fading_textview);
        
        initView();
        
    }

    private void initView() {

        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            int r = Color.red(0xffFF0000);
            int g = Color.green(0xffFF0000);
            int b = Color.blue(0xffFF0000);
            int a = Color.alpha(0x55FF0000);
            @Override
            public void onClick(View v) {
                textView.setAlpha(1f);
                int textColor = textView.getCurrentTextColor();
                int textR = Color.red(textColor);
                int textG = Color.green(textColor);
                int textB = Color.blue(textColor);
                int textA = Color.alpha(textColor);
                textView.setTextColor(Color.rgb(
//                        textA - (textA - a)/10,
                        textR - (textR-r)/2,
                        textG - (textG-g)/2,
                        textB - (textB-b)/2
                ));
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            int r = Color.red(0xffFF0000);
            int g = Color.green(0xffFF0000);
            int b = Color.blue(0xffFF0000);
            int a = Color.alpha(0x55FF0000);
            @Override
            public void onClick(View v) {
                int textColor = textView.getCurrentTextColor();
                int textR = Color.red(textColor);
                int textG = Color.green(textColor);
                int textB = Color.blue(textColor);
                int textA = Color.alpha(textColor);
                textView.setTextColor(Color.rgb(
//                        textA + (textA - a)/10,
                        textR + (textR-r)/2,
                        textG + (textG-g)/2,
                        textB + (textB-b)/2
                ));
            }
        });

    }
}
