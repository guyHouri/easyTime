package com.example.easytime101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (v==addplace) {
            if (placeSearched==true) {
                Intent intentplace = new Intent(this, open2.class);
                intentplace.putExtra("location", mSearchText.getText().toString() );
                intentplace.putExtra("happened", "true");
                intentplace.putExtra("inthap", "10");
                Log.d("intent", "happened = true");
                Intent name = getIntent();
                String sName = name.getStringExtra("theNameOfTheUser");
                intentplace.putExtra("theNameOfTheUser", name);
                startActivity(intentplace);
            }
            else
                Toast.makeText(this, "please choose location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("googleapiclient", "override in connection failed");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                return;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private GoogleMap mMap;
    SharedPreferences sp;
    int count=0;
    MarkerOptions options;
    Marker addMarker ;
    private GoogleApiClient mGoogleApiClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted=false;
    private static final int locationPermissionRequestCode=1234;
    private static final int erroeDialogRequest=9001;
    private static final float defaultZoom = 15f;
    private static boolean placeSearched=false;
    private FusedLocationProviderClient mfusedLocationProviderClient;
   // private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private AutoCompleteTextView mSearchText;
    private ImageView mSearchIcon;
    private ImageView mGps;
    private Button addplace;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        sp=getSharedPreferences("ifswitched",0);
        mSearchIcon = (ImageView) findViewById(R.id.magnify);
        mGps = (ImageView) findViewById(R.id.gps);
        addplace = (Button) findViewById(R.id.btnaddplace);
        addplace.setOnClickListener(this);
        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
        mLocationPermissionGranted=true;
        initMap();

    }

    public void initFragment() {

    }

    private void init() { // kashor lehipus
        Log.d("init", "initializing");


       // mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API)
        //        .enableAutoManage(this, this).build();
        Log.d("googleapiclient", "build new");
        //mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS );
        //Log.d("googleapiclient", "place auto complete adapter");
       // mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        //Log.d("googleapiclient", "setadapter");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyevent) {
                if( actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        keyevent.getAction() == KeyEvent.ACTION_DOWN  || keyevent.getAction() == KeyEvent.KEYCODE_ENTER ) {
                    geoLocate();
                }
                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("gps", "clicked gps icon");
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
        hideKeyboard(this);
    }

    private void geoLocate () {
        hideSoftKeyboard();
        Boolean a = true;
        hideKeyboard(this);
        Log.d("geolocate", "geolocating");
        String searchString = mSearchText.getText().toString();
        final Geocoder geocoder = new Geocoder(map.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
            Log.d("geolocate", "got to try");
        } catch (IOException e) {
            Log.d("geolocate", "got to catch");
            Log.d("h", "ide exeption"+e.getMessage());
        }
        if (list.size()>0) {
            Log.d("geolocate", "list size > 0");
            Address address=list.get(0);
            Log.d("geolocate", "found location"+address.toString());
             Toast.makeText(this, "geolocate"+address.toString(), Toast.LENGTH_SHORT);
            placeSearched=true;
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), defaultZoom, address.getAddressLine(0));
        } else {
            Log.d("geolocate", "got to else, list size < 0");
            Toast.makeText(this, "couldnt find location", Toast.LENGTH_LONG).show();
        }
    }

    private void getDeviceLocation () {
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("getDeviceLocation", "found location");
                            Location currentlocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()),
                                    defaultZoom, "my location");
                        }
                        else
                            Toast.makeText(map.this, "unable to get current location", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } catch (SecurityException e) {
            Log.d("getDeviceLocation", "catch");
        }
    }

    private void moveCamera (LatLng latLng, float zoom, String title) {
        Log.d("moveCamera", "moving camera");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!title.equals("my location")) {
            if (options!=null)
                mMap.clear();
            options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
            count++;
        }
        hideKeyboard(this);
        hideSoftKeyboard();
        // marker pin
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(map.this);
        Log.d("in", "init map");
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch (requestCode) {
            case locationPermissionRequestCode :{
                if (grantResults.length>0) {
                    for (int i =0; i<grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            mLocationPermissionGranted = false;
                        return;
                    }
                }
                mLocationPermissionGranted=true;
                // initialiize map
                initMap();
                }
            }
        }

}
