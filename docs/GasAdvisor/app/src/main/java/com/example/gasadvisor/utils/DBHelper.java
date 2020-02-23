package com.example.gasadvisor.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE = "create table user (_id integer primary key autoincrement, username text not null unique, " +
            "password text not null, name text not null, lastName text not null, email text not null unique);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int nuovoVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(sqLiteDatabase);
    }
}
