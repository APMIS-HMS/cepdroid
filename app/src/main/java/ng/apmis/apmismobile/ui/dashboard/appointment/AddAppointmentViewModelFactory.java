package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class AddAppointmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApmisNetworkDataSource apmisNetworkDataSource;

    public AddAppointmentViewModelFactory(ApmisNetworkDataSource apmisNetworkDataSource) {
        this.apmisNetworkDataSource = apmisNetworkDataSource;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddAppointmentViewModel(apmisNetworkDataSource);
    }
}
