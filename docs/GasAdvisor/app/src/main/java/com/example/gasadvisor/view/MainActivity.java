package com.example.gasadvisor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
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
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            indirizzo, comune, provincia, dtComu, descCarb, idImpianto, prezzo;
    private GasAdvisorApi gasAdvisorApi;
    private PrezzoDBAdapter prezzoDBAdapter;
    private DistributoreDBAdapter distributoreDBAdapter;
    private Double lat, longit;
    private PopupWindow popupWindow;
    private FeatureCollection featureCollection;
    private Drawable drawable;
    private Bitmap imgDistributore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext().getSharedPreferences("preferences", 0);
        carburantePreferito = preferences.getString("carburante", null);
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

        try {
            distributoreDBAdapter = new DistributoreDBAdapter(this);
            distributoreDBAdapter.open();
            featureCollection = FeatureCollection.fromJson(createJson(distributoreDBAdapter.getDistributoriEPrezzo()));
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fuel_15, null);
        imgDistributore = BitmapUtils.getBitmapFromDrawable(drawable);
        mapboxMap.setCameraPosition(new CameraPosition.Builder().zoom(11).build());
        if (carburantePreferito==null) {
            mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/navigation-preview-day-v4"),
                    new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            enableLocationComponent(style);
                        }
                    });
        } else {
            mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/navigation-preview-day-v4")
                            .withImage("image", imgDistributore)
                            .withSource(new GeoJsonSource("source", featureCollection))
                            .withLayer(new SymbolLayer("layer", "source")
                                    .withProperties(PropertyFactory.iconImage("image"), iconAllowOverlap(false),
                                            iconIgnorePlacement(false), iconOffset(new Float[]{0f, -9f}))),
                    new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            enableLocationComponent(style);
                        }
                    });
        }
        mapboxMap.addOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if (popupWindow != null) popupWindow.dismiss();
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, "layer");
        if (!features.isEmpty()) {
            if (destinationMarker != null) {
                mapboxMap.removeMarker(destinationMarker);
            }
            Feature selectedFeature = features.get(0);
            setPopUp(selectedFeature);
            double longitudine = (double) selectedFeature.getNumberProperty("longitudine");
            double latitudine = (double) selectedFeature.getNumberProperty("latitudine");
            destinationPosition = Point.fromLngLat(longitudine, latitudine);
            originPosition = Point.fromLngLat(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude(),
                    mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
            getRoute(originPosition, destinationPosition);
            btnStartNavigation.setEnabled(true);
            btnStartNavigation.setBackgroundResource(R.color.mapbox_blue);
        } else {
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
        }
        return true;
    }

    public String createJson(Cursor c) {
        JSONObject featureCollection = new JSONObject();
        try {
            featureCollection.put("type", "FeatureCollection");

            JSONArray features = new JSONArray();
            while (c.moveToNext()) {
                bandiera = c.getString(2);
                comune = c.getString(3);
                idImpianto = String.valueOf(c.getInt(6));
                prezzo = String.valueOf(c.getDouble(0));
                nomeImpianto = c.getString(4);
                String indirizzo = c.getString(5);
                String aggiornat = c.getString(1);
                double longi = c.getDouble(9);
                double lati = c.getDouble(8);
                JSONObject geometry = new JSONObject();
                JSONObject properties = new JSONObject();
                JSONArray JSONArrayCoord = new JSONArray();
                JSONObject newFeature = new JSONObject();
                properties.put("bandiera", bandiera);
                properties.put("comune", comune);
                properties.put("prezzo", prezzo);
                properties.put("nome", nomeImpianto);
                properties.put("indirizzo", indirizzo);
                properties.put("aggiornato", aggiornat);
                properties.put("id", idImpianto);
                properties.put("longitudine", longi);
                properties.put("latitudine", lati);
                JSONArrayCoord.put(0, longi);
                JSONArrayCoord.put(1, lati);
                geometry.put("type", "Point");
                geometry.put("coordinates", JSONArrayCoord);
                newFeature.put("type", "Feature");
                newFeature.put("properties", properties);
                newFeature.put("geometry", geometry);
                features.put(newFeature);
                featureCollection.put("features", features);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println(featureCollection.toString());
        return featureCollection.toString();
    }

    public void setPopUp(Feature feature) {
        bandiera = feature.getStringProperty("bandiera");
        comune = feature.getStringProperty("comune");
        nomeImpianto = feature.getStringProperty("nome");
        prezzo = feature.getStringProperty("prezzo");
        indirizzo = feature.getStringProperty("indirizzo");
        String aggiornato = feature.getStringProperty("aggiornato");
        View mainView = getLayoutInflater().inflate(R.layout.layout_marker_window, null);
        ViewFlipper markerInfoContainer = (ViewFlipper) mainView.findViewById(R.id.markerInfoContainer);
        View viewContainer = getLayoutInflater().inflate(R.layout.layout_marker, null);
        TextView tvBandiera = viewContainer.findViewById(R.id.tvBandiera_marker);
        TextView tvComune = viewContainer.findViewById(R.id.tvComune_marker);
        TextView tvNome = viewContainer.findViewById(R.id.tvNomeImpianto_marker);
        TextView tvIndirizzo = viewContainer.findViewById(R.id.tvIndirizzo_marker);
        TextView tvAggiornato = viewContainer.findViewById(R.id.tvAggiornato_marker);
        TextView tvPrezzo = viewContainer.findViewById(R.id.tvPrezzo_marker);
        Button close = viewContainer.findViewById(R.id.btnClose_marker);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        TextView tvRecensione = viewContainer.findViewById(R.id.tvRecensione_marker);
        tvRecensione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Diciamo che ha funzionato", Toast.LENGTH_SHORT).show();
            }
        });
        tvBandiera.setText(bandiera);
        tvComune.setText("Comune: " + comune);
        tvNome.setText("Nome Impianto: " + nomeImpianto);
        tvPrezzo.setText(prezzo);
        tvIndirizzo.setText("Indirizzo: " + indirizzo);
        tvAggiornato.setText("Aggiornato: " + aggiornato);
        markerInfoContainer.addView(viewContainer);
        popupWindow = new PopupWindow(mainView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER_HORIZONTAL, 0, 0);
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
