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
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;
import com.example.gasadvisor.viewAdapters.PiuEconomiciCursorAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheapestFragment extends Fragment {
    GasAdvisorApi gasAdvisorApi;
    ListView lvPrezzi;
    DistributoreDBAdapter distributoreDBAdapter;
    PiuEconomiciCursorAdapter piuEconomiciCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cheapest, container, false);
        lvPrezzi = v.findViewById(R.id.lv_distributori_FragmentCheapest);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        distributoreDBAdapter = new DistributoreDBAdapter(getContext());
        distributoreDBAdapter.open();
        piuEconomiciCursorAdapter = new PiuEconomiciCursorAdapter(getContext(), distributoreDBAdapter.getPiuEconomici());
        lvPrezzi.setAdapter(piuEconomiciCursorAdapter);
        return v;
    }

    @Override
    public void onDestroyView() {
        distributoreDBAdapter.close();
        super.onDestroyView();
    }
}
