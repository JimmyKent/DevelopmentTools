package com.jimmy.development.plugin.imgplugin.request;

/**
 * Created by jinguochong on 2017/8/30.
 */

public interface IHttpListener<T> {
    void ok(T data);
    void fail(int code, String msg);

}
