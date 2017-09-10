package com.jimmy.development.http.data;

import java.util.List;

/**
 * Created by jinguochong on 2017/8/23.
 */

public class ReturnDataListTest<T> {
    public boolean error;
    public List<T> results;

    @Override
    public String toString() {
        return "ReturnDataList{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
