package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.message.log.LogEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessage extends BaseMessage {
    private List<LogEntry> logEntries;

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(final List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("logEntries", logEntries)
                .toString();
    }
}
