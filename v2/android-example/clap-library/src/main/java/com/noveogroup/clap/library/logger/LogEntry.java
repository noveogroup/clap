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

package com.noveogroup.clap.library.logger;

import com.noveogroup.clap.library.system.LogUtils;

public class LogEntry {

    public static enum Level {

        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,

    }

    public static class Builder {

        private Long timestamp;
        private Level level;
        private String loggerName;
        private String threadName;
        private String message;

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setLevel(Level level) {
            this.level = level;
            return this;
        }

        public Builder setLoggerName(String loggerName) {
            this.loggerName = loggerName;
            return this;
        }

        public Builder setThreadName(String threadName) {
            this.threadName = threadName;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message == null ? "" : message;
            return this;
        }

        public Builder setMessage(Throwable throwable) {
            this.message = throwable == null ? "" : LogUtils.getStackTrace(throwable);
            return this;
        }

        public Builder setMessage(Throwable throwable, String message) {
            if (throwable == null) {
                this.message = message == null ? "" : message;
            } else {
                if (message == null || message.length() <= 0) {
                    this.message = LogUtils.getStackTrace(throwable);
                } else {
                    this.message = String.format("%s\n%s", message, LogUtils.getStackTrace(throwable));
                }
            }
            return this;
        }

        public LogEntry build() {
            if (timestamp == null)
                this.timestamp = System.currentTimeMillis();
            if (level == null)
                throw new NullPointerException("level is null");
            if (loggerName == null)
                throw new NullPointerException("logger name is null");
            if (threadName == null)
                this.threadName = Thread.currentThread().getName();
            if (message == null)
                throw new NullPointerException("message is null");
            return new LogEntry(timestamp, level, loggerName, threadName, message);
        }

    }

    private final long timestamp;
    private final Level level;
    private final String loggerName;
    private final String threadName;
    private final String message;

    private LogEntry(long timestamp, Level level, String loggerName, String threadName, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.loggerName = loggerName;
        this.threadName = threadName;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Level getLevel() {
        return level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getMessage() {
        return message;
    }

}
