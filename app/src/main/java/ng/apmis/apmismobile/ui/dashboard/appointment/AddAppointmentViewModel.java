package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

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

    private ApmisNetworkDataSource apmisNetworkDataSource;

    public AddAppointmentViewModel(ApmisNetworkDataSource apmisNetworkData) {
        this.apmisNetworkDataSource = apmisNetworkData;
        mPatients = apmisNetworkData.getPatientDetailsForPerson();
        mClinics = apmisNetworkData.getClinicsForFacility();
        mServices = apmisNetworkData.getServicesForFacility();
        mEmployees = apmisNetworkData.getEmployeesForFacility();
        mSchedules = apmisNetworkData.getSchedulesForClinic();
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
}
