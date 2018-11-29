package ng.apmis.apmismobile.ui.dashboard.places;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

/**
 * Created by Thadeus-APMIS on 11/29/2018.
 */

public class FacilityLocationFactory implements ViewModelProvider.Factory {

    private ApmisRepository mRepository;

    public FacilityLocationFactory(ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FacilityLocationViewModel(mRepository);
    }
}
