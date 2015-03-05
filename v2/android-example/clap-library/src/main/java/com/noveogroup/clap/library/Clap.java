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

package com.noveogroup.clap.library;

import android.content.Context;

import com.noveogroup.clap.library.logger.LoggerManager;

// todo configure ClapReporter
// todo handle Clap exceptions
// todo user can access LoggerManager
// todo implement activity & fragment trace
// todo log when use clicks buttons
// todo add fast redmine ticket creation
// todo implement screenshots
public class Clap {

    private static final Clap CLAP = new Clap();

    public static Clap getInstance() {
        return CLAP;
    }

    private final Object lock = new Object();
    private volatile boolean initStatic = false;
    private volatile boolean initContext = false;
    private volatile Context applicationContext = null;

    public void initStatic() {
        synchronized (lock) {
            if (!initStatic) {
                initStatic = true;
                doInitStatic();
            }
        }
    }

    public void initContext(Context context) {
        initStatic();

        synchronized (lock) {
            if (!initContext) {
                initContext = true;
                applicationContext = context.getApplicationContext();
                doInitContext(applicationContext);
            }
        }
    }

    public Context getApplicationContext() {
        synchronized (lock) {
            return applicationContext;
        }
    }

    private final LoggerManager loggerManager = new LoggerManager();

    private Clap() {
    }

    private void doInitStatic() {
        final Thread.UncaughtExceptionHandler previousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                try {
                    reportCrash(thread, ex);
                } finally {
                    previousHandler.uncaughtException(thread, ex);
                }
            }
        });
    }

    protected void doInitContext(Context applicationContext) {
        loggerManager.initContext(applicationContext);
    }

    public LoggerManager getLoggerManager() {
        return loggerManager;
    }

    public void reportCrash(Thread thread, Throwable throwable) {
        // todo implement
    }

}
