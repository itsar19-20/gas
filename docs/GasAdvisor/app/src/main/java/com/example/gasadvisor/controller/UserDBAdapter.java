package com.example.gasadvisor.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gasadvisor.utils.DBHelper;

public class UserDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public static final String DB_TABLE = "user";
    public static final String KEY_ID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";

    public UserDBAdapter(Context context) {
        this.context = context;
    }

    public UserDBAdapter open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        db.close();
    }

    private ContentValues createContentValues(String username, String password, String email,
                                              String name, String lastName) {
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_EMAIL, email);
        values.put(KEY_NAME, name);
        values.put(KEY_LASTNAME, lastName);
        return values;
    }

    public long addUser(String username, String password, String email,
                        String name, String lastName) {
        ContentValues values = createContentValues(username, password, email, name, lastName);
        return db.insertOrThrow(DB_TABLE, null, values);
    }

    public boolean updateUser(Integer id, String username, String password, String email,
                              String name, String lastName) {
        ContentValues values = createContentValues(username, password, email, name, lastName);
        return db.update(DB_TABLE, values, KEY_ID + "=" + id, null) > 0;
    }

    public boolean updateUserByUsername(String username, String password, String email,
                              String name, String lastName) {
        ContentValues values = createContentValues(username, password, email, name, lastName);
        return db.update(DB_TABLE, values, KEY_ID + "=" + username, null) > 0;
    }

    public Cursor getUserLogin(String username) {
        Cursor cursor = db.query(true, DB_TABLE, new String[]{KEY_USERNAME, KEY_PASSWORD}, KEY_USERNAME + "= '"
                + username + "'", null, null, null, null, null);
        return cursor;
    }

    public boolean deleteUserByUsername(String username) {
        return db.delete(DB_TABLE, KEY_USERNAME + "= '" + username + "'", null) > 0;
    }

    public Cursor getUserData(String username) {
        Cursor cursor = db.query(DB_TABLE, new String[]{KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL,
                KEY_NAME, KEY_LASTNAME}, KEY_USERNAME + "= '"
                + username + "'", null, null, null, null, null);
        return cursor;
    }

    public Cursor getUsers() {
        return db.query(DB_TABLE, new String[] {
                KEY_NAME, KEY_LASTNAME, KEY_USERNAME, KEY_PASSWORD
        }, null, null, null, null, null);
    }

}
