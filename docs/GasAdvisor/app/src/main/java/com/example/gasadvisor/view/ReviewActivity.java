package com.example.gasadvisor.view;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;

public class ReviewActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private UserDBAdapter userDBAdapter;
    private String username, password, nome, cognome, email, idImpianto, indirizzo, descrizione,
            giudizio;
    private Cursor user;
    private TextView tvBandiera, tvIndirizzo;
    private Button btnSalva;

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
        idImpianto = extras.getString("idImpianto");
        indirizzo = extras.getString("indirizzo");


    }


}
