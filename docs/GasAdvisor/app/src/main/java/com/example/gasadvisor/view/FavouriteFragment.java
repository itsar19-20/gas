package com.example.gasadvisor.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.controller.PreferitiDBAdapter;
import com.example.gasadvisor.viewAdapters.FavoritesCursorAdapter;

public class FavouriteFragment extends Fragment {
    SharedPreferences sharedPreferences;
    DistributoreDBAdapter distributoreDBAdapter;
    PreferitiDBAdapter preferitiDBAdapter;
    FavoritesCursorAdapter favoritesCursorAdapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("preferences", 0);
        String user = sharedPreferences.getString("username", null);
        preferitiDBAdapter = new PreferitiDBAdapter(getContext());
        listView = v.findViewById(R.id.lv_fragmentFav);
        preferitiDBAdapter.open();
        favoritesCursorAdapter = new FavoritesCursorAdapter(getContext(), preferitiDBAdapter.getPreferiti(user));
        listView.setAdapter(favoritesCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Cursor preferito = (Cursor) favoritesCursorAdapter.getItem(i);
                int idImpianto = preferito.getInt(7);
                AlertDialog.Builder builder = setAlert(getContext(), "Rimuovere da i preferiti?", true);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (preferitiDBAdapter.deletePreferito(user, idImpianto))
                            Toast.makeText(getContext(), "Carburante rimosso", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Qualcosa è andato storto, Prova di nuovo", Toast.LENGTH_SHORT).show();
                        favoritesCursorAdapter.swapCursor(preferitiDBAdapter.getPreferiti(user));
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return v;
    }

    public AlertDialog.Builder setAlert(Context context, String title, boolean cancelable) {
        AlertDialog.Builder _return = new AlertDialog.Builder(context);
        _return.setTitle(title);
        _return.setCancelable(cancelable);
        return _return;
    }
}
