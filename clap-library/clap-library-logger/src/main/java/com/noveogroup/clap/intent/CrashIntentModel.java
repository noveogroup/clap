package com.noveogroup.clap.intent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.ProjectInfo;

/**
 * @author Andrey Sokolov
 */
public class CrashIntentModel extends IntentModel {

    private String deviceInfo;
    private String stackTraceInfo;
    private String logCat;
    private String activityLog;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(final String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getStackTraceInfo() {
        return stackTraceInfo;
    }

    public void setStackTraceInfo(final String stackTraceInfo) {
        this.stackTraceInfo = stackTraceInfo;
    }

    public String getLogCat() {
        return logCat;
    }

    public void setLogCat(final String logCat) {
        this.logCat = logCat;
    }

    public String getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(final String activityLog) {
        this.activityLog = activityLog;
    }



    public Intent createIntent(Context context){
        final Intent intent = super.createIntent(context);
        if(intent != null){
            intent.putExtra("deviceInfo", deviceInfo);
            intent.putExtra("stackTraceInfo", stackTraceInfo);
            intent.putExtra("logCat", logCat);
            intent.putExtra("activityLog", activityLog);
        }
        return intent;
    }

    @Override
    protected String getModelType() {
        return "crash";
    }
}
