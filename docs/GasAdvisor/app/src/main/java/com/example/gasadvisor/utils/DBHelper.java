package com.example.gasadvisor.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE_USER = "create table user (_id integer primary key autoincrement, username text not null unique, " +
            "password text not null, name text not null, lastName text not null, email text not null unique);";
    private static final String DB_CREATE_DISTRIBUTORE = "create table distributore (_id integer primary key autoincrement," +
            " idImpianto integer not null unique, gestore text default null, bandiera text default null, tipoImpianto text default null, " +
            "nomeImpianto text default null, indirizzo text default null, comune text default null, provincia text default null, " +
            "latitudine real default null, longitudine real default null);";
    private static final String DB_CREATE_PREZZO ="create table prezzo (_id integer primary key autoincrement," +
            " descCarburante text default null, prezzo real not null, isSelf integer default null, " +
            "dtComu text default null, id_impianto integer not null unique, foreign key(id_impianto) references distributore(idImpianto));";
    private static final String DB_CREATE_PREFERITI="create table preferiti(_id integer primary key autoincrement, " +
            "user text not null, id_impianto integer not null);";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE_USER);
        sqLiteDatabase.execSQL(DB_CREATE_DISTRIBUTORE);
        sqLiteDatabase.execSQL(DB_CREATE_PREZZO);
        sqLiteDatabase.execSQL(DB_CREATE_PREFERITI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int nuovoVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS distributore");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS prezzo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS preferiti");
        onCreate(sqLiteDatabase);
    }
}
