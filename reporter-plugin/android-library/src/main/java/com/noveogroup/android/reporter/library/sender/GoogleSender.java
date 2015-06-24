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

package com.noveogroup.android.reporter.library.sender;

import android.content.Context;
import android.os.Bundle;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.noveogroup.android.reporter.library.events.CrashEvent;
import com.noveogroup.android.reporter.library.events.Event;
import com.noveogroup.android.reporter.library.events.ImageEvent;
import com.noveogroup.android.reporter.library.events.InfoEvent;
import com.noveogroup.android.reporter.library.events.LogEvent;
import com.noveogroup.android.reporter.library.events.LogcatEvent;
import com.noveogroup.android.reporter.library.events.Message;

import java.net.URL;
import java.util.List;

public class GoogleSender implements Sender {

    private static final String META_SPREADSHEET = "com.noveogroup.android.reporter.SPREADSHEET";
    private static final String META_USERNAME = "com.noveogroup.android.reporter.USERNAME";
    private static final String META_PASSWORD = "com.noveogroup.android.reporter.PASSWORD";

    private static final String SHEET_LOGCAT = "logcat";
    private static final String COL_LOGCAT_APPLICATION_ID = "applicationId";
    private static final String COL_LOGCAT_DEVICE_ID = "deviceId";
    private static final String COL_LOGCAT_MESSAGE = "message";

    private SpreadsheetService spreadsheetService;
    private SpreadsheetEntry reportSpreadsheetEntry;
    private WorksheetEntry logcatWorksheetEntry;

