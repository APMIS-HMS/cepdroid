package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduledClinic;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ClinicAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.EmployeeAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.FacilityAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ScheduleAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ServiceAdapter;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AddAppointmentFragment extends Fragment {

    @BindView(R.id.book_appointment_button)
    Button bookAppointmentButton;

    @BindView(R.id.select_hospital_spinner)
    Spinner selectHospitalSpinner;

    @BindView(R.id.select_clinic_spinner)
    Spinner selectClinicSpinner;

    @BindView(R.id.select_service_spinner)
    Spinner selectServiceSpinner;

    @BindView(R.id.whom_to_see_spinner)
    Spinner whomToSeeSpinner;

    @BindView(R.id.clinic_schedule_spinner)
    Spinner clinicScheduleSpinner;

    @BindView(R.id.your_schedule_textview)
    TextView yourScheduleTextView;

    @OnClick(R.id.your_schedule_textview)
    void scheduleTextViewClicked(){
        showDatePicker();
    }



    FacilityAdapter hospitalAdapter;
    ClinicAdapter clinicAdapter;
    ServiceAdapter serviceAdapter;
    EmployeeAdapter employeeAdapter;
    ScheduleAdapter scheduleAdapter;
    AddAppointmentViewModel appointmentViewModel;

    private List<Facility> mFacilities;
    private List<ScheduledClinic> mClinics;
    private List<Service> mServices;
    private List<Employee> mEmployees;
    private List<ScheduleItem> mSchedules;

    private Facility mSelectedFacility;
    private ScheduledClinic mSelectedClinic;
    private Service mSelectedService;
    private Employee mSelectedDoctor;
    private ScheduleItem mSelectedSchedule;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment_form, container, false);
        ButterKnife.bind(this, rootView);

        initViewModel();

        selectHospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the facility id of the work by reducing position by 1
                if (position > 0) {
                    InjectorUtils.provideNetworkData(getContext()).fetchClinicSchedulesForFacility(mFacilities.get(position - 1).getId());
                    InjectorUtils.provideNetworkData(getContext()).fetchCategoriesForFacility(mFacilities.get(position - 1).getId());
                    InjectorUtils.provideNetworkData(getContext()).fetchEmployeesForFacility(mFacilities.get(position - 1).getId());
                } else {
                    appointmentViewModel.resetClinics();
                    appointmentViewModel.resetServices();
                    appointmentViewModel.resetEmployees();
                    appointmentViewModel.resetSchedules();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectClinicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the clinic id of the work by reducing position by 1
                if (position > 0) {
                    appointmentViewModel.setSchedules(mClinics.get(position-1));


                } else {
                    appointmentViewModel.resetSchedules();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clinicScheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the correct schedule by reducing position by 1
                if (position > 0) {
                    mSelectedSchedule = mSchedules.get(position-1);

                } else {
                    appointmentViewModel.resetSchedules();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bookAppointmentButton.setOnClickListener((view) -> {
            //TODO sumbit the form and on success
            getActivity().onBackPressed();
        });

        return rootView;
    }

    private void initViewModel(){
        AddAppointmentViewModelFactory factory = InjectorUtils.provideAppointmentViewModelFactory(getActivity().getApplicationContext());
        appointmentViewModel = ViewModelProviders.of(this, factory).get(AddAppointmentViewModel.class);

        final Observer<List<Patient>> patientsObserver = patients -> {
            mFacilities = getFacilitiesFromPatients(patients);

            if (hospitalAdapter == null) {
                hospitalAdapter = new FacilityAdapter(getContext(), 0, mFacilities);
                selectHospitalSpinner.setAdapter(hospitalAdapter);
            } else {
                hospitalAdapter.notifyDataSetChanged();
            }
        };

        appointmentViewModel.getPatientsForPerson().observe(getActivity(), patientsObserver);


        final Observer<List<ScheduledClinic>> clinicsObserver = clinics -> {
            mClinics = clinics;

            if (clinicAdapter == null) {
                clinicAdapter = new ClinicAdapter(getContext(), 0, mClinics);
                selectClinicSpinner.setAdapter(clinicAdapter);
            } else {
                clinicAdapter.clear();
                clinicAdapter.addAllClinics(mClinics);
                clinicAdapter.notifyDataSetChanged();
                selectClinicSpinner.setSelection(0);
            }
        };

        appointmentViewModel.getClinics().observe(getActivity(), clinicsObserver);


        final Observer<List<Service>> serviceObserver = services -> {
            mServices = services;

            if (serviceAdapter == null) {
                serviceAdapter = new ServiceAdapter(getContext(), 0, mServices);
                selectServiceSpinner.setAdapter(serviceAdapter);
            } else {
                serviceAdapter.clear();
                serviceAdapter.addAllServices(mServices);
                serviceAdapter.notifyDataSetChanged();
                selectServiceSpinner.setSelection(0);
            }
        };

        appointmentViewModel.getServices().observe(getActivity(), serviceObserver);


        final Observer<List<Employee>> employeeObserver = employees -> {
            mEmployees = employees;

            if (employeeAdapter == null) {
                employeeAdapter = new EmployeeAdapter(getContext(), 0, mEmployees);
                whomToSeeSpinner.setAdapter(employeeAdapter);
            } else {
                employeeAdapter.clear();
                employeeAdapter.addAllEmployees(mEmployees);
                employeeAdapter.notifyDataSetChanged();
                whomToSeeSpinner.setSelection(0);
            }
        };

        appointmentViewModel.getEmployees().observe(getActivity(), employeeObserver);


        final Observer<List<ScheduleItem>> scheduleObserver = schedules -> {
            mSchedules = schedules;

            if (scheduleAdapter == null) {
                scheduleAdapter = new ScheduleAdapter(getContext(), 0, mSchedules);
                clinicScheduleSpinner.setAdapter(scheduleAdapter);
            } else {
                scheduleAdapter.clear();
                scheduleAdapter.addAllSchedules(mSchedules);
                scheduleAdapter.notifyDataSetChanged();
                clinicScheduleSpinner.setSelection(0);
            }
        };

        appointmentViewModel.getSchedules().observe(getActivity(), scheduleObserver);

    }


    private List<Facility> getFacilitiesFromPatients(List<Patient> patients){

        List<Facility> facilities = new ArrayList<>();

        for (Patient patient : patients){
            facilities.add(patient.getFacilityObj());
        }

        return facilities;
    }

    private void showDatePicker(){

        if (mSelectedSchedule == null){
            Toast.makeText(getContext(), "Please select a schedule first", Toast.LENGTH_LONG).show();
            return;
        }

        Date dates[] = AppUtils.getNextScheduleTimeLimits(mSelectedSchedule, false);

        Date start = dates[0];
        Date end = dates[1];

        final DatePickerDialog.OnDateSetListener startDateSetListener = (view, year, month, day) -> {
            showTimePicker(year, month, day);
        };

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(start);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogStart = new DatePickerDialog(getContext(),
                startDateSetListener, year, month, day);

        dialogStart.getDatePicker().setMinDate(start.getTime());

        dialogStart.show();
    }

    private void showTimePicker(int year, int month, int day ){

        Date dates[] = AppUtils.getNextScheduleTimeLimits(mSelectedSchedule, false);

        Date start = dates[0];
        Date end = dates[1];

        final TimePickerDialog.OnTimeSetListener startTimeSetListener = (view, hourOfDay, minute) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(year, month, day, hourOfDay, minute, 0);

            Log.i("Calendar Start", start.toString());
            Log.i("Calendar", calendar.getTime().toString());
            Log.i("Calendar End", end.toString());

            if (calendar.getTime().before(start) || calendar.getTime().after(end)) {
                Toast.makeText(AddAppointmentFragment.this.getContext(),
                        "Please ensure your appointment time falls within the selected schedule",
                        Toast.LENGTH_LONG).show();
            } else {
                String scheduleText = AppUtils.dateToReadableString(calendar.getTime());
                yourScheduleTextView.setText(scheduleText);
            }

        };


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialogStart = new TimePickerDialog(getContext(),
                startTimeSetListener, hourOfDay, minute, false);

        dialogStart.show();
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
