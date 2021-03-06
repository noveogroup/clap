package com.noveogroup.clap.library.api.model.message;

import com.noveogroup.clap.library.api.model.message.log.LogEntry;

import java.util.List;

public class LogsBunchMessage extends BaseMessage {
    private List<LogEntry> logEntries;

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(final List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }
}
