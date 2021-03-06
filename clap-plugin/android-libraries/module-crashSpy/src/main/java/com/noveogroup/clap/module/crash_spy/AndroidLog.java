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

import android.util.Log;

import com.noveogroup.clap.library.api.server.beans.LogEntry;

public final class AndroidLog {

    private AndroidLog() {
        throw new UnsupportedOperationException();
    }

    private static int convert(int level) {
        switch (level) {
            case Log.VERBOSE:
                return LogEntry.TRACE;
            case Log.DEBUG:
                return LogEntry.DEBUG;
            case Log.INFO:
                return LogEntry.INFO;
            case Log.WARN:
                return LogEntry.WARN;
            case Log.ERROR:
                return LogEntry.ERROR;
            case Log.ASSERT:
                return LogEntry.FATAL;
            default:
                return LogEntry.INFO;
        }
    }

    private static int convert(String methodName) {
        if (methodName.length() == 1) {
            switch (methodName.charAt(0)) {
                case 'v':
                    return LogEntry.TRACE;
                case 'd':
                    return LogEntry.DEBUG;
                case 'i':
                    return LogEntry.INFO;
                case 'w':
                    return LogEntry.WARN;
                case 'e':
                    return LogEntry.ERROR;
            }
        } else {
            if ("wtf".equals(methodName)) {
                return LogEntry.FATAL;
            }
        }
        return LogEntry.INFO;
    }

    private static void appendLogEntry(int level, String tag, String message, Throwable throwable) {
        if (throwable == null) {
            LogHelper.appendLogEntry(System.currentTimeMillis(), level,
                    tag, Thread.currentThread().getName(), message);
        } else {
            if (message == null) {
                LogHelper.appendLogEntry(System.currentTimeMillis(), level,
                        tag, Thread.currentThread().getName(), Log.getStackTraceString(throwable));
            } else {
                LogHelper.appendLogEntry(System.currentTimeMillis(), level,
                        tag, Thread.currentThread().getName(), message + "\n" + Log.getStackTraceString(throwable));
            }
        }
    }

    public static void log(String methodName, String tag, String message) {
        appendLogEntry(convert(methodName), tag, message, null);
    }

    public static void log(String methodName, String tag, Throwable throwable) {
        appendLogEntry(convert(methodName), tag, null, throwable);
    }

    public static void log(String methodName, String tag, String message, Throwable throwable) {
        appendLogEntry(convert(methodName), tag, message, throwable);
    }

    public static void log(int priority, String tag, String message) {
        appendLogEntry(convert(priority), tag, message, null);
    }

    public static void log(int priority, String tag, Throwable throwable) {
        appendLogEntry(convert(priority), tag, null, throwable);
    }

    public static void log(int priority, String tag, String message, Throwable throwable) {
        appendLogEntry(convert(priority), tag, message, throwable);
    }

}
