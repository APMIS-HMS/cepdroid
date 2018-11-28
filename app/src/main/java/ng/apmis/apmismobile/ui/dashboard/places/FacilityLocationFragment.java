package ng.apmis.apmismobile.ui.dashboard.places;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.GeofenceTransitionsIntentService;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;


public class FacilityLocationFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    GoogleMap gMap;
    GeofencingClient mGeofencingClient;
    ArrayList<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent;
    private LatLngBounds.Builder mBounds;

    public FacilityLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facility_location, container, false);

        mGeoDataClient = Places.getGeoDataClient(getContext());
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mGeofencingClient = LocationServices.getGeofencingClient(getContext());
        mGeofenceList = new ArrayList<>();
        mBounds = new LatLngBounds.Builder();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.place_map);
        supportMapFragment.getMapAsync(this);

        if (checkPermissions()) {
            /*updateLocationUI();
            getDeviceLocation();*/

        } else {
            requestPermissions();
        }


        return rootView;
    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
                8));
    }

    void updateLocationUI() {
        if (gMap == null) return;
        try {
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);

            gMap.setMyLocationEnabled(true);
            gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().include(ll).build(),16));
                    // we only want to grab the location once, to allow the user to pan and zoom freely.
                    gMap.setOnMyLocationChangeListener(null);
                }
            });

        }catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
        /*supportMapFragment.getMapAsync(googleMap -> {
            googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        });*/

    }

    void getDeviceLocation () {
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), task -> {
                Location mLastKnowLocation = (Location) task.getResult();
                LatLng ll = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                if (task.isSuccessful())

                    addPointToViewPort(ll);
                    // we only want to grab the location once, to allow the user to pan and zoom freely.
                    gMap.setOnMyLocationChangeListener(null);

                   /* mGeofenceList.add(new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId("here")

                            .setCircularRegion(
                                    mLastKnowLocation.getLatitude(),
                                    mLastKnowLocation.getLongitude(),
                                    Constants.GEOFENCE_RADIUS_IN_METERS
                            )
                            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build());


                    mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("Geofence", "Successful");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Geofence", e.getMessage());
                                }
                            });
                gMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude())).title("My Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), 16.0f));
*/
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    boolean checkPermissions () {
        return ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

     /*   if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }*/

    }

    void requestPermissions () {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            checkPermissions();
        } else {
            Toast.makeText(this.getActivity(), "Accessing Location", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateLocationUI();
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.LOCATION, false);
            ((DashboardActivity)getActivity()).mBottomNav.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMyLocationEnabled(true);
        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getDeviceLocation();
                return true;
            }
        });
    }
}
