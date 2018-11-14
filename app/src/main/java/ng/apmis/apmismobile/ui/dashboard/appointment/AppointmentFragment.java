package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/8/2018.
 */

public class AppointmentFragment extends Fragment implements View.OnClickListener {

    AppointmentAdapter appointmentAdapter;

    private SharedPreferencesManager sharedPreferencesManager;
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private AppointmentViewModel appointmentViewModel;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @BindView(R.id.appointments_shimmer)
    ShimmerFrameLayout appointmentsShimmer;

    RecyclerView appointmentRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        ButterKnife.bind(this, rootView);

        appointmentsShimmer.startShimmer();

        assert getActivity() != null;
        sharedPreferencesManager = new SharedPreferencesManager(getActivity());
        appointmentRecycler = rootView.findViewById(R.id.recently_booked_recycler);
        rootView.findViewById(R.id.appointment_add_fab).setOnClickListener(this);

        InjectorUtils.provideNetworkData(getActivity()).fetchAppointmentsForPerson(sharedPreferencesManager.getPersonId());

        appointmentRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        TransitionManager.beginDelayedTransition(relativeLayout);
        TransitionManager.beginDelayedTransition(appointmentRecycler);

        if (appointmentAdapter != null){
            appointmentsShimmer.stopShimmer();
            appointmentsShimmer.setVisibility(View.GONE);
            appointmentRecycler.setAdapter(appointmentAdapter);
        }

        initViewModel();

        return rootView;
    }



    public void initViewModel(){
        AppointmentViewModelFactory factory = InjectorUtils.provideAppointmentViewModelFactory(getActivity().getApplicationContext());
        appointmentViewModel = ViewModelProviders.of(this, factory).get(AppointmentViewModel.class);

        final Observer<List<Appointment>> appointmentsObserver = appointments -> {

            this.appointments.clear();
            this.appointments.addAll(appointments);


            if (appointmentAdapter == null) {

                appointmentAdapter = new AppointmentAdapter(getActivity());

                appointmentAdapter.setAppointmentList(this.appointments);

                appointmentRecycler.setAdapter(appointmentAdapter);

                appointmentsShimmer.stopShimmer();
                appointmentsShimmer.setVisibility(View.GONE);

                Log.d("Called fresh", this.appointments.size()+"");

            } else {
                Log.d("Called notify", this.appointments.size()+"");
                appointmentAdapter.clear();
                appointmentAdapter.setAppointmentList(this.appointments);
                appointmentAdapter.notifyDataSetChanged();
            }
        };

        appointmentViewModel.getAppointmentsForPatient().observe(getActivity(), appointmentsObserver);
    }



    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
        appointmentViewModel.getAppointmentsForPatient().removeObservers(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle(Constants.APPOINTMENTS, false);
        ((DashboardActivity)getActivity()).profileImage.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
