package com.example.gasadvisor.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.utils.ChangePasswordDialog;
import com.example.gasadvisor.utils.ChangeUsernameDialog;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ChangeUsernameDialog.UsernameDialogListener, ChangePasswordDialog.PasswordDialogListener {
    private Button btnCambiaCarburante, btnChangePass, btnChangeUsername, btnDeleteAccount;
    private UserDBAdapter userDBAdapter;
    private GasAdvisorApi gasAdvisorApi;
    SharedPreferences preferences;
    private String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences ColorPreference = getApplicationContext().getSharedPreferences("color", 0);
        Boolean isDark = ColorPreference.getBoolean("isDark", true);
        ConstraintLayout constraintLayout=findViewById(R.id.cl_activity_profile);
        constraintLayout.setBackgroundColor(getResources().getColor(!isDark ? R.color.whiteTextColor : R.color.colorBackground));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        userDBAdapter = new UserDBAdapter(this);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        nameUser = preferences.getString("username", null);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        btnCambiaCarburante = findViewById(R.id.btn_changeCarburante_profileAct);
        btnChangePass = findViewById(R.id.btn_changePass_profileAct);
        btnChangeUsername = findViewById(R.id.btn_changeUsername_profileAct);
        btnDeleteAccount = findViewById(R.id.btn_deleteAccount_profileAct);
        btnCambiaCarburante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFirst = new Intent(ProfileActivity.this, FirstActivity.class);
                startActivityForResult(toFirst, 3);

            }
        });
        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameUser == null) {
                    Toast.makeText(ProfileActivity.this, "Fare login per cambiare Username", Toast.LENGTH_SHORT).show();
                } else openDialogUsername();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameUser == null) {
                    Toast.makeText(ProfileActivity.this, "Entra nel tuo account per cambiare password", Toast.LENGTH_SHORT).show();
                } else openDialogPassword();
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameUser == null) {
                    Toast.makeText(ProfileActivity.this, "Entra nel tuo account per procedere", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = setAlert(ProfileActivity.this, "Cancellare il tuo account?", true);
                    builder.setMessage("I suoi dati verrano persi per sempre");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Call<ResponseBody> deleteCall = gasAdvisorApi.deleteUser(nameUser);
                            deleteCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Errore di server. Contattare via mail o provare piu tardi", Toast.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            String result = response.body().string();
                                            Toast.makeText(ProfileActivity.this, result, Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        userDBAdapter.open();
                                        try {
                                            userDBAdapter.deleteUserByUsername(nameUser);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        userDBAdapter.close();
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.remove("username");
                                        editor.commit();
                                        recreate();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(ProfileActivity.this, "Connessione ai server assente", Toast.LENGTH_SHORT).show();
                                }
                            });
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

    public AlertDialog.Builder setAlert(Context context, String title, boolean cancelable) {
        AlertDialog.Builder _return = new AlertDialog.Builder(context);
        _return.setTitle(title);
        _return.setCancelable(cancelable);
        return _return;
    }
}
