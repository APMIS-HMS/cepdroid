package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

public class AppointmentViewModel extends ViewModel {

    private SharedPreferencesManager sharedPreferencesManager;

    private LiveData<List<Appointment>> mAppointments;

    private LiveData<List<Appointment>> appointmentLoadStatus;

    private ApmisRepository apmisRepository;

    public AppointmentViewModel(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        sharedPreferencesManager = new SharedPreferencesManager(context);

    }

    public LiveData<List<Appointment>> getAppointmentsForPatient() {
        mAppointments = apmisRepository.getAppointmentsForPatient(sharedPreferencesManager.getPersonId());
        return mAppointments;
    }

    public void clearLoadStatus(){
        apmisRepository.getNetworkDataSource().clearFetchedAppointments();
    }

    public LiveData<List<Appointment>> getAppointmentLoadStatus(String personId){
        appointmentLoadStatus = apmisRepository.getNetworkDataSource().getAllAppointments(personId);
        return appointmentLoadStatus;
    }

}
