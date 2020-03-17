package com.example.gasadvisor.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.utils.ChangePasswordDialog;
import com.example.gasadvisor.utils.ChangeUsernameDialog;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ChangeUsernameDialog.UsernameDialogListener, ChangePasswordDialog.PasswordDialogListener {
    private TextView tvDatiUtente, tvCarburantePreferito, tvChangePass, tvChangeUsername;
    private ConstraintLayout clUserData;
    private UserDBAdapter userDBAdapter;
    private GasAdvisorApi gasAdvisorApi;
    SharedPreferences preferences;
    private String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        userDBAdapter = new UserDBAdapter(this);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        nameUser = preferences.getString("username", null);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        tvDatiUtente = findViewById(R.id.tv_userData_profileAct);
        clUserData = findViewById(R.id.cl_userData_profileAct);
        tvCarburantePreferito = findViewById(R.id.tv_carburantePref_profileAct);
        tvDatiUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameUser != null) {
                    if (clUserData.getVisibility() == View.GONE) {
                        clUserData.setVisibility(View.VISIBLE);
                    } else clUserData.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ProfileActivity.this, "Entra nel tuo account per visualizzare impostazioni", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvCarburantePreferito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Non funziona ancora cosa vuoi da me", Toast.LENGTH_SHORT).show();
                //commento da rimuovere quando logica drop vecchi dati-aggiungi nuovi si implementa
                /*   Intent toFirst = new Intent(ProfileActivity.this, FirstActivity.class);
                startActivity(toFirst);*/

            }
        });
        tvChangePass = findViewById(R.id.tv_changePass_profileAct);
        tvChangeUsername = findViewById(R.id.tv_changeUsername_profileAct);
        tvChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogUsername();
            }
        });
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPassword();
            }
        });


    }

    public void openDialogUsername() {
        ChangeUsernameDialog changeUsernameDialog = new ChangeUsernameDialog();
        changeUsernameDialog.show(getSupportFragmentManager(), "usernameDialog");
    }

    public void openDialogPassword() {
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
        changePasswordDialog.show(getSupportFragmentManager(), "passwordDialog");
    }

    @Override
    public void UsernameDialogClick(String username, String newUsername, String password) {
        if (newUsername.length() != 0) {
            Call<User> setNewUsername = gasAdvisorApi.postChangeUsername(username, password, newUsername);
            setNewUsername.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            Toast.makeText(ProfileActivity.this, "Credenziali errate", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(ProfileActivity.this, "Username gia' in uso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Impossibile cambiare Username", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        userDBAdapter.open();
                        User aggiornato = response.body();
                        Cursor userLocale = userDBAdapter.getUserData(username);
                        if (userLocale.moveToFirst()) {
                            String usernamevecchio = userLocale.getString(1);
                            userDBAdapter.updateUserByUsernameDiverso(usernamevecchio, password,
                                    aggiornato.getEmail(), aggiornato.getName(), aggiornato.getLastName(), aggiornato.getUsername());
                            userDBAdapter.close();
                        } else {
                            userDBAdapter.addUser(aggiornato.getUsername(), password,
                                    aggiornato.getEmail(), aggiornato.getName(), aggiornato.getLastName());
                            userDBAdapter.close();
                        }

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", aggiornato.getUsername());
                        editor.commit();
                        Toast.makeText(ProfileActivity.this, "Username cambiato correttamente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Inserire un Username valido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void PasswordDialogClick(String username, String newPassword, String password) {
        if (newPassword.length() != 0) {
            Call<User> setNewPassword = gasAdvisorApi.postChangePassword(username, password, newPassword);
            setNewPassword.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Credenziali errate", Toast.LENGTH_SHORT).show();
                    } else {
                        userDBAdapter.open();
                        User aggiornato = response.body();
                        Cursor userLocale = userDBAdapter.getUserData(username);
                        if (userLocale.moveToFirst()) {
                            userDBAdapter.updateUserByUsername(username, newPassword, aggiornato.getEmail()
                                    , aggiornato.getName(), aggiornato.getLastName());
                            userDBAdapter.close();
                        } else {
                            userDBAdapter.addUser(aggiornato.getUsername(), newPassword,
                                    aggiornato.getEmail(), aggiornato.getName(), aggiornato.getLastName());
                            userDBAdapter.close();
                        }
                        Toast.makeText(ProfileActivity.this, "Password cambiato correttamente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Inserire un password valido", Toast.LENGTH_SHORT).show();
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
