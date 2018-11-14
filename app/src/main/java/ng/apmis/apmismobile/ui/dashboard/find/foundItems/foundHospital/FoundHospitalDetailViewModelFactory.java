package ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

public class FoundHospitalDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApmisRepository mRepository;

    public FoundHospitalDetailViewModelFactory (ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new FoundHospitalDetailViewModel(mRepository);
    }

}
