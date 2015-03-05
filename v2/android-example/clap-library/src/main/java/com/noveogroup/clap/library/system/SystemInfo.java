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

package com.noveogroup.clap.library.system;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SystemInfo {

    private SystemInfo() {
        throw new UnsupportedOperationException();
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

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getSystemLanguage(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Resources resources = packageManager.getResourcesForApplication("android");
            return resources.getConfiguration().locale.getISO3Language();
        } catch (PackageManager.NameNotFoundException ignored) {
            return "";
        }
    }

    private static String getAndroidVersion() {
        return String.format("Android %s [API %d]", Build.VERSION.RELEASE, Build.VERSION.SDK_INT);
    }

    private static String getBatteryStatus(Context context) {
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

    public static Map<String, String> getDeviceInfo(Context context) {
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

        return Collections.unmodifiableMap(map);
    }

    public static String getStackTrace(Throwable throwable) {
        return Log.getStackTraceString(throwable);
    }

    private static List<StackTraceEntry> toStackTraceEntryList(StackTraceElement[] stackTrace) {
        ArrayList<StackTraceEntry> list = new ArrayList<>(stackTrace.length);
        for (StackTraceElement element : stackTrace) {
            StackTraceEntry entry = new StackTraceEntry();
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
            info.setState(entry.getKey().getState().toString());
            info.setStackTrace(toStackTraceEntryList(entry.getValue()));

            list.add(info);
        }

        return list;
    }

}
