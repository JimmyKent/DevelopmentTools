package com.jimmy.development.http;

/**
 * Created by jinguochong on 17-4-13.
 */

public interface IHttpStringListener {

    void success(String data);
    void fail(int code, String msg);
}
