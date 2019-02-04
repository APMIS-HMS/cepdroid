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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.appointmentModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.fundAccount.Price;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ClinicAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.EmployeeAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.FacilityAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.PriceAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ScheduleAdapter;
import ng.apmis.apmismobile.ui.dashboard.appointment.adapters.ServiceAdapter;
import ng.apmis.apmismobile.utilities.AlarmBroadcast;
import ng.apmis.apmismobile.utilities.AlarmManagerSingleton;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.<br/>
 * Fragment view where Appointments are created
 */

public class AddAppointmentFragment extends Fragment {

    @BindView(R.id.select_hospital_spinner)
    Spinner selectHospitalSpinner;

    @BindView(R.id.select_hospital_progress)
    ProgressBar selectHospitalProgress;

    @BindView(R.id.select_hospital_refresh)
    Button selectHospitalRefresh;

    @BindView(R.id.select_appt_type_spinner)
    Spinner selectAppointmentTypeSpinner;

    @BindView(R.id.select_appt_type_progress)
    ProgressBar selectAppointmentTypeProgress;

    @BindView(R.id.select_appt_type_refresh)
    Button selectAppointmentTypeRefresh;

    @BindView(R.id.book_appointment_button)
    Button bookAppointmentButton;

    @BindView(R.id.select_clinic_spinner)
    Spinner selectClinicSpinner;

    @BindView(R.id.select_clinic_progress)
    ProgressBar selectClinicProgress;

    @BindView(R.id.select_clinic_refresh)
    Button selectClinicRefresh;

    @BindView(R.id.select_service_spinner)
    Spinner selectServiceSpinner;

    @BindView(R.id.select_service_progress)
    ProgressBar selectServiceProgress;

    @BindView(R.id.select_service_refresh)
    Button selectServiceRefresh;

    @BindView(R.id.whom_to_see_spinner)
    Spinner whomToSeeSpinner;

    @BindView(R.id.select_employee_progress)
    ProgressBar selectEmployeeProgress;

    @BindView(R.id.select_employee_refresh)
    Button selectEmployeeRefresh;

    @BindView(R.id.pricing_text)
    TextView selectPriceTextView;

    @BindView(R.id.pricing_progress)
    ProgressBar selectPriceProgress;

    @BindView(R.id.pricing_refresh)
    Button selectPriceRefresh;

    @BindView(R.id.clinic_schedule_spinner)
    Spinner clinicScheduleSpinner;

    @BindView(R.id.your_schedule_textview)
    TextView yourScheduleTextView;

    @BindView(R.id.set_reminder_switch)
    Switch setReminderSwitch;

    @BindView(R.id.appointment_reason_edit_text)
    EditText appointmentReasonEdit;

    @OnClick(R.id.your_schedule_textview)
    void scheduleTextViewClicked(){
        showDatePicker();
    }

    //Adapters for drop down lists
    ArrayAdapter appointmentTypeArrayAdapter;
    FacilityAdapter hospitalAdapter;
    ClinicAdapter clinicAdapter;
    ServiceAdapter serviceAdapter;
    EmployeeAdapter employeeAdapter;
    ScheduleAdapter scheduleAdapter;
    AddAppointmentViewModel appointmentViewModel;

    //Selectable data lists
    private List<AppointmentType> mAppointmentTypes;
    private List<Facility> mFacilities;
    private List<ClinicSchedule> mClinics;
    private List<Service> mServices;
    private List<Employee> mEmployees;
    private List<ScheduleItem> mScheduleItems;
    private List<Price> mPrices;
    private BillManager mBill;

