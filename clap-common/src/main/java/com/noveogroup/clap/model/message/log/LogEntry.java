package com.noveogroup.clap.model.message.log;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class LogEntry implements Serializable{
    private String message;
    private String timestamp;
    private int level;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }
}
