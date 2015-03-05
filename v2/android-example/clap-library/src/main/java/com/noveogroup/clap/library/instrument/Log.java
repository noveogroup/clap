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

public final class Log {

    private Log() {
        throw new UnsupportedOperationException();
    }

    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int INFO = android.util.Log.INFO;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int ASSERT = android.util.Log.ASSERT;

    private static String convertTag(String tag) {
        return "LogCat." + tag;
    }

    private static LogEntry.Level convertPriority(int priority) {
        switch (priority) {
            case VERBOSE:
                return LogEntry.Level.TRACE;
            case DEBUG:
                return LogEntry.Level.DEBUG;
            case INFO:
                return LogEntry.Level.INFO;
            case WARN:
                return LogEntry.Level.WARN;
            case ERROR:
                return LogEntry.Level.ERROR;
            case ASSERT:
                return LogEntry.Level.FATAL;
            default:
                return LogEntry.Level.INFO;
        }
    }

    public static String getStackTraceString(Throwable throwable) {
        return android.util.Log.getStackTraceString(throwable);
    }

    private static void append(LogEntry logEntry) {
        Clap.getInstance().getLoggerManager().append(logEntry);
    }

    public static int println(int priority, String tag, String message) {
        LogEntry.Builder builder = new LogEntry.Builder();
        builder.setLevel(convertPriority(priority));
        builder.setLoggerName(convertTag(tag));
        builder.setMessage(message);
        append(builder.build());
        return android.util.Log.println(priority, tag, message);
    }

    public static int println(int priority, String tag, String message, Throwable throwable) {
        LogEntry.Builder builder = new LogEntry.Builder();
        builder.setLevel(convertPriority(priority));
        builder.setLoggerName(convertTag(tag));
        builder.setMessage(throwable, message);
        append(builder.build());
        return android.util.Log.println(priority, tag, message + '\n' + getStackTraceString(throwable));
    }

    public static int println(int priority, String tag, Throwable throwable) {
        LogEntry.Builder builder = new LogEntry.Builder();
        builder.setLevel(convertPriority(priority));
        builder.setLoggerName(convertTag(tag));
        builder.setMessage(throwable);
        append(builder.build());
        return android.util.Log.println(priority, tag, getStackTraceString(throwable));
    }

    public static int v(String tag, String message) {
        return println(VERBOSE, tag, message);
    }

    public static int v(String tag, String message, Throwable throwable) {
        return println(VERBOSE, tag, message, throwable);
    }

    public static int d(String tag, String message) {
        return println(DEBUG, tag, message);
    }

    public static int d(String tag, String message, Throwable throwable) {
        return println(DEBUG, tag, message, throwable);
    }

    public static int i(String tag, String message) {
        return println(INFO, tag, message);
    }

    public static int i(String tag, String message, Throwable throwable) {
        return println(INFO, tag, message, throwable);
    }

    public static int w(String tag, String message) {
        return println(WARN, tag, message);
    }

    public static int w(String tag, String message, Throwable throwable) {
        return println(WARN, tag, message, throwable);
    }

    public static int w(String tag, Throwable throwable) {
        return println(WARN, tag, throwable);
    }

    public static int e(String tag, String message) {
        return println(ERROR, tag, message);
    }

    public static int e(String tag, String message, Throwable throwable) {
        return println(ERROR, tag, message, throwable);
    }

}
