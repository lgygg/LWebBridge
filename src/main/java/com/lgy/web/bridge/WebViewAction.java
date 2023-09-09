package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public interface WebViewAction {
    void evaluateJavascriptCode(String script);
    void onDestroy();
}
