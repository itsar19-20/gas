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

public class PiuEconomiciArrayAdapt extends ArrayAdapter {
    List<Prezzo> listaPiuEconomici = new ArrayList<>();

    public PiuEconomiciArrayAdapt(@NonNull Context context, int resource, @NonNull List<Prezzo> objects) {
        super(context, resource, objects);
        listaPiuEconomici = objects;
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
        v = inflater.inflate(R.layout.layout_piu_economici, null);
        TextView tvBandiera = v.findViewById(R.id.tv_Bandiera_layoutCheapest);
        tvBandiera.setText(listaPiuEconomici.get(position).getDistributore().getBandiera());
        TextView tvComune = v.findViewById(R.id.tv_Comune_LayoutCheapest);
        tvComune.setText(listaPiuEconomici.get(position).getDistributore().getComune());
        TextView tvData = v.findViewById(R.id.tv_Data_LayoutCheapest);
        tvData.setText(listaPiuEconomici.get(position).getDtComu());
        TextView tvPrezzo = v.findViewById(R.id.tv_Prezzo_LayoutCheapest);
        tvPrezzo.setText(listaPiuEconomici.get(position).getPrezzo().toString());
        return v;
    }
}
