package com.noveogroup.clap.library.logs.log;

import android.app.Activity;
import android.util.Log;

import com.noveogroup.clap.library.api.IntentSender;
import com.noveogroup.clap.library.api.model.message.log.LogEntry;
import com.noveogroup.clap.library.logs.ActivityTraceLogger;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

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
        Activity lastContext = ActivityTraceLogger.getInstance().getLastContext();
        IntentSender sender = new IntentSender(lastContext);
        sender.sendLogsBunchMessage(logEntries);
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
