package com.example.gasadvisor.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gasadvisor.R;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ConstraintLayout constraintLayout = findViewById(R.id.cl_activity_settings);
        SharedPreferences ColorPreference = getApplicationContext().getSharedPreferences("color", 0);
        Boolean isDark = ColorPreference.getBoolean("isDark", true);
        Switch aSwitch = findViewById(R.id.temaSwitch);
        aSwitch.setTextColor(isDark ? getResources().getColor(R.color.whiteTextColor) : getResources().getColor(R.color.black));
        constraintLayout.setBackgroundColor(getResources().getColor(!isDark ? R.color.whiteTextColor : R.color.colorBackground));
        try {
            aSwitch.setChecked(isDark ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editorPref = ColorPreference.edit();

                Boolean switchState = aSwitch.isChecked();
                if (switchState) {
                    editorPref.putBoolean("isDark", true);

                } else {
                    editorPref.putBoolean("isDark", false);
                }
                editorPref.apply();
                AlertDialog.Builder builder = setAlert(SettingsActivity.this, "L'app verrà riavviata per apportare le modifiche", true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        ProcessPhoenix.triggerRebirth(SettingsActivity.this);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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





