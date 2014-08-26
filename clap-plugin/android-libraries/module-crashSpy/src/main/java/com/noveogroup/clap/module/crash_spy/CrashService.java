/*
 * Copyright (c) 2014 Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.clap.module.crash_spy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;

import com.google.gson.Gson;
import com.noveogroup.clap.library.api.ClapApi;
import com.noveogroup.clap.library.api.server.beans.CrashRequest;
import com.noveogroup.clap.library.api.utils.SystemUtils;

public class CrashService extends Service {

    private static final String DATABASE_NAME = "---clap-crash-spy.sqlite";
    private static final String TABLE_CRASH = "crash";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CRASH = "crash";
    private static final String SQL_CREATE_CRASH = "" +
            "CREATE TABLE " + TABLE_CRASH + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CRASH + " BLOB )";

    private static class CrashOpenHelper extends SQLiteOpenHelper {
        public CrashOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CRASH);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private static volatile SQLiteDatabase database = null;

    public static synchronized void saveCrash(Context context, Thread thread, Throwable uncaughtException) {
        CrashRequest.CrashMessage message = new CrashRequest.CrashMessage();
        message.setTimestamp(System.currentTimeMillis());
        message.setDeviceId(SystemUtils.getDeviceId(context));
        message.setDeviceInfo(SystemUtils.getDeviceInfo(context));
        message.setThreadId(thread.getId());
        message.setException(SystemUtils.getStackTrace(uncaughtException));
        message.setLogCat(LogHelper.getLogCat());
        message.setLogs(LogHelper.getLogs());
        message.setThreads(SystemUtils.getThreadsInfo());

        if (database == null) {
            database = new CrashOpenHelper(context).getWritableDatabase();
        }

        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CRASH, new Gson().toJson(message));
            database.insert(TABLE_CRASH, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        CrashService.startService(context);
    }

    private static synchronized void sendCrash(Context context, ClapApi clapApi) {
        if (database == null) {
            database = new CrashOpenHelper(context).getWritableDatabase();
        }

        database.beginTransaction();
        try {
            Cursor cursor = database.query(true, TABLE_CRASH, null, null, null, null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    long id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String json = cursor.getString(cursor.getColumnIndex(COLUMN_CRASH));
                    CrashRequest.CrashMessage message = new Gson().fromJson(json, CrashRequest.CrashMessage.class);

                    String token = clapApi.retrieveToken();
                    CrashRequest request = clapApi.prepare(new CrashRequest(), token, message);
                    clapApi.sendCrash(request);

                    database.delete(null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                } else {
                    stopService(context);
                }
            } finally {
                cursor.close();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private static final int RESTART_INTERVAL = 3 * 60 * 60 * 1000;
    private static final int SEND_INTERVAL = 5 * 60 * 1000;

    private static PendingIntent getPendingIntent(Context context) {
        Context applicationContext = context.getApplicationContext();
        Intent intent = new Intent(applicationContext, CrashService.class);
        return PendingIntent.getService(applicationContext, 0, intent, 0);
    }

    public static void startService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), RESTART_INTERVAL, pendingIntent);

        context.startService(new Intent(context, CrashService.class));
    }

    public static void stopService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(getPendingIntent(context));

        context.stopService(new Intent(context, CrashService.class));
    }

    private final Thread thread = new Thread() {
        @Override
        public void run() {
            ClapApi clapApi = new ClapApi(CrashService.this);
            while (!isInterrupted()) {
                sendCrash(CrashService.this, clapApi);
                try {
                    Thread.sleep(SEND_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
            }
            stopService(CrashService.this);
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

}
