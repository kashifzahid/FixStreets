package com.example.fixstreet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.fixstreet.Utils.PermissionUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";
    private CardView cardView;
    private static final int MY_LOCATION_REQUEST_CODE = 200;
    private static final String TAG = "MapActivity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 125;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mGoogleMap;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    Context context;
    Location mCurrentLocation;
    private Marker mCurrLocationMarker;
    private static TextView textView_address;
    private long UPDATE_INTERVAL = 50000;  /* 50 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private final static String KEY_LOCATION = "location";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    FloatingActionButton fab;
    private boolean mRequestingLocationUpdates;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    ImageView location_marker;
    Button location_request;
    LinearLayout location_not_granted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.e(TAG, "onCreate()");
        updateValuesFromBundle(savedInstanceState);
        context = this;

//        if(!mLocationPermissionGranted()){
//            Log.e(TAG, mLocationPermissionGranted()+" not granted");
//            requestPermission();
//        }

        mFusedLocationClient = getFusedLocationProviderClient(this);
        textView_address = findViewById(R.id.text);
        final LocationManager manager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MapsActivity.this)) {
            Log.d(TAG, "Gps already enabled");
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(MapsActivity.this)) {
            Log.d(TAG, "Gps not Supported");
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MapsActivity.this)) {
            Log.d(TAG, "Gps not enabled");
            // settingsrequest();
        } else {
            Log.d(TAG, "Gps already enabled");
        }
        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        fab = findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
            // is not null.
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        location_request = findViewById(R.id.location_request);
        location_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            }
        });
        location_not_granted = findViewById(R.id.location_not_granted);
        location_marker = findViewById(R.id.location_marker);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        // Update the value of mRequestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleApiClientt();
        // createLocationRequest();
        Log.e(TAG, "onMapReady()");
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


//        if (mLocationPermissionGranted()) {
//            //Location Permission already granted
//            location_marker.setVisibility(View.VISIBLE);
//            startLocationUpdates();
//
//        } else {
//            requestPermission();
//            //Request Location Permission
//            location_not_granted.setVisibility(View.VISIBLE);
//        }
        //startLocationUpdates();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(50.846629, 4.345558));

        // moves camera to coordinates
        mGoogleMap.moveCamera(point);
        // animates camera to coordinates
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ));

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Location location = new Location("me");
                location.setLatitude(mGoogleMap.getCameraPosition().target.latitude);
                location.setLongitude(mGoogleMap.getCameraPosition().target.longitude);

                getAddressFromLocation(location, getApplicationContext(), new GeocoderHandler());
                //textView.setText(getCompleteAddressString(mGoogleMap.getCameraPosition().target.latitude,mGoogleMap.getCameraPosition().target.longitude));
                textView_address.setTextSize(16);
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                textView_address.setText("Loading...");
            }
        });

    }

    private void equestPermission() {

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e(TAG, "shouldShowRequestPermissionRationale()");
                shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION);
            }


        } else {
            // No explanation needed; request the permission
            Log.e(TAG, "requestPermission()");
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {

        PermissionUtil.checkPermission(this, ACCESS_FINE_LOCATION, new PermissionUtil.PermissionAskListener() {
            @Override
            public void onPermissionAsk() {
                Log.e(TAG, "onPermissionAsk");
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }

            @Override
            public void onPermissionPreviouslyDenied() {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);

                alertDialogBuilder.setTitle("Share Your Location");
                alertDialogBuilder
                        .setMessage("For the best experience, Delivery needs to use your current location to find the delivery point. Do you want to share your location?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions((Activity) context, new String[]{ACCESS_FINE_LOCATION}, 23);

                            }
                        });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Log.e(TAG, "onPermissionPreviouslyDenied");
            }

            @Override
            public void onPermissionDisabled() {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);

                alertDialogBuilder.setTitle("Location Permission Required");
                alertDialogBuilder
                        .setMessage("For the best Delivery experience, please enable Permission->Location in the application settings.")
                        .setCancelable(false)
                        .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                            }
                        });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onPermissionGranted() {
                Log.e(TAG, "onPermissionGranted");
                // startLocationUpdates();
            }
        });
    }

//    public void settingsrequest() {
//
//        Log.e("settingRequest", "hi");
//        googleApiClientt();
//        createLocationRequest();
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        SettingsClient client = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
//
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                // All location settings are satisfied. The client can initialize
//                // location requests here.
//                // ...
//                Log.e(TAG, "settingRequest() startLocationUpdate()");
//                startLocationUpdates();
//            }
//        });
//
//        task.addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (e instanceof ResolvableApiException) {
//                    // Location settings are not satisfied, but this can be fixed
//                    // by showing the user a dialog.
//                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
//                        ResolvableApiException resolvable = (ResolvableApiException) e;
//                        resolvable.startResolutionForResult(MapsActivity.this,
//                                REQUEST_CHECK_SETTINGS);
//                    } catch (IntentSender.SendIntentException sendEx) {
//                        // Ignore the error.
//                    }
//                }
//            }
//        });
//    }

    private synchronized void googleApiClientt() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }
    }

//    protected void createLocationRequest() {
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(UPDATE_INTERVAL);
//        locationRequest.setFastestInterval(FASTEST_INTERVAL);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        location_not_granted.setVisibility(View.GONE);
                        Log.e(TAG, "onActivityResult() startLocationUpdate()");
                        // startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        //location_not_granted.setVisibility(View.VISIBLE);
                        break;
                }
                break;
        }
    }

//    private void startLocationUpdates() {
//        if (ContextCompat.checkSelfPermission(this,
//                ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.e(TAG, "startLocationUpdate()");
//            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
//        }
//    }
//
//    private void stopLocationUpdates() {
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected()");
        // startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

//    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            if (locationResult == null) {
//                return;
//            }
//            Log.e("MapsActivity", "before: ");
//            List<Location> locationList = locationResult.getLocations();
//            if (locationList.size() > 0) {
//                Log.e("MapsActivity", "before: ");
//                //The last location in the list is the newest
//                final Location location = locationList.get(locationList.size() - 1);
//                Log.e("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//                /*if (mCurrLocationMarker == null) {
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.positaion(latLng);
//                    markerOptions.title("Current Position");
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//                    markerOptions.draggable(true);
//                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
//
//                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                } else {
//                    //AnimateUtil.animateMarkerTo(mCurrLocationMarker, latLng);
//
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//                    mGoogleMap.moveCamera(cameraUpdate);
//                    //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                }*/
//
//                Location locationn = new Location("me");
//                locationn.setLatitude(location.getLatitude());
//                locationn.setLongitude(location.getLongitude());
//                onLocationChanged(locationn);
//                //move map camera
//            }
//        }
//    };

    public boolean mLocationPermissionGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //location_not_granted.setVisibility(View.GONE);
                // startLocationUpdates();

                Log.e(TAG, "onRequestPermissionsResult() granted");
            }
        } else {
            Log.e(TAG, "onRequestPermissionsResult() not granted");
            //location_not_granted.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stopLocationUpdates();
        googleApiClient.disconnect();
        Log.e(TAG, "onStop()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
        if (mLocationPermissionGranted()) {
            //Location Permission already granted
            if(googleApiClient == null)
                googleApiClientt();
//            createLocationRequest();
//            settingsrequest();
            location_marker.setVisibility(View.VISIBLE);
            location_not_granted.setVisibility(View.GONE);
            //getMyLocation();
        } else {
            location_not_granted.setVisibility(View.VISIBLE);
            requestPermission();
            //Request Location Permission
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
        if (mLocationPermissionGranted()) {
            //Location Permission already granted
            if(googleApiClient == null)
                googleApiClientt();
            // createLocationRequest();
            if (mCurrentLocation != null) {
                LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mGoogleMap.moveCamera(cameraUpdate);
            }
            //startLocationUpdates();
            location_marker.setVisibility(View.VISIBLE);
            location_not_granted.setVisibility(View.GONE);
            //getMyLocation();
        } else {
            location_not_granted.setVisibility(View.VISIBLE);
            requestPermission();
            //Request Location Permission
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    public void location_permission(View view) {
        if (!mLocationPermissionGranted())
            requestPermission();
        // settingsrequest();
    }

    public void Btn_Ok(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", result);
        returnIntent.putExtra("throughfare", throughfare);
        returnIntent.putExtra("locality", locality);
        returnIntent.putExtra("street", street);
        returnIntent.putExtra("lat", mCurrentLocation.getLatitude());
        returnIntent.putExtra("lng", mCurrentLocation.getLongitude());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void back_pressed(View view) {
        finish();
    }

    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    private static String result;
    private static String throughfare;
    private static String locality;
    private static String street;
    public void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        String[] s = address.getAddressLine(0).split(",[ ]*");
                        if (s[0] == null && s[1] == null) {
                            result = "Loading...";
                        } else if (s[0] != null && s[1] == null) {
                            result = s[0];
                        } else if (s[0] == null && s[1] != null) {
                            result = s[1];
                        } else {
                            result = s[0] + ", \n" + s[1];
                            Log.e(TAG, "run: " + s[0] +" "+ s[1] );
                            throughfare = address.getThoroughfare();
                            locality = address.getLocality();
                            street = address.getFeatureName();
//                            if(address.getLocality().length()>0&&result.length()>0){
//                                //Log.e(TAG, result+" '''' "+result.substring(0,result.indexOf(address.getLocality())));
//                                result = !(address.getLocality().equals(""))? result.substring(0,result.indexOf(address.getLocality())):"Loading...";
//                            }else
//                                result = "Loading...";
                            //Log.e(TAG, result.substring(0,result.indexOf(address.getLocality())));
                            Log.e(TAG, "run: "+ address.toString() );
                            String[] addre = address.getAddressLine(0).split(",");

                            Log.e(TAG, " getFeatureName " + address.getFeatureName() + " getAddressLine " + address.getAddressLine(0).split(",") + " getAdminArea " + address.getAdminArea() + " getLocality " + address.getLocality() + " getPremises " + address.getPremises() + " getSubAdminArea " + address.getSubAdminArea() + " getSubLocality " + address.getSubLocality() + " getMaxAddressLineIndex " + address.getMaxAddressLineIndex() + " locale " + address.getLocale());
                            if( address.getLocality().equals("Anderlecht") || address.getLocality().equals("Auderghem")
                                    || address.getLocality().equals("Berchem")|| address.getLocality().equals("Ste-Agathe")
                                    || address.getLocality().equals("Bruxelles")|| address.getLocality().equals("Etterbeek")
                                    || address.getLocality().equals("Evere")|| address.getLocality().equals("Forest")
                                    || address.getLocality().equals("Ganshoren")|| address.getLocality().equals("Ixelles")
                                    || address.getLocality().equals("Jette")|| address.getLocality().equals("Koekelberg")
                                    || address.getLocality().equals("Molenbeek")|| address.getLocality().equals("St-Jean")
                                    || address.getLocality().equals("St-Gilles")|| address.getLocality().equals("St-Josse-ten-Noode")
                                    || address.getLocality().equals("Schaerbeek")|| address.getLocality().equals("Uccle")
                                    || address.getLocality().equals("Watermael-Boitsfort")|| address.getLocality().equals("Woluwe-St-Lambert")
                                    || address.getLocality().equals("Woluwe-St-Pierre")){
//                            if( address.getAddressLine(0).contains("Brussel")){
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        textView_address.setTextSize(18);

                                    }
                                });
                            }else{
                                result = "Only the incidents staying within the Belgium region  boundaries can be introduced in the application";
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        textView_address.setTextSize(15);

                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    break;
                default:
                    result = null;
            }
            // replace by what you need to do
            if (result != null) {
                textView_address.setText(result);
            }
        }
    }

    void getMyLocation() {
        //map.setMyLocationEnabled(true);
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            return;
        }

        // Report to the UI that the location was updated
        mCurrentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mGoogleMap.moveCamera(cameraUpdate);
        getAddressFromLocation(location, getApplicationContext(), new GeocoderHandler());
    }

    public void order(View view) {
        CustomDialogClass cdd=new CustomDialogClass(MapsActivity.this);
        cdd.show();
    }

    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog2);
            yes =  findViewById(R.id.btn_yes);
            no =  findViewById(R.id.btn_no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:

                    //c.finish();
                    //send2();
                    break;
                case R.id.btn_no:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }


}


