package com.jimmy.development.logger.base;

import android.util.Log;

import com.jimmy.development.logger.MLog;
import com.jimmy.development.logger.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(MLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(MLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        Util.printLine(tag, true);
        message = headString + MLog.LINE_SEPARATOR + message;
        String[] lines = message.split(MLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        Util.printLine(tag, false);
    }
}