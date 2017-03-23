package com.viewsample.viewsample.utils;


import android.hardware.Camera;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.widget.Toast;

import java.io.IOException;

/**
 * 录像管理类
 */
public class MediaRecorderLifer {

    /**
     * 重置状态
     */
    private final int STAUE_RESET = 0x20;
    /**
     * 录制状态
     */
    private final int STAUE_START = 0x21;
    /**
     * 完成
     */
    private final int STAUE_OVER = 0x23;
    /**
     * 取消
     */
    private final int STAUE_CANCLE = 0x24;

    /**
     * 目前的状态
     */
    private int mediaRecorderStaue;

    //mediaRecorder对象
    private MediaRecorder mediaRecorder;

    /**
     * 保存文件类型
     */
    private String fileType = ".mp4";

    /**
     * 文件夹路径
     */
    private String dirPath = null;

    /**
     * 当下正在录制的文件路径
     */
    private String filePath = null;

    private int vidioWid = 320,vidioHei = 240;

    public MediaRecorderLifer(){
        mediaRecorderStaue = STAUE_RESET;
//        initMediaRecorder(mCamera);
    }
    public MediaRecorderLifer(@NonNull MediaRecorder m){
        mediaRecorderStaue = STAUE_RESET;
        mediaRecorder = m;
    }

    private void initMediaRecorder(Camera mCamera) {
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setVideoSize(vidioWid,vidioHei);
        mediaRecorder.setVideoSize(320,240);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//压缩率更高
//        mediaRecorder.setVideoEncodingBitRate(vidioWid*vidioHei);
        mediaRecorder.setVideoEncodingBitRate(320*240*Math.min(Math.round(vidioWid*1.0f/320),Math.round(vidioHei*1.0f/240))*5/4);
        mediaRecorder.setVideoFrameRate(20);
//        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

    }

    /**
     * 重置
     */
    public MediaRecorderLifer resetRecorder(){
        if (mediaRecorderStaue != STAUE_CANCLE && mediaRecorderStaue != STAUE_OVER ){
            return this;
        }
        if (mediaRecorder!=null){
            mediaRecorderStaue = STAUE_RESET;
            mediaRecorder.reset();
        }

        return this;
    }

    /**
     * 开始录制
     */
    public MediaRecorderLifer startRecorder(@NonNull Camera camera, @NonNull Surface surface,@NonNull String dirPath,@NonNull String fileName){
        if (mediaRecorderStaue != STAUE_RESET){
            return this;
        }
        return startRecorder(camera,surface,dirPath+fileName);
    }
    /**
     * 开始录制
     */
    public MediaRecorderLifer startRecorder(@NonNull Camera camera, @NonNull Surface surface,@NonNull String filePath){
        if (mediaRecorderStaue != STAUE_RESET){
            return this;
        }
//        if (mediaRecorder!=null){
            try {
                mediaRecorderStaue = STAUE_START;
                camera.unlock();
                initMediaRecorder(camera);
                mediaRecorder.setPreviewDisplay(surface);
                mediaRecorder.setOutputFile(this.filePath = filePath);
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

        return this;
    }

    /**
     * 开始录制
     */
    public MediaRecorderLifer startRecorderForMp4(@NonNull Camera camera, @NonNull Surface surface,@NonNull String dirPath){
        return startRecorder(camera,surface,dirPath, System.nanoTime()+fileType);
    }

    /**
     * 开始录制
     */
    public MediaRecorderLifer startRecorderForMp4(@NonNull Camera camera, @NonNull Surface surface){
        if (dirPath == null){
            throw  new NullPointerException("通过setDirPath(path)统一的路径");
        }
        return startRecorderForMp4(camera,surface,dirPath);
    }

    /**
     * 结束录制
     */
    public MediaRecorderLifer overRecorder(){
        if (mediaRecorderStaue != STAUE_START){
            return this;
        }
        if (mediaRecorder != null){
            mediaRecorderStaue = STAUE_OVER;
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.setOnErrorListener(null);
            try {
                mediaRecorder.stop();
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * 取消录制
     */
    public MediaRecorderLifer cancleRecorder(){
        if (mediaRecorderStaue != STAUE_START){
            return this;
        }

        if (mediaRecorder != null){
            mediaRecorderStaue = STAUE_CANCLE;
            resetRecorder();
            FileUtil.getInstance().deleteFile(filePath);
        }

        return this;
    }

    /**
     * 释放所有相关持有
     */
    public void freeRecorder(){

        switch (mediaRecorderStaue){

            case STAUE_RESET:

                break;

            case STAUE_START:

                break;
            case STAUE_OVER:

                break;
            case STAUE_CANCLE:

                break;

        }

    }


    /**
     * 获取MediaRecorder实例
     * @return MediaRecorder实例
     */
    public MediaRecorder getMediaRecorder() {
        return mediaRecorder;
    }


    /**
     * 设置统一的文件夹路径
     * @param dirPath 文件夹路径
     */
    public MediaRecorderLifer setDirPath(String dirPath) {
        this.dirPath = dirPath;
        return this;
    }

    public MediaRecorderLifer setVidioSize(int Wid,int Hei){
        vidioWid = Wid;
        vidioHei = Hei;
        return  this;
    }


    /**
     * 设置统一的文件类型
     * @param fileType
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }















/*
    与MediaPlayer类非常相似MediaRecorder也有它自己的状态图。下面是关于MediaRecorder的各个状态的介绍：

    Initial：初始状态，当使用new()方法创建一个MediaRecorder对象或者调用了reset()方法时，该MediaRecorder对象处于Initial状态。在设定视频源或者音频源之后将转换为Initialized状态。另外，在除Released状态外的其它状态通过调用reset()方法都可以使MediaRecorder进入该状态。

    Initialized：已初始化状态，可以通过在Initial状态调用setAudioSource()或setVideoSource()方法进入该状态。在这个状态可以通过setOutputFormat()方法设置输出格式，此时MediaRecorder转换为DataSourceConfigured状态。另外，通过reset()方法进入Initial状态。

    DataSourceConfigured：数据源配置状态，这期间可以设定编码方式、输出文件、屏幕旋转、预览显示等等。可以在Initialized状态通过setOutputFormat()方法进入该状态。另外，可以通过reset()方法回到Initial状态，或者通过prepare()方法到达Prepared状态。

    Prepared：就绪状态，在DataSourceConfigured状态通过prepare()方法进入该状态。在这个状态可以通过start()进入录制状态。另外，可以通过reset()方法回到Initialized状态。

    Recording：录制状态，可以在Prepared状态通过调用start()方法进入该状态。另外，它可以通过stop()方法或reset()方法回到Initial状态。

    Released：释放状态（官方文档给出的词叫做Idle state 空闲状态），可以通过在Initial状态调用release()方法来进入这个状态，这时将会释放所有和MediaRecorder对象绑定的资源。

    Error：错误状态，当错误发生的时候进入这个状态，它可以通过reset()方法进入Initial状态。

    提示：与MediaPlayer相似使用MediaRecorder录音录像时需要严格遵守状态图说明中的函数调用先后顺序，在不同的状态调用不同的函数，否则会出现异常。
*/
}
