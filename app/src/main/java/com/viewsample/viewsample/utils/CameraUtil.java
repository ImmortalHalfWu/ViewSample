package com.viewsample.viewsample.utils;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * 照相机工具类。
 *  <p></p>
 * 获取previewSize。根据previewSize获取surfaceViewSize。手动对焦
 * <p></p>
 * Created by immortalHalfWu on 2016/11/30.
 */
public class CameraUtil {

    /**
     * 根据surfaceView的宽高，获取预览图像的宽高。
     * <P>重要：
     * <P>1，perviewwid 是 surfaceHei，perviewHei 是 surfaceWid。
     * <P>2，系统不支持预览图像宽小于高，所以，预览图像的 宽 必须大于 高 ，surfaceView的 高 必须大于 宽。
     * @param surfaceViewWid surfaceView宽，如果旋转90 or 270度，则Math.max(ViewHei,ViewWid)，否则ViewWid
     * @param surfaceViewHei surfaceView高，如果旋转90 or 270度，则Math.max(ViewHei,ViewWid)，否则ViewHei
     * @param mSizes  {@link Camera.Parameters#getSupportedPreviewSizes()}
     * @return 预览图像的宽高
     */
    public static Camera.Size getPriviewSize(int surfaceViewWid,int surfaceViewHei,List<Camera.Size> mSizes){
        return getOptimalPreviewSize(mSizes,surfaceViewWid,surfaceViewHei);
    }

    /**
     * 获取PreviewSize的宽高，这是google官方的代码
     * @param sizes {@link Camera.Parameters#getSupportedPreviewSizes()}
     * @param w surface宽
     * @param h surface高
     * @return 预览图像的宽高
     */
    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     *
     * <p>根据预览图像大小，获取surfaceView的大小</p>
     * <p>重要：</p>
     * <P>1，预览图像的 宽 必须大于 高 ，也 就 是 说 ，surfaceView的 宽 必须小于 高</P>
     * <p>2，为解决预览图像的拉伸问题，需要surfaceView的 高 宽 比例 == 预览图像的 宽 高 比例</p>
     * <p>3，计算surfaceView宽高比例，需要知道其中一个，而surfaceView的宽度通常是填充屏幕</p>
     * <P></P>
     * <p>计算方式：</p>
     * @param preViewWid 预览图像的宽
     * @param preViewHei 预览图像的高
     * @param screenWid 屏幕宽
     * @return
     */
    public static Rect getSurfaceViewSizeForPreviewSize(int preViewWid,int preViewHei,int screenWid){
        Rect mRect = new Rect(0,0,0,0);
        mRect.right = screenWid;
//        mRect.bottom = Math.round(screenWid*1.0f/preViewHei*preViewWid);
        mRect.bottom = Math.round(screenWid-preViewHei+preViewWid);
        return mRect;
    }

    /**
     * 手动对焦
     * @param mCamera 照相机
     * @param callback 对焦后的回调，可以为null，默认手动对焦成功后，将对焦模式设置为连续自动对焦
     * @param paint 焦点的x，y
     */
    public static void focusOnTouch(Camera mCamera, final Camera.AutoFocusCallback callback, Point paint, int surfaceWid, int surfaceHei){

        if (mCamera == null){
            return;
        }

            mCamera.cancelAutoFocus();
            Rect focusRect = calculateTapArea(paint.x, paint.y, 1f,surfaceWid,surfaceHei);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            List<Camera.Area> list = new ArrayList<>();
            list.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(list);
//        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f);
//            List<Camera.Area> list1 = new ArrayList<>();
//            list.add(new Camera.Area(meteringRect, 1000));
//            if (meteringAreaSupported) {
//                parameters.setMeteringAreas(list1);
//            }

            mCamera.setParameters(parameters);
            mCamera.autoFocus(callback == null ? new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
//                    if (success){
//                        if (camera != null){
//                            Camera.Parameters mParameters = camera.getParameters();
//                            if (mParameters != null && mParameters.getFocusMode().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
//                                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//                            }
//                            camera.cancelAutoFocus();
//                            camera.setParameters(mParameters);
//                        }
//                    }
                }
            } : callback);

    }

    private static Rect calculateTapArea(float x, float y, float coefficient,int surfaceWid,int surfaceHei) {
        int areaSize = Float.valueOf(100 * coefficient).intValue();

        int left = clamp((int) x - areaSize / 2, 0, surfaceWid - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, surfaceHei - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        new Matrix().mapRect(rectF);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }
    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

}
