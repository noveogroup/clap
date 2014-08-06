package com.noveogroup.clap.aspect.log;

import android.app.Activity;
import android.util.Log;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.noveogroup.clap.aspect.ActivityTraceLogger;
import com.noveogroup.clap.aspect.intent.IntentSender;
import com.noveogroup.clap.aspect.intent.LogsBunchIntentModel;
import com.noveogroup.clap.model.message.log.LogEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Andrey Sokolov
 */
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
            Log.e(TAG,"Log consumer thread interrupted",e);
        }
    }

    @Override
    public void stop() {
        super.stop();
        logsConsumer.setEnabled(false);
    }

}
