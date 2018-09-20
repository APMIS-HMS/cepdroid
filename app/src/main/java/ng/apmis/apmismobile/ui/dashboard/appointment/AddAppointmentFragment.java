package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.facilityModel.AppointmentType;
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
import ng.apmis.apmismobile.utilities.AlarmBroadcast;
import ng.apmis.apmismobile.utilities.AlarmManagerSingleton;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AddAppointmentFragment extends Fragment {

    @BindView(R.id.new_radio)
    RadioButton newRadio;

    @BindView(R.id.follow_up_radio)
    RadioButton followUpRadio;

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

    @BindView(R.id.set_reminder_switch)
    Switch setReminderSwitch;

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


    private List<Patient> mPatients;
    private List<Facility> mFacilities;
    private List<ScheduledClinic> mClinics;
    private List<Service> mServices;
    private List<Employee> mEmployees;
    private List<ScheduleItem> mSchedules;

    private AppointmentType mAppointmentType;
    private OrderStatus mOrderStatus;
    private Patient mPatient;
    private Facility mSelectedFacility;
    private ScheduledClinic mSelectedClinic;
    private Service mSelectedService;
    private Employee mSelectedDoctor;
    private ScheduleItem mSelectedSchedule;
    private Date mYourSchedule;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment_form, container, false);
        ButterKnife.bind(this, rootView);

        progressDialog = new ProgressDialog(getContext());

        initViewModel();

        //Fetch the available appointment types and order statuses
        if (appointmentViewModel.getAppointmentTypes().isEmpty())
            InjectorUtils.provideNetworkData(getContext()).fetchAppointmentTypes();

        if (appointmentViewModel.getOrderStatuses().isEmpty())
            InjectorUtils.provideNetworkData(getContext()).fetchOrderStatuses();

        setSpinnerItemSelectListeners();

        bookAppointmentButton.setOnClickListener((view) -> {
            if (validateAppointmentBody()) {

                submitAppointment();
            }
        });

        return rootView;
    }

    private void initViewModel(){
        AddAppointmentViewModelFactory factory = InjectorUtils.provideAddAppointmentViewModelFactory(getActivity().getApplicationContext());
        appointmentViewModel = ViewModelProviders.of(this, factory).get(AddAppointmentViewModel.class);

        final Observer<List<Patient>> patientsObserver = patients -> {
            mPatients = patients;
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

        final Observer<Appointment> appointmentObserver = appointment -> {

            //Only record observations if appointment body is valid
            if (validateAppointmentBody()) {

                if (appointment != null) {
                    //Save to room here
                    appointment.setFacilityName(appointment.getPatientDetails().getFacilityObj().getName());
                    appointment.setPersonId(appointment.getPatientDetails().getPersonId());
                    appointmentViewModel.insertAppointment(appointment);

                    hideProgressDialog();
                    displaySuccessDialog(appointment.get_id());

                } else {
                    displayErrorDialog();
                }
            }
        };

        appointmentViewModel.getAppointment().observe(getActivity(), appointmentObserver);

    }

    private List<Facility> getFacilitiesFromPatients(List<Patient> patients){

        List<Facility> facilities = new ArrayList<>();

        for (Patient patient : patients){
            facilities.add(patient.getFacilityObj());
        }

        return facilities;
    }

    private void resetAllSelectedFields(){
        mPatient = null;
        mSelectedFacility = null;
        mSelectedClinic = null;
        mSelectedService = null;
        mSelectedDoctor = null;
        mSelectedSchedule = null;
        mYourSchedule = null;
    }

    private void showDatePicker(){

        if (mSelectedSchedule == null){
            Toast.makeText(getContext(), "Please select clinic schedule first", Toast.LENGTH_LONG).show();
            return;
        }

        Date dates[] = AppUtils.getNextScheduleTimeLimits(mSelectedSchedule, false, null);
        Date start = dates[0];

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

        Date dates[] = AppUtils.getNextScheduleTimeLimits(mSelectedSchedule, false, null);
        Date start = dates[0];

        final TimePickerDialog.OnTimeSetListener startTimeSetListener = (view, hourOfDay, minute) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(year, month, day, hourOfDay, minute, 0);

            if (AppUtils.isInPossibleScheduleTimeLimit(mSelectedSchedule, calendar.getTime())) {
                String scheduleText = AppUtils.dateToReadableFullDateString(calendar.getTime());
                mYourSchedule = calendar.getTime();
                yourScheduleTextView.setText(scheduleText);
            } else {
                Toast.makeText(AddAppointmentFragment.this.getContext(),
                        "Appointment time falls out of selected clinic schedule",
                        Toast.LENGTH_LONG).show();
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

    private void setSpinnerItemSelectListeners(){

        selectHospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the facility id of the work by reducing position by 1
                if (position > 0) {

                    mPatient = mPatients.get(position - 1);
                    mSelectedFacility = mFacilities.get(position - 1);

                    InjectorUtils.provideNetworkData(getContext()).fetchClinicSchedulesForFacility(mSelectedFacility.getId());
                    InjectorUtils.provideNetworkData(getContext()).fetchCategoriesForFacility(mSelectedFacility.getId());
                    InjectorUtils.provideNetworkData(getContext()).fetchEmployeesForFacility(mSelectedFacility.getId());

                } else {
                    resetAllSelectedFields();
                    yourScheduleTextView.setText("Select your schedule");

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
                    mSelectedClinic = mClinics.get(position-1);
                    appointmentViewModel.setSchedules(mSelectedClinic);

                } else {
                    mSelectedSchedule = null;
                    mYourSchedule = null;
                    yourScheduleTextView.setText("Select your schedule");

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
                    mSelectedSchedule = null;
                    yourScheduleTextView.setText("Select your schedule");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the correct schedule by reducing position by 1
                if (position > 0) {
                    mSelectedService = mServices.get(position-1);

                } else {
                    mSelectedService = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        whomToSeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the correct schedule by reducing position by 1
                if (position > 0) {
                    mSelectedDoctor = mEmployees.get(position-1);

                } else {
                    mSelectedDoctor = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validateAppointmentBody(){

        if (mPatient == null)
            return false;

        if (mSelectedFacility == null)
            return false;

        if (mSelectedSchedule == null)
            return false;

        if (mSelectedClinic == null)
            return false;

        if (mSelectedDoctor == null)
            return false;

        if (mSelectedService == null)
            return false;

        if (mYourSchedule == null)
            return false;


        return true;
    }

    private void createReminder(String appointmentId){
        Intent alarmIntent = new Intent(getContext(), AlarmBroadcast.class);

        alarmIntent.setAction("appointment_reminder");
        alarmIntent.putExtra("appointment_reminder_id", appointmentId);

        AlarmManager alarmManager = AlarmManagerSingleton.getInstance(getContext().getApplicationContext()).getAlarmManager();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext().getApplicationContext(), 0, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                mYourSchedule.getTime()-60*60*1000, //An hour behind
                pendingIntent);
        Log.d("Alarm", "Set");
    }

    private void submitAppointment(){

        Appointment appointment = new Appointment();
        AppointmentType type = new AppointmentType();

        if (newRadio.isChecked()){
            type.setName("New");
        } else {
            type.setName("Follow Up");
        }
        mAppointmentType = type;

        mOrderStatus = new OrderStatus();
        mOrderStatus.setName("Scheduled");

        appointment.setAppointmentTypeId(mAppointmentType.getName());
        appointment.setFacilityId(mSelectedFacility.getId());
        appointment.setStartDate(mYourSchedule);
        appointment.setClinicId(mSelectedClinic.getClinic());
        appointment.setDoctorId(mSelectedDoctor.getId());
        appointment.setOrderStatusId(mOrderStatus.getName());
        appointment.setPatientId(mPatient.getId());
        appointment.setAppointmentReason("");
        appointment.setCategory(mSelectedService.getName());


        appointmentViewModel.submitAppointment(appointment);
        showProgressDialog("Creating Appointment", "Please wait...");
    }

    private void showProgressDialog(String title, String message){
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void displaySuccessDialog(String appointmentId){
        if (setReminderSwitch.isChecked())
            createReminder(appointmentId);

        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setCancelable(false)
                .setMessage("Appointment Created!")
                .setPositiveButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                })
                .show();
    }

    private void displayErrorDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Failed")
                .setMessage("Could not create appointment.\nPlease try again")
                .setPositiveButton("Close", (dialog, which) -> {
                    hideProgressDialog();
                    dialog.dismiss();
                })
                .show();
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
