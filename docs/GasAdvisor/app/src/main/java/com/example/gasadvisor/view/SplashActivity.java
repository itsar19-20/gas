package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.controller.PrezzoDBAdapter;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private List<Integer> idImpianti;
    private DistributoreDBAdapter distributoreDBAdapter;
    private GasAdvisorApi gasAdvisorApi;
    private String carburantePref;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private String gestore, bandiera, tipoImpianto, nomeImpianto,
            indirizzo, comune, provincia, dtComu, descCarb;
    private int idImpianto, isSelf, intUltimoAggiornamento;
    private Double lat, longit, prezzo;
    private Map<Integer, String> impiantoData;
    private static final String PREFERENCES_NAME = "preferences";
    private static final String PREFERENCES_CARBURANTE = "carburante";
    private static final String PREFERENCES_DATA_AGGIORNAMENTO = "data_aggiornamento";
    private static final String TAG = "SPLASH ACTIVITY";
    private Date dateLocale;
    private SharedPreferences.Editor editorPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        distributoreDBAdapter = new DistributoreDBAdapter(this);
        progressDialog = new ProgressDialog(this);
        impiantoData = new HashMap<>();
        dateLocale = new Date();
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        preferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, 0);
        carburantePref = preferences.getString(PREFERENCES_CARBURANTE, null);
        intUltimoAggiornamento = preferences.getInt(PREFERENCES_DATA_AGGIORNAMENTO, 0);
        Intent toMain = new Intent(SplashActivity.this, MainActivity.class);
        Intent toFirst = new Intent(SplashActivity.this, FirstActivity.class);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (carburantePref != null) {
            if (!Objects.equals(intUltimoAggiornamento, dateLocale.getDate())) {
                updatePrezzi();
                editorPref = preferences.edit();
                editorPref.putInt(PREFERENCES_DATA_AGGIORNAMENTO, dateLocale.getDate());
                editorPref.commit();
            }
            startActivity(toMain);
            finish();
        } else {
            startActivity(toFirst);
            finish();
        }

    }

    public void updatePrezzi() {
        distributoreDBAdapter.open();
        int anno = new Date().getYear();
        idImpianti = new ArrayList<>();
        idImpianti = distributoreDBAdapter.getIdImpianti();
        impiantoData = distributoreDBAdapter.getImpiantoEData();
        progressDialog.setMessage("Operazione richiede fino a 10 secondi");
        progressDialog.setTitle("Aggiornamento Prezzi...");
        progressDialog.show();
        Call<List<Prezzo>> aggiornaPrezzi = gasAdvisorApi.getPrezziPiuEconomici(carburantePref);
        aggiornaPrezzi.enqueue(new Callback<List<Prezzo>>() {
            @Override
            public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SplashActivity.this, "Chiamata al server non completata correttamente", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    List<Prezzo> risposta = response.body();
                    //check se ci sono nuovi distributori da aggiungere
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
                    //check se ci sono nuovi prezzi da aggiungere
                    for (int i = 0; i < risposta.size(); i++) {
                        idImpianto = risposta.get(i).getDistributore().getIdImpianto();
                        //se abbiamo gia un prezzo per questo impianto e riceviamo uno piu recente
                        if (idImpianti.contains(idImpianto)) {
                            String dataNuova = risposta.get(i).getDtComu();
                            String dataVecchia = impiantoData.get(idImpianto);
                            if (!Objects.equals(dataNuova, dataVecchia)) {
                                try {
                                    distributoreDBAdapter.updateDataDelPrezzo(dataNuova, idImpianto);
                                } catch (Exception e) {
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        } else {
                            //se non abbiamo un prezzo per questo idImpianto lo aggiungiamo
                            idImpianto = risposta.get(i).getDistributore().getIdImpianto();
                            descCarb = risposta.get(i).getDescCarburante().replace("'", " ");
                            dtComu = risposta.get(i).getDtComu();
                            prezzo = risposta.get(i).getPrezzo();
                            isSelf = risposta.get(i).getIsSelf();
                            if (risposta.get(i).getDataComunicazione().getYear() == anno) {
                                try {
                                    distributoreDBAdapter.addPrezzoVeloce(idImpianto, descCarb, prezzo, dtComu, isSelf);
                                } catch (Exception e) {
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        super.onDestroy();
    }
}
