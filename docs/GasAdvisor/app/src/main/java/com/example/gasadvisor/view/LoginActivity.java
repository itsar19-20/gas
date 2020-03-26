package com.example.gasadvisor.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignup, tvForgotPass;
    EditText username, password;
    Button btnLogin;
    Cursor utente;
    UserDBAdapter dbAdapter = null;
    GasAdvisorApi gasAdvisorApi;
    SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        Intent success = new Intent(LoginActivity.this, HomeActivity.class);
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        tvSignup = findViewById(R.id.tv_signup_layoutLogin);
        tvForgotPass = findViewById(R.id.textView_forgotPassword);
        username = findViewById(R.id.editTextUsername_layoutLogin);
        password = findViewById(R.id.editTextPassword_layoutLogin);
        btnLogin = findViewById(R.id.btnLogin_layoutLogin);
        dbAdapter = new UserDBAdapter(this);
        dbAdapter.open();
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(toSignup);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> callLogin = gasAdvisorApi.getUserLogin(username.getText().toString(), password.getText().toString());
                callLogin.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Credenziali errate", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", username.getText().toString());
                            editor.commit();
                            User user = response.body();
                            try {
                                dbAdapter.addUser(user.getUsername(), password.getText().toString(),
                                        user.getEmail(), user.getName(), user.getLastName());
                            } catch (Exception e) {
                                Log.e("login", e.getLocalizedMessage());
                            }
                            finish();
                            startActivity(success);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        //in caso che il server e database e' sempre online e attivo il seguente codice va cancellato
                        try {
                            utente = dbAdapter.getUserLogin(username.getText().toString());
                            utente.moveToFirst();
                            if (utente.getString(1).contentEquals(password.getText().toString())) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", username.getText().toString());
                                editor.commit();
                                startActivity(success);
                            } else {
                                Toast.makeText(LoginActivity.this, "Password Errato", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Utente non trovato", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPass = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivityForResult(toPass, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                Toast.makeText(this, "Una mail e stata mandata al suo indirizzo mail", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }
}
