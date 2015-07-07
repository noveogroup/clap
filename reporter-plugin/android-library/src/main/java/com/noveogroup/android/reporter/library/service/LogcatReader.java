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

import android.os.SystemClock;

import com.noveogroup.android.reporter.library.Reporter;
import com.noveogroup.android.reporter.library.events.LogcatEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LogcatReader {

    private static final Pattern STATUS_MESSAGE_PATTERN = Pattern.compile("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\w/\\Q" + Reporter.TAG_STATUS + "\\E");

    private static final String LOGCAT_COMMAND = "logcat -v time";
    private static final long RETRY_TIME = 10 * 1000;

    private final int maxSizeKb;
    private final long delay;

    private final List<String> logcat = new ArrayList<>();
    private final Thread readThread = new Thread() {
        private void readLogcat() {
            try {

                Process process = null;
                try {
                    process = Runtime.getRuntime().exec(LOGCAT_COMMAND);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    for (String line = ""; line != null; line = reader.readLine()) {
                        if (!STATUS_MESSAGE_PATTERN.matcher(line).find()) {
                            appendLogcat(line);
                        }
                    }
                } finally {
                    if (process != null) {
                        process.destroy();
                    }
                }

            } catch (Exception ignored) {
                // ignore exception, we will retry to read logcat later
            }
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    readLogcat();

                    Thread.sleep(RETRY_TIME);
                }
            } catch (InterruptedException ignored) {
                // exit when interrupted
            }
        }
    };
    private final Thread senderThread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    // wait for messages
                    synchronized (logcat) {
                        while (logcat.size() <= 0) {
                            logcat.wait();
                        }
                    }

                    // wait a bit more
                    Thread.sleep(delay);

                    // send logcat messages
                    sendLogcat();
                }
            } catch (InterruptedException ignored) {
                // exit when interrupted
            } finally {
                // send rest of messages
                sendLogcat();
            }
        }
    };

    public LogcatReader(int maxSizeKb, long delay) {
        this.maxSizeKb = maxSizeKb;
        this.delay = delay;
    }

    private void appendLogcat(String line) {
        synchronized (logcat) {
            logcat.add(line);
            logcat.notifyAll();
        }
    }

    private void sendLogcat() {
        synchronized (logcat) {
            while (logcat.size() > 0) {
                // get messages
                List<String> list = new ArrayList<>();
                for (int size = 0; size < maxSizeKb * 1024 && logcat.size() > 0; ) {
                    String line = logcat.remove(0);
                    list.add(line);
                    size += line.length();
                }

                // send messages
                Reporter.send(LogcatEvent.create(
                        System.currentTimeMillis(), SystemClock.uptimeMillis(), list));
            }
        }
    }

    public void start() {
        readThread.start();
        senderThread.start();
    }

    public void interrupt() {
        readThread.interrupt();
        senderThread.interrupt();
    }

}
