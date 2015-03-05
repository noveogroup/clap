package com.noveogroup.clap.library.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.google.gson.Gson;
import com.noveogroup.clap.library.reporter.Messages;

public class ClapDatabase {

    private ClapDatabase() {
        throw new UnsupportedOperationException();
    }

    private static final String DATABASE_NAME = "---clap-crash-spy.sqlite";
    private static final String TABLE_CRASH = "crash";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CRASH = "crash";
    private static final String SQL_CREATE_CRASH = "" +
            "CREATE TABLE " + TABLE_CRASH + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CRASH + " BLOB )";

    private static class CrashOpenHelper extends SQLiteOpenHelper {
        public CrashOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CRASH);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static SQLiteDatabase open(Context context) {
        return new CrashOpenHelper(context).getWritableDatabase();
    }

    public static void insertCrash(SQLiteDatabase database, Messages.Crash crash) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CRASH, new Gson().toJson(crash));
            database.insert(TABLE_CRASH, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public static synchronized Pair<Long, Messages.Crash> selectLastCrash(SQLiteDatabase database) {
        Cursor cursor = database.query(true, TABLE_CRASH, null, null, null, null, null, COLUMN_ID + " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                long id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String json = cursor.getString(cursor.getColumnIndex(COLUMN_CRASH));
                return new Pair<>(id, new Gson().fromJson(json, Messages.Crash.class));
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    public static synchronized void deleteCrashById(SQLiteDatabase database, Long id) {
        database.delete(TABLE_CRASH, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

}
