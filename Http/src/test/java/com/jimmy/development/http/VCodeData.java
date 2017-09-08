package com.jimmy.development.http;


/**
 * Created by jinguochong on 17-9-5.
 */

public class VCodeData {
    public boolean result;
    public String vCodeRex;
    public String downServiceNumber;


    @Override
    public String toString() {
        return "VCodeData{" +
                "result=" + result +
                ", vCodeRex='" + vCodeRex + '\'' +
                ", downServiceNumber='" + downServiceNumber + '\'' +
                '}';
    }
}
