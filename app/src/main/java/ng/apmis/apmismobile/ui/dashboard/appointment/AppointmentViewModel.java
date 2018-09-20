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

    private ApmisRepository apmisRepository;

    public AppointmentViewModel(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        sharedPreferencesManager = new SharedPreferencesManager(context);

        mAppointments = apmisRepository.getAppointmentsForPatient(sharedPreferencesManager.getPersonId());
    }

    public LiveData<List<Appointment>> getAppointmentsForPatient() {
        return mAppointments;
    }

}
