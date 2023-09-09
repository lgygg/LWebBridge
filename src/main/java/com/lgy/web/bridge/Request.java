package com.lgy.web.bridge;

import java.util.List;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public class Request {
    private String callbackName;
    private String args;
    private boolean isAsync;

    public String getCallbackName() {
        return callbackName;
    }

    public void setCallbackName(String callbackName) {
        this.callbackName = callbackName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }
}
