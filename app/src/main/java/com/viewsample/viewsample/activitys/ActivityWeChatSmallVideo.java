package com.viewsample.viewsample.activitys;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.utils.CameraLifer;
import com.viewsample.viewsample.utils.CameraUtil;
import com.viewsample.viewsample.utils.FileUtil;
import com.viewsample.viewsample.utils.MediaRecorderLifer;
import com.viewsample.viewsample.views.WeChatVideoButton;
import com.viewsample.viewsample.views.WeChatVideoProgressBar;


/**
 *
 * Name:    ActivityWeChatSmallVideo
 *
 * User:    WuImmortalHalf
 * Data:    2016/11/15 14:18
 *
 * Todo:    ( 微信小视频 )
 *
*/
public class ActivityWeChatSmallVideo extends Activity {

    private final String TAG = getClass().getSimpleName();
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private WeChatVideoProgressBar progressBar;//进度条
    private WeChatVideoButton WeChatVideoButton;//底部按钮
    private MediaRecorderLifer mMediaRecorderLifer;

    /**
     * 照相机生命周期管理
     */
    private CameraLifer mCameraLefter;

    /**
     * 屏幕宽高
     */
    private int screenWid,screenHei;

    /**
     * 默认surfaceView宽高
     */
    private int defaultViewHei,defaultViewWid;

    /**
     * 默认surfaceView占屏幕高度的SURFACE_VIEW_WEIGHT-1成，SURFACE_VIEW_WEIGHT <= 1 则全屏；
     */
    private final int SURFACE_VIEW_WEIGHT = 4;

