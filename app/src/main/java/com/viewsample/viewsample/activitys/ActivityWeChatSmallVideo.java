package com.viewsample.viewsample.activitys;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.utils.CameraLifer;
import com.viewsample.viewsample.utils.CameraUtil;
import com.viewsample.viewsample.views.FocusingView;


public class ActivityWeChatSmallVideo extends AppCompatActivity {

    private static final String TAG = "ActivityWeChatSmallVide";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;

    private CameraLifer mCameraLifer;

    private PopupWindow mPopwindow;

    private int screenWid,screenHei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_small_video);

        initScreen();
        initCamera();
        initSurfaceView();
        initListener();
        initPopupWindow();

    }

    private void initPopupWindow() {
        mPopwindow = new PopupWindow();
        mPopwindow.setFocusable(false);
        mPopwindow.setWidth(Math.min(screenHei,screenWid)/4);
        mPopwindow.setHeight(Math.min(screenHei,screenWid)/4);
    }


    /**
     * 获取屏幕宽高
     */
    private void initScreen() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenHei = windowManager.getDefaultDisplay().getHeight();
        screenWid = windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 初始化照相机
     */
    private void initCamera() {
        if (mCamera == null){
            mCamera = Camera.open();
            mParameters = mCamera.getParameters();
            mCameraLifer = new CameraLifer(mCamera);
            Camera.Size previewSize = CameraUtil.getPriviewSize(screenWid,screenHei,mParameters.getSupportedPreviewSizes());
            mParameters.setPreviewSize(previewSize.width,previewSize.height);
            if (mParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mParameters.set("orientation", Camera.Parameters.SCENE_MODE_PORTRAIT);
            mCamera.cancelAutoFocus();
            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(mParameters);
        }

    }

    /**
     * 初始化surfaceView
     */
    private void initSurfaceView() {

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                mSurfaceHolder = surfaceHolder;
                mCameraLifer.surfaceCreated(mSurfaceHolder);
                mCameraLifer.startCamera(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mCameraLifer.surfaceDestroyed(mSurfaceHolder);
            }
        });

    }


    private void initListener() {

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    if (mPopwindow.isShowing()){
                        mPopwindow.dismiss();
                    }
                    mPopwindow.setContentView(new FocusingView(ActivityWeChatSmallVideo.this).setCallBack(new FocusingView.CallBack() {
                        @Override
                        public void over(FocusingView v) {
                            if (mPopwindow != null && mPopwindow.isShowing()){
                                mPopwindow.dismiss();
                            }
                        }
                    }));
                    int locationOffset = mPopwindow.getHeight()/2;
                    mPopwindow.showAtLocation(view, Gravity.TOP|Gravity.START,Math.round(motionEvent.getX())-locationOffset,Math.round(motionEvent.getY())-locationOffset);
                }

                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraLifer.onDestroy(mSurfaceHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraLifer.onResume(mSurfaceHolder);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCameraLifer.onRestart(mSurfaceHolder);
    }
}
