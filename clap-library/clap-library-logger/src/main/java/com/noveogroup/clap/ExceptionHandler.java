package com.noveogroup.clap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

//import javax.mail.MessagingException;

public class ExceptionHandler {


    public static final String TAG = "DEBUGGER";

    private static final Object lock = new Object();

    public static void replaceHandler() {
        synchronized (lock) {
            Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
            if (handler instanceof ExceptionHandler) {
                // do nothing
            } else {
                Log.i(ExceptionHandler.TAG, "default exception handler is replaced");
                Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(handler));
            }
        }
    }

    private static class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

        private final Thread.UncaughtExceptionHandler handler;


        public CustomExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            this.handler = handler;
        }

        @Override
        public void uncaughtException(Thread thread, final Throwable uncaughtException) {
            try {
                // log a message
                Log.e(ExceptionHandler.TAG, "uncaught exception", uncaughtException);

                // prepare error report
                final IntentSender sender;
                try {
                    Log.i(ExceptionHandler.TAG, "prepare error report ...");
                    sender = prepareSender(uncaughtException);
                    Log.i(ExceptionHandler.TAG, "done [prepare error report]");
                } catch (Throwable throwable) {
                    Log.i(ExceptionHandler.TAG, "fail [prepare error report]", throwable);
                    return;
                }

                // send report in separate thread
                Thread senderThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Log.i(ExceptionHandler.TAG, "send error report ...");
                            sender.send();
                            Log.i(ExceptionHandler.TAG, "done [send error report]");
                        } catch (Throwable throwable) {
                            Log.i(ExceptionHandler.TAG, "fail [send error report]", throwable);
                        }
                    }
                };
                senderThread.start();
                senderThread.join();
            } catch (Throwable throwable) {
                Log.e(ExceptionHandler.TAG, "unexpected error", throwable);
            } finally {
                // do standard handling
                handler.uncaughtException(thread, uncaughtException);
            }
        }

    }

    private static IntentSender prepareSender(Throwable throwable) {
        IntentSender intentSender = new IntentSender();
        intentSender.setDeviceInfo(DeviceInfoProvider.getDeviceInfo());
        intentSender.setStackTraceInfo(Log.getStackTraceString(throwable));
        intentSender.setActivityLog(ActivityTraceLogger.getInstance().getLog());
        intentSender.setLogCat(LogCatProvider.getLogCat());
        intentSender.setContext(ActivityTraceLogger.getInstance().getLastContext());
        return intentSender;
    }

}
