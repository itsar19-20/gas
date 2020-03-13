package com.example.gasadvisor.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gasadvisor.utils.DBHelper;

public class PreferitiDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    public static final String DB_TABLE = "preferiti";
    public static final String KEY_ID = "_id";
    public static final String KEY_IDIMPIANTO = "id_impianto";
    public static final String KEY_USER = "user";

    public PreferitiDBAdapter(Context context) {
        this.context = context;
    }

    public PreferitiDBAdapter open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        db.close();
    }

    private ContentValues createContentValues(Integer idImpianto, String user) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDIMPIANTO, idImpianto);
        values.put(KEY_USER, user);
        return values;
    }

    public long addPreferito(Integer idImpianto, String user) {
        ContentValues values = createContentValues(idImpianto, user);
        return db.insertOrThrow(DB_TABLE, null, values);
    }

    public Cursor getPreferiti(String user) {
        String query = "select prezzo.prezzo,prezzo.dtComu, distributore.bandiera," +
                "distributore.comune,distributore.tipoImpianto, distributore.nomeImpianto, distributore.indirizzo, distributore.idImpianto, " +
                "distributore._id, distributore.latitudine, distributore.longitudine " +
                "from prezzo inner join distributore on distributore.idImpianto = prezzo.id_impianto " +
                "inner join preferiti on preferiti.id_impianto = distributore.idImpianto where " +
                "preferiti.user = '" + user + "';";
        return db.rawQuery(query, null);
    }
    public boolean deletePreferito(String user, Integer idImpianto) {
        return db.delete(DB_TABLE, KEY_USER + "= '" + user + "' AND "+ KEY_IDIMPIANTO + "= "+ idImpianto, null) >0;
    }
    public boolean checkPreferito(String user, Integer idImpianto) {
        String query = "select * from preferiti where user = '" + user + "' and id_impianto = " + idImpianto + "";
        if (db.rawQuery(query, null).getCount() == 0)
            return false;
        else return true;
    }
}
