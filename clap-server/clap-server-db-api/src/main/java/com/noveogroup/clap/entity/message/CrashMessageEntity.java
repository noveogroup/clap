package com.noveogroup.clap.entity.message;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "CrashMessageEntity")
@Table(name = "crashMessages")
public class CrashMessageEntity extends BaseMessageEntity {

    @Column(name = "device_info", length = COLUMN_LENGTH)
    private String deviceInfo;
    @Column(name = "stack_trace", length = COLUMN_LENGTH)
    private String stackTraceInfo;
    @Column(name = "log", length = COLUMN_LENGTH)
    private String logCat;
    @Column(name = "activity_trace", length = COLUMN_LENGTH)
    private String activityTraceLog;

    /**
     * Constructor
     */
    public CrashMessageEntity() {
    }

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return BaseMessageEntity.class;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(final String message) {
        this.deviceInfo = message;
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
        return new ToStringBuilder(this)
                .append("deviceInfo", deviceInfo)
                .append("stackTraceInfo", stackTraceInfo)
                .append("logCat", logCat)
                .append("activityTraceLog", activityTraceLog)
                .toString();
    }
}
