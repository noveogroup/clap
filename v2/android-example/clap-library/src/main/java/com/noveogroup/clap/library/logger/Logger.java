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

public abstract class Logger {

    private final String loggerName;

    public Logger(String loggerName) {
        if (loggerName == null)
            throw new NullPointerException("logger name is null");
        this.loggerName = loggerName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    protected abstract void append(LogEntry logEntry);

    protected void print(LogEntry.Level level, String message) {
        LogEntry logEntry = new LogEntry.Builder()
                .setLevel(level)
                .setLoggerName(loggerName)
                .setMessage(message)
                .build();
        append(logEntry);
    }

    protected void print(LogEntry.Level level, Throwable throwable) {
        LogEntry logEntry = new LogEntry.Builder()
                .setLevel(level)
                .setLoggerName(loggerName)
                .setMessage(throwable)
                .build();
        append(logEntry);
    }

    protected void print(LogEntry.Level level, Throwable throwable, String message) {
        LogEntry logEntry = new LogEntry.Builder()
                .setLevel(level)
                .setLoggerName(loggerName)
                .setMessage(throwable, message)
                .build();
        append(logEntry);
    }

    public void log(LogEntry.Level level, Throwable throwable) {
        print(level, throwable);
    }

    public void log(LogEntry.Level level, Throwable throwable, String message) {
        print(level, throwable, message);
    }

    public void log(LogEntry.Level level, Throwable throwable, String messageFormat, Object... args) {
        print(level, throwable, String.format(messageFormat, args));
    }

    public void log(LogEntry.Level level, String message) {
        print(level, message);
    }

    public void log(LogEntry.Level level, String messageFormat, Object... args) {
        print(level, String.format(messageFormat, args));
    }

    public void t(Throwable throwable) {
        print(LogEntry.Level.TRACE, throwable);
    }

    public void t(Throwable throwable, String message) {
        print(LogEntry.Level.TRACE, throwable, message);
    }

    public void t(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.TRACE, throwable, String.format(messageFormat, args));
    }

    public void t(String message) {
        print(LogEntry.Level.TRACE, message);
    }

    public void t(String messageFormat, Object... args) {
        print(LogEntry.Level.TRACE, String.format(messageFormat, args));
    }

    public void d(Throwable throwable) {
        print(LogEntry.Level.DEBUG, throwable);
    }

    public void d(Throwable throwable, String message) {
        print(LogEntry.Level.DEBUG, throwable, message);
    }

    public void d(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.DEBUG, throwable, String.format(messageFormat, args));
    }

    public void d(String message) {
        print(LogEntry.Level.DEBUG, message);
    }

    public void d(String messageFormat, Object... args) {
        print(LogEntry.Level.DEBUG, String.format(messageFormat, args));
    }

    public void i(Throwable throwable) {
        print(LogEntry.Level.INFO, throwable);
    }

    public void i(Throwable throwable, String message) {
        print(LogEntry.Level.INFO, throwable, message);
    }

    public void i(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.INFO, throwable, String.format(messageFormat, args));
    }

    public void i(String message) {
        print(LogEntry.Level.INFO, message);
    }

    public void i(String messageFormat, Object... args) {
        print(LogEntry.Level.INFO, String.format(messageFormat, args));
    }

    public void w(Throwable throwable) {
        print(LogEntry.Level.WARN, throwable);
    }

    public void w(Throwable throwable, String message) {
        print(LogEntry.Level.WARN, throwable, message);
    }

    public void w(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.WARN, throwable, String.format(messageFormat, args));
    }

    public void w(String message) {
        print(LogEntry.Level.WARN, message);
    }

    public void w(String messageFormat, Object... args) {
        print(LogEntry.Level.WARN, String.format(messageFormat, args));
    }

    public void e(Throwable throwable) {
        print(LogEntry.Level.ERROR, throwable);
    }

    public void e(Throwable throwable, String message) {
        print(LogEntry.Level.ERROR, throwable, message);
    }

    public void e(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.ERROR, throwable, String.format(messageFormat, args));
    }

    public void e(String message) {
        print(LogEntry.Level.ERROR, message);
    }

    public void e(String messageFormat, Object... args) {
        print(LogEntry.Level.ERROR, String.format(messageFormat, args));
    }

    public void f(Throwable throwable) {
        print(LogEntry.Level.FATAL, throwable);
    }

    public void f(Throwable throwable, String message) {
        print(LogEntry.Level.FATAL, throwable, message);
    }

    public void f(Throwable throwable, String messageFormat, Object... args) {
        print(LogEntry.Level.FATAL, throwable, String.format(messageFormat, args));
    }

    public void f(String message) {
        print(LogEntry.Level.FATAL, message);
    }

    public void f(String messageFormat, Object... args) {
        print(LogEntry.Level.FATAL, String.format(messageFormat, args));
    }

}