    //Selectable data items
    private AppointmentType mAppointmentType;
    private OrderStatus mOrderStatus;
    private Facility mSelectedFacility;
    private ClinicSchedule mSelectedClinic;
    private Service mSelectedService;
    private Employee mSelectedDoctor;
    private Price mSelectedPrice;
    private ScheduleItem mSelectedSchedule;
    private Date mYourSchedule;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment_form, container, false);
        ButterKnife.bind(this, rootView);

        //instantiate loader
        progressDialog = new ProgressDialog(getContext());

        //initialize the AppointmentViewModel
        initViewModel();

        //TODO switch to LiveData
        //if (appointmentViewModel.getOrderStatuses().isEmpty())
        //InjectorUtils.provideNetworkData(getContext()).fetchOrderStatuses();

        //set up item select spinners
        setSpinnerItemSelectListeners();

        bookAppointmentButton.setOnClickListener((view) -> {
            if (validateAppointmentBody()) {
                submitAppointment();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 20f);
    }

    Observer<List<ClinicSchedule>> clinicsObserver;
    Observer<List<Service>> serviceObserver;
    Observer<List<Employee>> employeeObserver;
    Observer<BillManager> billManagerObserver;

    /**
     * Initialize the AppointmentViewModel and set up all the observers
     */
    private void initViewModel(){
        AddAppointmentViewModelFactory factory = InjectorUtils.provideAddAppointmentViewModelFactory(getActivity().getApplicationContext());
        appointmentViewModel = ViewModelProviders.of(this, factory).get(AddAppointmentViewModel.class);

        //Fetch facilities

        Observer<List<Facility>> facilitiesObserver = facilities -> {
            if (facilities == null){
                selectHospitalProgress.setVisibility(View.GONE);
                selectHospitalRefresh.setVisibility(View.VISIBLE);
                //allow facilities to reload if any fail occurs
                appointmentViewModel.tempRefreshFacilities();
                return;
            }

            mFacilities = facilities;

            if (hospitalAdapter == null) {
                hospitalAdapter = new FacilityAdapter(getContext(), 0, mFacilities);
                selectHospitalSpinner.setAdapter(hospitalAdapter);
            } else {
                hospitalAdapter.clear();
                hospitalAdapter.addAllFacilities(mFacilities);
                hospitalAdapter.notifyDataSetChanged();
            }

            selectHospitalProgress.setVisibility(View.GONE);
        };

        appointmentViewModel.getRegisteredFacilities(true).observe(this, facilitiesObserver);
        selectHospitalRefresh.setOnClickListener(v -> {

            selectHospitalProgress.setVisibility(View.VISIBLE);
            selectHospitalRefresh.setVisibility(View.GONE);

            appointmentViewModel.getRegisteredFacilities(false).removeObservers(AddAppointmentFragment.this);
            appointmentViewModel.getRegisteredFacilities(true).observe(AddAppointmentFragment.this, facilitiesObserver);
        });



        //Fetch Appointment types

        Observer<List<AppointmentType>> appointmentTypesObserver = appointmentTypes -> {
            if (appointmentTypes == null){
                selectAppointmentTypeProgress.setVisibility(View.GONE);
                selectAppointmentTypeRefresh.setVisibility(View.VISIBLE);
                appointmentViewModel.tempRefreshApptTypes();
                return;
            }

            mAppointmentTypes = appointmentTypes;

            if (appointmentTypeArrayAdapter == null) {
                appointmentTypeArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mAppointmentTypes);
                appointmentTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                selectAppointmentTypeSpinner.setAdapter(appointmentTypeArrayAdapter);
                if (appointmentTypes.size() > 0)
                    mAppointmentType = appointmentTypes.get(0);
            } else {
                appointmentTypeArrayAdapter.notifyDataSetChanged();
            }

            selectAppointmentTypeProgress.setVisibility(View.GONE);
        };

        appointmentViewModel.getAppointmentTypes().observe(this, appointmentTypesObserver);
        selectAppointmentTypeRefresh.setOnClickListener(v -> {
            //TODO prevent this double data fetch here

            selectAppointmentTypeProgress.setVisibility(View.VISIBLE);
            selectAppointmentTypeRefresh.setVisibility(View.GONE);

            appointmentViewModel.getAppointmentTypes().removeObservers(AddAppointmentFragment.this);
            appointmentViewModel.getAppointmentTypes().observe(AddAppointmentFragment.this, appointmentTypesObserver);
        });


        //Fetch Clinics

        clinicsObserver = clinics -> {
            //In case facility was reset before result came in
            if (mSelectedFacility == null){
                return;
            }

            //return in case a clinic from the wrong facility is returned from an earlier request
            if (clinics != null && clinics.size() > 0){
                if (!clinics.get(0).getFacilityId().equals(mSelectedFacility.getId()))
                    return;
            }

            Log.e("TAG", "Times Called");
            if (clinics == null){
                selectClinicProgress.setVisibility(View.GONE);
                selectClinicRefresh.setVisibility(View.VISIBLE);
                appointmentViewModel.resetClinics();
                return;
            }

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

            selectClinicProgress.setVisibility(View.GONE);
        };

        selectClinicRefresh.setOnClickListener(v -> {
            appointmentViewModel.getClinics(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);

            selectClinicProgress.setVisibility(View.VISIBLE);
            selectClinicRefresh.setVisibility(View.GONE);

            appointmentViewModel.getClinics(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, clinicsObserver);
        });


        //Services

        serviceObserver = services -> {
            //In case facility was reset before result came in
            if (mSelectedFacility == null){
                return;
            }

            //return in case a service from the wrong facility is returned from an earlier request
            if (services != null && services.size() > 0){
                if (!services.get(0).getFacilityId().equals(mSelectedFacility.getId()))
                    return;
            }

            if (services == null){
                selectServiceProgress.setVisibility(View.GONE);
                selectServiceRefresh.setVisibility(View.VISIBLE);
                appointmentViewModel.resetServices();
                return;
            }

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

            selectServiceProgress.setVisibility(View.GONE);
        };

        selectServiceRefresh.setOnClickListener(v -> {

            appointmentViewModel.getServices(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);

            selectServiceProgress.setVisibility(View.VISIBLE);
            selectServiceRefresh.setVisibility(View.GONE);

            appointmentViewModel.getServices(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, serviceObserver);
        });



        //Employees

        employeeObserver = employees -> {
            //In case facility was reset before result came in
            if (mSelectedFacility == null){
                return;
            }

            //return in case an employee from the wrong facility is returned from an earlier request
            if (employees != null && employees.size() > 0){
                if (!employees.get(0).getFacilityId().equals(mSelectedFacility.getId()))
                    return;
            }

            if (employees == null){
                selectEmployeeProgress.setVisibility(View.GONE);
                selectEmployeeRefresh.setVisibility(View.VISIBLE);
                appointmentViewModel.resetEmployees();
                return;
            }

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

            selectEmployeeProgress.setVisibility(View.GONE);
        };

        selectEmployeeRefresh.setOnClickListener(v -> {
            appointmentViewModel.getEmployees(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);

            selectEmployeeProgress.setVisibility(View.VISIBLE);
            selectEmployeeRefresh.setVisibility(View.GONE);

            appointmentViewModel.getEmployees(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, employeeObserver);
        });


        //Bill Manager Observer

        billManagerObserver = bill -> {

            //return in case a bill from the wrong facility is returned from an earlier request
            if (bill != null){
                if (!bill.getFacilityId().equals(mSelectedFacility.getId()))
                    return;
            }

            if (bill == null){
                selectPriceProgress.setVisibility(View.GONE);
                selectPriceRefresh.setVisibility(View.VISIBLE);
                selectPriceTextView.setText("Unable to fetch price");
                //TODO i don't think this last guy should be here, since in the
                //todo new style, my observer observes once and re-fetches thereafter
                //todo so I can just call this clear in the reload
                //appointmentViewModel.clearBillManager();
                return;
            }

            mBill = bill;

            Price price = null;

            //Check the found service and get the base price
            for (Service service : mBill.getServices()){
                if (service.getId().equals(mSelectedService.getId())){
                    if (service.getPrices().size() == 1)
                        price = service.getPrices().get(0);

                    else if (service.getPrices().size() > 1)
                        for (Price inPrice : service.getPrices()){
                            if (inPrice.getIsBase()) {
                                price = inPrice;
                                break;
                            }
                        }
                }
            }

            if (price == null)
                selectPriceTextView.setText("Unable to fetch price");
            else
                selectPriceTextView.setText(price.toString());

            mSelectedPrice = price;

            selectPriceProgress.setVisibility(View.GONE);


        };

        selectPriceRefresh.setOnClickListener(v -> {
            selectPriceTextView.setText("Loading price...");
            selectPriceProgress.setVisibility(View.VISIBLE);
            selectPriceRefresh.setVisibility(View.GONE);
            appointmentViewModel.clearBillManager();
            appointmentViewModel.getBill(mSelectedService.getFacilityId(), mSelectedService.getParentCategoryId())
                    .observe(AddAppointmentFragment.this, billManagerObserver);

        });



        //Appointment

        final Observer<Appointment> appointmentObserver = appointment -> {

            //Only record observations if appointment body is valid
            if (validateAppointmentBody()) {
                hideProgressDialog();

                if (appointment != null) {
                    //Save to room here
                    //Always pre-fill the facilityName and PersonId into the top level of the Appointment object
                    //for easy Room database storage
                    appointment.setFacilityName(appointment.getPatientDetails().getFacilityObj().getName());
                    appointment.setPersonId(appointment.getPatientDetails().getPersonId());
                    appointmentViewModel.insertAppointment(appointment);

                    displaySuccessDialog(appointment.get_id());

                } else {
                    displayErrorDialog();
                }
            }
        };

        appointmentViewModel.getAppointment().observe(this, appointmentObserver);

    }

    /**
     * Reset all selectable fields to null
     */
    private void resetAllSelectedFields(){
        mSelectedFacility = null;
        mSelectedClinic = null;
        mSelectedService = null;
        mSelectedDoctor = null;
        mSelectedSchedule = null;
        mBill = null;
        mSelectedPrice = null;
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


    boolean areBillsObserved;

    /**
     * Set {@link AdapterView.OnItemSelectedListener}s on the spinners
     */
    private void setSpinnerItemSelectListeners(){

        selectAppointmentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mAppointmentTypes != null && mAppointmentTypes.size() > 0) {
                    mAppointmentType = mAppointmentTypes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectHospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the facility id of the work by reducing position by 1
                if (position > 0) {

                    if (mSelectedFacility != null && mSelectedFacility != mFacilities.get(position - 1)){
                        appointmentViewModel.resetClinics();
                        appointmentViewModel.resetServices();
                        appointmentViewModel.resetEmployees();

                        if (clinicAdapter != null){
                            clinicAdapter.clear();
                            serviceAdapter.clear();
                            employeeAdapter.clear();
                        }

                        Log.e("TAG", "I am refreshed");
                    }

                    mSelectedFacility = mFacilities.get(position - 1);

                    selectClinicRefresh.setVisibility(View.GONE);
                    selectServiceRefresh.setVisibility(View.GONE);
                    selectEmployeeRefresh.setVisibility(View.GONE);

                    selectClinicProgress.setVisibility(View.VISIBLE);
                    selectServiceProgress.setVisibility(View.VISIBLE);
                    selectEmployeeProgress.setVisibility(View.VISIBLE);

                    appointmentViewModel.getClinics(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                    appointmentViewModel.getClinics(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, clinicsObserver);

                    appointmentViewModel.getServices(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                    appointmentViewModel.getServices(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, serviceObserver);

                    appointmentViewModel.getEmployees(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                    appointmentViewModel.getEmployees(mSelectedFacility.getId(), true).observe(AddAppointmentFragment.this, employeeObserver);

                } else {

                    if (mSelectedFacility != null) {
                        appointmentViewModel.getClinics(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                        appointmentViewModel.getServices(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                        appointmentViewModel.getEmployees(mSelectedFacility.getId(), false).removeObservers(AddAppointmentFragment.this);
                    }

                    selectClinicRefresh.setVisibility(View.GONE);
                    selectServiceRefresh.setVisibility(View.GONE);
                    selectEmployeeRefresh.setVisibility(View.GONE);

                    selectClinicProgress.setVisibility(View.GONE);
                    selectServiceProgress.setVisibility(View.GONE);
                    selectEmployeeProgress.setVisibility(View.GONE);

                    resetAllSelectedFields();
                    yourScheduleTextView.setText("Select your schedule");

                    appointmentViewModel.resetClinics();
                    appointmentViewModel.resetServices();
                    appointmentViewModel.resetEmployees();

                    if (clinicAdapter != null){
                        clinicAdapter.clear();
                        serviceAdapter.clear();
                        employeeAdapter.clear();
                    }
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
                    mScheduleItems = mSelectedClinic.getSchedules();

                    if (scheduleAdapter == null) {
                        scheduleAdapter = new ScheduleAdapter(getContext(), 0, mScheduleItems);
                        clinicScheduleSpinner.setAdapter(scheduleAdapter);
                    } else {
                        scheduleAdapter.clear();
                        scheduleAdapter.addAllSchedules(mScheduleItems);
                        scheduleAdapter.notifyDataSetChanged();
                        clinicScheduleSpinner.setSelection(0);
                    }

                } else {
                    mSelectedClinic = null;
                    mScheduleItems = null;

                    if (scheduleAdapter != null)
                        scheduleAdapter.clear();

                    mSelectedSchedule = null;
                    mYourSchedule = null;
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
                    selectPriceTextView.setText("Loading price...");
                    selectPriceProgress.setVisibility(View.VISIBLE);
                    selectPriceRefresh.setVisibility(View.GONE);

                    if (!areBillsObserved) {
                        appointmentViewModel.clearBillManager();
                        appointmentViewModel.getBill(mSelectedService.getFacilityId(), mSelectedService.getParentCategoryId()).observe(AddAppointmentFragment.this, billManagerObserver);
                    } else {
                        appointmentViewModel.reFetchBillData(mSelectedService.getFacilityId(), mSelectedService.getParentCategoryId());
                    }

                } else {
                    selectPriceProgress.setVisibility(View.GONE);
                    selectPriceRefresh.setVisibility(View.GONE);
                    mSelectedService = null;
                    mBill = null;
                    areBillsObserved = false;
                    appointmentViewModel.clearBillManager();
                    selectPriceTextView.setText("Please select a service");
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

        clinicScheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the correct schedule by reducing position by 1
                if (position > 0) {
                    mSelectedSchedule = mScheduleItems.get(position-1);

                } else {
                    mSelectedSchedule = null;
                    yourScheduleTextView.setText("Select your schedule");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Perform Validation
     * @return true if all fields are filled and correct
     */
    private boolean validateAppointmentBody(){

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

        mOrderStatus = new OrderStatus();
        mOrderStatus.setName("Scheduled");

        appointment.setAppointmentTypeId(mAppointmentType.getName());
        appointment.setFacilityId(mSelectedFacility.getId());
        appointment.setStartDate(mYourSchedule);
        appointment.setClinicId(mSelectedClinic.getClinic());
        appointment.setDoctorId(mSelectedDoctor.getId());
        appointment.setOrderStatusId(mOrderStatus.getName());
        appointment.setPatientId(mSelectedFacility.getPatientIdForPerson());
        appointment.setAppointmentReason(appointmentReasonEdit.getText().toString());
        appointment.setCategory(mSelectedService.getName());

        Log.e("Submit ted", appointment.toString());

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



//    @Override
//    public void onStop() {
//        super.onStop();
//        ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility("APPOINTMENTS", false);
//
//        //remove the observers now to avoid double observers when popped from back stack
////        appointmentViewModel.getAppointmentTypes().removeObservers(getActivity());
////        appointmentViewModel.getAppointment().removeObservers(getActivity());
////        appointmentViewModel.getSchedules().removeObservers(getActivity());
////        appointmentViewModel.getServices().removeObservers(getActivity());
////        appointmentViewModel.getClinics().removeObservers(getActivity());
////        appointmentViewModel.getRegisteredFacilities().removeObservers(getActivity());
////        appointmentViewModel.getEmployees().removeObservers(getActivity());
//    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility("New Appointment", false);
        ((DashboardActivity)getActivity()).profileImage.setVisibility(View.GONE);
    }
}
