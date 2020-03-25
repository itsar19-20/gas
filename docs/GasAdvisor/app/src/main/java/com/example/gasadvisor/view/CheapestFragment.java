package com.example.gasadvisor.view;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;
import com.example.gasadvisor.viewAdapters.PiuEconomiciCursorAdapter;

public class CheapestFragment extends Fragment {
    GasAdvisorApi gasAdvisorApi;
    ListView lvPrezzi;
    DistributoreDBAdapter distributoreDBAdapter;
    PiuEconomiciCursorAdapter piuEconomiciCursorAdapter;
    SharedPreferences ColorPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cheapest, container, false);
        ColorPreference=getActivity().getApplicationContext().getSharedPreferences("color",0);
        Boolean isDark=ColorPreference.getBoolean("isDark",true);
        LinearLayout linearLayout=v.findViewById(R.id.ll_fragment_cheapest);
        if (!isDark) linearLayout.setBackgroundColor((getActivity().getResources().getColor(R.color.whiteCardColor)));
        lvPrezzi = v.findViewById(R.id.lv_distributori_FragmentCheapest);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        distributoreDBAdapter = new DistributoreDBAdapter(getContext());
        distributoreDBAdapter.open();
        piuEconomiciCursorAdapter = new PiuEconomiciCursorAdapter(getContext(), distributoreDBAdapter.getPiuEconomici());
        lvPrezzi.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        lvPrezzi.setAdapter(piuEconomiciCursorAdapter);
        return v;
    }

    @Override
    public void onDestroyView() {
        distributoreDBAdapter.close();
        super.onDestroyView();
    }
}
