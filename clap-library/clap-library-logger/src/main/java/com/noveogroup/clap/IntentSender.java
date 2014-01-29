package com.noveogroup.clap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class IntentSender {


    private String deviceInfo;
    private String stackTraceInfo;
    private String logCat;
    private String activityLog;
    private Context context;

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

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public void send() {
        Class<?> c = null;
        try {
            c = Class.forName("com.noveogroup.clap.RevisionImpl");
            ProjectInfo revision = (ProjectInfo) c.newInstance();
            Intent intent = new Intent("com.noveogroup.clap.SEND_MESSAGE");
            intent.putExtra("deviceInfo", deviceInfo);
            intent.putExtra("stackTraceInfo", stackTraceInfo);
            intent.putExtra("logCat", logCat);
            intent.putExtra("activityLog", activityLog);
            intent.putExtra("revision", revision.getRevisionId());
            intent.putExtra("project", revision.getProjectId());
            context.startService(intent);
        } catch (ClassNotFoundException e) {
            // TODO fix this
        } catch (InstantiationException e) {
            // TODO fix this
        } catch (IllegalAccessException e) {
            // TODO fix this
        } catch (Exception e) {
            // TODO fix this
        }


    }


}
