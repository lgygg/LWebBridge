package com.lgy.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lgy.web.bridge.CustomWebChromeClient;
import com.lgy.web.bridge.IBaseJSInterface;
import com.lgy.web.bridge.IManage;
import com.lgy.web.bridge.IWebView;
import com.lgy.web.bridge.util.MainThreadUtil;


public class LWebView extends WebView implements IWebView {
    private static final String BRIDGE_NAME = "LBridge";
    // 缓存地址
    private String cacheDir = getContext().getFilesDir().getAbsolutePath() + "/webCache";
    private WebChromeClient webChromeClient;
    private IManage manager;

    public LWebView(@NonNull Context context) {
        super(context);
    }

    public LWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
        WebSettings settings = getSettings();
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setAllowFileAccess(false);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCachePath(cacheDir);
        settings.setUseWideViewPort(true);
        webChromeClient = new CustomWebChromeClient();
        super.setWebChromeClient(webChromeClient);

    }

    @Override
    public void setManage(IManage manager) {
        this.manager = manager;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void setJSInterface(IBaseJSInterface iBaseJSInterface) {
        //注册原生提供的基础接口
        manager.addJavascriptObject(iBaseJSInterface);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            super.addJavascriptInterface(iBaseJSInterface, BRIDGE_NAME);
        } else {
            // add name in lower android version
            getSettings().setUserAgentString(getSettings().getUserAgentString() + " " + BRIDGE_NAME);
        }
    }

    /*
    * 执行js
    * */
    public void evaluateJavascriptCode(String script) {
        MainThreadUtil.runMain(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    LWebView.super.evaluateJavascript(script, null);
                } else {
                    LWebView.super.loadUrl("javascript:" + script);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        this.destroy();
    }


}
