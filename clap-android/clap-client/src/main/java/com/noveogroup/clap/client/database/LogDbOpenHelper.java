package com.noveogroup.clap.client.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Andrey Sokolov
 */
public class LogDbOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CLAP_LOGS";

    public static final String TABLE_NAME = "logs";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";
    public static final String LEVEL = "level";
    public static final String[] COLUMNS = new String[]{
            MESSAGE,TIMESTAMP,LEVEL
    };

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
            + MESSAGE + " TEXT, " + TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+LEVEL+" integer)";

    public LogDbOpenHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

}
