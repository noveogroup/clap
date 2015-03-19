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

import com.noveogroup.android.reporter.library.system.Info;

public final class Log {

    private Log() {
        throw new UnsupportedOperationException();
    }

    public static void crash(Throwable exception) {
        Reporter.getLogger().crash(exception);
    }

    public static void crash(Throwable exception, String description) {
        Reporter.getLogger().crash(exception, description);
    }

    public static void crash(Throwable exception, Info info) {
        Reporter.getLogger().crash(exception, info);
    }

    public static void crash(Throwable exception, String description, Info info) {
        Reporter.getLogger().crash(exception, description, info);
    }

    public static void crash(Thread thread, Throwable exception) {
        Reporter.getLogger().crash(thread, exception);
    }

    public static void crash(Thread thread, Throwable exception, String description) {
        Reporter.getLogger().crash(thread, exception, description);
    }

    public static void crash(Thread thread, Throwable exception, Info info) {
        Reporter.getLogger().crash(thread, exception, info);
    }

    public static void crash(Thread thread, Throwable exception, String description, Info info) {
        Reporter.getLogger().crash(thread, exception, description, info);
    }

    public static void image(Bitmap image) {
        Reporter.getLogger().image(image);
    }

    public static void image(Bitmap image, String description) {
        Reporter.getLogger().image(image, description);
    }

    public static void image(Bitmap image, Info info) {
        Reporter.getLogger().image(image, info);
    }

    public static void image(Bitmap image, String description, Info info) {
        Reporter.getLogger().image(image, description, info);
    }

    public static void t(String message) {
        Reporter.getLogger().t(message);
    }

    public static void t(Throwable throwable, String message) {
        Reporter.getLogger().t(throwable, message);
    }

    public static void t(String messageFormat, Object... args) {
        Reporter.getLogger().t(messageFormat, args);
    }

    public static void t(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().t(throwable, messageFormat, args);
    }

    public static void d(String message) {
        Reporter.getLogger().d(message);
    }

    public static void d(Throwable throwable, String message) {
        Reporter.getLogger().d(throwable, message);
    }

    public static void d(String messageFormat, Object... args) {
        Reporter.getLogger().d(messageFormat, args);
    }

    public static void d(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().d(throwable, messageFormat, args);
    }

    public static void i(String message) {
        Reporter.getLogger().i(message);
    }

    public static void i(Throwable throwable, String message) {
        Reporter.getLogger().i(throwable, message);
    }

    public static void i(String messageFormat, Object... args) {
        Reporter.getLogger().i(messageFormat, args);
    }

    public static void i(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().i(throwable, messageFormat, args);
    }

    public static void w(String message) {
        Reporter.getLogger().w(message);
    }

    public static void w(Throwable throwable, String message) {
        Reporter.getLogger().w(throwable, message);
    }

    public static void w(String messageFormat, Object... args) {
        Reporter.getLogger().w(messageFormat, args);
    }

    public static void w(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().w(throwable, messageFormat, args);
    }

    public static void e(String message) {
        Reporter.getLogger().e(message);
    }

    public static void e(Throwable throwable, String message) {
        Reporter.getLogger().e(throwable, message);
    }

    public static void e(String messageFormat, Object... args) {
        Reporter.getLogger().e(messageFormat, args);
    }

    public static void e(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().e(throwable, messageFormat, args);
    }

    public static void f(String message) {
        Reporter.getLogger().f(message);
    }

    public static void f(Throwable throwable, String message) {
        Reporter.getLogger().f(throwable, message);
    }

    public static void f(String messageFormat, Object... args) {
        Reporter.getLogger().f(messageFormat, args);
    }

    public static void f(Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().f(throwable, messageFormat, args);
    }

    public static void log(Logger.Level level, String message) {
        Reporter.getLogger().log(level, message);
    }

    public static void log(Logger.Level level, Throwable throwable, String message) {
        Reporter.getLogger().log(level, throwable, message);
    }

    public static void log(Logger.Level level, String messageFormat, Object... args) {
        Reporter.getLogger().log(level, messageFormat, args);
    }

    public static void log(Logger.Level level, Throwable throwable, String messageFormat, Object... args) {
        Reporter.getLogger().log(level, throwable, messageFormat, args);
    }

}
