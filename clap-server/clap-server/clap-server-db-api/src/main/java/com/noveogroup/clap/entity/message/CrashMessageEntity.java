package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.message.log.LogEntryEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "CrashMessageEntity")
@DiscriminatorValue("crash")
public class CrashMessageEntity extends BaseMessageEntity {

    @Column(name = "thread_id")
    private long threadId;
    @Column(name = "exception")
    private String exception;
    @Column(name = "log_cat", length = COLUMN_LENGTH)
    private String logCat;
    @Column(name = "thread_info_json", length = COLUMN_LENGTH)
    private String threadsInfoJSON;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<LogEntryEntity> logs;

    /**
     * Constructor
     */
    public CrashMessageEntity() {
    }

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return CrashMessageEntity.class;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(final long threadId) {
        this.threadId = threadId;
    }

    public String getException() {
        return exception;
    }

    public void setException(final String exception) {
        this.exception = exception;
    }

    public String getLogCat() {
        return logCat;
    }

    public void setLogCat(final String logCat) {
        this.logCat = logCat;
    }

    public String getThreadsInfoJSON() {
        return threadsInfoJSON;
    }

    public void setThreadsInfoJSON(final String threadsInfoJSON) {
        this.threadsInfoJSON = threadsInfoJSON;
    }

    public List<LogEntryEntity> getLogs() {
        return logs;
    }

    public void setLogs(final List<LogEntryEntity> logs) {
        this.logs = logs;
    }
}
