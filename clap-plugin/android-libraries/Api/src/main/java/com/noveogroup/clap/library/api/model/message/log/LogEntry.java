package com.noveogroup.clap.library.api.model.message.log;

import java.io.Serializable;

public class LogEntry implements Serializable {
    private String message;
    private Long timestamp;
    private int level;

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
}
