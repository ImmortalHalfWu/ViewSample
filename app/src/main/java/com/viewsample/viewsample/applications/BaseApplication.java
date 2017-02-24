package com.viewsample.viewsample.applications;

import android.app.Application;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;

/**   
 * 
 * Name:    BaseApplication
 * 
 * User:    WuImmortalHalf
 * Data:    2017/2/22 15:16
 *
 * Todo:    ( 主要是为了测试x5WebView )
 * 
*/ 
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;

        initTBS();

    }

    /**
     *<p> User:    WuImmortalHalf
     *<p> Data:    2017/2/22 15:13
     *<p> Todo:    ( 初始化X5 内核 )
    */ 
    private void initTBS() {

        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
            }
        });
        
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }
}
