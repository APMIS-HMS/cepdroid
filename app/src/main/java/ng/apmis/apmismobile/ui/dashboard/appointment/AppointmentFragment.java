package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/8/2018.
 */

public class AppointmentFragment extends Fragment implements View.OnClickListener {

    AppointmentAdapter appointmentAdapterForRecent;
    AppointmentAdapter appointmentAdapterForPrevious;
    boolean showMorePrevious;

    ProgressBar loadUpcomingProgress;
    ProgressBar loadPastProgress;

    @BindView(R.id.previous_appointment_more_less)
    TextView showMorePreviousTv;

    private SharedPreferencesManager sharedPreferencesManager;
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private AppointmentViewModel appointmentViewModel;

    RecyclerView appointmentBookedRecycler;
    RecyclerView previousAppointmentRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        ButterKnife.bind(this, rootView);

        assert getActivity() != null;
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
        ((DashboardActivity)getActivity()).setToolBarTitle(Constants.APPOINTMENTS, false);
        sharedPreferencesManager = new SharedPreferencesManager(getActivity());
        appointmentBookedRecycler = rootView.findViewById(R.id.recently_booked_recycler);
        previousAppointmentRecycler = rootView.findViewById(R.id.previous_appointment_recycler);
        rootView.findViewById(R.id.appointment_add_fab).setOnClickListener(this);

        loadUpcomingProgress = rootView.findViewById(R.id.recently_booked_loader);
        loadPastProgress = rootView.findViewById(R.id.previous_appointment_loader);


        InjectorUtils.provideNetworkData(getActivity()).fetchAppointmentsForPerson(sharedPreferencesManager.getPersonId());

        initRecyclerViews();
        initViewModel();

        return rootView;
    }



    public void initViewModel(){
        AppointmentViewModelFactory factory = InjectorUtils.provideAppointmentViewModelFactory(getActivity().getApplicationContext());
        appointmentViewModel = ViewModelProviders.of(this, factory).get(AppointmentViewModel.class);

        final Observer<List<Appointment>> appointmentsObserver = appointments -> {

            this.appointments.clear();
            this.appointments.addAll(appointments);

            if (appointmentAdapterForRecent == null && appointmentAdapterForPrevious == null) {

                appointmentAdapterForRecent = new AppointmentAdapter(getActivity());
                appointmentAdapterForPrevious = new AppointmentAdapter(getActivity());

                appointmentAdapterForRecent.setAppointmentList(this.appointments, true);
                appointmentAdapterForPrevious.setAppointmentList(this.appointments, false);

                appointmentBookedRecycler.setAdapter(appointmentAdapterForRecent);
                previousAppointmentRecycler.setAdapter(appointmentAdapterForPrevious);

                hideLoaders();
                Log.d("Called fresh", this.appointments.size()+"");

            } else {
                Log.d("Called notify", this.appointments.size()+"");
                appointmentAdapterForPrevious.clear();
                appointmentAdapterForRecent.clear();
                appointmentAdapterForRecent.setAppointmentList(this.appointments, true);
                appointmentAdapterForPrevious.setAppointmentList(this.appointments, false);
                appointmentAdapterForRecent.notifyDataSetChanged();
                appointmentAdapterForPrevious.notifyDataSetChanged();
            }
        };

        appointmentViewModel.getAppointmentsForPatient().observe(getActivity(), appointmentsObserver);
    }

    private void initRecyclerViews(){

        appointmentBookedRecycler.setNestedScrollingEnabled(false);
        previousAppointmentRecycler.setNestedScrollingEnabled(false);
        showMorePreviousTv.setOnClickListener(this);


        appointmentBookedRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        previousAppointmentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void hideLoaders(){
        loadUpcomingProgress.setVisibility(View.INVISIBLE);
        loadPastProgress.setVisibility(View.INVISIBLE);
    }



    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
        ((DashboardActivity)getActivity()).setToolBarTitle("VIEW", false);
        appointmentViewModel.getAppointmentsForPatient().removeObservers(getActivity());
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
            case R.id.previous_appointment_more_less:
                if (!showMorePrevious) {
                    appointmentAdapterForPrevious.showMore();
                    showMorePrevious = true;
                    showMorePreviousTv.setText(getResources().getString(R.string.show_less));
                    Drawable rightDrawable = VectorDrawableCompat
                            .create(getContext().getResources(), R.drawable.ic_arrow_drop_up, null);
                    showMorePreviousTv.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
                } else {
                    appointmentAdapterForPrevious.showLess();
                    showMorePrevious = false;
                    showMorePreviousTv.setText(getResources().getString(R.string.show_more));
                    Drawable rightDrawable = VectorDrawableCompat
                            .create(getContext().getResources(), R.drawable.ic_arrow_drop_down, null);
                    showMorePreviousTv.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
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
