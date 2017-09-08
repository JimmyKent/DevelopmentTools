package com.jimmy.development.http.data;

import android.support.annotation.Keep;

/**
 * Created by fengzhiping on 15-3-27.
 */
@Keep
public class ReturnData<T> {
    public int code;
    public String message;
    public String redirect;
    public T value;

    @Override
    public String toString() {
        return "ReturnData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", redirect='" + redirect + '\'' +
                ", value=" + value +
                '}';
    }
}
