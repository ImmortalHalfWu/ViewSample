package com.viewsample.viewsample.activitys;

import android.os.Bundle;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.viewsample.viewsample.R;

/**
 * 
 * Name:    ActivityTencentX5WebView
 * 
 * User:    WuImmortalHalf
 * Data:    2017/2/22 15:02
 *
 * Todo:    ( 腾讯X5内核WebView )
 * 
*/ 
public class ActivityTencentX5WebView extends SuperActivity{

    private final String URL = "http://www.baidu.com/";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5_webview);
        
        initView();
        initWebView();

    }


    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
    }


    private void initWebView() {

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });

        webView.loadUrl(URL);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (webView != null && webView.canGoBack()){
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null) webView.destroy();
    }
}
