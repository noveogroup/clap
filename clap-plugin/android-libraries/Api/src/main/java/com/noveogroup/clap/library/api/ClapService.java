package com.noveogroup.clap.library.api;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.noveogroup.clap.library.api.model.message.log.LogEntry;

import java.util.List;

public class ClapService extends IntentService {

    public static final String EXTRA_METHOD = "method";
    public static final String EXTRA_DEVICE_INFO = "deviceInfo";
    public static final String EXTRA_STACK_TRACE_INFO = "stackTraceInfo";
    public static final String EXTRA_LOG_CAT = "logCat";
    public static final String EXTRA_ACTIVITY_TRACE_LOG = "activityTraceLog";
    public static final String EXTRA_LOGS = "logs";
    public static final String EXTRA_BITMAP = "bitmap";

    public static final String METHOD_SEND_CRASH_MESSAGE = "sendCrashMessage";
    public static final String METHOD_SEND_LOGS_BUNCH_MESSAGE = "sendLogsBunchMessage";
    public static final String METHOD_SEND_TEST_SCREEN_SHOT = "sendTestScreenShot";
    public static final String METHOD_SEND_TRACE_SCREEN_SHOT = "sendTraceScreenShot";

    public ClapService() {
        super("CLAP");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onHandleIntent(Intent intent) {
        String method = intent.getStringExtra(EXTRA_METHOD);

        try {
            if (METHOD_SEND_CRASH_MESSAGE.equals(method)) {
                new ClapApi(this).saveCrashMessage(
                        intent.getStringExtra(EXTRA_DEVICE_INFO),
                        intent.getStringExtra(EXTRA_STACK_TRACE_INFO),
                        intent.getStringExtra(EXTRA_LOG_CAT),
                        intent.getStringExtra(EXTRA_ACTIVITY_TRACE_LOG));
            } else if (METHOD_SEND_LOGS_BUNCH_MESSAGE.equals(method)) {
                new ClapApi(this).saveLogsBunchMessage((List<LogEntry>) intent.getSerializableExtra(EXTRA_LOGS));
            } else if (METHOD_SEND_TEST_SCREEN_SHOT.equals(method)) {
                new ClapApi(this).saveScreenShot(intent.<Bitmap>getParcelableExtra(EXTRA_BITMAP));
            } else if (METHOD_SEND_TRACE_SCREEN_SHOT.equals(method)) {
                new ClapApi(this).saveScreenShot(intent.<Bitmap>getParcelableExtra(EXTRA_BITMAP));
            }
        } catch (Exception e) {
            Log.e(ClapService.class.getName(), "Error while sending message to server", e);
        }
    }

}
