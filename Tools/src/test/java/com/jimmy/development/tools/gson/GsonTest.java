package com.jimmy.development.tools.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinguochong on 26/02/2018.
 * bean -> json
 * list -> json
 * <p>
 * json -> bean
 * json[] -> list
 */

public class GsonTest {

    @Test
    public void parse() {
        StudentBean zhangsan = new StudentBean();
        zhangsan.name = "zhangsan";
        zhangsan.id = 1;
        String zhangsanStr = new Gson().toJson(zhangsan);
        System.out.println(zhangsanStr);
        //{"name":"zhangsan","id":1}

        StudentBean lisi = new StudentBean();
        lisi.id = 2;
        String lisiStr = new Gson().toJson(lisi);
        System.out.println(lisiStr);
        //{"id":2}

        StudentBean lisi2 = new StudentBean();
        lisi2.name = "lisi";
        String lisi2Str = new Gson().toJson(lisi2);
        System.out.println(lisi2Str);
        //{"name":"lisi","id":0}

        zhangsan = new Gson().fromJson(zhangsanStr, StudentBean.class);
        System.out.println(zhangsan);
        //StudentBean{name='zhangsan', id=1}

        zhangsan = new Gson().fromJson(zhangsanStr, new TypeToken<StudentBean>() {
        }.getType());
        System.out.println(zhangsan);
        //StudentBean{name='zhangsan', id=1}


    }

    @Test
    public void parseList() {
        List<StudentBean> list = new ArrayList<>();
        StudentBean zhangsan = new StudentBean();
        zhangsan.name = "zhangsan";
        zhangsan.id = 1;
        list.add(zhangsan);

        StudentBean lisi = new StudentBean();
        lisi.name = "lisi";
        list.add(lisi);

        String listStr = new Gson().toJson(list, new TypeToken<List<StudentBean>>() {
        }.getType());
        System.out.println(listStr);

        List<StudentBean> list2 = new Gson().fromJson(listStr, new TypeToken<List<StudentBean>>() {
        }.getType());
        System.out.println(list2);


    }


}
