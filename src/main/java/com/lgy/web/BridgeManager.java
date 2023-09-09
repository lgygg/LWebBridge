package com.lgy.web;

import android.util.Log;

import com.lgy.web.bridge.BaseJavaScriptInterface;
import com.lgy.web.bridge.IBaseJSInterface;
import com.lgy.web.bridge.IBridge;
import com.lgy.web.bridge.IManage;
import com.lgy.web.bridge.IWebView;
import com.lgy.web.bridge.NameSpace;
import com.lgy.web.bridge.NormalHandleParameterStrategy;

/**
 * @author: Administrator
 * @date: 2023/9/7
 * 管理类
 */
public class BridgeManager implements IManage {
    private static final String BRIDGE_MANAGER = "BridgeManager";
    private IBridge bridge;
    private IWebView webView;
    //webView是否debug模式
    private boolean isDebug = false;
    // 本地宝缓存地址
    private String cachePackageDir = null;

    private IBaseJSInterface iBaseJSInterface;

    public BridgeManager(IBridge bridge,IWebView webView){
        this.bridge = bridge;
        this.webView = webView;
        this.webView.setManage(this);
        this.webView.init();
        iBaseJSInterface = new BaseJavaScriptInterface(this);
        iBaseJSInterface.setParameterStrategy(new NormalHandleParameterStrategy());
        this.webView.setJSInterface(iBaseJSInterface);
    }

    //设置是否调试
    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    //返回是否是调试模式
    public boolean isDebug() {
        return false;
    }

    //设置缓存地址
    public void setCachePackageDir(String dir) {
        this.cachePackageDir = dir;
    }

    //获取缓存地址
    public String getCachePackageDir() {
        return this.cachePackageDir;
    }

    @Override
    public IBaseJSInterface getBaseJSInterface() {
        return null;
    }

    @Override
    public void addJavascriptObject(NameSpace nameSpace) {
        this.bridge.addJavascriptObject(nameSpace);
    }

    @Override
    public void removeJavascriptObject(String nameSpace) {
        this.bridge.removeJavascriptObject(nameSpace);
    }

    @Override
    public NameSpace getJavascriptObject(String nameSpace){
        return this.bridge.getJavascriptObject(nameSpace);
    }

    /**
     * debug模式下：输出日志
     *
     * @param error
     */
    @Override
    public void printDebugInfo(String error) {
        if (isDebug()) {
            Log.d(BRIDGE_MANAGER, error);
            this.evaluateJavascriptCode(String.format("alert('%s')", "DEBUG ERR MSG:\\n" + error.replaceAll("\\'", "\\\\'")));
        }
    }

    @Override
    public void evaluateJavascriptCode(String script) {
        this.webView.evaluateJavascriptCode(script);
    }

    @Override
    public void onDestroy() {
        this.webView.onDestroy();
        bridge = null;
        webView = null;
    }
}
