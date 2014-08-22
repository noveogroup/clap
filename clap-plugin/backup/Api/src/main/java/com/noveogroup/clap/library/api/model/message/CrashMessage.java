package com.noveogroup.clap.library.api.model.message;

public class CrashMessage extends BaseMessage {
    private String deviceInfo;
    private String stackTraceInfo;
    private String logCat;
    private String activityTraceLog;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(final String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getStackTraceInfo() {
        return stackTraceInfo;
    }

    public void setStackTraceInfo(final String stackTraceInfo) {
        this.stackTraceInfo = stackTraceInfo;
    }

    public String getLogCat() {
        return logCat;
    }

    public void setLogCat(final String logCat) {
        this.logCat = logCat;
    }

    public String getActivityTraceLog() {
        return activityTraceLog;
    }

    public void setActivityTraceLog(final String activityTraceLog) {
        this.activityTraceLog = activityTraceLog;
    }
}
