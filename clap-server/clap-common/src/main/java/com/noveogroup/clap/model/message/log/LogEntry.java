package com.noveogroup.clap.model.message.log;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class LogEntry implements Serializable{
    private long timestamp;
    private int level;
    private String loggerName;
    private String threadName;
    private String message;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public static enum Level {
        TRACE(0),
        DEBUG(1),
        INFO(2),
        WARN(3),
        ERROR(4),
        FATAL(5),;

        private final int level;

        Level(final int level) {
            this.level = level;
        }

        public int getInt(){
            return level;
        }

        public Level getByInt(final int level){
            for(Level l: Level.values()){
                if(level == l.getInt()){
                    return l;
                }
            }
            return null;
        }
    }
}
