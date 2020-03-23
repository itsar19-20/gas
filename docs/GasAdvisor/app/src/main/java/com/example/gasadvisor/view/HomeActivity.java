package com.example.gasadvisor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.PrezzoDBAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    private DrawerLayout drawer;
    private FloatingActionButton btnMap;
    SharedPreferences preferences;
    BottomNavigationView bottomNavigationView;
    TextView prezzo, greeting;
    PrezzoDBAdapter prezzoDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences ColorPreference=getApplicationContext().getSharedPreferences("color",0);
        int theme=ColorPreference.getInt("tema",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTheme(theme);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        greeting = findViewById(R.id.tv_greeting_homeAct);
        String carburantePreferito = preferences.getString("carburante", null);
        greeting.setText("Il prezzo medio del carburante: " + carburantePreferito + " e` di:");
        prezzo = findViewById(R.id.tv_price_homeAct);
        prezzoDBAdapter = new PrezzoDBAdapter(this);
        prezzoDBAdapter.open();
        try {
            prezzo.setText(String.valueOf(prezzoDBAdapter.getMediaPrezzo()));
            prezzoDBAdapter.close();
        } catch (Exception e) {
            prezzoDBAdapter.close();
        }
        createDrawer();
        createBottomNav();
        //button torna in main activity
        btnMap = findViewById(R.id.btnMappaMain);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHome = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(toHome);
                finish();
            }
        });
    }

    public void createBottomNav() {
        //linstener per bottom nav, e mettiamo predefinito PiuEconomici
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home
                , new CheapestFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_cheapest:
                    selectedFragment = new CheapestFragment();
                    break;
                case R.id.nav_favourite:
                    selectedFragment = new FavouriteFragment();
                    break;
                case R.id.nav_ricercaAvanzata:
                    selectedFragment = new AdvancedSearchFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_home, selectedFragment).commit();
            return true;
        }
    };

    public void createDrawer() {
        //Inizio Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //mettiamo nome Utente su drawer
        username = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        try {
            String name = preferences.getString("username", null);
            username.setText(name.toUpperCase());
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                Toast.makeText(this, "Hai schiacciato proprio qui", Toast.LENGTH_LONG).show();
                startActivity(settingsIntent);
                break;
            case R.id.nav_profile:
                Intent toProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(toProfile);
                break;
            case R.id.nav_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.commit();
                Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true; //se return false nessun elemento si seleziona on click
    }

    @Override
    public void onBackPressed() {
        //serve quando clichiamo indietro dal telefono, non
        //lasciare l`activity ma solo chiudere drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //se drawer fosse a destra scrivi GravityCompat.END
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
