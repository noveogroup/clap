package com.noveogroup.clap.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.client.service.message.CrashMessageProcessor;
import com.noveogroup.clap.client.service.message.MessageProcessor;
import com.noveogroup.clap.client.service.message.ScreenshotMessageProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Demidov
 */
public class ClapService extends IntentService {


    public static final String TAG = "CLAP_SERVICE";

    private Map<String, MessageProcessor> processorMap = new HashMap<String, MessageProcessor>();

    public ClapService() {
        super("Clap service");
        processorMap.put("crash", new CrashMessageProcessor());
        processorMap.put("testScreenshot", new ScreenshotMessageProcessor());
        processorMap.put("traceScreenshot", new ScreenshotMessageProcessor());
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "SERVICE START");
        try {
            final String traceType = intent.getStringExtra("traceType");
            if (traceType != null) {
                final MessageProcessor messageProcessor = processorMap.get(traceType);
                if (messageProcessor != null) {
                    messageProcessor.processIntent(intent);
                } else {
                    Log.d(TAG, "unknown trace type: " + traceType);
                }
            } else {
                Log.d(TAG, "no trace type specified");
            }

        } catch (Throwable e) {
            Log.e(TAG, "Error while sending message to server " + e.getMessage(), e);
        }

    }
}
