package com.hairstyle.weshow.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Logging {
    private Logger logger_;

    /**
     * 获取 类名
     *
     * @return
     */
    protected String getLogName() {
        return getClass().getName();
    }

    /**
     * 获取 Logger
     *
     * @return
     */
    protected Logger getLogger() {
        if (logger_ == null) {
            logger_ = LoggerFactory.getLogger(getLogName());
        }
        return logger_;
    }

    /**
     * 是否打开TRACE模式
     *
     * @return
     */
    protected boolean isTraceEnabled() {
        return logger_.isTraceEnabled();
    }

    /**
     * 是否打开DEBUG模式
     *
     * @return
     */
    protected boolean isDebugEnabled() {
        return logger_.isTraceEnabled();
    }


    /**
     * 打印 Debug 日志
     *
     * @param msg
     */
    protected void logDebug(String msg) {
        getLogger().debug(msg);
    }

    /**
     * 打印 Debug 日志
     *
     * @param msg
     * @param throwable
     */
    protected void logDebug(String msg, Throwable throwable) {
        getLogger().debug(msg, throwable);
    }

    /**
     * 打印 Trace 日志
     *
     * @param msg
     */
    protected void logTrace(String msg) {
        getLogger().trace(msg);
    }

    /**
     * 打印 Trace 日志
     *
     * @param msg
     * @param throwable
     */
    protected void logTrace(String msg, Throwable throwable) {
        getLogger().trace(msg, throwable);
    }


    /**
     * 打印 info日志
     *
     * @param msg
     */
    protected void logInfo(String msg) {
        getLogger().info(msg);
    }

    /**
     * 打印 info日志
     *
     * @param msg
     * @param throwable
     */
    protected void logInfo(String msg, Throwable throwable) {
        getLogger().info(msg, throwable);
    }


    /**
     * 打印 warning日志
     *
     * @param msg
     */
    protected void logWarning(String msg) {
        getLogger().warn(msg);
    }

    /**
     * 打印 warning日志
     *
     * @param msg
     * @param throwable
     */
    protected void logWarning(String msg, Throwable throwable) {
        getLogger().warn(msg, throwable);
    }

    /**
     * 打印 Error日志
     *
     * @param msg
     */
    protected void logError(String msg) {
        getLogger().error(msg);
    }

    /**
     * 打印 Error日志
     *
     * @param msg
     * @param throwable
     */
    protected void logError(String msg, Throwable throwable) {
        getLogger().error(msg, throwable);
    }


}
