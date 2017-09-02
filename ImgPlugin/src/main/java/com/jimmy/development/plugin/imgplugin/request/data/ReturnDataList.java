package com.jimmy.development.plugin.imgplugin.request.data;

import java.util.List;

/**
 * Created by jinguochong on 2017/8/23.
 */

public class ReturnDataList<T> {
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
