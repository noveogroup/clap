package com.noveogroup.clap.library.logs;

import android.util.Log;

import com.noveogroup.clap.library.logs.log.ClapLogbackAppender;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LoggerAdjuster {

    private static final String APPENDER = "clapAppender";
    private static final String TAG = "ClapLoggerAdjuster";

    public synchronized static void adjustLogger() {
        org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (logger instanceof Logger) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            ClapLogbackAppender clapLogbackAppender = (ClapLogbackAppender) ((Logger) logger).getAppender(APPENDER);
            if (clapLogbackAppender == null) {
                final ClapLogbackAppender newAppender = new ClapLogbackAppender();
                newAppender.setContext(lc);
                newAppender.setName(APPENDER);
                newAppender.start();
                ((Logger) logger).addAppender(newAppender);
                Log.e(TAG, "logger added");
            }
        } else {
            Log.e(TAG, "logger not received - have you setup logback?");
        }
    }

}
