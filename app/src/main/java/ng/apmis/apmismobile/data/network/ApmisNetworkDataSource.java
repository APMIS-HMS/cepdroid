package ng.apmis.apmismobile.data.network;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.appointmentModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.Category;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;

/**
 * Provides an api for getting network data
 */
//TODO person data should not necessarily be synced over and over, call to network service sync should have action so as to handle action for different data calls
public class ApmisNetworkDataSource {

    private static final String LOG_TAG = ApmisNetworkDataSource.class.getSimpleName();

    // Interval at which to sync with the weather. Use TimeUnit for convenience, rather than
    // writing out a bunch of multiplication ourselves and risk making a silly mistake.
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String APMIS_SYNC = "apmis-sync";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ApmisNetworkDataSource sInstance;
    private final Context mContext;

    //Reference to the app ex
    private final APMISAPP apmisExecutors;

    //SharedPref Manager
    private final SharedPreferencesManager sharedPreferencesManager;

    //LiveData references
    private final MutableLiveData<PersonEntry[]> mDownloadedPersonData;
    private MutableLiveData<List<Patient>> patientDetails;
    private MutableLiveData<List<ClinicSchedule>> clinics;
    private MutableLiveData<List<ScheduleItem>> schedules;
    private MutableLiveData<List<Employee>> employees;
    private MutableLiveData<List<Documentation>> documentations;
    private MutableLiveData<List<Service>> services;
    private MutableLiveData<List<Prescription>> prescriptions;
    private MutableLiveData<List<LabRequest>> labRequests;
    private MutableLiveData<Appointment> appointment;
    private MutableLiveData<String> profilePhotoPath;
    private MutableLiveData<List<AppointmentType>> appointmentTypes;

    //TODO Switch to LiveData later
    private List<OrderStatus> orderStatuses;


