package com.example.gasadvisor.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gasadvisor.utils.DBHelper;

public class DistributoreDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public static final String DB_TABLE = "distributore";
    public static final String KEY_ID = "_id";
    public static final String KEY_IDIMPIANTO = "idImpianto";
    public static final String KEY_GESTORE = "gestore";
    public static final String KEY_BANDIERA = "bandiera";
    public static final String KEY_TIPOIMPIANTO = "tipoImpianto";
    public static final String KEY_NOMEIMPIANTO = "nomeImpianto";
    public static final String KEY_INDIRIZZO = "indirizzo";
    public static final String KEY_COMUNE = "comune";
    public static final String KEY_PROVINCIA = "provincia";
    public static final String KEY_LATITUDINE = "latitudine";
    public static final String KEY_LONGITUDINE = "longitudine";

    public DistributoreDBAdapter(Context context) {
        this.context = context;
    }

    public DistributoreDBAdapter open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        db.close();
    }

    private ContentValues createContentValues(String idImpianto, String gestore, String bandiera,
                                              String tipoImpianto, String nomeImpianto, String indirizzo,
                                              String comune, String provincia, String lat, String longit) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDIMPIANTO, idImpianto);
        values.put(KEY_GESTORE, gestore);
        values.put(KEY_BANDIERA, bandiera);
        values.put(KEY_TIPOIMPIANTO, tipoImpianto);
        values.put(KEY_NOMEIMPIANTO, nomeImpianto);
        values.put(KEY_INDIRIZZO, indirizzo);
        values.put(KEY_COMUNE, comune);
        values.put(KEY_PROVINCIA, provincia);
        values.put(KEY_LATITUDINE, lat);
        values.put(KEY_LONGITUDINE, longit);
        return values;
    }

    public long addDistributore(String idImpianto, String gestore, String bandiera,
                                String tipoImpianto, String nomeImpianto, String indirizzo,
                                String comune, String provincia, String lat, String longit) {
        ContentValues values = createContentValues(idImpianto, gestore, bandiera,
                tipoImpianto, nomeImpianto, indirizzo, comune, provincia, lat, longit);
        return db.insertOrThrow(DB_TABLE, null, values);
    }
    public void addDistributoreVeloce(int idImpianto, String gestore, String bandiera,
                                      String tipoImpianto, String nomeImpianto, String indirizzo,
                                      String comune, String provincia, Double lat, Double longit) {
        String query = "insert into distributore(idImpianto, gestore, bandiera, tipoImpianto," +
                "nomeImpianto, indirizzo, comune, provincia, latitudine, longitudine)" +
                "values (" + idImpianto + ",'" + gestore + "','" + bandiera + "','" + tipoImpianto
                + "','" + nomeImpianto + "','" + indirizzo + "','"
                + comune + "','" + provincia + "'," + lat + "," + longit + ");";
        db.execSQL(query);
    }
    public Cursor getPiuEconomici(){
        String query ="select distinct prezzo.prezzo,prezzo.dtComu, distributore.comune,distributore.bandiera,distributore.idImpianto,distributore._id " +
                "from prezzo inner join distributore on distributore.idImpianto = prezzo.id_impianto " +
                "order by prezzo.prezzo limit 40";
        return  db.rawQuery(query, null);
    }
    public Cursor getDistributori(){
        return db.query(DB_TABLE, new String[] {
                KEY_BANDIERA, KEY_COMUNE
        }, null, null, null, null, null);
    }

}
