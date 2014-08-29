package com.noveogroup.clap.model.message;


import com.noveogroup.clap.model.message.log.LogEntry;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
public class CrashMessage extends BaseMessage implements Serializable {

    private long threadId;
    private String exception;
    private List<String> logCat;
    private List<ThreadInfo> threads;
    private List<LogEntry> logs;

    public String getException() {
        return exception;
    }

    public void setException(final String exception) {
        this.exception = exception;
    }

    public List<String> getLogCat() {
        return logCat;
    }

    public void setLogCat(final List<String> logCat) {
        this.logCat = logCat;
    }

    public List<ThreadInfo> getThreads() {
        return threads;
    }

    public void setThreads(final List<ThreadInfo> threads) {
        this.threads = threads;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(final List<LogEntry> logs) {
        this.logs = logs;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(final long threadId) {
        this.threadId = threadId;
    }

    @Override
    public MessageType type() {
        return MessageType.CRASH;
    }
}