    ApmisNetworkDataSource(Context context, APMISAPP executorThreads) {
        mContext = context;
        apmisExecutors = executorThreads;
        mDownloadedPersonData = new MutableLiveData<>();
        patientDetails = new MutableLiveData<>();
        clinics = new MutableLiveData<>();
        schedules = new MutableLiveData<>();
        employees = new MutableLiveData<>();
        services = new MutableLiveData<>();
        documentations = new MutableLiveData<>();
        appointment = new MutableLiveData<>();
        prescriptions = new MutableLiveData<>();
        labRequests = new MutableLiveData<>();
        profilePhotoPath = new MutableLiveData<>();
        appointmentTypes = new MutableLiveData<>();

        orderStatuses = new ArrayList<>();
        sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public static ApmisNetworkDataSource getsInstance(Context context, APMISAPP executorThreads) {
        Log.d(LOG_TAG, "Getting the network data");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ApmisNetworkDataSource(context.getApplicationContext(), executorThreads);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Start service to fetch person details
     */
    public void startPersonDataFetchService() {
        Intent intentToFetch = new Intent(mContext, ApmisSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    /**
     * Schedules a repeating job service which fetches the person information.
     * This is scheduled after every login, say 10 secs interval
     * Should not run at long intervals since the application session times out (like a banking app)
     */
    public void scheduleRecurringFetchPersonDataSync() {
        Driver driver = new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync Sunshine
        Job apmisSyncJob = dispatcher.newJobBuilder()
                /* The Service that will be used to sync Sunshine's data */
                .setService(ApmisFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(APMIS_SYNC)
                /*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(apmisSyncJob);
        Log.d(LOG_TAG, "Job scheduled");
    }

    /**
     * Gets the most recent person details
     */
    void fetchPersonDetails() {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch person details started");
            try {
                new NetworkDataCalls(mContext).getPersonData(mContext, sharedPreferencesManager.getStoredLoggedInUser().getString("pid"), sharedPreferencesManager.getStoredLoggedInUser().getString("token"));
                new NetworkDataCalls(mContext).fetchPatientDetailsForPerson(mContext, sharedPreferencesManager.getPersonId(),  sharedPreferencesManager.getStoredUserAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Fetch Appointment types
     */
    public void fetchAppointmentTypes(){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Appointment Types started");
            new NetworkDataCalls(mContext).fetchAppointmentTypes(mContext, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch Order status
     */
    public void fetchOrderStatuses(){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Order Statuses started");
            new NetworkDataCalls(mContext).fetchOrderStatuses(mContext, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch ClinicSchedule Objects for the facility Id provided
     * @param facilityId Facility Id of the facility
     */
    public void fetchClinicSchedulesForFacility(String facilityId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Clinics started");
            new NetworkDataCalls(mContext).fetchClinicsForFacility(mContext, facilityId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch Category Array using the facility Id provided
     * @param facilityId Facility Id of the facility
     */
    public void fetchCategoriesForFacility(String facilityId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Categories started");
            new NetworkDataCalls(mContext).fetchCategoriesForFacility(mContext, facilityId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch Employee Array using the facility Id provided
     * @param facilityId Facility Id of the facility
     */
    public void fetchEmployeesForFacility(String facilityId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Employees started");
            new NetworkDataCalls(mContext).fetchEmployeesForFacility(mContext, facilityId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Submit a scheduled appointment
     * @param appointment The appointment object
     */
    public void submitAppointment(Appointment appointment) {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Submit appointment started");
            new NetworkDataCalls(mContext).submitAppointment(mContext, appointment, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch all appointments for a person, across all facilities
     * @param personId
     */
    public void fetchAppointmentsForPerson(String personId) {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch appointments started");
            new NetworkDataCalls(mContext).fetchAppointmentsForPerson(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch all Medical records for a person, across all facilities
     * @param personId Person's Id
     */
    public void fetchMedicalRecordsForPerson(String personId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Records started");
            new NetworkDataCalls(mContext).fetchMedicalRecordForPerson(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch Prescription Array using the person Id provided
     * @param personId Person's Id
     */
    public void fetchPrescriptionsForPerson(String personId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Prescription started");
            new NetworkDataCalls(mContext).fetchPrescriptionsForPerson(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    /**
     * Fetch Laboratory Request Array using the person Id provided
     * @param personId Person's Id
     */
    public void fetchLabRequestsForPerson(String personId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch Prescription started");
            new NetworkDataCalls(mContext).fetchLabRequestsForPerson(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    public void fetchAndDownloadPersonProfilePhoto(PersonEntry personEntry, File downloadFile){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Image", "Fetch Image started");
            new NetworkDataCalls(mContext).downloadProfilePictureForPerson(mContext, personEntry, downloadFile);
        });
    }





    //Person entry
    public LiveData<PersonEntry[]> getCurrentPersonData() {
        return mDownloadedPersonData;
    }

    public void setCurrentPersonData(PersonEntry[] personEntries) {
        //post value for liveData use
        mDownloadedPersonData.postValue(personEntries);
    }

    public void setProfilePhotoPath(String path){
        profilePhotoPath.postValue(path);
    }

    public MutableLiveData<String> getPersonProfilePhotoPath(){
        return profilePhotoPath;
    }


    //Order Status

    public void setOrderStatuses(List<OrderStatus> orderStatuses){
        this.orderStatuses = orderStatuses;
    }

    public List<OrderStatus> getOrderStatuses(){
        return this.orderStatuses;
    }


    //Appointment Types

    public void setAppointmentTypes(List<AppointmentType> appointmentTypes){
        this.appointmentTypes.postValue(appointmentTypes);
    }

    public MutableLiveData<List<AppointmentType>> getAppointmentTypes(){
        return this.appointmentTypes;
    }


    //Employees

    public void setEmployeesForFacility(List<Employee> employees){
        List<Employee> doctors = new ArrayList<>();
        for (Employee employee : employees){
            if (employee.getProfessionId().equalsIgnoreCase("Doctor"))
                doctors.add(employee);
        }
        this.employees.postValue(doctors);
    }

    public MutableLiveData<List<Employee>> getEmployeesForFacility() {
        return employees;
    }

    public void refreshEmployees(){
        this.employees.postValue(new ArrayList<>());
    }



    //Clinics & Schedules

    public void setSchedulesForClinic(ClinicSchedule clinic){
        this.schedules.postValue(clinic.getSchedules());
    }

    public MutableLiveData<List<ScheduleItem>> getSchedulesForClinic() {
        return schedules;
    }

    public void refreshSchedules(){
        this.schedules.postValue(new ArrayList<>());
    }

    public void setClinicsForFacility(List<ClinicSchedule> clinics){
        this.clinics.postValue(clinics);
    }

    public MutableLiveData<List<ClinicSchedule>> getClinicsForFacility() {
        return clinics;
    }

    public void refreshClinics(){
        this.clinics.postValue(new ArrayList<>());
    }


    //Services

    public void setServicesForFacility(List<Category> categories){
        for (Category category : categories){
            if (category.getName().equalsIgnoreCase("Appointment")) {
                this.services.postValue(category.getServices());
                break;
            }
        }
    }

    public MutableLiveData<List<Service>> getServicesForFacility() {
        return services;
    }

    public void refreshServices(){
        this.services.postValue(new ArrayList<>());
    }


    //Patients

    public void setPatientDetailsForPerson(List<Patient> patientDetails){
        this.patientDetails.postValue(patientDetails);
    }

    public MutableLiveData<List<Patient>> getPatientDetailsForPerson(){
        return patientDetails;
    }


    //Appointment
    public void setAppointment(Appointment appointment){
        this.appointment.postValue(appointment);
    }

    public MutableLiveData<Appointment> getAppointment() {
        return appointment;
    }

    //Documentation
    public void setDocumentationsForPerson(List<Documentation> documentations){
        this.documentations.postValue(documentations);
    }

    public MutableLiveData<List<Documentation>> getDocumentationsForPerson(){
        return this.documentations;
    }


    //Prescription

    public void setPrescriptionsForPerson(List<Prescription> prescriptions) {
        this.prescriptions.postValue(prescriptions);
    }

    public MutableLiveData<List<Prescription>> getPrescriptionsForPerson(){
        return this.prescriptions;
    }

    //Laboratory Requests

    public void setLabRequestsForPerson(List<LabRequest> labRequests) {
        this.labRequests.postValue(labRequests);
    }

    public MutableLiveData<List<LabRequest>> getLabRequestsForPerson(){
        return this.labRequests;
    }
}
