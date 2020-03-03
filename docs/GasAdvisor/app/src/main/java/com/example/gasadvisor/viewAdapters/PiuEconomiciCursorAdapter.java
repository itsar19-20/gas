package com.example.gasadvisor.viewAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.PreferitiDBAdapter;


public class PiuEconomiciCursorAdapter extends CursorAdapter {
    SharedPreferences preferences;
    PreferitiDBAdapter preferitiDBAdapter;

    public PiuEconomiciCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.layout_piu_economici, viewGroup, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        preferitiDBAdapter = new PreferitiDBAdapter(context);
        preferitiDBAdapter.open();
        preferences = context.getSharedPreferences("preferences", 0);
        String user = preferences.getString("username", null);
        int impianto = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(4)));
        TextView tvBandiera = view.findViewById(R.id.tv_Bandiera_layoutCheapest);
        TextView tvComune = view.findViewById(R.id.tv_Comune_LayoutCheapest);
        TextView tvData = view.findViewById(R.id.tv_Data_LayoutCheapest);
        TextView tvPrezzo = view.findViewById(R.id.tv_Prezzo_LayoutCheapest);
        tvBandiera.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
        tvComune.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        tvData.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        tvPrezzo.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
        CheckBox pref = view.findViewById(R.id.checkbox_layoutCheapest);
        if (preferitiDBAdapter.checkPreferito(user, impianto))
            pref.setChecked(true);
        else pref.setChecked(false);
        if (!pref.isChecked())
            pref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferitiDBAdapter.addPreferito(impianto, user);
                }
            });
        else pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferitiDBAdapter.deletePreferito(user, impianto);
            }
        });
    }
}
