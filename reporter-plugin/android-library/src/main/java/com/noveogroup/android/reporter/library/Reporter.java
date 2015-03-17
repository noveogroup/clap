/*
 * Copyright (c) 2015 Noveo
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

package com.noveogroup.android.reporter.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;

import com.noveogroup.android.reporter.library.system.ThreadInfo;
import com.noveogroup.android.reporter.library.system.Utils;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class Reporter {

    private Reporter() {
        throw new UnsupportedOperationException();
    }

    private static volatile Context applicationContext;

    public static synchronized void init(Context context) {
        if (applicationContext == null) {
            applicationContext = context.getApplicationContext();
        }

        sendInfo(System.currentTimeMillis(), SystemClock.uptimeMillis(), Utils.getDeviceInfo(applicationContext));
    }

    public static synchronized Context getApplicationContext() {
        return applicationContext;
    }

    public static synchronized void putCustomInfo(String key, String value) {
        // todo implement
    }

    public static synchronized Map<String, String> getCustomInfo() {
        // todo implement
        return null;
    }

    public static synchronized void sendCrash(long timestamp, long uptime,
                                              String loggerName, String description,
                                              Thread thread, Throwable exception,
                                              Map<String, String> deviceInfo, List<ThreadInfo> threads) {
        // todo implement
    }

    public static synchronized void sendInfo(long timestamp, long uptime,
                                             Map<String, String> deviceInfo) {
        // todo implement
    }

    public static synchronized void sendLogcat(long timestamp, long uptime,
                                               List<String> messages) {
        // todo implement
    }

    public static synchronized void sendLog(long timestamp, long uptime,
                                            String loggerName, String threadName,
                                            Logger.Level level, String message) {
        // todo implement
    }

    public static synchronized void sendScreenshot(long timestamp, long uptime,
                                                   String loggerName, String description,
                                                   Bitmap screenshot) {
        // todo implement
    }

    public static synchronized void sendSystemError(long timestamp, long uptime,
                                                    String description, Throwable exception) {
        // todo implement
    }

    private static final WeakHashMap<String, Logger> loggerMap = new WeakHashMap<>();

    public static Logger getLogger() {
        return getLogger(Utils.getCallerClassName());
    }

    public static Logger getLogger(Class<?> aClass) {
        return getLogger(aClass.getName());
    }

    public static synchronized Logger getLogger(String name) {
        Logger logger = loggerMap.get(name);
        if (logger == null) {
            loggerMap.put(name, logger = new Logger(name));
        }
        return logger;
    }

}
