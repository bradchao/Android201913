package tw.org.iii.android201913;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    private LocationManager lmgr;
    private MyGPSListener myGPSListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        123);

        } else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        lmgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment =
                (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MyCallback());
    }

    private class MyGPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myGPSListener = new MyGPSListener();
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, myGPSListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lmgr.removeUpdates(myGPSListener);
    }

    private class MyCallback implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MainActivity.this.googleMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng kd = new LatLng(21.946308, 120.793959);
            MainActivity.this.googleMap.addMarker(new MarkerOptions().position(kd).title("Marker in KD"));
            MainActivity.this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(kd));
            MainActivity.this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kd, 14f));
        }
    }

    public void zoomIn(View view) {
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
    }
    public void zoomOut(View view) {
        googleMap.animateCamera(CameraUpdateFactory.zoomOut());
    }
}
