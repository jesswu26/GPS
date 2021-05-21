package fr.myapp.mongps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import org.osmdroid.config.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapActivity extends AppCompatActivity implements LocationListener {

    MapView mvCarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

       // mvCarte = findViewById(R.id.mvCarte);
      //  Configuration.getInstance().setUserAgentValue("jessica.wu@free.fr");

        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mvCarte = (MapView) findViewById(R.id.mvCarte);
        mvCarte.setTileSource(TileSourceFactory.MAPNIK);
        mvCarte.getController().setZoom(12);
        mvCarte.setMultiTouchControls(true);
        mvCarte.setBuiltInZoomControls(true);

        // sidentifier pour la connexion au serveur
                //configurer la carte
//        mvCarte.setTileSource(TileSourceFactory.MAPNIK);
//        mvCarte.setBuiltInZoomControls(true);
//        mvCarte.getController().setZoom(12);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Positionnement impossible : permission non accordée.", Toast.LENGTH_LONG).show();
            return;
        }

        locationManager.requestLocationUpdates(GPS_PROVIDER, 100, 1, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        GeoPoint lyon = new GeoPoint(latitude, longitude);
        mvCarte.getController().setCenter(lyon);
        mvCarte.invalidate();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

}