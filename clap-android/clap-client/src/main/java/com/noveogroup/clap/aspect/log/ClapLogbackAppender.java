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

/**
 * @author Andrey Sokolov
 */
public class ClapLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public static final String TAG = "CLAP_LOGBACK_APPENDER";
    private static final int LOGS_THRESHOLD = 20;

    private ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();


    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {
        final LogEntry logEntry = new LogEntry();
        logEntry.setLevel(iLoggingEvent.getLevel().toInt());
        logEntry.setMessage(iLoggingEvent.getMessage());
        logEntry.setTimestamp(iLoggingEvent.getTimeStamp());
        logEntries.add(logEntry);
        int count = logEntries.size();
        if (count >= LOGS_THRESHOLD) {
            sendLogs();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private void sendLogs() {
        try {
            final Thread senderThread = new Thread() {
                @Override
                public void run() {
                    if (executeSending(logEntries)) {
                        logEntries.clear();
                    }
                }
            };
            senderThread.start();
        } catch (Throwable e) {
            Log.i(TAG, "fail [send logs bunch]", e);
        }
    }

    private boolean executeSending(final ArrayList<LogEntry> logEntries) {
        final IntentSender sender = new IntentSender();
        final Activity lastContext = ActivityTraceLogger.getInstance().getLastContext();
        sender.setContext(lastContext);
        sender.setIntentModel(new LogsBunchIntentModel(new ArrayList<LogEntry>(logEntries)));
        try {
            Log.i(TAG, "lastContext == null :" + (lastContext == null));
            Log.i(TAG, "send logs bunch ...");
            if (sender.send()) {
                Log.i(TAG, "done [send logs bunch]");
                return true;
            } else {
                Log.i(TAG, "fail [send logs bunch] - context, " +
                        "intent model or created intent == null");
            }
        } catch (Throwable throwable) {
            Log.i(TAG, "fail [send logs bunch]", throwable);
        }
        return false;
    }
}
