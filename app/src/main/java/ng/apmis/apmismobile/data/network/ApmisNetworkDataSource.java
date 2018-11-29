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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.appointmentModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.Category;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
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
    private final MutableLiveData<PersonEntry> mDownloadedPersonData;
    private MutableLiveData<List<Patient>> patientDetails;
    private MutableLiveData<List<Facility>> registeredFacilities;
    private MutableLiveData<List<String>> registeredFacilityIds;
    private MutableLiveData<List<ClinicSchedule>> clinics;
    private MutableLiveData<List<ScheduleItem>> schedules;
    private MutableLiveData<List<Employee>> employees;
    private MutableLiveData<List<Documentation>> documentations;
    private MutableLiveData<List<Service>> services;
    private MutableLiveData<List<Prescription>> prescriptions;
    private MutableLiveData<List<LabRequest>> labRequests;
    private MutableLiveData<List<SearchTermItem>> foundObjects;
    private MutableLiveData<Appointment> appointment;
    private MutableLiveData<List<Appointment>> appointments;
    private MutableLiveData<String> profilePhotoPath;
    private MutableLiveData<Facility> facilityData;
    private MutableLiveData<String> serviceCategoryId;
    private MutableLiveData<BillManager> categoryBillManager;
    private MutableLiveData<Wallet> personWallet;
    private MutableLiveData<String> paymentVerificationData;
    private MutableLiveData<Patient> registeredPatient;
    private MutableLiveData<List<AppointmentType>> appointmentTypes;
    private MutableLiveData<PersonEntry> paidByPerson;

    //TODO Switch to LiveData later
    private List<OrderStatus> orderStatuses;
    private MutableLiveData<Facility[]> nearbyLocations;


    ApmisNetworkDataSource(Context context, APMISAPP executorThreads) {
        mContext = context;
        apmisExecutors = executorThreads;
        mDownloadedPersonData = new MutableLiveData<>();
        patientDetails = new MutableLiveData<>();
        registeredFacilities = new MutableLiveData<>();
        registeredFacilityIds = new MutableLiveData<>();
        clinics = new MutableLiveData<>();
        schedules = new MutableLiveData<>();
        employees = new MutableLiveData<>();
        services = new MutableLiveData<>();
        documentations = new MutableLiveData<>();
        appointment = new MutableLiveData<>();
        foundObjects = new MutableLiveData<>();
        appointments = new MutableLiveData<>();
        prescriptions = new MutableLiveData<>();
        labRequests = new MutableLiveData<>();
        facilityData = new MutableLiveData<>();
        serviceCategoryId = new MutableLiveData<>();
        categoryBillManager = new MutableLiveData<>();
        profilePhotoPath = new MutableLiveData<>();
        personWallet = new MutableLiveData<>();
        appointmentTypes = new MutableLiveData<>();
        paymentVerificationData = new MutableLiveData<>();
        paidByPerson = new MutableLiveData<>();
        registeredPatient = new MutableLiveData<>();

        orderStatuses = new ArrayList<>();
        sharedPreferencesManager = new SharedPreferencesManager(context);
        nearbyLocations = new MutableLiveData<>();

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
                new NetworkDataCalls(mContext).fetchRegisteredFacilities(mContext, sharedPreferencesManager.getPersonId(),  sharedPreferencesManager.getStoredUserAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Update a person's details
     */
    public void updatePersonDetails(PersonEntry personEntry) {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Update person started");
            new NetworkDataCalls(mContext).updatePersonData(personEntry, sharedPreferencesManager.getStoredUserAccessToken());
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
            new NetworkDataCalls(mContext).fetchClinicalDocumentationForPerson(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
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

    /**
     * Fetch the person's profile photo and save it to a local file
     * @param personEntry The Person fetched
     * @param downloadFile The local file to download the photo into
     */
    private void fetchAndDownloadPersonProfilePhoto(PersonEntry personEntry, File downloadFile){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Image", "Fetch Image started");
            new NetworkDataCalls(mContext).downloadProfilePictureForPerson(mContext, personEntry, downloadFile);
        });
    }


    /**
     *
     * @param itemType
     * @param searchQuery
     */
    private void fetchFoundItems(String itemType, String searchQuery){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch found items started");
            new NetworkDataCalls(mContext).searchForItems(mContext, itemType, searchQuery, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    private void fetchFacilityData(String id){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch facility details started");
            new NetworkDataCalls(mContext).fetchFacilityDetails(mContext, id, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    private void fetchServiceCategoryForFacility(String id){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch service category started");
            new NetworkDataCalls(mContext).fetchServiceCategoryId(mContext, id, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    private void fetchServiceCategoryBillManager(String facilityId, String categoryId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch bill manager started");
            new NetworkDataCalls(mContext).fetchServiceCategoryBillManager(mContext, facilityId, categoryId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    private void fetchPersonWallet(String personId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch person wallet started");
            new NetworkDataCalls(mContext).fetchPersonWallet(mContext, personId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }

    private void fetchPaymentVerificationData(String referenceCode, int amountPaid, boolean isCardReused, boolean shouldSaveCard){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Fetch payment verification started");
            new NetworkDataCalls(mContext).fetchPaymentVerificationData(mContext, referenceCode, amountPaid, sharedPreferencesManager.getPersonId(),
                    sharedPreferencesManager.getStoredUserAccessToken(), isCardReused, shouldSaveCard);
        });
    }

    private void registerPatientInFacility(String personId, String facilityId){
        apmisExecutors.networkIO().execute(() -> {
            Log.d("Found", "Patient registration started");
            new NetworkDataCalls(mContext).registerPatientInFacility(mContext, personId, facilityId, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }




    private void fetchPaidByPerson(String paidById) {
        apmisExecutors.networkIO().execute(() -> {
            new NetworkDataCalls(mContext).getPaidBy(mContext, paidById, sharedPreferencesManager.getStoredUserAccessToken());
        });
    }




















    //Person entry
    public LiveData<PersonEntry> getCurrentPersonData() {
        return mDownloadedPersonData;
    }

    public void setCurrentPersonData(PersonEntry personEntry) {
        //post value for liveData use
        mDownloadedPersonData.postValue(personEntry);
    }

    public void setProfilePhotoPath(String path){
        profilePhotoPath.postValue(path);
    }

    public MutableLiveData<String> getPersonProfilePhotoPath(PersonEntry person, File finalLocalFile){
        //TODO: When implementing liveData fetch in other methods, do it like this here, instead of in the view
        //Fetch the photo
        fetchAndDownloadPersonProfilePhoto(person, finalLocalFile);

        //return the local path the photo was downloaded to
        return profilePhotoPath;
    }


    //Registered Facility Ids and metadata

    public void setRegisteredFacilities(List<String> ids, List<Facility> facilities){
        List<String> previousIds = this.registeredFacilityIds.getValue();
        List<Facility> previous = this.registeredFacilities.getValue();


        if (ids != null)
            this.registeredFacilityIds.postValue(ids);

        else {//set null if a null value was received, but post back the original value afterwards
            this.registeredFacilityIds.setValue(null);

            if (previous != null)
                this.registeredFacilityIds.postValue(previousIds);
            else
                this.registeredFacilityIds = new MutableLiveData<>();
        }

        if (facilities != null)
            this.registeredFacilities.postValue(facilities);

        else {//set null if a null value was received, but post back the original value afterwards
            this.registeredFacilities.setValue(null);

            if (previous != null)
                this.registeredFacilities.postValue(previous);
            else
                this.registeredFacilities = new MutableLiveData<>();
        }
    }

    public LiveData<List<String>> getRegisteredFacilityIds(){
        return registeredFacilityIds;
    }

    public LiveData<List<Facility>> getRegisteredFacilities(){
        fetchPersonDetails();
        return registeredFacilities;
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
        List<AppointmentType> previous = this.appointmentTypes.getValue();


        if (appointmentTypes != null)
            this.appointmentTypes.postValue(appointmentTypes);

        else {//set null if a null value was received, but post back the original value afterwards
            this.appointmentTypes.setValue(null);

            if (previous != null)
                this.appointmentTypes.postValue(appointmentTypes);
            else
                this.appointmentTypes = new MutableLiveData<>();
        }
    }

    public MutableLiveData<List<AppointmentType>> getAppointmentTypes(){
        fetchAppointmentTypes();
        return this.appointmentTypes;
    }


    //Employees

    public void setEmployeesForFacility(List<Employee> employees){
        if (employees != null) {
            List<Employee> doctors = new ArrayList<>();
            for (Employee employee : employees){
                if (employee.getProfessionId().equalsIgnoreCase("Doctor"))
                    doctors.add(employee);
            }
            this.employees.postValue(doctors);

        } else {//post null if a null value was received
            this.employees.postValue(null);
        }
    }

    public MutableLiveData<List<Employee>> getEmployeesForFacility(String facilityId) {
        fetchEmployeesForFacility(facilityId);
        return employees;
    }

    public void resetEmployees(){
        this.employees = new MutableLiveData<>();
    }



    //Clinic schedules

    public void setClinicsForFacility(List<ClinicSchedule> clinics){
        if (clinics != null) {
            this.clinics.postValue(clinics);

        } else {//post null if a null value was received
            this.clinics.postValue(null);
        }
    }

    public MutableLiveData<List<ClinicSchedule>> getClinicsForFacility(String facilityId) {
        fetchClinicSchedulesForFacility(facilityId);
        return clinics;
    }

    public void resetClinics(){
        this.clinics = new MutableLiveData<>();
    }


    //Services

    public void setServicesForFacility(List<Category> categories) {
        if (categories != null) {
            for (Category category : categories){
                if (category.getName().equalsIgnoreCase("Appointment")) {

                    List<Service> services = category.getServices();

                    for (Service service : services) //input the facilityId
                        service.setFacilityId(category.getFacilityId());

                    this.services.postValue(services);
                    break;
                }
            }
        }

        else {//post null if a null value was received
            this.services.postValue(null);
        }

    }

    public MutableLiveData<List<Service>> getServicesForFacility(String facilityId) {
        fetchCategoriesForFacility(facilityId);
        return services;
    }

    public void resetServices(){
        this.services = new MutableLiveData<>();
    }


    //Category Ids, looking for Registration

    public LiveData<String> getServiceCategoryIdForFacility(String id){
        fetchServiceCategoryForFacility(id);
        return serviceCategoryId;
    }

    public void setServiceCategoryIdForFacility(String categoryServiceIdValue){
        serviceCategoryId.postValue(categoryServiceIdValue);
    }

    public void clearServiceCategoryId(){
        serviceCategoryId = new MutableLiveData<>();
    }


    //Patients

    public void setPatientDetailsForPerson(List<Patient> patientDetails){
        this.patientDetails.postValue(patientDetails);
    }

    public MutableLiveData<List<Patient>> getPatientDetailsForPerson(){
        return patientDetails;
    }


    //Appointment(s)
    public void setAppointment(Appointment appointment){
        this.appointment.postValue(appointment);
    }

    public MutableLiveData<Appointment> getAppointment() {
        return appointment;
    }


    //TODO add the empty sized change to all of them
    public void setAppointments(List<Appointment> appointments){
        List<Appointment> previous = this.appointments.getValue();

        if (appointments != null ) {
            this.appointments.postValue(appointments);

            if (appointments.size() == 0)
                this.appointments = new MutableLiveData<>();
        }

        else {//set null if a null value was received, but post back the original value afterwards
            this.appointments.setValue(null);

            if (previous != null && previous.size() > 0)
                this.appointments.postValue(previous);
            else
                this.appointments = new MutableLiveData<>();
        }
    }

    public LiveData<List<Appointment>> getAllAppointments(String personId){
        fetchAppointmentsForPerson(personId);
        return appointments;
    }

    public void clearFetchedAppointments(){
        appointments = new MutableLiveData<>();
    }




    //Documentation
    public void setDocumentationsForPerson(List<Documentation> documentations){
        List<Documentation> previous = this.documentations.getValue();

        if (documentations != null)
            this.documentations.postValue(documentations);

        else {//set null if a null value was received, but post back the original value afterwards
            this.documentations.setValue(null);

            if (previous != null)
                this.documentations.postValue(previous);
            else
                this.documentations = new MutableLiveData<>();
        }
    }

    public MutableLiveData<List<Documentation>> getDocumentationsForPerson(String personId){
        fetchMedicalRecordsForPerson(personId);
        return this.documentations;
    }


    //Prescription

    public void setPrescriptionsForPerson(List<Prescription> prescriptions) {
        List<Prescription> previous = this.prescriptions.getValue();

        if (prescriptions != null)
            this.prescriptions.postValue(prescriptions);

        else {//set null if a null value was received, but post back the original value afterwards
            this.prescriptions.setValue(null);

            if (previous != null)
                this.prescriptions.postValue(previous);
            else
                this.prescriptions = new MutableLiveData<>();
        }
    }

    public MutableLiveData<List<Prescription>> getPrescriptionsForPerson(String personId){
        fetchPrescriptionsForPerson(personId);
        return this.prescriptions;
    }

    //Laboratory Requests

    public void setLabRequestsForPerson(List<LabRequest> labRequests) {
        List<LabRequest> previous = this.labRequests.getValue();

        if (labRequests != null)
            this.labRequests.postValue(labRequests);

        else {//set null if a null value was received, but post back the original value afterwards
            this.labRequests.setValue(null);

            if (previous != null)
                this.labRequests.postValue(previous);
            else
                this.labRequests = new MutableLiveData<>();
        }

    }

    public MutableLiveData<List<LabRequest>> getLabRequestsForPerson(String personId){
        fetchLabRequestsForPerson(personId);
        return this.labRequests;
    }


    //Found Objects in searches
    public LiveData<List<SearchTermItem>> getFoundObjects(String itemType, String searchQuery){
        fetchFoundItems(itemType, searchQuery);
        return foundObjects;
    }

    public void setFoundObjects(List<SearchTermItem> foundObjectsList){
        foundObjects.postValue(foundObjectsList);
    }

    public void clearFoundObjects(){
        foundObjects = new MutableLiveData<>();
    }


    //Facility Details

    public LiveData<Facility> getFacilityDetails(String id){
        fetchFacilityData(id);
        return facilityData;
    }

    public void setFacilityData(Facility facilityDataValue){
        facilityData.postValue(facilityDataValue);
    }

    public void clearFacilityData(){
        facilityData = new MutableLiveData<>();
    }


    //Bill Manager
    public LiveData<BillManager> getBillManagerForFacilityServiceCategory(String facilityId, String categoryId){
        fetchServiceCategoryBillManager(facilityId, categoryId);
        return categoryBillManager;
    }

    public void setCategoryBillManager(BillManager billManager){
        categoryBillManager.postValue(billManager);
    }

    public void clearBillManager(){
        categoryBillManager = new MutableLiveData<>();
    }



    //Wallet
    public LiveData<Wallet> getPersonWallet(String personId){
        fetchPersonWallet(personId);
        return personWallet;
    }

    public void setPersonWallet(Wallet wallet){
        personWallet.postValue(wallet);
    }

    public void clearWallet(){
        personWallet = new MutableLiveData<>();
    }


    //Payment verification data
    public LiveData<String> getPaymentVerificationData(String referenceCode, int amountPaid, boolean isCardReused, boolean shouldSaveCard){
        fetchPaymentVerificationData(referenceCode, amountPaid, isCardReused, shouldSaveCard);
        return paymentVerificationData;
    }

    public LiveData<PersonEntry> getPaidByPersonData (String personId) {
        fetchPaidByPerson(personId);
        return paidByPerson;
    }

    public void setPaymentVerificationData(String status){
        paymentVerificationData.postValue(status);
    }

    public void clearPaymentVerificationData(){
        paymentVerificationData = new MutableLiveData<>();
    }


    /**
     * Fetch all Nearby Locations Object[]
     */
    public void setNearbyLocations (Facility[] nearbyLocations) {
        this.nearbyLocations.postValue(nearbyLocations);
    }

    public LiveData<Facility[]> getNearbyLocations () {
        apmisExecutors.networkIO().execute(() -> {
            Log.d(LOG_TAG, "Fetch NearbyFacilities started");
            new NetworkDataCalls(mContext).fetchNearbyFacilities(mContext, sharedPreferencesManager.getStoredUserAccessToken());
        });
        return nearbyLocations;
    }



    //Patient registration
    public void clearPatientOnRegistration(){
        registeredPatient = new MutableLiveData<>();
    }

    public LiveData<Patient> registerPatient(String personId, String facilityId){
        registerPatientInFacility(personId, facilityId);
        return registeredPatient;
    }

    public void setRegisteredPatient(Patient patient){
        if (patient != null) {
            //quickly add the new facility id to avoid double registration
            if (registeredFacilityIds.getValue() != null) {
                List<String> current = new ArrayList<>(registeredFacilityIds.getValue());
                current.add(patient.getFacilityId());
                registeredFacilityIds.postValue(current);
                Log.e("Reg","added "+patient.getFacilityId());
            }

            //then refresh the facility list for the new facility the patient just registered in
            new NetworkDataCalls(mContext).fetchRegisteredFacilities(mContext, sharedPreferencesManager.getPersonId(), sharedPreferencesManager.getStoredUserAccessToken());
        }

        registeredPatient.postValue(patient);
    }

    public void setPaidByName(PersonEntry personEntry) {
        paidByPerson.postValue(personEntry);
    }
}
