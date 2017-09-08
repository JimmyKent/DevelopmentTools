package com.jimmy.development.tools;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jinguochong on 17-8-30.
 */
public class TimeUtilsTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void formatDuring() throws Exception {
        String time1 = TimeUtils.formatDuring(1);
        System.out.println(time1);
        time1 = TimeUtils.formatDuring(100000000000L);
        System.out.println(time1);
        time1 = TimeUtils.formatDuring(1000 * 60 * 60 * 24);
        System.out.println(time1);
        long t =8640000000L;
        time1 = TimeUtils.formatDuring(t);
        System.out.println(time1);
    }

}