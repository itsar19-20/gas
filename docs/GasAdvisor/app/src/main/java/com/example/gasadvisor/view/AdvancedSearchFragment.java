package com.example.gasadvisor.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;
import com.example.gasadvisor.model.Distributore;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;
import com.example.gasadvisor.viewAdapters.PiuViciniArrayAdapt;

import java.security.Permission;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedSearchFragment extends Fragment {
    private Button btnPiuVicino, btnAvviaRicerca;
    private Spinner spinnerRaggio, spinnerCarburante;
    private String carburante;
    private int raggio;
    private LocationManager locationManager;
    private Location location;
    private GasAdvisorApi gasAdvisorApi;
    private Double latitudine, longitudine, lat1, long1;
    private ListView lvCarburanti;
    private PiuViciniArrayAdapt piuViciniArrayAdapt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_advanced, container, false);
        btnPiuVicino = v.findViewById(R.id.btn_piuVicino_avanzFrag);
        btnPiuVicino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(2);
                getActivity().finish();
            }
        });
        spinnerCarburante = v.findViewById(R.id.spinner_carburante_avanzFrag);
        spinnerRaggio = v.findViewById(R.id.spinner_raggio_avanzFrag);
        String[] arraySpinnerDistributori = new String[]{"Benzina", "Gasolio", "GPL", "Metano"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arraySpinnerDistributori);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCarburante.setAdapter(adapterSpinner);
        Integer[] arraySpinnerRaggio = new Integer[]{5, 10, 50};
        ArrayAdapter<Integer> adapterSpinnerRaggio = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arraySpinnerRaggio);
        adapterSpinnerRaggio.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerRaggio.setAdapter(adapterSpinnerRaggio);
        btnAvviaRicerca = v.findViewById(R.id.btn_avvia_avanzFrag);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitudine = location.getLatitude();
        longitudine = location.getLongitude();
        lvCarburanti = v.findViewById(R.id.lv_avanzFrag);
        btnAvviaRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carburante = spinnerCarburante.getSelectedItem().toString();
                raggio = Integer.parseInt(spinnerRaggio.getSelectedItem().toString());
                Call<List<Prezzo>> call = gasAdvisorApi.getPiuVicini(carburante, raggio, latitudine, longitudine);
                call.enqueue(new Callback<List<Prezzo>>() {
                    @Override
                    public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Problemi nel server", Toast.LENGTH_SHORT).show();
                        } else {
                            List<Prezzo> risposta = response.body();
                            piuViciniArrayAdapt = new PiuViciniArrayAdapt(getContext(), R.id.lv_avanzFrag, risposta);
                            lvCarburanti.setAdapter(piuViciniArrayAdapt);

                            lvCarburanti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Prezzo prezzo = risposta.get(i);
                                    lat1 = prezzo.getDistributore().getLatitudine();
                                    long1 = prezzo.getDistributore().getLongitudine();
                                    Intent toMainNavigation = new Intent(getContext(), MainActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putDouble("latitudine", lat1);
                                    extras.putDouble("longitudine", long1);
                                    toMainNavigation.putExtras(extras);
                                    startActivity(toMainNavigation);
                                    getActivity().finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                        Toast.makeText(getContext(), "Chiamata al server fallita", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return v;
    }
}
