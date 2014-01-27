package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    private static final int COLUMN_LENGTH = 16777215;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(length = COLUMN_LENGTH)
    private String deviceInfo;
    @Column(length = COLUMN_LENGTH)
    private String stackTraceInfo;
    @Column(length = COLUMN_LENGTH)
    private String logCat;
    @Column(length = COLUMN_LENGTH)
    private String activityTraceLog;

    @ManyToOne(optional = false)
    private RevisionEntity revision;

    @ManyToOne(optional = false)
    private UserEntity uploadedBy;

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

    public UserEntity getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final UserEntity uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setRevision(final RevisionEntity revision) {
        this.revision = revision;
    }

    public RevisionEntity getRevision() {
        return revision;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("deviceInfo", deviceInfo)
                .append("stackTraceInfo", stackTraceInfo)
                .append("logCat", logCat)
                .append("activityTraceLog", activityTraceLog)
                .append("uploadedBy", uploadedBy)
                .toString();
    }
}
