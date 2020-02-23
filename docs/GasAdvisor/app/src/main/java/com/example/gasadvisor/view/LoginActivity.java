package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.w3c.dom.Text;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignup;
    EditText username, password;
    Button btnLogin;
    Cursor utente;
    UserDBAdapter dbAdapter = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignup = findViewById(R.id.tv_signup_layoutLogin);
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
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    utente = dbAdapter.getUserLogin(username.getText().toString());
                    utente.moveToFirst();
                    if (utente.getString(1).contentEquals(password.getText().toString())){
                        Intent success = new Intent(LoginActivity.this, HomeActivity.class);
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("preferences", 0);
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

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }
}
