package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public interface IBaseJSInterface extends NameSpace{

    String call(String methodName, String argStr);
    //native是否存在该方法
    boolean existMethod(Object args);
    void setParameterStrategy(HandleParameterStrategy parameterStrategy);
}
