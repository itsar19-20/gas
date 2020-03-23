package com.example.gasadvisor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.gasadvisor.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch aSwitch = findViewById(R.id.temaSwitch);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences ColorPreference = getApplicationContext().getSharedPreferences("color", 0);
                SharedPreferences.Editor editorPref = ColorPreference.edit();
                Boolean switchState = aSwitch.isChecked();
                if (switchState) {
                    editorPref.putInt("tema", R.style.AppTheme);
                } else {
                    editorPref.putInt("tema", R.style.AppThemeLight);
                }
                editorPref.commit();
            }
        });
    }
}
