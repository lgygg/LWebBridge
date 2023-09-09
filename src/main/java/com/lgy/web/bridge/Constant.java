package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public class Constant {

    public static final String MSG_CANNOT_FIND_JS_INTERFACE = "Js bridge called, but can't find a corresponded " +
            "JavascriptInterface object , please check your code!";
    public static final String MSG_METHOD_NOT_PERMISSION = "Method %s is not invoked, since it is not declared with JavascriptInterface annotation!";
    public static final String MSG_METHOD_NOT_FIND = "Not find method %s implementation! please check if the signature or namespace of the method is right!";
    public static final String MSG_SUCCESS = "call success!";


    public static final Integer CODE_ERROR = -2;
    public static final Integer CODE_NOT_NAMESPACE = -1;
    public static final Integer CODE_SUCCESS = 0;
}
