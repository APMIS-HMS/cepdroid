package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/8/2018.
 */

public class AppointmentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle("APPOINTMENTS", false);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }

}