    @Override
    public void init(Context context, Bundle meta) throws Exception {
        String metaSpreadsheet = meta.getString(META_SPREADSHEET);
        String metaUsername = meta.getString(META_USERNAME);
        String metaPassword = meta.getString(META_PASSWORD);

        // TODO service name should be configured
        spreadsheetService = new SpreadsheetService("clap-reporter");
        spreadsheetService.setUserCredentials(metaUsername, metaPassword);

        // todo попробовать может исправлю логин http://stackoverflow.com/questions/30483601/create-spreadsheet-using-google-spreadsheet-api-in-google-drive-in-java
        URL spreadsheetFeedURL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(spreadsheetFeedURL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheetEntries = spreadsheetFeed.getEntries();

        for (SpreadsheetEntry spreadsheetEntry : spreadsheetEntries) {
            if (spreadsheetEntry.getKey().equals(metaSpreadsheet)) {
                reportSpreadsheetEntry = spreadsheetEntry;
                break;
            }
        }
        // TODO reportSpreadsheetEntry can be null

        WorksheetFeed worksheetFeed = spreadsheetService.getFeed(reportSpreadsheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheetEntries = worksheetFeed.getEntries();
        for (WorksheetEntry worksheetEntry : worksheetEntries) {
            if (SHEET_LOGCAT.equals(worksheetEntry.getTitle().getPlainText())) {
                logcatWorksheetEntry = worksheetEntry;
            }
            // TODO there can be two or more "logcat" worksheets
        }

        // TODO logcatWorksheetEntry can be null

        // TODO logcat worksheet may have wrong header
    }

    @Override
    public void send(String applicationId, String deviceId, List<Message<?>> messages) throws Exception {
        for (Message<?> message : messages) {
            Event event = message.getEvent();
            if (event instanceof CrashEvent) {
                send(applicationId, deviceId, (CrashEvent) event);
            }
            if (event instanceof ImageEvent) {
                send(applicationId, deviceId, (ImageEvent) event);
            }
            if (event instanceof InfoEvent) {
                send(applicationId, deviceId, (InfoEvent) event);
            }
            if (event instanceof LogEvent) {
                send(applicationId, deviceId, (LogEvent) event);
            }
            if (event instanceof LogcatEvent) {
                send(applicationId, deviceId, (LogcatEvent) event);
            }
            // TODO throw an exception if event has unexpected type
        }
    }

    private void send(String applicationId, String deviceId, CrashEvent event) throws Exception {
        // TODO implement
    }

    private void send(String applicationId, String deviceId, ImageEvent event) throws Exception {
        // TODO implement
    }

    private void send(String applicationId, String deviceId, InfoEvent event) throws Exception {
        // TODO implement
    }

    private void send(String applicationId, String deviceId, LogEvent event) throws Exception {
        // TODO implement
    }

    private void send(String applicationId, String deviceId, LogcatEvent event) throws Exception {
        for (String message : event.getMessages()) {
            ListEntry listEntry = new ListEntry();
            listEntry.getCustomElements().setValueLocal(COL_LOGCAT_APPLICATION_ID, applicationId);
            listEntry.getCustomElements().setValueLocal(COL_LOGCAT_DEVICE_ID, deviceId);
            listEntry.getCustomElements().setValueLocal(COL_LOGCAT_MESSAGE, message);
            spreadsheetService.insert(logcatWorksheetEntry.getListFeedUrl(), listEntry);
        }
    }

/*
                    // todo test doc: https://docs.google.com/spreadsheets/d/1bts03f5dFL3cLuTiNsrTaH4dBebvXNGJ7-Dp5gw7E5Q
                    Log.i("XXX", "----- ----- ----- ----- ----- ----- ----- -----");

                    SpreadsheetService spreadsheetService = new SpreadsheetService("reporter");
                    spreadsheetService.setUserCredentials("noveo.test", "noveonsk");
                    URL spreadsheetFeedURL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

                    SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(spreadsheetFeedURL, SpreadsheetFeed.class);
                    List<SpreadsheetEntry> spreadsheetEntries = spreadsheetFeed.getEntries();

                    Log.i("XXX", "spreadsheetEntries: " + spreadsheetEntries.size());
                    for (SpreadsheetEntry spreadsheetEntry : spreadsheetEntries) {
                        Log.i("XXX", "----- ----- ----- -----");
                        Log.i("XXX", "id      : " + spreadsheetEntry.getId());
                        Log.i("XXX", "key     : " + spreadsheetEntry.getKey());
                        Log.i("XXX", "title   : " + spreadsheetEntry.getTitle().getPlainText());
                    }
                    Log.i("XXX", "----- ----- ----- -----");

                    SpreadsheetEntry spreadsheetEntry = spreadsheetEntries.get(0);
                    Log.i("XXX", "spreadsheetEntry.title = " + spreadsheetEntry.getTitle().getPlainText());

                    WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
                    List<WorksheetEntry> worksheetEntries = worksheetFeed.getEntries();
                    WorksheetEntry worksheetEntry = worksheetEntries.get(0);
                    Log.i("XXX", "worksheetEntry.title = " + worksheetEntry.getTitle().getPlainText());

                    // todo List-based feed [https://developers.google.com/google-apps/spreadsheets/data#work_with_list-based_feeds]
                    Log.i("XXX", "----- ----- ----- ----- ----- ----- ----- -----");

                    ListFeed listFeed = spreadsheetService.getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
                    for (ListEntry row : listFeed.getEntries()) {
                        StringBuilder builder = new StringBuilder();

                        // Print the first column's cell value
                        builder.append(row.getTitle().getPlainText());
                        // Iterate over the remaining columns, and print each cell value
                        for (String tag : row.getCustomElements().getTags()) {
                            builder.append("\t").append(tag).append(": ").append(row.getCustomElements().getValue(tag));
                        }

                        Log.i("XXX", builder.toString());
                    }

//                    ListEntry row = new ListEntry();
//                    row.getCustomElements().setValueLocal("_cn6ca", "Joe");
//                    row.getCustomElements().setValueLocal("_cokwr", "Smith");
//                    row.getCustomElements().setValueLocal("_cpzh4", "26");
//                    row = service.insert(listFeedUrl, row);

                    // todo Cell-based feed [https://developers.google.com/google-apps/spreadsheets/data#work_with_cell-based_feeds]
                    Log.i("XXX", "----- ----- ----- ----- ----- ----- ----- -----");

                    CellFeed cellFeed = spreadsheetService.getFeed(worksheetEntry.getCellFeedUrl(), CellFeed.class);
                    for (CellEntry cell : cellFeed.getEntries()) {
                        StringBuilder builder = new StringBuilder();

                        // Print the cell's address in A1 notation
                        builder.append("\t").append(cell.getTitle().getPlainText());
                        // Print the cell's address in R1C1 notation
                        builder.append("\t").append(cell.getId().substring(cell.getId().lastIndexOf('/') + 1));
                        // Print the cell's formula or text value
                        builder.append("\t").append(cell.getCell().getInputValue());
                        // Print the cell's calculated value if the cell's value is numeric
                        // Prints empty string if cell's value is not numeric
                        builder.append("\t").append(cell.getCell().getNumericValue());
                        // Print the cell's displayed value (useful if the cell has a formula)
                        builder.append("\t").append(cell.getCell().getValue());

                        Log.i("XXX", builder.toString());
                    }
 */
}
