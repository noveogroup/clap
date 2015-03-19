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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.noveogroup.android.reporter.library.events.Event;
import com.noveogroup.android.reporter.library.events.Message;
import com.noveogroup.android.reporter.library.system.Utils;

import java.util.ArrayList;
import java.util.List;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.noveogroup.android.reporter.library.sqlite";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MESSAGE = "message";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EVENT_DATA = "event_data";

    private static final String SQL_CREATE_MESSAGE =
            "CREATE TABLE " + TABLE_MESSAGE + " ( " +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_DATA + " BLOB NOT NULL )";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public void saveEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_DATA, Utils.encodeSerializable(event));
            db.insert(TABLE_MESSAGE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception ignored) {
            // ignore event if we cannot save it
        } finally {
            db.endTransaction();
        }
    }

    public List<Message<Event>> loadMessages(int maxSize) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();

            List<Message<Event>> messages = new ArrayList<>();

            Cursor cursor = db.query(TABLE_MESSAGE, null, null, null, null, null, COLUMN_ID + " ASC");
            try {
                long sumSize = 0;
                while (cursor.moveToNext()) {
                    Long id = null;
                    try {
                        id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_EVENT_DATA));

                        Message<Event> message = new Message<>();
                        message.setId(id);
                        message.setEvent((Event) Utils.decodeSerializable(bytes));
                        messages.add(message);

                        sumSize += bytes.length;
                        if (sumSize > maxSize) {
                            break;
                        }
                    } catch (Exception ignored) {
                        // delete message if we cannot load it
                        if (id != null) {
                            db.delete(TABLE_MESSAGE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                        }
                    }
                }
            } finally {
                cursor.close();
            }

            db.setTransactionSuccessful();

            return messages;
        } finally {
            db.endTransaction();
        }
    }

    public void deleteMessage(List<Message<?>> messages) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();

            for (Message<?> message : messages) {
                db.delete(TABLE_MESSAGE, COLUMN_ID + " = ?", new String[]{String.valueOf(message.getId())});
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
