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

public class LogcatReader {

    private static final String LOGCAT_COMMAND = "logcat -v time";
    private static final long RETRY_TIME = 10 * 1000;

    private final int maxSize;
    private final long delay;

    private final List<String> logcat = new ArrayList<>();
    private final Thread readThread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    try {

                        Process process = null;
                        try {
                            process = Runtime.getRuntime().exec(LOGCAT_COMMAND);

                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            for (String line = ""; line != null; line = reader.readLine()) {
                                appendLine(line);
                            }
                        } finally {
                            if (process != null) {
                                process.destroy();
                            }
                        }

                    } catch (Exception ignored) {
                        // ignore exception, we will retry to read logcat later
                    }

                    // wait before retry reading
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
                long lastSendTime = 0;
                while (!isInterrupted()) {
                    synchronized (logcat) {
                        while (logcat.size() < maxSize) {
                            long remainingDelay = delay - (SystemClock.uptimeMillis() - lastSendTime);
                            if (remainingDelay > 0) {
                                logcat.wait(remainingDelay);
                            } else {
                                break;
                            }
                        }

                        if (logcat.size() > 0) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < maxSize; i++) {
                                if (logcat.size() <= 0) {
                                    break;
                                } else {
                                    list.add(logcat.remove(0));
                                }
                            }
                            send(list);
                            lastSendTime = SystemClock.uptimeMillis();
                        }
                    }
                }
            } catch (InterruptedException ignored) {
                // exit when interrupted
            } finally {
                // send rest of messages
                synchronized (logcat) {
                    if (logcat.size() > 0) {
                        send(new ArrayList<>(logcat));
                        logcat.clear();
                    }
                }
            }
        }
    };

    public LogcatReader(int maxSize, long delay) {
        this.maxSize = maxSize;
        this.delay = delay;
    }

    private void appendLine(String line) {
        synchronized (logcat) {
            logcat.add(line);
            if (logcat.size() >= maxSize) {
                logcat.notifyAll();
            }
        }
    }

    private void send(List<String> lines) {
        Reporter.send(LogcatEvent.create(
                System.currentTimeMillis(), SystemClock.uptimeMillis(), lines));
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
