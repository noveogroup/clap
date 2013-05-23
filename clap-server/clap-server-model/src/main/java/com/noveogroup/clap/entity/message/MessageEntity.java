package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: admin2
 * Date: 5/11/13
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {


    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(length = 16777215)
    private String deviceInfo;
    @Column(length = 16777215)
    private String stackTraceInfo;
    @Column(length = 16777215)
    private String logCat;
    @Column(length = 16777215)
    private String activityTraceLog;

    /**
     * Constructor
     */
    public MessageEntity() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
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
}
