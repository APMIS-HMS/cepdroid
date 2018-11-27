package ng.apmis.apmismobile.ui.dashboard.places;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FacilityLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FacilityLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilityLocationFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SupportMapFragment supportMapFragment;
    GeofencingClient geofencingClient;
    ArrayList<Geofence> geofenceList = new ArrayList<>();
    GoogleApiClient googleApiClient;
    LatLng latLng;
    LocationRequest locationRequest;

    public FacilityLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FacilityLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FacilityLocationFragment newInstance(String param1, String param2) {
        FacilityLocationFragment fragment = new FacilityLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facility_location, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.place_map);


        if (checkPermissions()) {
            setUpGoogleApiClientAndLocation();
        } else {
            requestPermissions();
        }


        return rootView;
    }

    void setUpGoogleApiClientAndLocation () {
        geofencingClient = LocationServices.getGeofencingClient(getActivity());

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

      /*  geofenceList.add(new Geofence.Builder()
        .setCircularRegion())*/
    }


    boolean checkPermissions () {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermissions () {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
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
        if (googleApiClient.isConnected()) googleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpGoogleApiClientAndLocation();
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.LOCATION, false);
            ((DashboardActivity)getActivity()).mBottomNav.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("connected", "connected");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(5000);
        if (checkPermissions()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Failed", "failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("location changed", location.toString());
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("Lat Lng", String.valueOf(latLng));
        supportMapFragment.getMapAsync(googleMap -> {
            googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
