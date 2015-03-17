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

package com.noveogroup.android.reporter.library.beans;

import com.noveogroup.android.reporter.library.system.ThreadInfo;
import com.noveogroup.android.reporter.library.system.Utils;

import java.util.List;
import java.util.Map;

public class CrashEvent extends Event {

    public static CrashEvent create(long timestamp, long uptime,
                                    String loggerName, String description,
                                    Thread thread, Throwable exception,
                                    Map<String, String> deviceInfo, List<ThreadInfo> threads) {
        CrashEvent event = new CrashEvent();
        event.setTimestamp(timestamp);
        event.setUptime(uptime);
        event.setLoggerName(loggerName);
        event.setDescription(description);
        event.setThreadId(thread.getId());
        event.setException(Utils.getStackTrace(exception));
        event.setDeviceInfo(deviceInfo);
        event.setThreads(threads);
        return event;
    }

    private String loggerName;
    private String description;
    private long threadId;
    private String exception;
    private Map<String, String> deviceInfo;
    private List<ThreadInfo> threads;

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Map<String, String> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(Map<String, String> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public List<ThreadInfo> getThreads() {
        return threads;
    }

    public void setThreads(List<ThreadInfo> threads) {
        this.threads = threads;
    }

}
