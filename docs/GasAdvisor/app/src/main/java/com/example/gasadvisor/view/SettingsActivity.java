package com.example.gasadvisor.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.gasadvisor.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference tema = findPreference("tema");
           tema.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {
                   SharedPreferences ColorPreference= getApplicationContext().getSharedPreferences("color", 0);
                   SharedPreferences.Editor editorPref = ColorPreference.edit();
                   if (newValue.equals("@style/AppTheme")) {
                       editorPref.putInt("tema", R.style.AppTheme);
                   } else {
                       editorPref.putInt("tema", R.style.AppThemeLight);
                   }
                   editorPref.commit();
                   return false;
               }
           });

        }
    }
}