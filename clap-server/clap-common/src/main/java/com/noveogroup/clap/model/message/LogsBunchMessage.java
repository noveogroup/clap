package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.message.log.LogEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessage extends BaseMessage implements Serializable {
    private List<LogEntry> logs;
    private List<String> logCat;

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(final List<LogEntry> logs) {
        this.logs = logs;
    }

    public List<String> getLogCat() {
        return logCat;
    }

    public void setLogCat(final List<String> logCat) {
        this.logCat = logCat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("logs", logs)
                .toString();
    }

    @Override
    public MessageType type() {
        return MessageType.LOGS;
    }
}
