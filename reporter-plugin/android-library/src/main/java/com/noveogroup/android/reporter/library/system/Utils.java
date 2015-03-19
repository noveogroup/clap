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

package com.noveogroup.android.reporter.library.system;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.noveogroup.android.reporter.library.Reporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {

    private static final String PACKAGE_NAME = Reporter.class.getPackage().getName();

    private static final class CallerResolver extends SecurityManager {
        public Class<?> getCaller() {
            Class[] classContext = getClassContext();
            // sometimes class context is null (usually on new Android devices)
            if (classContext == null || classContext.length <= 0) {
                return null; // if class context is null or empty
            }

            boolean packageFound = false;
            for (Class aClass : classContext) {
                if (!packageFound) {
                    if (aClass.getPackage().getName().startsWith(PACKAGE_NAME)) {
                        packageFound = true;
                    }
                } else {
                    if (!aClass.getPackage().getName().startsWith(PACKAGE_NAME)) {
                        return aClass;
                    }
                }
            }
            return classContext[classContext.length - 1];
        }
    }

    private static final CallerResolver CALLER_RESOLVER = new CallerResolver();

    private static StackTraceElement getCallerStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length <= 0) {
            return null; // if stack trace is null or empty
        }

        boolean packageFound = false;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!packageFound) {
                if (stackTraceElement.getClassName().startsWith(PACKAGE_NAME)) {
                    packageFound = true;
                }
            } else {
                if (!stackTraceElement.getClassName().startsWith(PACKAGE_NAME)) {
                    return stackTraceElement;
                }
            }
        }
        return stackTrace[stackTrace.length - 1];
    }

    /**
     * Returns a name of a class that calls logging methods.
     * <p/>
     * Can be much faster than {@link #getCaller()} because
     * this method tries to use {@link SecurityManager} to get
     * caller context.
     *
     * @return the caller's name.
     */
    public static String getCallerClassName() {
        Class<?> caller = CALLER_RESOLVER.getCaller();
        if (caller == null) {
            StackTraceElement callerStackTrace = getCallerStackTrace();
            return callerStackTrace == null ? null : callerStackTrace.getClassName();
        } else {
            return caller.getName();
        }
    }

    /**
     * Returns stack trace element corresponding to a class that calls
     * logging methods.
     * <p/>
     * This method compares names of the packages of stack trace elements
     * with the package of this library to find information about caller.
     *
     * @return the caller stack trace element.
     */
    public static StackTraceElement getCaller() {
        return getCallerStackTrace();
    }

    private static final String INFO_SYSTEM_LANGUAGE = "system-language";
    private static final String INFO_ANDROID_VERSION = "android-version";
    private static final String INFO_BUILD_FINGERPRINT = "build-fingerprint";
    private static final String INFO_DEVICE_NAME = "device-name";
    private static final String INFO_CPU_ABI = "cpu-abi";
    private static final String INFO_BATTERY_STATUS = "battery-status";
    private static final String INFO_ROOT_DISK_USAGE = "root-disk-usage";
    private static final String INFO_DATA_DISK_USAGE = "data-disk-usage";
    private static final String INFO_STORAGE_DISK_USAGE = "storage-disk-usage";
    private static final String INFO_RAM_USAGE = "ram-usage";

    public static String getApplicationId(Context context) {
        String packageName = context.getPackageName();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return String.format("%s[%s](%d)", packageName, info.versionName, info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return String.format("%s[?](?)", packageName);
        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getSystemLanguage(Context context) {
        if (context == null) {
            return Locale.getDefault().getISO3Language();
        } else {
            try {
                PackageManager packageManager = context.getPackageManager();
                Resources resources = packageManager.getResourcesForApplication("android");
                return resources.getConfiguration().locale.getISO3Language();
            } catch (PackageManager.NameNotFoundException ignored) {
                return "";
            }
        }
    }

    private static String getAndroidVersion() {
        return String.format("Android %s [API %d]", Build.VERSION.RELEASE, Build.VERSION.SDK_INT);
    }

    private static String getBatteryStatus(Context context) {
        if (context == null) {
            return "unknown";
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);

        if (batteryStatus == null) {
            return "unknown";
        } else {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = level * 100 / scale;

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    return String.format("%d%% ++", percent);
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    return String.format("%d%% --", percent);
                case BatteryManager.BATTERY_STATUS_FULL:
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                default:
                    return String.format("%d%%", percent);
            }
        }
    }

    private static String getDiskUsage(File root) {
        StatFs statFs = new StatFs(root.getAbsolutePath());
        long totalBytes = statFs.getBlockCount() * (long) statFs.getBlockSize();
        long freeBytes = statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        long busyBytes = totalBytes - freeBytes;
        double total = totalBytes / 1024.0 / 1024.0 / 1024.0;
        double busy = busyBytes / 1024.0 / 1024.0 / 1024.0;
        return String.format("%.3f/%.3f Gb", busy, total);
    }

    private static String getRamUsage() {
        Runtime runtime = Runtime.getRuntime();
        double busy = (runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024.0;
        double total = runtime.totalMemory() / 1024.0 / 1024.0;
        double max = runtime.maxMemory() / 1024.0 / 1024.0;
        return String.format("%.3f/%.3f/%.3f Mb", busy, total, max);
    }

    /**
     * Returns device info.
     *
     * @param context an Android context. Can be {@code null}.
     * @return device info map.
     */
    public static Info getDeviceInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();

        map.put(INFO_SYSTEM_LANGUAGE, getSystemLanguage(context));
        map.put(INFO_ANDROID_VERSION, getAndroidVersion());
        map.put(INFO_BUILD_FINGERPRINT, Build.FINGERPRINT);
        map.put(INFO_DEVICE_NAME, String.format("%s %s", Build.MANUFACTURER, Build.MODEL));
        map.put(INFO_CPU_ABI, Build.CPU_ABI);
        map.put(INFO_BATTERY_STATUS, getBatteryStatus(context));
        map.put(INFO_ROOT_DISK_USAGE, getDiskUsage(Environment.getRootDirectory()));
        map.put(INFO_DATA_DISK_USAGE, getDiskUsage(Environment.getDataDirectory()));
        map.put(INFO_STORAGE_DISK_USAGE, getDiskUsage(Environment.getExternalStorageDirectory()));
        map.put(INFO_RAM_USAGE, getRamUsage());

        return new Info(map);
    }

    public static String getStackTrace(Throwable throwable) {
        return Log.getStackTraceString(throwable);
    }

    private static List<StackTraceItem> toStackTraceEntryList(StackTraceElement[] stackTrace) {
        ArrayList<StackTraceItem> list = new ArrayList<>(stackTrace.length);
        for (StackTraceElement element : stackTrace) {
            StackTraceItem entry = new StackTraceItem();
            entry.setClassName(element.getClassName());
            entry.setMethodName(element.getMethodName());
            entry.setLineNumber(element.getLineNumber());

            list.add(entry);
        }
        return list;
    }

    public static List<ThreadInfo> getThreadsInfo() {
        ArrayList<ThreadInfo> list = new ArrayList<>();

        Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
            ThreadInfo info = new ThreadInfo();
            info.setId(entry.getKey().getId());
            info.setName(entry.getKey().getName());
            info.setState(entry.getKey().getState());
            info.setStackTrace(toStackTraceEntryList(entry.getValue()));

            list.add(info);
        }

        return list;
    }

    public static String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)) {
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } else {
            return null;
        }
    }

    public static byte[] encodeSerializable(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object decodeSerializable(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

}
