package com.noveogroup.clap.library.reporter;

import android.content.Context;

import com.noveogroup.clap.library.logger.LogEntry;
import com.noveogroup.clap.library.system.SystemInfo;
import com.noveogroup.clap.library.system.ThreadInfo;

import java.util.List;
import java.util.Map;

public final class Messages {

    private Messages() {
        throw new UnsupportedOperationException();
    }

    public static abstract class Base {

        private String deviceId;
        private long timestamp;
        private Map<String, String> deviceInfo;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, String> getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(Map<String, String> deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

    }

    public static class Info extends Base {
    }

    public static class Crash extends Base {

        private long threadId;
        private String exception;
        private List<String> logCat;
        private List<ThreadInfo> threads;
        private List<LogEntry> logs;

        public long getThreadId() {
            return threadId;
        }

        public void setThreadId(long threadId) {
            this.threadId = threadId;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public List<String> getLogCat() {
            return logCat;
        }

        public void setLogCat(List<String> logCat) {
            this.logCat = logCat;
        }

        public List<ThreadInfo> getThreads() {
            return threads;
        }

        public void setThreads(List<ThreadInfo> threads) {
            this.threads = threads;
        }

        public List<LogEntry> getLogs() {
            return logs;
        }

        public void setLogs(List<LogEntry> logs) {
            this.logs = logs;
        }

    }

    private static <M extends Base> M prepareBase(M message, Context context) {
        message.setDeviceId(SystemInfo.getDeviceId(context));
        message.setTimestamp(System.currentTimeMillis());
        message.setDeviceInfo(SystemInfo.getDeviceInfo(context));
        return message;
    }

    public static Info newInfo(Context context) {
        return prepareBase(new Info(), context);
    }

    public static Crash newCrash(Context context, Thread thread, Throwable throwable, List<String> logCat, List<LogEntry> logs) {
        Crash crash = prepareBase(new Crash(), context);
        crash.setThreadId(thread.getId());
        crash.setException(SystemInfo.getStackTrace(throwable));
        crash.setLogCat(logCat);
        crash.setThreads(SystemInfo.getThreadsInfo());
        crash.setLogs(logs);
        return crash;
    }

}
