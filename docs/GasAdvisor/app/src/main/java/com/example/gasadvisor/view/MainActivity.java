package com.example.gasadvisor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gasadvisor.R;
import com.example.gasadvisor.controller.DistributoreDBAdapter;
import com.example.gasadvisor.controller.PrezzoDBAdapter;
import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.utils.GasAdvisorApi;
import com.example.gasadvisor.utils.RetrofitUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        MapboxMap.OnMapClickListener, NavigationView.OnNavigationItemSelectedListener {
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private FloatingActionButton btnHome;
    private DrawerLayout drawer;
    private Point originPosition, destinationPosition;
    private Marker destinationMarker; //marker deprecato, da sostituire
    private Button btnStartNavigation;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    private DirectionsRoute currentRoute;
    TextView username;
    SharedPreferences preferences;
    private String carburantePreferito, gestore, bandiera, tipoImpianto, nomeImpianto,
            indirizzo, comune, provincia, dtComu, descCarb;
    private GasAdvisorApi gasAdvisorApi;
    private PrezzoDBAdapter prezzoDBAdapter;
    private DistributoreDBAdapter distributoreDBAdapter;
    private int idImpianto, isSelf;
    private Double lat, longit, prezzo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        //inizializazzione mappa
        Mapbox.getInstance(this, getString(R.string.MAPBOX_ACCESS_TOKEN_DEFAULT));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        // Mappa è caricata, aggiungiamo button navigazione
        btnStartNavigation = findViewById(R.id.btnStartNavigation);
        btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)
                        .build();
                NavigationLauncher.startNavigation(MainActivity.this, options);
            }
        });
        createDrawer();
        updateDB();
        //floating button HOME
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getString("username", null) == null) {
                    Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(homeIntent);
                } else {
                    Intent toLogin = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(toLogin);
                }
            }
        });
    }

    public void createDrawer() {
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_mainActivity);
        drawer = findViewById(R.id.drawer_layout_home);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view_mainActivity);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

    public void updateDB() {
        carburantePreferito = "Benzina";
        gasAdvisorApi = RetrofitUtils.getInstance().getGasAdvisorApi();
        int anno = new Date().getYear();
        prezzoDBAdapter = new PrezzoDBAdapter(this);
        distributoreDBAdapter = new DistributoreDBAdapter(this);
        Call<List<Prezzo>> callPrezzi = gasAdvisorApi.getPrezziPiuEconomici(carburantePreferito);
        callPrezzi.enqueue(new Callback<List<Prezzo>>() {
            @Override
            public void onResponse(Call<List<Prezzo>> call, Response<List<Prezzo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Chiamata al server non completata", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Prezzo> listRisposta = response.body();
                prezzoDBAdapter.open();
                for (int i = 0; i < listRisposta.size(); i++) {
                    idImpianto = listRisposta.get(i).getDistributore().getIdImpianto();
                    descCarb = listRisposta.get(i).getDescCarburante().replace("'", " ");
                    dtComu = listRisposta.get(i).getDtComu();
                    prezzo = listRisposta.get(i).getPrezzo();
                    isSelf = listRisposta.get(i).getIsSelf();
                    if (listRisposta.get(i).getDataComunicazione().getYear() == anno) {
                        try {
                            prezzoDBAdapter.addPrezzoVeloce(idImpianto, descCarb, prezzo, dtComu, isSelf);
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
                prezzoDBAdapter.close();
                distributoreDBAdapter.open();
                for (int i = 0; i < listRisposta.size(); i++) {
                    idImpianto = listRisposta.get(i).getDistributore().getIdImpianto();
                    gestore = listRisposta.get(i).getDistributore().getGestore().replace("'", " ");
                    bandiera = listRisposta.get(i).getDistributore().getBandiera().replace("'", " ");
                    tipoImpianto = listRisposta.get(i).getDistributore().getTipoImpianto().replace("'", " ");
                    nomeImpianto = listRisposta.get(i).getDistributore().getNomeImpianto().replace("'", " ");
                    indirizzo = listRisposta.get(i).getDistributore().getIndirizzo().replace("'", " ");
                    comune = listRisposta.get(i).getDistributore().getComune().replace("'", " ");
                    provincia = listRisposta.get(i).getDistributore().getProvincia().replace("'", " ");
                    lat = listRisposta.get(i).getDistributore().getLatitudine();
                    longit = listRisposta.get(i).getDistributore().getLongitudine();
                    if (listRisposta.get(i).getDataComunicazione().getYear() == anno) {
                        try {
                            distributoreDBAdapter.addDistributoreVeloce(idImpianto, gestore, bandiera,
                                    tipoImpianto, nomeImpianto, indirizzo, comune,
                                    provincia, lat, longit);
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
                distributoreDBAdapter.close();
                Toast.makeText(MainActivity.this, "Aggiornamento dati finito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Prezzo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connessione al server assente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new StatisticheFragment()).commit();
                break;
            case R.id.nav_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.commit();
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

    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setCameraPosition(new CameraPosition.Builder().zoom(11).build());
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/navigation-preview-day-v4"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
        mapboxMap.addOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        //mettiamo Marker in mappa e prendiamo latitudine longitudine
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(point));
        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        originPosition = Point.fromLngLat(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude(),
                mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
        getRoute(originPosition, destinationPosition);
        btnStartNavigation.setEnabled(true);
        btnStartNavigation.setBackgroundResource(R.color.mapbox_blue);
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this).accessToken(Mapbox.getAccessToken()).origin(origin)
                .destination(destination).build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, check user and access token");
                    return;
                } else if (response.body().routes().size() == 0) {
                    Toast.makeText(MainActivity.this, "Nessun percorso trovato", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentRoute = response.body().routes().get(0);
                if (navigationMapRoute != null) {
                    navigationMapRoute.removeRoute();
                } else {
                    navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                }
                navigationMapRoute.addRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Non è possibile ottenere percorsi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
