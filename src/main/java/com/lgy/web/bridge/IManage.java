package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public interface IManage extends IBridge,WebViewAction{

    /**
     * debug模式下：输出日志
     * @param error
     */
    //
    void printDebugInfo(String error);
    //设置是否调试
    void setDebug(boolean debug);
    //返回是否是调试模式
    boolean isDebug();
    //设置缓存地址
    void setCachePackageDir(String dir);
    //获取本地包缓存地址
    String getCachePackageDir();

    IBaseJSInterface getBaseJSInterface();

}
