package com.noveogroup.clap.library.logs.log;

import android.util.Log;

import com.noveogroup.clap.library.api.model.message.log.LogEntry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class ClapLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final String TAG = "CLAP_LOGBACK_APPENDER";

    private static final int QUEUE_CAPACITY = 2000;
    private BlockingQueue<LogEntry> queue = new ArrayBlockingQueue<LogEntry>(QUEUE_CAPACITY);

    private final LogsConsumer logsConsumer = new LogsConsumer(queue);
    private final Thread consumerThread = new Thread(logsConsumer);

    @Override
    public void start() {
        super.start();
        consumerThread.start();
    }

    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {
        final LogEntry logEntry = new LogEntry();
        logEntry.setLevel(iLoggingEvent.getLevel().toInt());
        logEntry.setMessage(iLoggingEvent.getMessage());
        logEntry.setTimestamp(iLoggingEvent.getTimeStamp());
        try {
            queue.put(logEntry);
        } catch (InterruptedException e) {
            Log.e(TAG, "Log consumer thread interrupted", e);
        }
    }

    @Override
    public void stop() {
        super.stop();
        logsConsumer.setEnabled(false);
    }

}
