package com.lgy.web.bridge;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2022/9/1
 * 定义桥的管理内容
 */
public class BridgeImpl implements IBridge{
    //离线包加载
    public IOfflinePackage offlinePackage = null;
    // 命名空间，Object即原生提供给js调用接口
    private Map<String, NameSpace> javaScriptNamespaceInterfaces = new HashMap<String, NameSpace>();

    /**
     * Add a java object which implemented the javascript interfaces to dsBridge with namespace.
     * Remove the object using {@link #removeJavascriptObject(String) removeJavascriptObject(String)}
     *
     * @param action if empty, the object have no namespace.
     */
    @Override
    public void addJavascriptObject(NameSpace action) {
        if (action != null) {
            String namespace = action.getNameSpace();
            javaScriptNamespaceInterfaces.put(TextUtils.isEmpty(namespace)?"":namespace, action);
        }
    }

    /**
     * remove the javascript object with supplied namespace.
     *
     * @param namespace
     */
    @Override
    public void removeJavascriptObject(String namespace) {
        if (namespace == null) {
            namespace = "";
        }
        javaScriptNamespaceInterfaces.remove(namespace);

    }

    @Override
    public NameSpace getJavascriptObject(String namespace) {
       return javaScriptNamespaceInterfaces.get(namespace);
    }
}
