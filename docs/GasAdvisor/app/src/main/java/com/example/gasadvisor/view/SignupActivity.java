package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;

import java.util.regex.PatternSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText username, password, email, name, lastName;
    Button btnRegistrati;
    UserDBAdapter dbAdapter = null;
    GasAdvisorApi gasAdvisorApi;
    User user;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        tvLogin = findViewById(R.id.tv_login_layoutRegister);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(toLogin);
                finish();
            }
        });
        username = findViewById(R.id.et_userName_layoutRegister);
        password = findViewById(R.id.et_password_layoutRegister);
        email = findViewById(R.id.et_email_layoutRegister);
        name = findViewById(R.id.et_name_layoutRegister);
        lastName = findViewById(R.id.et_lastName_layoutRegister);
        btnRegistrati = findViewById(R.id.btn_signup_layoutRegister);
        dbAdapter = new UserDBAdapter(this);
        dbAdapter.open();
        btnRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //controlliamo l'indirizzo email se assomiglia a un pattern email predefinito
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    email.requestFocus();
                    Toast.makeText(SignupActivity.this, "Inserire un indirizzo Email valido", Toast.LENGTH_SHORT).show();
                } else if (notEmpty(username) && notEmpty(password) && notEmpty(email) && notEmpty(name) &&
                        notEmpty(lastName)) {
                    Call<User> registerUser = gasAdvisorApi.postUserSignUp(username.getText().toString(), password.getText().toString(), email.getText().toString(),
                            name.getText().toString(), lastName.getText().toString());
                    registerUser.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Questo Username e' gia' in uso", Toast.LENGTH_SHORT).show();
                            } else {
                                dbAdapter.addUser(username.getText().toString(), password.getText().toString(),
                                        email.getText().toString(), name.getText().toString(), lastName.getText().toString());
                                editor.putString("username", username.getText().toString());
                                editor.commit();
                                Intent toHome = new Intent(SignupActivity.this, HomeActivity.class);
                                startActivity(toHome);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(SignupActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Completa tutti i campi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean notEmpty(EditText et) {
        if (et.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }
}
