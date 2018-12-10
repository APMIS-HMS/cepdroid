package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

public class DashboardFragmentViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private SharedPreferencesManager sharedPreferencesManager;
    private LiveData<List<Appointment>> mAppointments;

    public DashboardFragmentViewModel(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public LiveData<List<Appointment>> getTodaysAppointments() {
        mAppointments = apmisRepository.getTodaysAppointments(sharedPreferencesManager.getPersonId());
        return mAppointments;
    }

}
