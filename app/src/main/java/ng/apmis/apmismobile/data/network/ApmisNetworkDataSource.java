package ng.apmis.apmismobile.data.network;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.facilityModel.Category;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduledClinic;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;

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

    private final APMISAPP apmisExecutors;

    private final MutableLiveData<PersonEntry[]> mDownloadedPersonData;
    private final SharedPreferencesManager sharedPreferencesManager;

    private MutableLiveData<List<Patient>> patientDetails;
    private MutableLiveData<List<ScheduledClinic>> clinics;
    private MutableLiveData<List<ScheduleItem>> schedules;
    private MutableLiveData<List<Employee>> employees;
    private MutableLiveData<List<Service>> services;


    ApmisNetworkDataSource(Context context, APMISAPP executorThreads) {
        mContext = context;
        apmisExecutors = executorThreads;
        mDownloadedPersonData = new MutableLiveData<>();
        patientDetails = new MutableLiveData<>();
        clinics = new MutableLiveData<>();
        schedules = new MutableLiveData<>();
        employees = new MutableLiveData<>();
        services = new MutableLiveData<>();
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


    public LiveData<PersonEntry[]> getCurrentPersonData() {
        return mDownloadedPersonData;
    }

    public void setCurrentPersonData(PersonEntry[] personEntries) {
        mDownloadedPersonData.postValue(personEntries);
    }

    /**
     * Start service to fetch patient details
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
     * This function is only called on a background service
     */
    void fetchPersonDetails() {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch person details started");
            try {
                new NetworkDataCalls(mContext).getPersonData(mContext, sharedPreferencesManager.storedLoggedInUser().getString("pid"), sharedPreferencesManager.storedLoggedInUser().getString("token"));
                new NetworkDataCalls(mContext).fetchPatientDetailsForPerson(mContext, sharedPreferencesManager.getPersonId(),  sharedPreferencesManager.getStoredUserAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    public void setSchedulesForClinic(ScheduledClinic clinic){
        this.schedules.postValue(clinic.getSchedules());
    }

    public MutableLiveData<List<ScheduleItem>> getSchedulesForClinic() {
        return schedules;
    }

    public void refreshSchedules(){
        this.schedules.postValue(new ArrayList<>());
    }

    public void setClinicsForFacility(List<ScheduledClinic> clinics){
        this.clinics.postValue(clinics);
    }

    public MutableLiveData<List<ScheduledClinic>> getClinicsForFacility() {
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
        //Log.d(LOG_TAG, "set patients started: "+patientDetails.get(0).getPersonDetails().getFirstName());
        this.patientDetails.postValue(patientDetails);
    }

    public MutableLiveData<List<Patient>> getPatientDetailsForPerson(){
        return patientDetails;
    }



}
