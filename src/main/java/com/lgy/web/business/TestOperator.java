package com.lgy.web.business;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.lgy.web.bridge.IResult;
import com.lgy.web.bridge.NameSpace;

/**
 * @author: Administrator
 * @date: 2023/9/9
 */
public class TestOperator implements NameSpace {
    @Override
    public String getNameSpace() {
        return "test";
    }

    @JavascriptInterface
    public String test1(String phoneNum) {
        return "hello:"+phoneNum;
    }
    @JavascriptInterface
    public void test2(String phoneNum, IResult result) {
        result.success("phone is "+phoneNum);
    }
}
