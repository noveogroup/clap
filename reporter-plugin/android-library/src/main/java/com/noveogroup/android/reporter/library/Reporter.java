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
import android.os.SystemClock;

import com.noveogroup.android.reporter.library.events.Event;
import com.noveogroup.android.reporter.library.events.InfoEvent;
import com.noveogroup.android.reporter.library.service.ReporterService;
import com.noveogroup.android.reporter.library.system.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

public final class Reporter {

    private Reporter() {
        throw new UnsupportedOperationException();
    }

    public static final String TAG_STATUS = "REPORTER";

    private static final Object lock = new Object();
    private static volatile boolean initStatic = false;
    private static volatile boolean initContext = false;
    private static volatile Context applicationContext = null;

    private static final List<Event> eventCache = new ArrayList<>();

    static {
        Reporter.initStatic();
    }

    public static void initStatic() {
        synchronized (lock) {
            if (!initStatic) {
                doInitStatic();
                initStatic = true;
            }
        }
    }

    public static void initContext(Context context) {
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
        send(InfoEvent.create(
                System.currentTimeMillis(), SystemClock.uptimeMillis(),
                Utils.getDeviceInfo(null)));
    }

    private static void doInitContext(Context applicationContext) {
        ReporterService.initStarter(applicationContext);
        sendCachedEvents();
    }

    public static <E extends Event> void send(E event) {
        synchronized (lock) {
            eventCache.add(event);
            sendCachedEvents();
        }
    }

    private static void sendCachedEvents() {
        synchronized (lock) {
            if (applicationContext != null) {
                for (Iterator<Event> iterator = eventCache.iterator(); iterator.hasNext(); ) {
                    Event event = iterator.next();
                    ReporterService.send(applicationContext, event);
                    iterator.remove();
                }
            }
        }
    }

    public static void status(String message) {
        android.util.Log.i(TAG_STATUS, message);
    }

    public static void status(String message, Exception exception) {
        android.util.Log.e(TAG_STATUS, message, exception);
    }

    private static final WeakHashMap<String, Logger> loggerMap = new WeakHashMap<>();

    public static Logger getRootLogger() {
        return getLogger(Logger.ROOT_LOGGER_NAME);
    }

    public static Logger getLogger() {
        return getLogger(Utils.getCallerClassName());
    }

    public static Logger getLogger(Class<?> aClass) {
        return getLogger(aClass.getName());
    }

    public static Logger getLogger(String name) {
        synchronized (lock) {
            Logger logger = loggerMap.get(name);
            if (logger == null) {
                loggerMap.put(name, logger = new Logger(name));
            }
            return logger;
        }
    }

}
