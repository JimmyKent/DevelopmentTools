package com.jimmy.development.logger.report.persistent.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jimmy.development.logger.report.LogReport;
import com.jimmy.development.logger.report.persistent.BaseSaver;
import com.jimmy.development.logger.util.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * 在崩溃之后，马上异步保存崩溃信息，完成后退出线程，并且将崩溃信息都写在一个文件中
 */
public class CrashWriter extends BaseSaver {

    private final static String TAG = "CrashWriter";

    /**
     * 崩溃日志文件的文件名：eg： CrashLog_2016-07-19.txt
     */
    public final static String LOG_FILE_NAME_EXCEPTION = "CrashLog_" + LOG_CREATE_TIME + SAVE_FILE_TYPE;


    /**
     * 。系统默认异常处理
     */
    private static final Thread.UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    /**
     * 初始化，继承父类
     *
     * @param context 上下文
     */
    public CrashWriter(Context context) {
        super(context);
    }

    /**
     * 异步的写操作，使用线程池对异步操作做统一的管理
     *
     * @param thread  发生崩溃的线程
     * @param ex      崩溃的错误信息
     * @param tag     标签，用于区别Log和Crash，一并写入文件中
     * @param content 写入的Crash内容
     */
    @Override
    public synchronized void writeCrash(final Thread thread, final Throwable ex, final String tag, final String content) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (BaseSaver.class) {
                    TimeLogFolder = LogReport.getInstance().getROOT() + "Log/" + yyyy_mm_dd.format(new Date(System.currentTimeMillis())) + "/";
                    File logsDir = new File(TimeLogFolder);
                    File crashFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION);
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Log.d(TAG, "SDcard 不可用");
                        return;
                    }
                    if (!logsDir.exists()) {
                        Log.d(TAG, "logsDir.mkdirs() =  +　" + logsDir.mkdirs());
                    }
                    if (!crashFile.exists()) {
                        createFile(crashFile, mContext);
                    }
                    StringBuilder preContent = new StringBuilder(FileUtil.getText(crashFile));
                    //Log.d(TAG, "读取本地的Crash文件， \n" + preContent.toString());
                    preContent.append(formatLogMsg(tag, content)).append("\n");
                    Log.d(TAG, "即将保存的Crash文件内容 = \n" + preContent.toString());
                    writeText(crashFile, preContent.toString());
                    sDefaultHandler.uncaughtException(thread, ex);
                }
            }
        });

    }

}