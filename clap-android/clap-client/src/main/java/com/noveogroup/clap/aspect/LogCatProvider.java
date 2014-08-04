package com.noveogroup.clap.aspect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class LogCatProvider {

    private LogCatProvider() {
    }

    public static String getLogCat() {
        try {
            Log.i(ExceptionHandler.TAG, "get logcat ...");

            String baseCommand = "logcat -v time -t 1000";

            Process logReaderProcess = Runtime.getRuntime().exec(baseCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(logReaderProcess.getInputStream()));

            StringBuilder log = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                log.append(line).append("\n");
            }

            Log.i(ExceptionHandler.TAG, "done [get logcat]");
            return log.toString();
        } catch (Throwable throwable) {
            Log.i(ExceptionHandler.TAG, "fail [get logcat]", throwable);
            return "cannot get logcat:\n" + Log.getStackTraceString(throwable);
        }
    }

}
