package com.example.gasadvisor.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.gasadvisor.utils.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributoreDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public static final String DB_TABLE = "distributore";
    public static final String DB_TABLE_Prezzo = "prezzo";
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
    public static final String KEY_MEDIAVAL = "mediaVal";

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

    public boolean updateMediaValutazioni(int idImpianto, float mediaVal) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_MEDIAVAL, mediaVal);
        return db.update(DB_TABLE, cv, KEY_IDIMPIANTO + "=" + idImpianto, null) > 0;
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

    public Cursor getDistributoriEPrezzo() {
        String query = "select prezzo.prezzo,prezzo.dtComu, distributore.bandiera," +
                "distributore.comune, distributore.nomeImpianto, distributore.indirizzo, distributore.idImpianto, " +
                "distributore._id, distributore.latitudine, distributore.longitudine, distributore.mediaVal" +
                " from prezzo inner join distributore on distributore.idImpianto = prezzo.id_impianto;";
        return db.rawQuery(query, null);
    }

    public Cursor getPiuEconomici() {
        String query = "select distinct prezzo.prezzo,prezzo.dtComu, distributore.comune,distributore.bandiera,distributore.idImpianto,distributore._id " +
                "from prezzo inner join distributore on distributore.idImpianto = prezzo.id_impianto " +
                "order by prezzo.prezzo limit 40";
        return db.rawQuery(query, null);
    }

    public List<Integer> getIdImpianti(){
        List<Integer> _return = new ArrayList<>();
        String query = "select distributore.idImpianto from distributore;";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            _return.add(cursor.getInt(0));
        }
        return _return;
    }
    public Map<Integer, String> getImpiantoEData(){
        Map<Integer,String> _return = new HashMap<>();
        String query = "select id_impianto, dtComu from prezzo;";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            _return.put(c.getInt(0), c.getString(1));
        }
        return _return;
    }

    //lo metto qui per non fare prezzoDBAdapter.open in un ciclo dove ho gia fatto DistrDBAdapter.open
    public void addPrezzoVeloce(int id_impianto, String descCarb, Double prezzo,
                                String dtComu, int isSelf) {
        String query = "insert into prezzo(descCarburante, prezzo, isSelf, dtComu, id_impianto)" +
                "values ('" + descCarb + "'," + prezzo + "," + isSelf + ",'" + dtComu
                + "'," + id_impianto + ");";
        db.execSQL(query);
    }

    public boolean updateDataDelPrezzo(String dtComu, int idImpianto) {
        ContentValues values = new ContentValues();
        values.put("dtComu", dtComu);
        return db.update(DB_TABLE_Prezzo, values, "id_impianto = " + idImpianto, null) > 0;
    }

    public Cursor getDistributori() {
        return db.query(DB_TABLE, new String[]{
                KEY_BANDIERA, KEY_COMUNE
        }, null, null, null, null, null);
    }

}
