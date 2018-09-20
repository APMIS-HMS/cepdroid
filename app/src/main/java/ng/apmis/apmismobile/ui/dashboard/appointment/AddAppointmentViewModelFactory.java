package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class AddAppointmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApmisRepository apmisRepository;

    public AddAppointmentViewModelFactory(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddAppointmentViewModel(apmisRepository);
    }
}
