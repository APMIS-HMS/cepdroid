package ng.apmis.apmismobile.ui.dashboard.places;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.GeofenceTransitionsIntentService;
import ng.apmis.apmismobile.data.database.facilityModel.Address;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;


public class FacilityLocationFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient mFusedLocationProviderClient;
    GoogleMap gMap;
    GeofencingClient mGeofencingClient;
    ArrayList<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent;
    Address[] facilityAddresses = null;
    Location currentLocation;

    public FacilityLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facility_location, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.place_map);
        supportMapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
        mGeofenceList = new ArrayList<>();

        return rootView;
    }

    void initViewModel() {
        FacilityLocationFactory facilityLocationFactory = InjectorUtils.provideFacilityLocationFactory(getContext());
        FacilityLocationViewModel facilityLocationViewModel = ViewModelProviders.of(this, facilityLocationFactory).get(FacilityLocationViewModel.class);

        facilityLocationViewModel.getFacilityLocations().observe(this, facilityLocations -> {
            LatLng latLng;
            if (facilityLocations != null) {
                facilityAddresses = new Address[facilityLocations.length];
                for (int i = 0; i < facilityLocations.length; i++) {
                    if (facilityLocations[i].getAddress() != null) {
                        latLng = new LatLng(facilityLocations[i].getAddress().getGeometry().getLocation().getLat(), facilityLocations[i].getAddress().getGeometry().getLocation().getLng());
                        facilityAddresses[i] = facilityLocations[i].getAddress();
                        if (gMap != null) {
                            gMap.addMarker(new MarkerOptions().position(latLng).title(facilityLocations[i].getName()));
                        }
                    }
                }
            }
        });
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

    boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1000 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "Grant permission to use this activity", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
            setupMap();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((DashboardActivity) getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.LOCATION, false);
            ((DashboardActivity) getActivity()).mBottomNav.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    void setupMap() {

        if (checkPermissions()) {

            Toast.makeText(this.getActivity(), "Accessing Location", Toast.LENGTH_SHORT).show();

            initViewModel();

            gMap.getUiSettings().setZoomControlsEnabled(true);
            //gMap.setMinZoomPreference(15);

            gMap.setMyLocationEnabled(true);

            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Log.e("Current location", String.valueOf(location));
                            this.currentLocation = location;
                            gMap.addMarker(new MarkerOptions().position(new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude())).title("My Location"));

                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()), 16));

                            gMap.setOnMyLocationButtonClickListener(() -> {
                                Log.e("gmap current location", String.valueOf(currentLocation));
                                mGeofenceList.add(new Geofence.Builder()
                                        .setRequestId("Here")
                                        .setCircularRegion(this.currentLocation.getLatitude(),
                                                this.currentLocation.getLongitude(),
                                                Constants.GEOFENCE_RADIUS_IN_METERS)
                                        .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER)
                                        .setLoiteringDelay(10000)
                                        .build()
                                );

                                mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                                        .addOnSuccessListener(success -> {
                                            Log.e("geo fence added", "success");
                                        })
                                        .addOnFailureListener(failure -> {
                                            Log.e("geo fence failed", "failed");
                                        });

                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()), 16));
                                return true;

                            });
                        }
                    })
                    .addOnFailureListener(failed -> {
                        Log.e("location failed", failed.getMessage());
                    });

        } else {
            requestPermissions();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        setupMap();
    }

}
