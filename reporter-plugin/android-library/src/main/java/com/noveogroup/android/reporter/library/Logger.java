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

import android.graphics.Bitmap;
import android.os.SystemClock;

import com.noveogroup.android.reporter.library.system.Utils;

public class Logger {

    public static enum Level {

        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,

    }

    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void crash(Throwable exception) {
        crash(null, Thread.currentThread(), exception);
    }

    public void crash(Thread thread, Throwable exception) {
        crash(null, thread, exception);
    }

    public void crash(String description, Throwable exception) {
        crash(description, Thread.currentThread(), exception);
    }

    public void crash(String description, Thread thread, Throwable exception) {
        Reporter.sendCrash(
                System.currentTimeMillis(), SystemClock.uptimeMillis(),
                name, description,
                thread, exception,
                Utils.getDeviceInfo(Reporter.getApplicationContext()), Utils.getThreadsInfo());
    }

    public void screenshot(Bitmap screenshot) {
        screenshot(null, screenshot);
    }

    public void screenshot(String description, Bitmap screenshot) {
        Reporter.sendScreenshot(
                System.currentTimeMillis(), SystemClock.uptimeMillis(),
                name, description,
                screenshot);
    }

    public void t(String message) {
        log(Level.TRACE, message);
    }

    public void t(Throwable throwable, String message) {
        log(Level.TRACE, throwable, message);
    }

    public void t(String messageFormat, Object... args) {
        log(Level.TRACE, messageFormat, args);
    }

    public void t(Throwable throwable, String messageFormat, Object... args) {
        log(Level.TRACE, throwable, messageFormat, args);
    }

    public void d(String message) {
        log(Level.DEBUG, message);
    }

    public void d(Throwable throwable, String message) {
        log(Level.DEBUG, throwable, message);
    }

    public void d(String messageFormat, Object... args) {
        log(Level.DEBUG, messageFormat, args);
    }

    public void d(Throwable throwable, String messageFormat, Object... args) {
        log(Level.DEBUG, throwable, messageFormat, args);
    }

    public void i(String message) {
        log(Level.INFO, message);
    }

    public void i(Throwable throwable, String message) {
        log(Level.INFO, throwable, message);
    }

    public void i(String messageFormat, Object... args) {
        log(Level.INFO, messageFormat, args);
    }

    public void i(Throwable throwable, String messageFormat, Object... args) {
        log(Level.INFO, throwable, messageFormat, args);
    }

    public void w(String message) {
        log(Level.WARN, message);
    }

    public void w(Throwable throwable, String message) {
        log(Level.WARN, throwable, message);
    }

    public void w(String messageFormat, Object... args) {
        log(Level.WARN, messageFormat, args);
    }

    public void w(Throwable throwable, String messageFormat, Object... args) {
        log(Level.WARN, throwable, messageFormat, args);
    }

    public void e(String message) {
        log(Level.ERROR, message);
    }

    public void e(Throwable throwable, String message) {
        log(Level.ERROR, throwable, message);
    }

    public void e(String messageFormat, Object... args) {
        log(Level.ERROR, messageFormat, args);
    }

    public void e(Throwable throwable, String messageFormat, Object... args) {
        log(Level.ERROR, throwable, messageFormat, args);
    }

    public void f(String message) {
        log(Level.FATAL, message);
    }

    public void f(Throwable throwable, String message) {
        log(Level.FATAL, throwable, message);
    }

    public void f(String messageFormat, Object... args) {
        log(Level.FATAL, messageFormat, args);
    }

    public void f(Throwable throwable, String messageFormat, Object... args) {
        log(Level.FATAL, throwable, messageFormat, args);
    }

    public void log(Level level, String message) {
        sendLog(level, message);
    }

    public void log(Level level, Throwable throwable, String message) {
        if (throwable == null) {
            log(level, message);
        } else {
            sendLog(level, message + "\n" + Utils.getStackTrace(throwable));
        }
    }

    public void log(Level level, String messageFormat, Object... args) {
        sendLog(level, String.format(messageFormat, args));
    }

    public void log(Level level, Throwable throwable, String messageFormat, Object... args) {
        if (throwable == null) {
            log(level, messageFormat, args);
        } else {
            sendLog(level, String.format(messageFormat, args) + "\n" + Utils.getStackTrace(throwable));
        }
    }

    private void sendLog(Level level, String message) {
        Reporter.sendLog(
                System.currentTimeMillis(), SystemClock.uptimeMillis(),
                name, Thread.currentThread().getName(),
                level, message);
    }

}
