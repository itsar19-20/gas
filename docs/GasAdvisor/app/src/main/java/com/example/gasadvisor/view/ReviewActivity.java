package com.example.gasadvisor.view;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.Valutazione;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private UserDBAdapter userDBAdapter;
    private String username, password, nome, cognome, email, idImpianto, indirizzo, descrizione,
            giudizio, bandiera;
    private Cursor user;
    private TextView tvBandiera, tvIndirizzo;
    private Button btnSalva;
    private GasAdvisorApi gasAdvisorApi;
    private EditText etDescrizione;
    private Map<String, String> parametri;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        username = preferences.getString("username", null);
        userDBAdapter = new UserDBAdapter(this);
        userDBAdapter.open();
        user = userDBAdapter.getUserData(username);
        user.moveToFirst();
        password = user.getString(2);
        nome = user.getString(4);
        cognome = user.getString(5);
        email = user.getString(3);
        userDBAdapter.close();
        Bundle extras = getIntent().getExtras();
        bandiera = extras.getString("bandiera");
        idImpianto = extras.getString("idImpianto");
        indirizzo = extras.getString("indirizzo");
        tvBandiera = findViewById(R.id.tv_bandiera_reviewAct);
        tvBandiera.setText(bandiera);
        tvIndirizzo = findViewById(R.id.tv_indirizzo_reviewAct);
        tvIndirizzo.setText(indirizzo);
        btnSalva = findViewById(R.id.btn_salva_revieWAct);
        parametri = new HashMap<>();
        parametri.put("username", username);
        parametri.put("password", password);
        parametri.put("idImpianto", idImpianto);
        parametri.put("nome", nome);
        parametri.put("cognome", cognome);
        parametri.put("email", email);
        ratingBar = findViewById(R.id.ratingBar_reviewAct);
        etDescrizione = findViewById(R.id.et_descrizzione_reviewAct);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Salvando recensione...");
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descrizione = etDescrizione.getText().toString();
                int a = (int) ratingBar.getRating();
                if (descrizione.trim().length() == 0 || a == 0) {
                    Toast.makeText(ReviewActivity.this, "Inserire un commento e giudizio per continuare", Toast.LENGTH_SHORT).show();
                } else {
                    giudizio = String.valueOf(a);
                    parametri.put("descrizione", descrizione);
                    parametri.put("giudizio", giudizio);
                    gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
                    progressDialog.show();
                    Call<Valutazione> aggiungi = gasAdvisorApi.aggiungiValutazione(parametri);
                    aggiungi.enqueue(new Callback<Valutazione>() {
                        @Override
                        public void onResponse(Call<Valutazione> call, Response<Valutazione> response) {
                            if (!response.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(ReviewActivity.this, "Solo 1 valutazione per Distributore permessa", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReviewActivity.this, "Commento inserito correttamente", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Valutazione> call, Throwable t) {
                            Toast.makeText(ReviewActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}
