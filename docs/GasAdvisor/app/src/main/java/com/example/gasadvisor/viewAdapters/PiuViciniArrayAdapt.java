package com.example.gasadvisor.viewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gasadvisor.R;
import com.example.gasadvisor.model.Prezzo;

import java.util.ArrayList;
import java.util.List;

public class PiuViciniArrayAdapt extends ArrayAdapter {
    List<Prezzo> listaPiuVicini = new ArrayList<>();

    public PiuViciniArrayAdapt(@NonNull Context context, int resource, @NonNull List<Prezzo> objects) {
        super(context, resource, objects);
        listaPiuVicini = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.layout_piu_vicini, null);
        TextView tvBandiera = v.findViewById(R.id.tv_Bandiera_layoutPiuVicini);
        tvBandiera.setText(listaPiuVicini.get(position).getDistributore().getBandiera());
        TextView tvComune = v.findViewById(R.id.tv_Comune_layoutPiuVicini);
        tvComune.setText(listaPiuVicini.get(position).getDistributore().getComune());
        TextView tvData = v.findViewById(R.id.tv_data_layoutPiuVicini);
        tvData.setText(listaPiuVicini.get(position).getDtComu());
        TextView tvPrezzo = v.findViewById(R.id.tv_Prezzo_layoutPiuVicini);
        tvPrezzo.setText(listaPiuVicini.get(position).getPrezzo().toString());
        return v;
    }
}
