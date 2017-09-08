package com.jimmy.development.logger.base;

import android.util.Log;

import com.jimmy.development.logger.MLog;

public class BaseLog {

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case MLog.V:
                Log.v(tag, sub);
                break;
            case MLog.D:
                Log.d(tag, sub);
                break;
            case MLog.I:
                Log.i(tag, sub);
                break;
            case MLog.W:
                Log.w(tag, sub);
                break;
            case MLog.E:
                Log.e(tag, sub);
                break;
            case MLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}