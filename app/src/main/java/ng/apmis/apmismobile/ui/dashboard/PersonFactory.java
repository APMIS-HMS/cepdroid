package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

/**
 * This class is a viewModelProvider class
 */

public class PersonFactory extends ViewModelProvider.NewInstanceFactory {

    ApmisRepository mRepository;

    public PersonFactory (ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PersonViewModel(mRepository);
    }
}
