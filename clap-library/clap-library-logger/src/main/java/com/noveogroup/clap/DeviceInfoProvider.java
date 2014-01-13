package com.noveogroup.clap;

import android.os.Build;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

public final class DeviceInfoProvider {

    private DeviceInfoProvider() {
    }

    public static String getDeviceInfo() {
        try {
            Log.i(ExceptionHandler.TAG, "get device info ...");

            StringBuilder builder = new StringBuilder();

            builder.append("--- Device info ---\n");
            builder.append("Device manufacturer : ").append(Build.MANUFACTURER).append("\n");
            builder.append("Device model : ").append(Build.MODEL).append("\n");
            builder.append("Build fingerprint : ").append(Build.FINGERPRINT).append("\n");
            builder.append("Android OS version : ").append(Build.VERSION.RELEASE).append("\n");
            builder.append("API level : ").append(Build.VERSION.SDK).append("\n");
            builder.append("\n");

            builder.append("--- User settings ---\n");
            builder.append("Current time : ").append(new Date()).append("\n");
            builder.append("Default locale : ").append(Locale.getDefault()).append("\n");
            builder.append("\n");

            builder.append("--- Memory usage ---\n");
            builder.append("Free memory : ").append(Runtime.getRuntime().freeMemory() / 1024).append(" Kb\n");
            builder.append("Total memory : ").append(Runtime.getRuntime().totalMemory() / 1024).append(" Kb\n");
            builder.append("Max memory : ").append(Runtime.getRuntime().maxMemory() / 1024).append(" Kb\n");
            builder.append("\n");

            Log.i(ExceptionHandler.TAG, "done [get device info]");
            return builder.toString();
        } catch (Throwable throwable) {
            Log.i(ExceptionHandler.TAG, "fail [get device info]", throwable);
            return "cannot get device info:\n" + Log.getStackTraceString(throwable);
        }
    }

}
