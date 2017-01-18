package com.viewsample.viewsample.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

/**
 * Created by ImmortalHalfWu on 2016/11/24.
 * <p>照相机生命周期管理类,此类只负责在对应的时间，关闭或打开照相机预览，Activity关闭时，仍需 mCamera=null;surfaceViewHolder = null;
 * <p>使用：
 * <p>1,初始化Camera后，通过构造函数{@link #CameraLifer(Camera)}将照相机传进来，当Camera改变时，可以通过{@link #setCamera(Camera)}方法传进来，不建议反复Camera.open()
 * <p>2,重写Activity的{@link Activity#onRestart()},{@link Activity#onResume()},{@link Activity#onDestroy()}方法，在方法中调用{@link #onRestart(SurfaceHolder)},{@link #onResume(SurfaceHolder)},{@link #onDestroy(SurfaceHolder)}.
 * <p>3,在{@link SurfaceHolder.Callback}的{@link SurfaceHolder.Callback#surfaceCreated(SurfaceHolder)}与{@link SurfaceHolder.Callback#surfaceDestroyed(SurfaceHolder)}中调用{@link #surfaceCreated(SurfaceHolder)},{@link #surfaceDestroyed(SurfaceHolder)}方法
 * <p>4,在确保上面3步正常通过后，通过{@link #startCamera(SurfaceHolder)}开启预览
 */

public class CameraLifer {

    /**
     * 照相机
     */
    private Camera mCamera;

    /**
     * 照相机是否打开中
     */
    private boolean cameraRuning  = false;

    public CameraLifer(Camera mCamera){
        this.mCamera = mCamera;
    }

    /**
     * <p>在{@link Activity#onResume()}中调用
     * @param mSurfaceHolder SurfaceHolder
     * @return
     */
    public Camera onResume(SurfaceHolder mSurfaceHolder) {
        return startCamera(mSurfaceHolder);
    }

    /**
     * <p>开启预览</p>
     * @param mSurfaceHolder SurfaceHolder
     * @return
     */
    public Camera startCamera(SurfaceHolder mSurfaceHolder) {
        if (cameraRuning){
            return mCamera;
        }
        if (mCamera == null){
            return mCamera;
        }
        if (mSurfaceHolder == null){
            return mCamera;
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            cameraRuning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    /**
     * <p>在{@link Activity#onRestart()}中调用</p>
     * @param mSurfaceHolder
     * @return
     */
    public Camera onRestart(SurfaceHolder mSurfaceHolder) {
        return stopCamera(mSurfaceHolder);
    }

    /**
     * <p>停止预览</p>
     * @param mSurfaceHolder
     * @return
     */
    private Camera stopCamera(SurfaceHolder mSurfaceHolder) {
        if (mCamera!=null && cameraRuning){
            mCamera.stopPreview();
            cameraRuning = false;
        }
        return mCamera;
    }

    /**
     * <p>在{@link Activity#onDestroy()}中调用</p>
     * @param mSurfaceHolder
     * @return
     */
    public Camera onDestroy(SurfaceHolder mSurfaceHolder) {
        destroyCamera(mSurfaceHolder);
        mSurfaceHolder.addCallback(null);
        return mCamera;
    }

    /**
     * <p>关闭照相机引用</p>
     * @param mSurfaceHolder
     * @return
     */
    private Camera destroyCamera(SurfaceHolder mSurfaceHolder) {
        stopCamera(mSurfaceHolder);
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
        return mCamera;
    }

    /**
     * <p>在{@link SurfaceHolder.Callback#surfaceCreated(SurfaceHolder)}中调用</p>
     * @param holder
     * @return
     */
    public Camera surfaceCreated(SurfaceHolder holder){
        cameraRuning = false;
        return mCamera;
    }

    /**
     * <p>在{@link SurfaceHolder.Callback#surfaceDestroyed(SurfaceHolder)}中调用</p>
     * @param holder
     * @return
     */
    public Camera surfaceDestroyed(SurfaceHolder holder) {
        return stopCamera(holder);
    }

    /**
     * <p>设置照相机引用</p>
     * @param mCamera
     */
    public void setCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

}
