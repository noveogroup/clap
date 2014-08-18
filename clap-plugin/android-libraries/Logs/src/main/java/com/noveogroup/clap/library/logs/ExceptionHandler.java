package com.noveogroup.clap.library.logs;

import android.util.Log;

import com.noveogroup.clap.library.api.IntentSender;

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

                // send report in separate thread
                Thread senderThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Log.i(ExceptionHandler.TAG, "send error report ...");
                            if (send(uncaughtException)) {
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

    private static boolean send(Throwable throwable) {
        new IntentSender(ActivityTraceLogger.getInstance().getLastContext())
                .sendCrashMessage(
                        DeviceInfoProvider.getDeviceInfo(),
                        Log.getStackTraceString(throwable),
                        ActivityTraceLogger.getInstance().getLog(),
                        LogCatProvider.getLogCat());
        return true;
    }

}
