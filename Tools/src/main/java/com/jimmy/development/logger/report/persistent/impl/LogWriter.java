package com.jimmy.development.logger.report.persistent.impl;

import android.util.Log;

import com.jimmy.development.logger.report.LogReport;
import com.jimmy.development.logger.report.persistent.ISave;

/**
 * 用于写入Log到本地
 */
public class LogWriter {
    private static LogWriter mLogWriter;
    private static ISave mSave;

    private LogWriter() {
    }


    public static LogWriter getInstance() {
        if (mLogWriter == null) {
            synchronized (LogReport.class) {
                if (mLogWriter == null) {
                    mLogWriter = new LogWriter();
                }
            }
        }
        return mLogWriter;
    }


    public LogWriter init(ISave save) {
        mSave = save;
        return this;
    }

    public static void writeLog(String tag, String content) {
        Log.d(tag, content);
        mSave.writeLog(tag, content);
    }
}