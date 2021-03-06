package com.noveogroup.clap.entity.message.log;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "LogEntry")
@Table(name = "logs")
public class LogEntryEntity extends BaseEntity {

    @Column(name = "message", length = COLUMN_LENGTH)
    private String message;
    @Column(name = "timestamp")
    private Long timestamp;
    @Column(name = "level")
    private int level;
    @Column(name = "loggerName")
    private String loggerName;
    @Column(name = "threadName")
    private String threadName;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(final String loggerName) {
        this.loggerName = loggerName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(final String threadName) {
        this.threadName = threadName;
    }
}
