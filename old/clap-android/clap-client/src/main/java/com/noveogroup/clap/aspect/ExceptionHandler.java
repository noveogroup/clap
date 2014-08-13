package com.noveogroup.clap.aspect;

import android.util.Log;
import com.noveogroup.clap.aspect.intent.CrashIntentModel;
import com.noveogroup.clap.aspect.intent.IntentSender;

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
                            if(sender.send()){
                                Log.i(ExceptionHandler.TAG, "done [send error report]");
                            } else {
                                Log.i(ExceptionHandler.TAG, "fail [send error report] - context, " +
                                        "intent model or created intent == null");
                            }
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
        final CrashIntentModel intentModel = new CrashIntentModel();
        intentSender.setIntentModel(intentModel);
        intentModel.setDeviceInfo(DeviceInfoProvider.getDeviceInfo());
        intentModel.setStackTraceInfo(Log.getStackTraceString(throwable));
        intentModel.setActivityLog(ActivityTraceLogger.getInstance().getLog());
        intentModel.setLogCat(LogCatProvider.getLogCat());
        intentSender.setContext(ActivityTraceLogger.getInstance().getLastContext());
        return intentSender;
    }

}