package com.jimmy.development.logger.report;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.jimmy.development.logger.report.crash.CrashHandler;
import com.jimmy.development.logger.report.persistent.ISave;
import com.jimmy.development.logger.report.persistent.impl.LogWriter;


/**
 * 日志崩溃管理框架
 */
public class LogReport {

    private static LogReport mLogReport;

    /**
     * 设置缓存文件夹的大小,默认是20MB
     */
    private long mCacheSize = 10 * 1024 * 1024;

    /**
     * 设置日志保存的路径
     */
    private String mROOT;


    /**
     * 设置日志的保存方式
     */
    private ISave mLogSaver;


    private LogReport() {
    }


    public static LogReport getInstance() {
        if (mLogReport == null) {
            synchronized (LogReport.class) {
                if (mLogReport == null) {
                    mLogReport = new LogReport();
                }
            }
        }
        return mLogReport;
    }

    public LogReport setCacheSize(long cacheSize) {
        this.mCacheSize = cacheSize;
        return this;
    }


    public LogReport setLogDir(Context context, String logDir) {
        if (TextUtils.isEmpty(logDir)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tools/";
            } else {
                mROOT = context.getCacheDir().getAbsolutePath();
            }
        } else {
            mROOT = logDir;
        }
        return this;
    }

    public LogReport setLogSaver(ISave logSaver) {
        this.mLogSaver = logSaver;
        return this;
    }


    public String getROOT() {
        return mROOT;
    }

    public void init(Context context) {
        if (TextUtils.isEmpty(mROOT)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                mROOT = context.getCacheDir().getAbsolutePath();
            }
        }
        CrashHandler.getInstance().init(mLogSaver);
        LogWriter.getInstance().init(mLogSaver);
    }


    public long getCacheSize() {
        return mCacheSize;
    }



}