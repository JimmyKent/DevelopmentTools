package com.jimmy.development.http.data;

import android.support.annotation.Keep;

import java.util.ArrayList;

/**
 * Created by jinguochong on 17-8-21.
 * 两种列表格式之一
 */
@Keep
public class ReturnDataList<T> {
    public int expire;
    public int index;
    public int size;
    public int total;
    public ArrayList<T> values;

    @Override
    public String toString() {
        return "ReturnDataList{" +
                "expire=" + expire +
                ", index=" + index +
                ", size=" + size +
                ", total=" + total +
                ", values=" + values +
                '}';
    }
}
