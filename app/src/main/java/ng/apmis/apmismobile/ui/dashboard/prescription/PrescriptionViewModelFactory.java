package ng.apmis.apmismobile.ui.dashboard.prescription;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import ng.apmis.apmismobile.data.ApmisRepository;

public class PrescriptionViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApmisRepository apmisRepository;

    public PrescriptionViewModelFactory(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PrescriptionViewModel(apmisRepository);
    }
}
