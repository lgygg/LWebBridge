package com.lgy.web.bridge.util;

/**
 * @author: Administrator
 * @date: 2023/8/29
 */
public class NameSpaceUtil {

    /*
     * 解析命名空间和方法名
     * */
    public static String[] parseNameSpace(String method) {
        int pos = method.lastIndexOf('.');
        String namespace = "";
        if (pos != -1) {
            namespace = method.substring(0, pos);
            method = method.substring(pos + 1);
        }
        return new String[]{namespace, method};
    }
}
