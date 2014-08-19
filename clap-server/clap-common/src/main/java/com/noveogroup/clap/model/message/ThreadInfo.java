package com.noveogroup.clap.model.message;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class ThreadInfo implements Serializable {
    private long id;
    private String name;
    private String state;
    private List<StackTraceEntry> stackTrace;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public List<StackTraceEntry> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(final List<StackTraceEntry> stackTrace) {
        this.stackTrace = stackTrace;
    }
}
