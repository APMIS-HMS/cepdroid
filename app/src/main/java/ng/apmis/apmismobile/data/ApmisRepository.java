package ng.apmis.apmismobile.data;


import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.Date;
import java.util.List;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.ApmisDao;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Source of truth for data whether from database or from server
 * First serves data from room database with livedata
 */

public class ApmisRepository {

    private static final String LOG_TAG = ApmisRepository.class.getSimpleName();

    private static long A_DAY_IN_MILLIS = 60 * 60 * 24 * 1000;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ApmisRepository sInstance;
    private final ApmisDao mApmisDao;
    private final ApmisNetworkDataSource mApmisNetworkDataSource;
    private final APMISAPP mExecutors;
    private boolean mInitialized = false;

    private ApmisRepository(ApmisDao apmisDao,
                               ApmisNetworkDataSource apmisNetworkDataSource,
                               APMISAPP executors) {
        mApmisDao = apmisDao;
        mApmisNetworkDataSource = apmisNetworkDataSource;
        mExecutors = executors;
        LiveData<PersonEntry> networkData = mApmisNetworkDataSource.getCurrentPersonData();

        networkData.observeForever(newpersonData -> {
            mExecutors.diskIO().execute(() -> {
                if (newpersonData == null)
                    return;
                // Insert our new weather data into Sunshine's database
                deleteOldData();
                Log.d(LOG_TAG, "Old person detail deleted");
                // Insert our new weather data into Sunshine's database
                mApmisDao.insertData(newpersonData);
                Log.d(LOG_TAG, "New values inserted");
            });
        });

    }

    public synchronized static ApmisRepository getsInstance (ApmisDao apmisDao, ApmisNetworkDataSource apmisNetworkDataSource, APMISAPP executorThreads) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ApmisRepository(apmisDao, apmisNetworkDataSource,
                        executorThreads);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    public synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded()) {
                startFetchPersonDataService();
            }
        });
    }

    public ApmisNetworkDataSource getNetworkDataSource(){
        return this.mApmisNetworkDataSource;
    }

    public LiveData<PersonEntry> getUserData () {
        initializeData();
        Log.e("Viewmodel", "fetching user data was called");
        return mApmisDao.getUserData();
    }

    public PersonEntry getStaticUserData () {
        initializeData();
        return mApmisDao.getStaticUserData();
    }

    /**
     * Update the user data in the db
     * @param personEntry
     */
    public void updateUserData(PersonEntry personEntry){
        mExecutors.diskIO().execute(() -> {
            deleteOldData();
            Log.d(LOG_TAG, "Old person detail deleted");
            // Insert our new weather data into Sunshine's database
            mApmisDao.insertData(personEntry);
            Log.d("Image", "New values inserted "+personEntry.getProfileImageObject().toString());
        });
    }


    /**
     * Remove all old person data to allow new update
     */
    private void deleteOldData () {
        mApmisDao.deleteOldData();
    }

    //TODO consider condition to check before fetching data from server
    //TODO If data equal to null and or if the time of last update is greater than 1 hour
    private boolean isFetchNeeded() {
        return true;
    }

    private void startFetchPersonDataService() {
        mApmisNetworkDataSource.startPersonDataFetchService();
    }

    /**
     * Inserts the {@link Appointment} object from the server into the
     * Appointment Room Database
     * @param appointment The Appointment object fetched
     */
    public void insertAppointment(Appointment appointment){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mApmisDao.insertAppointment(appointment);
            }
        });
    }

    /**
     * Batch procedure for inserting Appointments into Room database
     * @param appointments The list of Appointments to insert
     */
    public void insertAppointmentsForPatient(List<Appointment> appointments){
        for (Appointment appointment : appointments){
            //Always pre-fill the facilityName and PersonId into the top level of the Appointment object
            //for easy Room database storage
            appointment.setFacilityName(appointment.getPatientDetails().getFacilityObj().getName());
            appointment.setPersonId(appointment.getPatientDetails().getPersonId());
            insertAppointment(appointment);
        }
    }

    /**
     * Get Appointment LiveData from room database
     * @param personId PatientId to query the Appointment
     * @return The list of {@link Appointment}s wrapped in a LiveData object
     */
    public LiveData<List<Appointment>> getAppointmentsForPerson(String personId){
        return mApmisDao.findAppointmentsForPerson(personId);
    }

    /**
     * Get a Single Appointment LiveData object from room db
     * @param id the Appointment Id for the query
     * @return The Appointment wrapped in a LiveData object
     */
    public LiveData<Appointment> findAppointmentById(String id){
        return mApmisDao.findAppointmentById(id);
    }

    /**
     * Get Appointment from room database. <br/>
     * <b>Note: This method must be called in a background thread</b>
     * @param id the Appointment id for the query
     * @return The Appointment object
     */
    public Appointment getAppointmentById(String id){
        return mApmisDao.getAppointmentById(id);
    }

    /**
     * Get Appointments for today as LiveData from room database
     * @return The list of {@link Appointment}s wrapped in a LiveData object
     */
    public LiveData<List<Appointment>> getTodaysAppointments(String personId){
        Date today = new Date();
        Date yesterday = new Date(today.getTime() - A_DAY_IN_MILLIS);
        Date tomorrow = new Date(today.getTime() + A_DAY_IN_MILLIS);

        String todayQuery = AppUtils.dateToDbString(today).substring(0, 10) + "%";
        String yesterdayQuery = AppUtils.dateToDbString(yesterday).substring(0, 10) + "%";
        String tomorrowQuery = AppUtils.dateToDbString(tomorrow).substring(0, 10) + "%";

        return mApmisDao.getDailyAppointments(personId, todayQuery, yesterdayQuery, tomorrowQuery);
    }

    public void deleteAllAppointments(){
        mApmisDao.deleteAllAppointments();
    }




}
