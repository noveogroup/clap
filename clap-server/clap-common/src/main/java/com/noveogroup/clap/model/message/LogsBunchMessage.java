package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.message.log.LogEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessage extends BaseMessage {
    private List<LogEntry> logs;
    private String logCat;

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(final List<LogEntry> logs) {
        this.logs = logs;
    }

    public String getLogCat() {
        return logCat;
    }

    public void setLogCat(final String logCat) {
        this.logCat = logCat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("logs", logs)
                .toString();
    }

    @Override
    public MessageType getType() {
        return MessageType.LOGS;
    }
}
