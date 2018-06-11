package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointments;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/8/2018.
 */

public class AppointmentFragment extends Fragment implements View.OnClickListener {

    AppointmentAdapter appointmentAdapterForRecent;
    AppointmentAdapter appointmentAdapterForPrevious;
    boolean showMorePrevious, showMoreRecent = false;
    @BindView(R.id.recently_booked_more_less)
    TextView showMoreRecentTv;
    @BindView(R.id.previous_appointment_more_less)
    TextView showMorePreviousTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        ButterKnife.bind(this, rootView);

        assert getActivity() != null;
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle("APPOINTMENTS", false);
        ArrayList<Appointments> appointments = new ArrayList<>();
        appointments.add(new Appointments("LightHouse Hospital", "Emergency Clinic", new Date().getTime()));
        appointments.add(new Appointments("APMIS HMS", "Maternity Clinic", new Date().getTime()));
        appointments.add(new Appointments("Sacred Heart Hospital", "GOP Clinic", new Date().getTime()));
        appointments.add(new Appointments("Lagoon Hospital", "Maternity Clinic", new Date().getTime()));
        appointments.add(new Appointments("Mobonike Hospital", "GOP Clinic", new Date().getTime()));
        appointments.add(new Appointments("Promise Hospital", "Emergency Clinic", new Date().getTime()));

        RecyclerView appointmentBookedRecycler = rootView.findViewById(R.id.recently_booked_recycler);
        RecyclerView previousAppointmentRecycler = rootView.findViewById(R.id.previous_appointment_recycler);
        showMorePreviousTv.setOnClickListener(this);
        showMoreRecentTv.setOnClickListener(this);
        rootView.findViewById(R.id.appointment_add_fab).setOnClickListener(this);

        appointmentAdapterForRecent = new AppointmentAdapter(getActivity());
        appointmentAdapterForPrevious = new AppointmentAdapter(getActivity());

        appointmentBookedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        previousAppointmentRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        appointmentBookedRecycler.setAdapter(appointmentAdapterForRecent);
        previousAppointmentRecycler.setAdapter(appointmentAdapterForPrevious);

        appointmentAdapterForRecent.setAppointmentList(appointments, true);
        appointmentAdapterForPrevious.setAppointmentList(appointments, false);


        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
        ((DashboardActivity)getActivity()).setToolBarTitle("VIEW", false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle("APPOINTMENTS", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recently_booked_more_less:
                if (!showMoreRecent) {
                    appointmentAdapterForRecent.showMore();
                    showMoreRecent = true;
                    showMoreRecentTv.setText(getResources().getString(R.string.show_less));
                } else {
                    appointmentAdapterForRecent.showLess();
                    showMoreRecent = false;
                    showMoreRecentTv.setText(getResources().getString(R.string.show_more));
                }
                break;
            case R.id.previous_appointment_more_less:
                if (!showMorePrevious) {
                    appointmentAdapterForPrevious.showMore();
                    showMorePrevious = true;
                    showMorePreviousTv.setText(getResources().getString(R.string.show_less));
                } else {
                    appointmentAdapterForPrevious.showLess();
                    showMorePrevious = false;
                    showMorePreviousTv.setText(getResources().getString(R.string.show_more));
                }
                break;
            case R.id.appointment_add_fab:
                Toast.makeText(getActivity(), "Add New Appointment", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new AddAppointmentFragment())
                        .addToBackStack("appointment")
                        .commit();
        }
    }
}
