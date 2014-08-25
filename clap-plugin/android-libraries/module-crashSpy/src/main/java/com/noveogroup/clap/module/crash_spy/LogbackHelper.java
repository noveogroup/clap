/*
 * Copyright (c) 2014 Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.clap.module.crash_spy;

import com.noveogroup.clap.library.api.server.beans.LogEntry;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;

public final class LogbackHelper {

    private LogbackHelper() {
        throw new UnsupportedOperationException();
    }

    private static final String LOGBACK_APPENDER_NAME = "logback-appender-name";

    private static int convertLevel(Level level) {
        switch (level.levelInt) {
            case Level.TRACE_INT:
                return LogEntry.TRACE;
            case Level.DEBUG_INT:
                return LogEntry.DEBUG;
            case Level.INFO_INT:
                return LogEntry.INFO;
            case Level.WARN_INT:
                return LogEntry.WARN;
            case Level.ERROR_INT:
                return LogEntry.ERROR;
            case Level.OFF_INT:
            case Level.ALL_INT:
            default:
                return LogEntry.INFO;
        }
    }

    public static void initLogback() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        StatusManager statusManager = loggerContext.getStatusManager();
        if (statusManager != null) {
            statusManager.add(new InfoStatus("Setting up logger configuration.", loggerContext));
        }

        final PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern("%msg%n");
        layout.start();

        AppenderBase<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
            @Override
            protected void append(ILoggingEvent event) {
                String message = layout.doLayout(event);
                LogHelper.appendLogEntry(event.getTimeStamp(), convertLevel(event.getLevel()),
                        event.getLoggerName(), event.getThreadName(), message);
            }
        };
        appender.setContext(loggerContext);
        appender.setName(LOGBACK_APPENDER_NAME);
        appender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }

}
