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
    private ProgressButtonView mProgressBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_button);
        initView();
    }

    private void initView() {
        mProgressBt  = (ProgressButtonView) findViewById(R.id.progressButtonView);
        mProgressBt.setCallBackListener(new ProgressButtonView.CallBackListener() {
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
    }
}
