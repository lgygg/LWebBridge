package com.lgy.web.bridge;

import java.util.Map;

/**
 * @author: Administrator
 * @date: 2023/9/7
 */
public class Response {

    private Integer code;
    private String msg;
    private Map<String,Object> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
