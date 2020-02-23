package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gasadvisor.R;

public class SignupActivity extends AppCompatActivity {
    TextView tvLogin;
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
    }
}
