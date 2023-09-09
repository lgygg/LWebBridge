package com.lgy.web.bridge;

/**
 * @author: Administrator
 * @date: 2022/9/1
 */
public interface IResult<T> {
    void success(T retValue);
    void success();
    void failure(T exception);
    void setProgressData(T value);
}
