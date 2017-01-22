package com.viewsample.viewsample.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.views.ProgressButtonView;

public class ActivityProgressButton extends AppCompatActivity {

    private static final String TAG = "ActivityProgressButton";
    private ProgressButtonView mProgressBt1;
    private ProgressButtonView mProgressBt2;
    private ProgressButtonView mProgressBt3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_button);
        initView();
    }

    private void initView() {
        mProgressBt1  = (ProgressButtonView) findViewById(R.id.progressButtonView1);
        mProgressBt1.setCallBackListener(new ProgressButtonView.CallBackListener() {
            @Override
            public void shortClick() {
//                Log.i(TAG, "shortClick: ");
            }

            @Override
            public void longClick() {
//                Log.i(TAG, "longClick: ");
            }

            @Override
            public void longClickUp() {
//                Log.i(TAG, "longClickUp: ");
            }

            @Override
            public void progressOver() {
//                Log.i(TAG, "progressOver: ");
            }

            @Override
            public void move(MotionEvent event) {
//                Log.i(TAG, "move: ");
            }
        });

        mProgressBt2 = (ProgressButtonView) findViewById(R.id.progressButtonView2);
        mProgressBt2.setInCircleColor(0xffffffff);
        mProgressBt2.setOutCircleColor(0xff99cccc);
        mProgressBt2.setProgressColor(0xff336699);
        mProgressBt2.setProgressMaxTime(3*1000);

        mProgressBt3 = (ProgressButtonView) findViewById(R.id.progressButtonView3);
        mProgressBt3.setInCircleColor(0xffffffcc);
        mProgressBt3.setOutCircleColor(0xffff9900);
        mProgressBt3.setProgressColor(0xff99cc33);
        mProgressBt3.setProgressMaxTime(6*1000);
    }
}
