package com.noveogroup.clap.aspect.log;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.noveogroup.clap.aspect.ActivityTraceLogger;
import com.noveogroup.clap.aspect.intent.IntentSender;
import com.noveogroup.clap.aspect.intent.LogsBunchIntentModel;
import com.noveogroup.clap.client.database.LogDbOpenHelper;
import com.noveogroup.clap.model.message.log.LogEntry;

import java.util.ArrayList;

/**
 * @author Andrey Sokolov
 */
public class ClapLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public static final String TAG = "CLAP_LOGBACK_APPENDER";
    private static final int LOGS_THRESHOLD = 20;
    private LogDbOpenHelper dbOpenHelper;


    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {
        if (dbOpenHelper == null) {
            initDb();
        }
        if (dbOpenHelper != null) {
            final SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(LogDbOpenHelper.MESSAGE, iLoggingEvent.getMessage());
            values.put(LogDbOpenHelper.LEVEL, iLoggingEvent.getLevel().toInteger());
            database.insert(LogDbOpenHelper.TABLE_NAME, null, values);
            Log.v(TAG, "clap log written");
            Cursor mCount = null;
            int count = 0;
            try {
                mCount = database.rawQuery("select count(*) from " + LogDbOpenHelper.TABLE_NAME, null);
                mCount.moveToFirst();
                count = mCount.getInt(0);
                mCount.close();
            } finally {
                if (mCount != null) {
                    mCount.close();
                }
            }
            if (count >= LOGS_THRESHOLD) {
                sendLogs(database);
            }
            database.close();
        }
    }

    @Override
    public void start() {
        super.start();
        initDb();
    }

    protected synchronized void initDb() {
        final Activity lastContext = ActivityTraceLogger.getInstance().getLastContext();
        if (lastContext != null) {
            dbOpenHelper = new LogDbOpenHelper(lastContext.getApplicationContext());
            Log.d(TAG, "clap logs database initialized");
        } else {
            Log.e(TAG, "context == null");
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (dbOpenHelper != null) {
            final SQLiteDatabase writableDatabase = dbOpenHelper.getWritableDatabase();
            sendLogs(writableDatabase);
            writableDatabase.close();
            dbOpenHelper.close();
            Log.d(TAG, "clap logs database closed");
        }
    }

    private void sendLogs(final SQLiteDatabase database) {
        try {
            final Thread senderThread = new Thread() {
                @Override
                public void run() {
                    database.beginTransaction();
                    Cursor cursor = null;
                    try {
                        cursor = database.query(LogDbOpenHelper.TABLE_NAME, LogDbOpenHelper.COLUMNS, null, null, null, null, null);
                        if (cursor != null) {
                            final ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();
                            cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {
                                final LogEntry logEntry = new LogEntry();
                                logEntry.setMessage(cursor.getString(0));
                                logEntry.setTimestamp(cursor.getString(1));
                                logEntry.setLevel(cursor.getInt(2));
                                logEntries.add(logEntry);
                            }
                            if (executeSending(logEntries)) {
                                database.delete(LogDbOpenHelper.TABLE_NAME, null, null);
                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    database.endTransaction();
                }
            };
            senderThread.start();
            senderThread.join();
        } catch (Throwable e) {
            Log.i(TAG, "fail [send logs bunch]", e);
        }


    }

    private boolean executeSending(final ArrayList<LogEntry> logEntries) {
        final IntentSender sender = new IntentSender();
        sender.setContext(ActivityTraceLogger.getInstance().getLastContext());
        sender.setIntentModel(new LogsBunchIntentModel(logEntries));
        try {
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
