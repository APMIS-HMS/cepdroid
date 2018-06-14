package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AddAppointmentFragment extends Fragment {

    @BindView(R.id.book_appointment_button)
    Button bookAppointmentButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment_form, container, false);
        ButterKnife.bind(this, rootView);

        bookAppointmentButton.setOnClickListener((view) -> {
            //TODO sumbit the form and on success
            getActivity().onBackPressed();
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle("APPOINTMENTS", false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle("NEW APPOINTMENT", false);
    }
}
