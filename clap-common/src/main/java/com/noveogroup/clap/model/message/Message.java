package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.BaseModel;

import java.util.Date;

/**
 * @author Mikhail Demidov
 */
public class Message extends BaseModel {

    private Date timestamp;

    private String deviceInfo;
    private String stackTraceInfo;
    private String logCat;
    private String activityTraceLog;
    private String uploadedBy;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

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

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final String uploadedBy) {
        this.uploadedBy = uploadedBy;
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
}
