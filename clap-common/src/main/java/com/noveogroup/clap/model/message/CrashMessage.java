package com.noveogroup.clap.model.message;


/**
 * @author Mikhail Demidov
 */
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", deviceInfo='").append(deviceInfo).append('\'');
        sb.append(", stackTraceInfo='").append(stackTraceInfo).append('\'');
        sb.append(", logCat='").append(logCat).append('\'');
        sb.append(", activityTraceLog='").append(activityTraceLog).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Class<? extends BaseMessage> getMessageType() {
        return CrashMessage.class;
    }
}
