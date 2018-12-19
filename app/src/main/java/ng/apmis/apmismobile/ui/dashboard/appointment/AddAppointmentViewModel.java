package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.appointmentModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class AddAppointmentViewModel extends ViewModel {

    private LiveData<List<Facility>> mFacilities;
    private MutableLiveData<List<ClinicSchedule>> mClinics;
    private MutableLiveData<List<Service>> mServices;
    private MutableLiveData<List<Employee>> mEmployees;
    private LiveData<BillManager> mBill;

    private MutableLiveData<Appointment> mAppointment;

    private MutableLiveData<List<AppointmentType>> appointmentTypes;
    private List<OrderStatus> orderStatuses;

    private ApmisNetworkDataSource apmisNetworkDataSource;
    private ApmisRepository apmisRepository;

    public AddAppointmentViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
        this.apmisNetworkDataSource = apmisRepository.getNetworkDataSource();

        orderStatuses = apmisNetworkDataSource.getOrderStatuses();
        mAppointment = apmisNetworkDataSource.getAppointment();
    }


    //TODO Change to LiveData when ready to make statuses dynamically added
    public List<OrderStatus> getOrderStatuses() {
        return orderStatuses;
    }

    public MutableLiveData<List<AppointmentType>> getAppointmentTypes() {
        appointmentTypes = apmisNetworkDataSource.getAppointmentTypes();
        return appointmentTypes;
    }

    public LiveData<List<Facility>> getRegisteredFacilities(boolean shouldFetch) {
        if (shouldFetch)
            mFacilities = apmisNetworkDataSource.getRegisteredFacilities();
        else if (mFacilities == null)
            mFacilities = new MutableLiveData<>();
        return mFacilities;
    }

    public MutableLiveData<List<ClinicSchedule>> getClinics(String facilityId, boolean shouldFetch) {
        if (shouldFetch)
            mClinics = apmisNetworkDataSource.getClinicsForFacility(facilityId);
        else if (mClinics == null)
            mClinics = new MutableLiveData<>();
        return mClinics;
    }

    public MutableLiveData<List<Service>> getServices(String facilityId, boolean shouldFetch) {
        if (shouldFetch)
            mServices = apmisNetworkDataSource.getServicesForFacility(facilityId);
        else if (mServices == null)
            mServices = new MutableLiveData<>();
        return mServices;
    }

    public MutableLiveData<List<Employee>> getEmployees(String facilityId, boolean shouldFetch) {
        if (shouldFetch)
            mEmployees = apmisNetworkDataSource.getEmployeesForFacility(facilityId);
        else if (mEmployees == null)
            mEmployees = new MutableLiveData<>();
        return mEmployees;
    }

    public MutableLiveData<Appointment> getAppointment() {
        return mAppointment;
    }

    public LiveData<BillManager> getBill(String facilityId, String categoryId) {
        mBill = apmisNetworkDataSource.getBillManagerForFacilityServiceCategory(facilityId, categoryId);
        return mBill;
    }

    public void reFetchBillData(String facilityId, String categoryId){
        apmisNetworkDataSource.getBillManagerForFacilityServiceCategory(facilityId, categoryId);
    }


    public void resetClinics(){
        apmisNetworkDataSource.resetClinics();
    }

    public void resetServices(){
        apmisNetworkDataSource.resetServices();
    }

    public void resetEmployees(){
        apmisNetworkDataSource.resetEmployees();
    }

    public void tempRefreshFacilities(){
        mFacilities = new MutableLiveData<>();
    }

    public void tempRefreshApptTypes(){
        appointmentTypes = new MutableLiveData<>();
    }

    public void clearBillManager(){
        apmisNetworkDataSource.clearBillManager();
    }







    public void submitAppointment(Appointment appointment){
        apmisNetworkDataSource.submitAppointment(appointment);
    }

    public void insertAppointment(Appointment appointment){
        apmisRepository.insertAppointment(appointment);
    }
}
