package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.gasadvisor.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent toMain = new Intent(SplashActivity.this, MainActivity.class);
        Intent toFirst = new Intent(SplashActivity.this, FirstActivity.class);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("preferences", 0);

        if (preferences.getString("carburante", null) != null) {
            startActivity(toMain);
        } else {
            startActivity(toFirst);
        }

    }
}
