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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.SystemClock;

import com.noveogroup.android.reporter.library.events.CrashEvent;
import com.noveogroup.android.reporter.library.events.Event;
import com.noveogroup.android.reporter.library.events.InfoEvent;
import com.noveogroup.android.reporter.library.events.LogEvent;
import com.noveogroup.android.reporter.library.events.LogcatEvent;
import com.noveogroup.android.reporter.library.events.ScreenshotEvent;
import com.noveogroup.android.reporter.library.events.SystemErrorEvent;
import com.noveogroup.android.reporter.library.service.ReporterService;
import com.noveogroup.android.reporter.library.system.ThreadInfo;
import com.noveogroup.android.reporter.library.system.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class Reporter {

    private Reporter() {
        throw new UnsupportedOperationException();
    }

    static {
        Reporter.initStatic();
    }

    private static final Object lock = new Object();
    private static volatile boolean initStatic = false;
    private static volatile boolean initContext = false;
    private static volatile Context applicationContext = null;

    public static void initStatic() {
        synchronized (lock) {
            if (!initStatic) {
                doInitStatic();
                initStatic = true;
            }
        }
    }

    public static synchronized void initContext(Context context) {
        synchronized (lock) {
            if (!initContext) {
                applicationContext = context.getApplicationContext();
                doInitContext(applicationContext);
                initContext = true;
            }
        }
    }

    public static Context getApplicationContext() {
        synchronized (lock) {
            return applicationContext;
        }
    }

    private static void doInitStatic() {
        final Thread.UncaughtExceptionHandler previousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                try {
                    getRootLogger().crash(thread, ex);
                } finally {
                    previousHandler.uncaughtException(thread, ex);
                }
            }
        });
    }

    private static void doInitContext(Context applicationContext) {
        // save custom info
        syncCustomInfo(applicationContext);

        // send cached log events
        sendCachedEvents(applicationContext);

        // send info
        sendInfo(System.currentTimeMillis(), SystemClock.uptimeMillis(),
                Utils.getDeviceInfo(applicationContext, getCustomInfo()));
    }

    private static final String CUSTOM_INFO_PREFERENCES = "com.noveogroup.android.reporter.library.preferences";
    private static final Map<String, String> customInfoCache = new HashMap<>();

    private static synchronized void syncCustomInfo(Context applicationContext) {
        SharedPreferences preferences = applicationContext.getSharedPreferences(CUSTOM_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<String, String> entry : customInfoCache.entrySet()) {
            if (entry.getValue() == null) {
                editor.remove(entry.getKey());
            } else {
                editor.putString(entry.getKey(), entry.getValue());
            }
        }
        editor.commit();

        customInfoCache.clear();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            customInfoCache.put(entry.getKey(), entry.getValue().toString());
        }
    }

    public static synchronized void putCustomInfo(String key, String value) {
        if (applicationContext == null) {
            customInfoCache.put(key, value);
        } else {
            SharedPreferences preferences = applicationContext.getSharedPreferences(CUSTOM_INFO_PREFERENCES, Context.MODE_PRIVATE);
            preferences.edit()
                    .putString(key, value)
                    .commit();
        }
    }

    public static synchronized Map<String, String> getCustomInfo() {
        if (applicationContext == null) {
            return Collections.unmodifiableMap(new HashMap<>(customInfoCache));
        } else {
            syncCustomInfo(applicationContext);
            return customInfoCache;
        }
    }

    private static final List<Event> eventCache = new ArrayList<>();

    private static synchronized void sendCachedEvents(Context applicationContext) {
        for (Iterator<Event> iterator = eventCache.iterator(); iterator.hasNext(); ) {
            Event event = iterator.next();
            ReporterService.sendEvent(applicationContext, event);
            iterator.remove();
        }
    }

    private static synchronized void send(Event event) {
        eventCache.add(event);
        if (applicationContext != null) {
            sendCachedEvents(applicationContext);
        }
    }

    public static synchronized void sendCrash(long timestamp, long uptime,
                                              String loggerName, String description,
                                              Thread thread, Throwable exception,
                                              Map<String, String> deviceInfo, List<ThreadInfo> threads) {
        send(CrashEvent.create(
                timestamp, uptime,
                loggerName, description,
                thread, exception,
                deviceInfo, threads));
    }

    public static synchronized void sendInfo(long timestamp, long uptime,
                                             Map<String, String> deviceInfo) {
        send(InfoEvent.create(
                timestamp, uptime,
                deviceInfo));
    }

    public static synchronized void sendLogcat(long timestamp, long uptime,
                                               List<String> messages) {
        send(LogcatEvent.create(
                timestamp, uptime,
                messages));
    }

    public static synchronized void sendLog(long timestamp, long uptime,
                                            String loggerName, String threadName,
                                            Logger.Level level, String message) {
        send(LogEvent.create(
                timestamp, uptime,
                loggerName, threadName,
                level, message));
    }

    public static synchronized void sendScreenshot(long timestamp, long uptime,
                                                   String loggerName, String description,
                                                   Bitmap screenshot) {
        send(ScreenshotEvent.create(
                timestamp, uptime,
                loggerName, description,
                screenshot));
    }

    public static synchronized void sendSystemError(long timestamp, long uptime,
                                                    String description, Throwable exception) {
        send(SystemErrorEvent.create(
                timestamp, uptime,
                description, exception));
    }

    private static final WeakHashMap<String, Logger> loggerMap = new WeakHashMap<>();

    public static Logger getRootLogger() {
        return getLogger("");
    }

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
