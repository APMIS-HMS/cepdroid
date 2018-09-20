package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.facilityModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduledClinic;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class AddAppointmentViewModel extends ViewModel {

    private MutableLiveData<List<Patient>> mPatients;
    private MutableLiveData<List<ScheduledClinic>> mClinics;
    private MutableLiveData<List<Service>> mServices;
    private MutableLiveData<List<Employee>> mEmployees;
    private MutableLiveData<List<ScheduleItem>> mSchedules;

    private MutableLiveData<Appointment> mAppointment;

    private List<AppointmentType> appointmentTypes;
    private List<OrderStatus> orderStatuses;

    private ApmisNetworkDataSource apmisNetworkDataSource;
    private ApmisRepository apmisRepository;

    public AddAppointmentViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
        this.apmisNetworkDataSource = apmisRepository.getNetworkDataSource();

        appointmentTypes = apmisNetworkDataSource.getAppointmentTypes();
        orderStatuses = apmisNetworkDataSource.getOrderStatuses();

        mPatients = apmisNetworkDataSource.getPatientDetailsForPerson();
        mClinics = apmisNetworkDataSource.getClinicsForFacility();
        mServices = apmisNetworkDataSource.getServicesForFacility();
        mEmployees = apmisNetworkDataSource.getEmployeesForFacility();
        mSchedules = apmisNetworkDataSource.getSchedulesForClinic();
        mAppointment = apmisNetworkDataSource.getAppointment();
    }

    //TODO Change to LiveData when ready to make types dynamically added
    public List<AppointmentType> getAppointmentTypes() {
        return appointmentTypes;
    }

    //TODO Change to LiveData when ready to make statuses dynamically added
    public List<OrderStatus> getOrderStatuses() {
        return orderStatuses;
    }

    public MutableLiveData<List<Patient>> getPatientsForPerson() {
        return mPatients;
    }

    public MutableLiveData<List<ScheduledClinic>> getClinics() {
        return mClinics;
    }

    public MutableLiveData<List<ScheduleItem>> getSchedules() {
        return mSchedules;
    }

    public MutableLiveData<List<Service>> getServices() {
        return mServices;
    }

    public MutableLiveData<List<Employee>> getEmployees() {
        return mEmployees;
    }

    public MutableLiveData<Appointment> getAppointment() {
        return mAppointment;
    }




    public void setSchedules(ScheduledClinic clinic){
        apmisNetworkDataSource.setSchedulesForClinic(clinic);
    }

    public void resetClinics(){
        apmisNetworkDataSource.refreshClinics();
    }

    public void resetServices(){
        apmisNetworkDataSource.refreshServices();
    }

    public void resetEmployees(){
        apmisNetworkDataSource.refreshEmployees();
    }

    public void resetSchedules(){
        apmisNetworkDataSource.refreshSchedules();
    }


    public void submitAppointment(Appointment appointment){
        apmisNetworkDataSource.submitAppointment(appointment);
    }

    public void insertAppointment(Appointment appointment){
        apmisRepository.insertAppointment(appointment);
    }
}
