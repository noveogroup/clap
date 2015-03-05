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

package com.noveogroup.clap.library.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Pair;

import com.noveogroup.clap.library.logger.LogEntry;
import com.noveogroup.clap.library.reporter.ClapReporter;
import com.noveogroup.clap.library.reporter.Messages;
import com.noveogroup.clap.library.reporter.server.ServerReporter;
import com.noveogroup.clap.library.system.LogUtils;

import java.util.List;

public class ClapService extends Service {

    private static final int RESTART_INTERVAL = 3 * 60 * 60 * 1000;
    private static final int SEND_INTERVAL = 5 * 60 * 1000;

    private static PendingIntent getPendingIntent(Context context) {
        Context applicationContext = context.getApplicationContext();
        Intent intent = new Intent(applicationContext, ClapService.class);
        return PendingIntent.getService(applicationContext, 0, intent, 0);
    }

    // todo integrate it
    private static void sendCrash(Context context, Thread thread, Throwable throwable) {
        List<LogEntry> logs = null; // todo get logs
        Messages.Crash crash = Messages.newCrash(context, thread, throwable, LogUtils.getLogCat(), logs);
        // todo ClapDatabase.insertCrash(database, crash);
        startService(context);
    }

    private static void startService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), RESTART_INTERVAL, pendingIntent);

        context.startService(new Intent(context, ClapService.class));
    }

    private static void stopService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(getPendingIntent(context));

        context.stopService(new Intent(context, ClapService.class));
    }

    private final Thread thread = new Thread() {
        @Override
        public void run() {
            SQLiteDatabase database = ClapDatabase.open(ClapService.this);
            // todo configure reporter
            ClapReporter reporter = new ServerReporter("http://clap.noveogroup.com/", true, "clap-xtime-android", "revisionHash", "variantHash", "random");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Pair<Long, Messages.Crash> pair = ClapDatabase.selectLastCrash(database);
                    if (pair != null) {
                        reporter.reportCrash(pair.second);
                        ClapDatabase.deleteCrashById(database, pair.first);
                    }
                } catch (Throwable ignored) {
                    // todo handle exception
                }
                try {
                    Thread.sleep(SEND_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
            }
            stopService(ClapService.this);
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
