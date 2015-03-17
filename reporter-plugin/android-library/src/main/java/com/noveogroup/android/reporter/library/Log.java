package com.noveogroup.android.reporter.library;

import android.graphics.Bitmap;

public final class Log {

    private Log() {
        throw new UnsupportedOperationException();
    }

    public static void crash(Throwable exception) {
        Reporter.getLogger().crash(exception);
    }

    public static void crash(Thread thread, Throwable exception) {
        Reporter.getLogger().crash(thread, exception);
    }

    public static void crash(String description, Throwable exception) {
        Reporter.getLogger().crash(description, exception);
    }

    public static void crash(String description, Thread thread, Throwable exception) {
        Reporter.getLogger().crash(description, thread, exception);
    }

    public static void screenshot(Bitmap screenshot) {
        Reporter.getLogger().screenshot(screenshot);
    }

    public static void screenshot(String description, Bitmap screenshot) {
        Reporter.getLogger().screenshot(description, screenshot);
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
