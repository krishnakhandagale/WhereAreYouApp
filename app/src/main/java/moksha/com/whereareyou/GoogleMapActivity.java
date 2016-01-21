package moksha.com.whereareyou;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import moksha.com.whereareyou.DialogUtils.CustomDialogInterface;
import moksha.com.whereareyou.DialogUtils.ShowDialog;
import moksha.com.whereareyou.Permissions.PermissionUtils;


public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,CustomDialogInterface {

    public static int CURRENT_SDK_VERSION;
    private LocationManager locationManager;
    private GoogleMap globalGoogleMap;

    //Location manager request location updates by following parameters.
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        CURRENT_SDK_VERSION= Build.VERSION.SDK_INT;
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(CURRENT_SDK_VERSION>=23){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                PermissionUtils.requestPermission(this,Manifest.permission.ACCESS_FINE_LOCATION,101,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION});

            }else{
                locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);

                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    ShowDialog.showDialogWithOkAndCancel(this,"Enable GPS","Please enable GPS to show your current location.","Allow","Cancel",100,GoogleMapActivity.this);
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, GoogleMapActivity.this);
                }
            }
        }else{

            locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);

            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                ShowDialog.showDialogWithOkAndCancel(this,"Enable GPS","Please enable GPS to show your current location.","Allow","Cancel",100,GoogleMapActivity.this);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, GoogleMapActivity.this);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        globalGoogleMap=googleMap;

        if(CURRENT_SDK_VERSION>=23){
            //Special permissions handeling for marshmallow
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                PermissionUtils.requestPermission(this,Manifest.permission.ACCESS_FINE_LOCATION,100,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
            }else{
                globalGoogleMap.setMyLocationEnabled(true);
            }
        }else{
            //If current version is less than version 23
            globalGoogleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==100){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                globalGoogleMap.setMyLocationEnabled(true);

            }else{
                Toast.makeText(GoogleMapActivity.this,"You can not use some of the features.Allow permissions needed from settings.",Toast.LENGTH_LONG).show();
            }

        }else if(requestCode==101){
            if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    ShowDialog.showDialogWithOkAndCancel(this,"Enable GPS","Please enable GPS to show your current location.","Allow","Cancel",100,GoogleMapActivity.this);
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, GoogleMapActivity.this);
                }
            }else{
                Toast.makeText(this,"You can not move to your current location directly.Allow permissions needed from settings.",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        globalGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current location"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        globalGoogleMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if(CURRENT_SDK_VERSION>=23){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                PermissionUtils.requestPermission(this,Manifest.permission.ACCESS_FINE_LOCATION,101,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION});

            }else{
                locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, GoogleMapActivity.this);
            }
        }else{

            locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, GoogleMapActivity.this);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void handleOkAction(int actionID) {
        if(actionID==100){
            if(locationManager!=null){
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        }
    }

    @Override
    public void handleOnCancelAction(int actionID) {

    }
}
