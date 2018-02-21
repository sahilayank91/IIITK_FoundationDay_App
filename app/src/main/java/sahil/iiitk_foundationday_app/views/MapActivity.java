package sahil.iiitk_foundationday_app.views;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.MarkerOptions;

import sahil.iiitk_foundationday_app.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar action = getActionBar();
        //action.hide();
        //setContentView(R.layout.activity_main);
        if (googleServicesAvailable()) {
          //  Toast.makeText(getApplicationContext(), "Perfect!!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_map);
            initMap();
        } else {
            // No Google Map
            Toast.makeText(getApplicationContext(), "This version of android doesn't support Google Maps.", Toast.LENGTH_LONG).show();
        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS)
            return true;
        else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Can't connect to play services", Toast.LENGTH_LONG);
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        // go to patricular location

        boolean success = mGoogleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        //todo adding more places and correct locations
        gotoLocationZoom(26.8639207,75.810202,15,"MNIT Prabha Bhawan");
        gotoLocationZoom(26.8621895,75.8099985,15,"MNIT Dispensary");
        gotoLocationZoom(26.861871,75.8088408,15,"MNIT Central Lawn");
        gotoLocationZoom(26.861871,75.8071993,15,"MNIT Annapurna");
        gotoLocationZoom(26.8628927,75.8123115,15,"MNIT VLTC");

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//        }
//        mGoogleMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    private void gotoLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void gotoLocationZoom(double lat, double lng, float zoom,String s) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

        MarkerOptions options = new MarkerOptions().title("Jaipur").position(ll).snippet(s);
        mGoogleMap.addMarker(options);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu1, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.mapTypeNone:
//                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//                break;
//            case R.id.mapTypeNormal:
//                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                break;
//            case R.id.mapTypeSatellite:
//                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                break;
//            case R.id.mapTypeTerrain:
//                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                break;
//            case R.id.mapTypeHybrid:
//                //Toast.makeText(getApplicationContext(),"Hybrid",Toast.LENGTH_LONG).show();
//                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_LONG).show();
    }
    Marker marker;
    @Override
    public void onLocationChanged(Location location) {
        if(location == null)
        {
            Toast.makeText(getApplicationContext(),"Can't get current location",Toast.LENGTH_LONG).show();
        }
        else
        {
            gotoLocationZoom(26.8639207,75.810202,15,"MNIT Prabha Bhawan");
            LatLng ll=new LatLng(26.8639207,75.810202);
            CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,15);
            mGoogleMap.animateCamera(update);

//            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
//            mGoogleMap.animateCamera(update);
//            if(marker!=null)
//            {
//                marker.remove();
//            }
//
//            MarkerOptions options = new MarkerOptions().title("User Current Location").position(ll).snippet("I am Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//            marker=mGoogleMap.addMarker(options);

        }
    }
}
