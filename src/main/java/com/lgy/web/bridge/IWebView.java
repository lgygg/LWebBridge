package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public interface IWebView extends WebViewAction{
    void init();
    void setManage(IManage bridge);
    void setJSInterface(IBaseJSInterface iBaseJSInterface);
}
