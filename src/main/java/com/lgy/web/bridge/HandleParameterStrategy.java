package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2023/9/9
 */
public interface HandleParameterStrategy {
    Class[] getParameterTypes(String args);
    Object[] getParameter(String args);
}
