package com.jimmy.development.http;

import com.google.gson.reflect.TypeToken;

/**
 * Created by jinguochong on 17-7-28.
 * 这种情况比较多
 */
public interface IHttpListener<T> {

    void success(T data);
    void fail(int code, String msg);
    TypeToken<T> createTypeToken();
}
