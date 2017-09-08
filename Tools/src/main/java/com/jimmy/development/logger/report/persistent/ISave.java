package com.jimmy.development.logger.report.persistent;

/**
 * 保存日志与崩溃信息的接口
 */
public interface ISave {

    void writeLog(String tag, String content);

    void writeCrash(Thread thread, Throwable ex, String tag, String content);
}