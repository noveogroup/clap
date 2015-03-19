package com.noveogroup.android.reporter.library.service;

import com.noveogroup.android.reporter.library.events.Message;
import com.noveogroup.android.reporter.library.sender.Sender;

import java.util.List;

public class SenderRunnable implements Runnable {

    private final OpenHelper openHelper;
    private final Sender sender;

    private final String applicationId;
    private final String deviceId;

    private final int maxSizeKb;
    private final long delay;

    public SenderRunnable(OpenHelper openHelper, Sender sender,
                          String applicationId, String deviceId,
                          int maxSizeKb, long delay) {
        this.openHelper = openHelper;
        this.sender = sender;
        this.applicationId = applicationId;
        this.deviceId = deviceId;
        this.maxSizeKb = maxSizeKb;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (true) {
                try {
                    List<Message<?>> messages = openHelper.loadMessages(maxSizeKb * 1024);
                    if (messages.size() <= 0) {
                        break;
                    } else {
                        sender.send(applicationId, deviceId, messages);
                        openHelper.deleteMessage(messages);
                    }
                } catch (Exception ignored) {
                    // try to send later, if we cannot send them now
                    break;
                }
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }

}