    /**
     *  照相机预览视图的宽高
     */
    private int PreviewWid,PreviewHei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_small_video);
        FileUtil.instance(this);

        initScreen();
        initView();
        instancesCamera();
        initMediaRecorde();
        initSurfaceView();

    }

    private void initMediaRecorde() {
        mMediaRecorderLifer = new MediaRecorderLifer()
                .setDirPath(FileUtil.getAppVidioFilePath())
                .setVidioSize(PreviewWid,PreviewHei)
        ;
    }


    /**
     * 获取屏幕宽高，根据权重比，求出surfaceView初始级别的宽高
     */
    private void initScreen() {

        WindowManager windowManager = getWindowManager();
        screenHei = windowManager.getDefaultDisplay().getHeight();
        screenWid = windowManager.getDefaultDisplay().getWidth();

        //如果权重比小于等于1，则全屏
        if (SURFACE_VIEW_WEIGHT<=1){
            defaultViewHei = screenHei;
        }else {
            //否则，根据权重比，求出一个大于屏幕宽度的view高度
            for (
                    int i = SURFACE_VIEW_WEIGHT;
                    (defaultViewHei = screenHei/i*(i-1))<=screenWid;
                    ++i
                    );
        }


        defaultViewWid = screenWid;
    }

    /**
     * 初始化View
     */
    private void initView() {
        //进度条初始化
        progressBar = (WeChatVideoProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMaxTime(7*1000);
        progressBar.setProgressColor(Color.GREEN);
        progressBar.setCallback(new WeChatVideoProgressBar.Callback() {
            @Override
            public void overCallBack() {
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        timeOver();
                    }
                });
            }
        });

        //录制按钮点击事件
        WeChatVideoButton = (WeChatVideoButton) findViewById(R.id.button);

        WeChatVideoButton.setTextString("按住拍");


        WeChatVideoButton.setOnTouchListener(btOnTouchListener);

        //surfaceView初始化
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceHolder = holder;
                mCameraLefter.surfaceCreated(holder);
                mCameraLefter.startCamera(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraLefter.surfaceDestroyed(holder);
            }
        });

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CameraUtil.focusOnTouch(mCamera, null,new Point(Math.round(event.getX()),Math.round(event.getY())),mSurfaceView.getWidth(),mSurfaceView.getHeight());
                return false;
            }
        });

    }


    /**
     * 初始化照相机
     */
    private void instancesCamera() {

        mCamera = Camera.open();

        if (mCameraLefter ==null){
            mCameraLefter = new CameraLifer(mCamera);
        }else{
            mCameraLefter.setCamera(mCamera);
        }

        if (mParameters == null){
            mParameters = mCamera.getParameters();
//            List<Camera.Size> mSizes = mParameters.getSupportedPreviewSizes();
            //90 or 270
//            Camera.Size mSize = getOptimalPreviewSize(mSizes,Math.max(defaultViewHei,defaultViewWid),Math.min(defaultViewHei,defaultViewWid));
            Camera.Size mSize = CameraUtil.getPriviewSize(Math.max(defaultViewHei,defaultViewWid),Math.min(defaultViewHei,defaultViewWid),mParameters.getSupportedPreviewSizes());
            //0 or 180
//            Camera.Size mSize = CameraUtil.getPriviewSize(screenWid  ,screenHei ,mParameters.getSupportedPreviewSizes());
//            Camera.Size mSize = CameraUtil.getPriviewSize( defaultViewWid , defaultViewHei,mParameters.getSupportedPreviewSizes());
//          Camera.Size mSize = getOptimalPreviewSize(mSizes,surfaceViewhei,surfaceViewWid);
            PreviewHei = mSize.height;
            PreviewWid = mSize.width;
            mParameters.setPreviewSize(PreviewWid,PreviewHei);

            if (mParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                //连续对焦
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mParameters.set("orientation", "portrait");
        }
        mCamera.cancelAutoFocus();
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(mParameters);
    }

    /**
     *
     */
    private void initSurfaceView() {
        Rect mRect = CameraUtil.getSurfaceViewSizeForPreviewSize(PreviewWid,PreviewHei,screenWid);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWid,Math.round(screenWid * 1f/PreviewHei*PreviewWid));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mRect.width(),mRect.height());
        mSurfaceView.setLayoutParams(layoutParams);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraLefter.onResume(mSurfaceHolder);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCameraLefter.onRestart(mSurfaceHolder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraLefter.onDestroy(mSurfaceHolder);
    }

    //==============================以下为录像相关==========================================================
    /**
     * y轴关于取消的偏移量
     */
    private int btCaccleOffY = -1;
    /**
     * 上移取消popwindow
     */
    private PopupWindow popupWindowStart;

    /**
     * 松开取消popwindow
     */
    private PopupWindow popupWindowCancle;
    /**
     * 重置状态
     */
    private final int STATUE_RESET = 0x10;
    /**
     * 重置--->录像中
     * 取消录像--->录像中
     */
    private final int STATUE_VIDIOING = 0x11;
    /**
     * 录像中--->时间到
     */
    private final int STATUE_TIME_OVER = 0x12;
    /**
     * 录像中--->手指移动到取消录像
     */
    private final int STATUE_CANCLE = 0x13;
    /**
     * 时间到--->录像结束
     * 抬起手指--->录像结束
     */
    private final int STATUE_OVER = 0x15;
    /**
     * 录像中--->抬起手指
     * 手指移动到取消录像--->抬起手指
     */
    private final int STATUE_MOVE_UP = 0x16;

    /**
     * vidio的状态
     */
    private int vidioStatue;

    private View.OnTouchListener btOnTouchListener = new View.OnTouchListener() {

        private final int CANCLE = 0x02;
        private final int START = 0x03;

        private int statu;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if (btCaccleOffY==-1){
                        btCaccleOffY =v.getHeight()/10;
                    }
                    statu = START;
                    moveDown();
//                    startVideo();
                case MotionEvent.ACTION_MOVE:
                    if (event.getY() < btCaccleOffY){
                        if (statu == START){
                            statu = CANCLE;
                            moveToCancle();
                        }
                    }else {
                        if (statu == CANCLE){
                            statu = START;
                            moveToStart();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    moveUp();
                    break;
            }

            return true;
        }
    };

    /**
     * 重置录像相关
     */
    private void resetVidio(){
        if (vidioStatue != STATUE_RESET){
            Log.i(getClass().getSimpleName(),"重置");
            progressBar.reset();
            progressBar.setProgressColor(Color.GREEN);
            popupWindowStart = popupWindowStart== null ? new PopupWindow(getLayoutInflater().inflate(R.layout.layout_popupwindow_start,null), RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT):popupWindowStart;
            popupWindowCancle = popupWindowCancle == null?new PopupWindow(getLayoutInflater().inflate(R.layout.layout_popwindow_cancle,null), RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT) : popupWindowCancle;
            dimessPopupWindow(popupWindowCancle);
            dimessPopupWindow(popupWindowStart);
            vidioStatue = STATUE_RESET;
            mMediaRecorderLifer.resetRecorder();
        }
    }

    private void moveToCancle(){
        dimessPopupWindow(popupWindowStart);
        showCanclePopWindow();
        //录制中--->手指移动到取消
        if (vidioStatue == STATUE_VIDIOING){
            Log.i(getClass().getSimpleName(),"手指移动到取消");
            vidioStatue = STATUE_CANCLE;
            progressBar.setProgressColor(Color.RED);
        }
    }

    private void moveToStart(){
        dimessPopupWindow(popupWindowCancle);
        showStartPopwindow();
        Log.i(getClass().getSimpleName(),"移动到录像");
        vidioStatue = STATUE_VIDIOING;
        progressBar.setProgressColor(Color.GREEN);
    }

    /**
     * 时间结束
     */
    private void timeOver(){
            //录制中--->时间结束
//            if (vidioStatue == STATUE_VIDIOING){
                Log.i(getClass().getSimpleName(),"时间结束");
                vidioStatue = STATUE_TIME_OVER;
                //重置录像
                overVideo();
//            //重置其他相关
//            resetVidio();
//            }
    }


    /**
     * 按下屏幕
     */
    private void moveDown(){
        resetVidio();
        showStartPopwindow();
        progressBar.start();
        //只有reset或cancle状态能转为录制状态
        if (vidioStatue == STATUE_CANCLE || vidioStatue == STATUE_RESET){
            Log.i(getClass().getSimpleName(),"开始录像");
            vidioStatue = STATUE_VIDIOING;
            mMediaRecorderLifer.startRecorderForMp4(mCamera,mSurfaceHolder.getSurface());
        }
    }

    /**
     * 抬起手指
     */
    private void moveUp(){
        dimessPopupWindow(popupWindowCancle);
        dimessPopupWindow(popupWindowStart);
        if (vidioStatue == STATUE_VIDIOING){
            Log.i(getClass().getSimpleName(),"录制--->抬起手指");
            vidioStatue = STATUE_MOVE_UP;
            overVideo();
            return;
        }
        if (vidioStatue == STATUE_CANCLE){
            Log.i(getClass().getSimpleName(),"手指移动到取消--->抬起手指");
            vidioStatue = STATUE_MOVE_UP;
            mMediaRecorderLifer.cancleRecorder();
            Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();

            resetVidio();
        }

    }

    /**
     * 录像完成，重置录像相关
     */
    private void overVideo(){
        //时间到或抬起手指才能重置录像相关
        if (vidioStatue == STATUE_TIME_OVER || vidioStatue == STATUE_MOVE_UP){
            Log.i(getClass().getSimpleName(),"overVideo录像完成,关闭录像机");
            vidioStatue = STATUE_OVER;
            mMediaRecorderLifer.overRecorder();
            resetVidio();
            Toast.makeText(this,"File path:" + FileUtil.getAppVidioFilePath(),Toast.LENGTH_LONG).show();
        }
    }





















    private void showStartPopwindow(){
        if (popupWindowStart!=null && !popupWindowStart.isShowing()){
            popupWindowStart.showAtLocation(WeChatVideoButton, Gravity.BOTTOM,0,WeChatVideoButton.getHeight()+btCaccleOffY);
        }
    }

    private void showCanclePopWindow(){
        if (popupWindowCancle!=null && !popupWindowCancle.isShowing()){
            popupWindowCancle.showAtLocation(WeChatVideoButton, Gravity.BOTTOM,0,WeChatVideoButton.getHeight()+btCaccleOffY);
        }
    }
    private void dimessPopupWindow(@NonNull PopupWindow popupWindow){
        if (popupWindow.isShowing()){
           popupWindow.dismiss();
        }
    }

    /**
     * 释放录像相关资源
     */
    private void freeVideo(){
        //录制结束或重置状态才能释放录像资源
        if (vidioStatue == STATUE_OVER || vidioStatue == STATUE_RESET){

        }
    }
}
