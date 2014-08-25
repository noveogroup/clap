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

package com.noveogroup.clap.module.crash_spy;

import android.util.Log;

import com.noveogroup.clap.library.api.server.beans.LogEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LogHelper {

    private LogHelper() {
        throw new UnsupportedOperationException();
    }

    private static final int MAX_LOG_SIZE = 1000;

    public static List<String> getLogCat() {
        try {
            String baseCommand = "logcat -v time -t " + MAX_LOG_SIZE;

            Process logReaderProcess = Runtime.getRuntime().exec(baseCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(logReaderProcess.getInputStream()));

            List<String> list = new ArrayList<String>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                list.add(line);
            }

            return list;
        } catch (Throwable throwable) {
            return Collections.singletonList(Log.getStackTraceString(throwable));
        }
    }

    private static final List<LogEntry> logs = new ArrayList<LogEntry>();

    public synchronized static void appendLogEntry(long timestamp, int level, String loggerName, String threadName, String message) {
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(timestamp);
        logEntry.setLevel(level);
        logEntry.setLoggerName(loggerName);
        logEntry.setThreadName(threadName);
        logEntry.setMessage(message);
        logs.add(logEntry);
        if (logs.size() > MAX_LOG_SIZE) {
            logs.remove(0);
        }
    }

    public synchronized static List<LogEntry> getLogs() {
        return new ArrayList<LogEntry>(logs);
    }

}
