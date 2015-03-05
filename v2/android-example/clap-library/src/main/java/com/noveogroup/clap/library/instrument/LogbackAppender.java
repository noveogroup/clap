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

package com.noveogroup.clap.library.instrument;

import com.noveogroup.clap.library.Clap;
import com.noveogroup.clap.library.logger.LogEntry;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;

public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public static void init() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        StatusManager statusManager = loggerContext.getStatusManager();
        if (statusManager != null) {
            statusManager.add(new InfoStatus("Setting up logger configuration.", loggerContext));
        }

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%msg%n");
        encoder.start();

        LogbackAppender appender = new LogbackAppender();
        appender.setContext(loggerContext);
        appender.setEncoder(encoder);
        appender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }

    private static LogEntry.Level convertLevel(Level level) {
        switch (level.levelInt) {
            case Level.TRACE_INT:
                return LogEntry.Level.TRACE;
            case Level.DEBUG_INT:
                return LogEntry.Level.DEBUG;
            case Level.INFO_INT:
                return LogEntry.Level.INFO;
            case Level.WARN_INT:
                return LogEntry.Level.WARN;
            case Level.ERROR_INT:
                return LogEntry.Level.ERROR;
            case Level.OFF_INT:
            case Level.ALL_INT:
            default:
                return LogEntry.Level.INFO;
        }
    }

    private PatternLayoutEncoder encoder = null;

    @Override
    public void start() {
        if ((this.encoder == null) || (this.encoder.getLayout() == null)) {
            addError("No layout set for the appender named [" + name + "].");
            return;
        }

        super.start();
    }

    protected void append(LogEntry logEntry) {
        Clap.getInstance().getLoggerManager().append(logEntry);
    }

    @Override
    public void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }

        LogEntry logEntry = new LogEntry.Builder()
                .setTimestamp(event.getTimeStamp())
                .setLevel(convertLevel(event.getLevel()))
                .setLoggerName(event.getLoggerName())
                .setThreadName(event.getThreadName())
                .setMessage(encoder.getLayout().doLayout(event))
                .build();
        append(logEntry);
    }

    public PatternLayoutEncoder getEncoder() {
        return this.encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

}
