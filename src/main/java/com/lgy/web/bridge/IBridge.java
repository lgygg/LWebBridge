package com.lgy.web.bridge;

public interface IBridge {
    public void addJavascriptObject(NameSpace action);
    public void removeJavascriptObject(String namespace);
    public NameSpace getJavascriptObject(String namespace);
}
