package com.jimmy.development.tools;

/**
 * Created by jinguochong on 17-8-30.
 */

public class TimeUtils {

    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        if (days == 0 && hours == 0) {
            return "小于1小时";
        }
        return days + "天" + hours + "小时";
    }
}
