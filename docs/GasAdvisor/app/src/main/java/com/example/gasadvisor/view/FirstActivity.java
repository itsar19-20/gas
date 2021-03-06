package com.example.gasadvisor.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.controller.PrezzoDBAdapter;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {
    private List<Integer> idImpianti;
    private Spinner spinner;
    private String carburante, carburantePreferito;
    private Button avanti;
    private DistributoreDBAdapter distributoreDBAdapter;
    private PrezzoDBAdapter prezzoDBAdapter;
    private GasAdvisorApi gasAdvisorApi;
    private String gestore, bandiera, tipoImpianto, nomeImpianto,
            indirizzo, comune, provincia, dtComu, descCarb;
    private int idImpianto, isSelf;
    private Double lat, longit, prezzo;
    private SharedPreferences preferences;
    private Intent toMain;
    private static final String PREFERENCES_NAME = "preferences";
    private static final String PREFERENCES_CARBURANTE = "carburante";
    private static final String PREFERENCES_DATA_AGGIORNAMENTO = "data_aggiornamento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toMain = new Intent(FirstActivity.this, MainActivity.class);
        preferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, 0);
        carburantePreferito = preferences.getString(PREFERENCES_CARBURANTE, null);
        if (carburantePreferito != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        setContentView(R.layout.activity_first);
        String[] arraySpinner = new String[]{"Benzina", "Gasolio", "GPL", "Metano"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, R.layout.layout_spinner_first_act, arraySpinner);
        adapterSpinner.setDropDownViewResource(R.layout.layout_spinner_first_act);
        spinner = findViewById(R.id.spinner_fisrtAct);
        spinner.setAdapter(adapterSpinner);
        avanti = findViewById(R.id.btn_startApp_firstAct);
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carburantePreferito == null) {
                    createDB();
                } else {
                    updateDB();
                }
            }
        });

    }

    public void updateDB() {
        final ProgressDialog progressDialog = new ProgressDialog(FirstActivity.this);
        progressDialog.setMessage("L'operazione richiede fino a 10 secondi");
        progressDialog.setTitle("Aggiornando i dati...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        carburante = spinner.getSelectedItem().toString();
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putString(PREFERENCES_CARBURANTE, carburante);
        Date dateToday = new Date();
        editorPref.putInt(PREFERENCES_DATA_AGGIORNAMENTO, dateToday.getDate());
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        int anno = new Date().getYear();
        prezzoDBAdapter = new PrezzoDBAdapter(this);
        distributoreDBAdapter = new DistributoreDBAdapter(this);
        distributoreDBAdapter.open();
        idImpianti = new ArrayList<>();
        idImpianti = distributoreDBAdapter.getIdImpianti();
        progressDialog.show();
        Call<List<Prezzo>> callPrezzi = gasAdvisorApi.getPrezziPiuEconomici(carburante);
        callPrezzi.enqueue(new Callback<List<Prezzo>>() {
            @Override
            public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(FirstActivity.this, "Chiamata al server non completata corretamente", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    List<Prezzo> risposta = response.body();
                    for (int i = 0; i < risposta.size(); i++) {
                        if (!idImpianti.contains(risposta.get(i).getDistributore().getIdImpianto())) {
                            if (risposta.get(i).getDataComunicazione().getYear() == anno) {
                                idImpianto = risposta.get(i).getDistributore().getIdImpianto();
                                gestore = risposta.get(i).getDistributore().getGestore().replace("'", " ");
                                bandiera = risposta.get(i).getDistributore().getBandiera().replace("'", " ");
                                tipoImpianto = risposta.get(i).getDistributore().getTipoImpianto().replace("'", " ");
                                nomeImpianto = risposta.get(i).getDistributore().getNomeImpianto().replace("'", " ");
                                indirizzo = risposta.get(i).getDistributore().getIndirizzo().replace("'", " ");
                                comune = risposta.get(i).getDistributore().getComune().replace("'", " ");
                                provincia = risposta.get(i).getDistributore().getProvincia().replace("'", " ");
                                lat = risposta.get(i).getDistributore().getLatitudine();
                                longit = risposta.get(i).getDistributore().getLongitudine();
                                try {
                                    distributoreDBAdapter.addDistributoreVeloce(idImpianto, gestore, bandiera,
                                            tipoImpianto, nomeImpianto, indirizzo, comune,
                                            provincia, lat, longit);
                                } catch (Exception e) {
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                    distributoreDBAdapter.close();
                    prezzoDBAdapter.open();
                    prezzoDBAdapter.deleteAllPrezzo();
                    for (int i = 0; i < risposta.size(); i++) {
                        idImpianto = risposta.get(i).getDistributore().getIdImpianto();
                        descCarb = risposta.get(i).getDescCarburante().replace("'", " ");
                        dtComu = risposta.get(i).getDtComu();
                        prezzo = risposta.get(i).getPrezzo();
                        isSelf = risposta.get(i).getIsSelf();
                        if (risposta.get(i).getDataComunicazione().getYear() == anno) {
                            try {
                                prezzoDBAdapter.addPrezzoVeloce(idImpianto, descCarb, prezzo, dtComu, isSelf);
                            } catch (Exception e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        }
                    }
                    prezzoDBAdapter.close();
                    editorPref.commit();
                    progressDialog.dismiss();
                    setResult(1);
                    startActivity(toMain);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                Toast.makeText(FirstActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createDB() {
        final ProgressDialog progressDialog = new ProgressDialog(FirstActivity.this);
        progressDialog.setMessage("L'applicazione si sta avviando...");
        progressDialog.setTitle("Personalizzando la tua esperienza");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        carburante = spinner.getSelectedItem().toString();
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putString(PREFERENCES_CARBURANTE, carburante);
        Date dateToday = new Date();
        editorPref.putInt(PREFERENCES_DATA_AGGIORNAMENTO, dateToday.getDate());
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        int anno = new Date().getYear();
        prezzoDBAdapter = new PrezzoDBAdapter(this);
        distributoreDBAdapter = new DistributoreDBAdapter(this);
        progressDialog.show();
        Call<List<Prezzo>> callPrezzi = gasAdvisorApi.getPrezziPiuEconomici(carburante);
        callPrezzi.enqueue(new Callback<List<Prezzo>>() {
            @Override
            public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(FirstActivity.this, "Chiamata al server non completata corretamente", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                List<Prezzo> listRisposta = response.body();
                prezzoDBAdapter.open();
                for (int i = 0; i < listRisposta.size(); i++) {
                    idImpianto = listRisposta.get(i).getDistributore().getIdImpianto();
                    descCarb = listRisposta.get(i).getDescCarburante().replace("'", " ");
                    dtComu = listRisposta.get(i).getDtComu();
                    prezzo = listRisposta.get(i).getPrezzo();
                    isSelf = listRisposta.get(i).getIsSelf();
                    if (listRisposta.get(i).getDataComunicazione().getYear() == anno) {
                        try {
                            prezzoDBAdapter.addPrezzoVeloce(idImpianto, descCarb, prezzo, dtComu, isSelf);
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
                prezzoDBAdapter.close();
                distributoreDBAdapter.open();
                for (int i = 0; i < listRisposta.size(); i++) {
                    idImpianto = listRisposta.get(i).getDistributore().getIdImpianto();
                    gestore = listRisposta.get(i).getDistributore().getGestore().replace("'", " ");
                    bandiera = listRisposta.get(i).getDistributore().getBandiera().replace("'", " ");
                    tipoImpianto = listRisposta.get(i).getDistributore().getTipoImpianto().replace("'", " ");
                    nomeImpianto = listRisposta.get(i).getDistributore().getNomeImpianto().replace("'", " ");
                    indirizzo = listRisposta.get(i).getDistributore().getIndirizzo().replace("'", " ");
                    comune = listRisposta.get(i).getDistributore().getComune().replace("'", " ");
                    provincia = listRisposta.get(i).getDistributore().getProvincia().replace("'", " ");
                    lat = listRisposta.get(i).getDistributore().getLatitudine();
                    longit = listRisposta.get(i).getDistributore().getLongitudine();
                    if (listRisposta.get(i).getDataComunicazione().getYear() == anno) {
                        try {
                            distributoreDBAdapter.addDistributoreVeloce(idImpianto, gestore, bandiera,
                                    tipoImpianto, nomeImpianto, indirizzo, comune,
                                    provincia, lat, longit);
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
                editorPref.commit();
                distributoreDBAdapter.close();
                progressDialog.dismiss();
                startActivity(toMain);
            }

            @Override
            public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                Toast.makeText(FirstActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //il bottone back che si mette automaticamente da android nel getSupportActionBar()
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

