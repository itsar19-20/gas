package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.UserDBAdapter;

public class SignupActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText username, password, email, name, lastName;
    Button btnRegistrati;
    UserDBAdapter dbAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tvLogin = findViewById(R.id.tv_login_layoutRegister);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(toLogin);
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
                if (notEmpty(username) && notEmpty(password) && notEmpty(email) && notEmpty(name) &&
                        notEmpty(lastName)){
                    try {
                        dbAdapter.addUser(username.getText().toString(), password.getText().toString(),
                                email.getText().toString(), name.getText().toString(), lastName.getText().toString());
                        Intent success = new Intent(SignupActivity.this, HomeActivity.class);
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("preferences", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", username.getText().toString());
                        editor.commit();
                        startActivity(success);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(SignupActivity.this, "Impossibile aggiungere questo utente", Toast.LENGTH_SHORT).show();
                    }
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
