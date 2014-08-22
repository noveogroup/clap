package com.noveogroup.clap.library.api;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.noveogroup.clap.library.api.model.message.log.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class IntentSender {

    private final Context applicationContext;

    public IntentSender(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public void sendCrashMessage(String deviceInfo, String stackTraceInfo, String logCat, String activityTraceLog) {
        Intent intent = new Intent(applicationContext, ClapService.class)
                .putExtra(ClapService.EXTRA_METHOD, ClapService.METHOD_SEND_CRASH_MESSAGE)
                .putExtra(ClapService.EXTRA_DEVICE_INFO, deviceInfo)
                .putExtra(ClapService.EXTRA_STACK_TRACE_INFO, stackTraceInfo)
                .putExtra(ClapService.EXTRA_LOG_CAT, logCat)
                .putExtra(ClapService.EXTRA_ACTIVITY_TRACE_LOG, activityTraceLog);
        applicationContext.startService(intent);
    }

    public void sendLogsBunchMessage(List<LogEntry> logs) {
        Intent intent = new Intent(applicationContext, ClapService.class)
                .putExtra(ClapService.EXTRA_METHOD, ClapService.METHOD_SEND_LOGS_BUNCH_MESSAGE)
                .putExtra(ClapService.EXTRA_LOGS, new ArrayList<LogEntry>(logs));
        applicationContext.startService(intent);
    }

    public void sendTestScreenShot(Bitmap bitmap) {
        Intent intent = new Intent(applicationContext, ClapService.class)
                .putExtra(ClapService.EXTRA_METHOD, ClapService.METHOD_SEND_TEST_SCREEN_SHOT)
                .putExtra(ClapService.EXTRA_BITMAP, bitmap);
        applicationContext.startService(intent);
    }

    public void sendTraceScreenShot(Bitmap bitmap) {
        Intent intent = new Intent(applicationContext, ClapService.class)
                .putExtra(ClapService.EXTRA_METHOD, ClapService.METHOD_SEND_TRACE_SCREEN_SHOT)
                .putExtra(ClapService.EXTRA_BITMAP, bitmap);
        applicationContext.startService(intent);
    }

}
