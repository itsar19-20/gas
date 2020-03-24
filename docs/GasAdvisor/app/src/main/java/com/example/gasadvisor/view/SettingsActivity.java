package com.example.gasadvisor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gasadvisor.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences ColorPreference = getApplicationContext().getSharedPreferences("color", 0);
        Boolean isDark = ColorPreference.getBoolean("isDark", true);
        setTheme(isDark ? R.style.temaScuro : R.style.temaChiaro);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch aSwitch = findViewById(R.id.temaSwitch);
        aSwitch.setTextColor(isDark ? getResources().getColor(R.color.whiteTextColor) : getResources().getColor(R.color.black));
        try {
            aSwitch.setChecked(isDark ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button button = findViewById(R.id.settinsActivity_backButton);
        button.setBackgroundTintList(isDark ? ColorStateList.valueOf(getResources().getColor(R.color.whiteCardColor)) : ColorStateList.valueOf(getResources().getColor(R.color.back)));
        button.setTextColor(!isDark ? ColorStateList.valueOf(getResources().getColor(R.color.whiteCardColor)) : ColorStateList.valueOf(getResources().getColor(R.color.back)));
        ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
            }
        });
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editorPref = ColorPreference.edit();

                Boolean switchState = aSwitch.isChecked();
                if (switchState) {
                    editorPref.putBoolean("isDark", true);
                    setTheme(R.style.temaScuro);
                } else {
                    editorPref.putBoolean("isDark", false);
                    setTheme(R.style.temaChiaro);
                }
                editorPref.apply();
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
    }
}
