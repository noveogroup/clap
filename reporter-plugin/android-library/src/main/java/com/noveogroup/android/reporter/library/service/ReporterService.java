/*
 * Copyright (c) 2015 Noveo
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

package com.noveogroup.android.reporter.library.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.noveogroup.android.reporter.library.events.Event;
import com.noveogroup.android.reporter.library.system.Utils;

public class ReporterService extends Service {

    public static class Starter extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context, ReporterService.class));
        }

    }

    private static final long STARTER_INTERVAL = 60 * 1000;

    public static void initStarter(Context context) {
        // create intents
        Intent intent = new Intent(context, ReporterService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);

        // start service immediately
        context.startService(intent);

        // register reporting alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + STARTER_INTERVAL, STARTER_INTERVAL, pendingIntent);
    }

    private static final String ACTION_SEND = "com.noveogroup.android.reporter.library.ACTION_SEND";
    private static final String EXTRA_EVENT = "com.noveogroup.android.reporter.library.EXTRA_EVENT";

    public static void send(Context context, Event event) {
        Intent intent = new Intent(context, ReporterService.class)
                .setAction(ACTION_SEND)
                .putExtra(EXTRA_EVENT, event);
        context.startService(intent);
    }

    private static final long INFO_DELAY = 60 * 1000;
    private static final int LOGCAT_MAX_SIZE_KB = 4;
    private static final long LOGCAT_DELAY = 10 * 1000;
    private static final int SENDER_MAX_SIZE_KB = 4;
    private static final long SENDER_DELAY = 10 * 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private OpenHelper openHelper;
    private final InfoSender infoSender = new InfoSender(INFO_DELAY);
    private final LogcatReader logcatReader = new LogcatReader(LOGCAT_MAX_SIZE_KB, LOGCAT_DELAY);
    private Thread senderThread;

    @Override
    public void onCreate() {
        super.onCreate();

        openHelper = new OpenHelper(this);

        senderThread = new Thread(new SenderRunnable(this, openHelper,
                Utils.getApplicationId(this), Utils.getDeviceId(this),
                SENDER_MAX_SIZE_KB, SENDER_DELAY));
        senderThread.start();
        infoSender.start();
        logcatReader.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_SEND.equals(intent.getAction())) {
            Event event = (Event) intent.getSerializableExtra(EXTRA_EVENT);
            openHelper.saveEvent(event);
            return START_NOT_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        senderThread.interrupt();
        infoSender.interrupt();
        logcatReader.interrupt();
    }

}
