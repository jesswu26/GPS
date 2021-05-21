package fr.myapp.mongps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.BreakIterator;
import java.util.IllegalFormatCodePointException;

/*

 */

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView etLatitude, etLongitude, etAltitude;

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //cette méthode est obsolète mais semble indispensable pour éviter des plantages aléatoires
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLatitude = findViewById(R.id.tvLatitude);
        etLongitude = findViewById(R.id.tvLongitude);
        //etAltitude = fiendViewById(R.id.tvAltitude);
    /*
    final Context ctx = getApplicationContext();
Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
mvMap = (MapView) findViewById(R.id.mvMap);
mvMap.setTileSource(TileSourceFactory.MAPNIK);
mvMap.getController().setZoom(12);
mvMap.setMultiTouchControls(true);
mvMap.setBuiltInZoomControls(true);

     */

        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, perms, 1);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);

    }
    public void btnLocalisationGpsClick(View view){

        //Tester si l'autorisation a été accordée
        if (Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS non autorisé", Toast.LENGTH_LONG).show();
                return;
            }
        }
        // Tester si le GPS est opérationel
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS non disponible", Toast.LENGTH_LONG).show();;
            return;


        }
        //Integrer le GPS
        // temps mini : 100 ms, distance mini = 1 mètre
        //on définit MainActivity
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Double altitude = location.getAltitude();
        etLatitude.setText(latitude.toString());
        etLongitude.setText(longitude.toString());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "Le GPS est actif", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Le GPS est désactivé", Toast.LENGTH_LONG).show();

    }

    public void btMapViewClick(View view) {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

}

