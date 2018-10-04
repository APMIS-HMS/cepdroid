package ng.apmis.apmismobile.ui.dashboard.diagnoses;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ng.apmis.apmismobile.data.ApmisRepository;

public class DiagnosisViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApmisRepository apmisRepository;

    public DiagnosisViewModelFactory(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DiagnosisViewModel(apmisRepository);
    }
}