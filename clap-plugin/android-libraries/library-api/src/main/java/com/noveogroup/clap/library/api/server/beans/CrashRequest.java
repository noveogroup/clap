/*
 * Copyright (c) 2014 Noveo Group
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

package com.noveogroup.clap.library.api.server.beans;

import java.util.List;

public class CrashRequest extends BaseRequest<CrashRequest.CrashMessage> {

    public static class CrashMessage extends BaseRequest.BaseMessage {

        private long threadId;
        private String exception;
        private List<String> logCat;
        private List<ThreadInfo> threads;
        private List<LogEntry> logs;

        public CrashMessage() {
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

        public List<String> getLogCat() {
            return logCat;
        }

        public void setLogCat(List<String> logCat) {
            this.logCat = logCat;
        }

        public List<ThreadInfo> getThreads() {
            return threads;
        }

        public void setThreads(List<ThreadInfo> threads) {
            this.threads = threads;
        }

        public List<LogEntry> getLogs() {
            return logs;
        }

        public void setLogs(List<LogEntry> logs) {
            this.logs = logs;
        }

    }

    public CrashRequest() {
    }

    @Override
    public CrashMessage createMessage() {
        return new CrashMessage();
    }

}
