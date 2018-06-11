package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointments;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/8/2018.
 */

public class AppointmentFragment extends Fragment implements View.OnClickListener {

    AppointmentAdapterInteraction appointmentAdapterInteractionListener;
    AppointmentAdapter appointmentAdapterForRecent;
    AppointmentAdapter appointmentAdapterForPrevious;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

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
        rootView.findViewById(R.id.previous_appointment_more_less).setOnClickListener(this);
        rootView.findViewById(R.id.recently_booked_more_less).setOnClickListener(this);

        appointmentAdapterForRecent = new AppointmentAdapter(getActivity());
        appointmentAdapterForPrevious = new AppointmentAdapter(getActivity());

        appointmentBookedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        previousAppointmentRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        appointmentBookedRecycler.setAdapter(appointmentAdapterForRecent);
        previousAppointmentRecycler.setAdapter(appointmentAdapterForPrevious);

        appointmentAdapterForRecent.setAppointmentList(appointments);
        appointmentAdapterForPrevious.setAppointmentList(appointments);


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        assert getActivity() != null;
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recently_booked_more_less:
                Toast.makeText(getActivity(), "recently booked", Toast.LENGTH_SHORT).show();
                if ((appointmentAdapterForRecent.num *2) < appointmentAdapterForRecent.appointmentList.size()) {
                    appointmentAdapterForRecent.num = appointmentAdapterForRecent.appointmentList.size();
                } else {
                    appointmentAdapterForRecent.num = appointmentAdapterForRecent.num * 2;
                }
                break;
            case R.id.previous_appointment_more_less:
                Toast.makeText(getActivity(), "previously booked", Toast.LENGTH_SHORT).show();
                if ((appointmentAdapterForPrevious.num *2) < appointmentAdapterForPrevious.appointmentList.size()) {
                    appointmentAdapterForPrevious.num = appointmentAdapterForPrevious.appointmentList.size();
                } else {
                    appointmentAdapterForPrevious.num = appointmentAdapterForPrevious.num * 2;
                }
                break;
        }
    }

    interface AppointmentAdapterInteraction {
        void showMore (View view);
        void showLess (View view);
    }

}
