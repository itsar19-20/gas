package com.example.gasadvisor.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gasadvisor.utils.DBHelper;

public class PrezzoDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public static final String DB_TABLE = "prezzo";
    public static final String KEY_DESCCARBURANTE = "descCarburante";
    public static final String KEY_PREZZO = "prezzo";
    public static final String KEY_ISSELF = "isSelf";
    public static final String KEY_DTCOMU = "dtComu";
    public static final String KEY_IDIMPIANTO = "id_impianto";

    public PrezzoDBAdapter(Context context) {
        this.context = context;
    }

    public PrezzoDBAdapter open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        db.close();
    }
    private ContentValues createContentValues(String descCarb, String prezzo, String isSelf,
                                              String dtComu, String id_impianto){
        ContentValues values = new ContentValues();
        values.put(KEY_DESCCARBURANTE, descCarb);
        values.put(KEY_PREZZO, prezzo);
        values.put(KEY_ISSELF, isSelf);
        values.put(KEY_DTCOMU, dtComu);
        values.put(KEY_IDIMPIANTO, id_impianto);
        return values;
    }
    public long addPrezzo(String descCarb, String prezzo, String isSelf,
                          String dtComu, String id_impianto){
        ContentValues values = createContentValues(descCarb, prezzo, isSelf, dtComu, id_impianto);
        return db.insertOrThrow(DB_TABLE, null, values);
    }
    public Cursor getPiuEconomici(){
        String query = "select prezzo."+KEY_PREZZO+",prezzo."+KEY_DTCOMU+", distributore.bandiera," +
                "distributore.comune from prezzo inner join distributore on distributore.idImpianto = prezzo.id_impianto;";
        return db.rawQuery(query, null);
    }

}
