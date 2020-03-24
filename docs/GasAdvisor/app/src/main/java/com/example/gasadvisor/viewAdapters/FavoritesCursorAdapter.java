package com.example.gasadvisor.viewAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.IconCompat;

import com.example.gasadvisor.R;
import com.example.gasadvisor.view.MainActivity;


public class FavoritesCursorAdapter extends CursorAdapter {

    public FavoritesCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.layout_favourite, viewGroup, false);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        double latitudine = cursor.getDouble(9);
        double longitudine = cursor.getDouble(10);
        TextView tvBandiera = view.findViewById(R.id.tv_bandiera_layoutFav);
        tvBandiera.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        TextView tvComune = view.findViewById(R.id.tv_comune_layoutFav);
        tvComune.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
        TextView tvTipoImpianto = view.findViewById(R.id.tv_tipoImpianto_layoutFav);
        tvTipoImpianto.setText("Tipo: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
        TextView tvNomeImpianto = view.findViewById(R.id.tv_nomeImpianto_layoutFav);
        tvNomeImpianto.setText("Nome: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
        TextView tvIndirizzo = view.findViewById(R.id.tv_adress_layoutFav);
        tvIndirizzo.setText("Indirizzo: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))));
        TextView tvData = view.findViewById(R.id.tv_date_layoutFav);
        tvData.setText("Aggiornato: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        TextView tvPrezzo = view.findViewById(R.id.tv_price_layoutFav);
        tvPrezzo.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0).toString())));
        Button expand = view.findViewById(R.id.btnExpand_layoutFav);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout layout = view.findViewById(R.id.cl_expandable_layoutFav);
                if (layout.getVisibility() == View.VISIBLE)
                    layout.setVisibility(View.GONE);
                else
                    layout.setVisibility(View.VISIBLE);
            }
        });
        Button btnNav = view.findViewById(R.id.btn_startNav_layoutFav);
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainNavigation = new Intent(context, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putDouble("latitudine", latitudine);
                extras.putDouble("longitudine", longitudine);
                toMainNavigation.putExtras(extras);
                context.startActivity(toMainNavigation);
            }
        });
    }
}
