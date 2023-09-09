package com.lgy.web.bridge;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2023/9/9
 */
public class NormalHandleParameterStrategy implements HandleParameterStrategy{
    @Override
    public Class[] getParameterTypes(String args) {
        List<Class> list = new ArrayList<>();
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(args,Map.class);
        for (int i = 0; i < map.size(); i++) {
            Object value = map.get("arg"+i);
            if (value instanceof String) {
                list.add(String.class);
            }else if (value instanceof Integer) {
                list.add(Integer.class);
            }else if (value instanceof Float) {
                list.add(Float.class);
            }else if (value instanceof Double) {
                list.add(Double.class);
            }
        }
        return list.toArray(new Class[list.size()]);
    }

    @Override
    public Object[] getParameter(String args) {
        List<Object> list = new ArrayList<>();
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(args,Map.class);
        for (int i = 0; i < map.size(); i++) {
            list.add(map.get("arg"+i));
        }
        return list.toArray();
    }
}
