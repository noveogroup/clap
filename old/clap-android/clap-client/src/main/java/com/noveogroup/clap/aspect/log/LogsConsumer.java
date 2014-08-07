package com.noveogroup.clap.aspect.log;

import android.app.Activity;
import android.util.Log;
import com.noveogroup.clap.aspect.ActivityTraceLogger;
import com.noveogroup.clap.aspect.intent.IntentSender;
import com.noveogroup.clap.aspect.intent.LogsBunchIntentModel;
import com.noveogroup.clap.model.message.log.LogEntry;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * @author Andrey Sokolov
 */
public class LogsConsumer implements Runnable {

    private static final int LOGS_THRESHOLD = 20;
    private static final String TAG = "CLAP_LOG_CONSUMER";

    private final ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();

    private final BlockingQueue<LogEntry> queue;

    private boolean enabled = true;

    public LogsConsumer(final BlockingQueue<LogEntry> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        while (enabled) {
            try {
                LogEntry logEntry = queue.take();
                logEntries.add(logEntry);
                if (logEntries.size() >= LOGS_THRESHOLD) {
                    sendLogs();
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Log producer thread interrupted", e);
            }
        }
        sendLogs();
    }


    private void sendLogs() {
        if (executeSending(logEntries)) {
            logEntries.clear();
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
