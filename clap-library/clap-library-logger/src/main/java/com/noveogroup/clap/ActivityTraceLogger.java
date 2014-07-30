package com.noveogroup.clap;

import android.app.Activity;

import java.util.LinkedList;

public class ActivityTraceLogger {

    private static final ActivityTraceLogger instance = new ActivityTraceLogger(1000);

    public static ActivityTraceLogger getInstance() {
        return instance;
    }

    private static class Message {

        private String className;
        private String hashCode;
        private String methodName;

        private Message() {
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getHashCode() {
            return hashCode;
        }

        public void setHashCode(String hashCode) {
            this.hashCode = hashCode;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

    }

    private static Message createMessage(String className, String hashCode, String methodName) {
        Message message = new Message();
        message.setClassName(className);
        message.setHashCode(hashCode);
        message.setMethodName(methodName);
        return message;
    }

    private final Object lock = new Object();
    private final LinkedList<Message> messages = new LinkedList<Message>();
    private final int maxMessagesCount;

    private Activity lastContext;

    public ActivityTraceLogger(int maxMessagesCount) {
        this.maxMessagesCount = maxMessagesCount;
    }

    public void logMessage(Activity activity, String methodName) {
        synchronized (lock) {
            Message message = createMessage(
                    activity.getClass().getCanonicalName(),
                    String.format("@%d", activity.hashCode()),
                    methodName);
            messages.addFirst(message);
            if (messages.size() > maxMessagesCount) {
                messages.removeLast();
            }
            lastContext = activity;
        }
    }

    public String getLog() {
        StringBuilder builder = new StringBuilder();
        synchronized (lock) {
            for (Message message : messages) {
                builder.append(String.format("%s %s - %s\n",
                        message.getClassName(), message.getHashCode(), message.getMethodName()));
            }
        }
        return builder.toString();
    }

    public Activity getLastContext() {
        return lastContext;
    }

    public void setLastContext(final Activity lastContext) {
        this.lastContext = lastContext;
    }
}
