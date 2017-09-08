package com.jimmy.development.http.data;

import android.support.annotation.Keep;

import java.util.ArrayList;

/**
 * Created by jinguochong on 17-8-21.
 * 两种列表格式之一 继承会有问题，无数据
 * 参照GiftListReturnData
 */
@Keep
public class ReturnDataList2<T> {
    public ArrayList<T> data;
    public boolean more;
}
