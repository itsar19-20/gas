package com.example.gasadvisor.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;
import com.example.gasadvisor.viewAdapters.PiuEconomiciArrayAdapt;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheapestFragment extends Fragment {
    String carburantePreferito;
    GasAdvisorApi gasAdvisorApi;
    ListView lvPrezzi;
    List<Prezzo> _return;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cheapest, container, false);
        lvPrezzi = v.findViewById(R.id.lv_distributori_FragmentCheapest);
        carburantePreferito = "Benzina";
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        cercaPiuEconomici(carburantePreferito);
        return v;
    }

    public void cercaPiuEconomici(String carburantePreferito) {
        Call<List<Prezzo>> callPrezzi = gasAdvisorApi.getPrezziPiuEconomici(carburantePreferito);
        callPrezzi.enqueue(new Callback<List<Prezzo>>() {
            @Override
            public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Chiamata al server non completata", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Prezzo> _return = new ArrayList<>();
                List<Prezzo> listRisposta = response.body();
                for (int i = 0; i < 40; i++) {
                    //aggiungiamo solo prezzi di 2020
                    if ((Long.parseLong(String.valueOf(listRisposta.get(i).getDataComunicazione().getTime())) >= 1577833200000L)) {
                        _return.add(listRisposta.get(i));
                    }
                }
                PiuEconomiciArrayAdapt adapt = new PiuEconomiciArrayAdapt(getActivity(), R.layout.layout_piu_economici, _return);
                lvPrezzi.setAdapter(adapt);
            }

            @Override
            public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                Toast.makeText(getActivity(), "Cerca connesione e prova di nuovo " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
