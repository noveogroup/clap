/*
 * Copyright (c) 2015 Noveo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    private void sendMessages() {
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
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                sendMessages();

                Thread.sleep(delay);
            }
        } catch (InterruptedException ignored) {
            // exit when interrupted
        }
    }

}
