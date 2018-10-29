package ng.apmis.apmismobile.ui.dashboard.find;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.io.File;
import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

public class PreviousItemsViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<List<Appointment>> personAppointments;

    PreviousItemsViewModel(Context context, ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }

    public LiveData<List<Appointment>> getPersonAppointments(String personId){
        personAppointments = apmisRepository.getNetworkDataSource().getAllAppointments(personId);
        return personAppointments;
    }

}
