package com.jimmy.development.tools;

import org.junit.Test;

/**
 * Created by jinguochong on 2017/9/8.
 */

public class NullTest {

    @Test
    public void nullTest(){
        String s = null;
        try{
            s.split(",");
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.print("111111");
    }
}
