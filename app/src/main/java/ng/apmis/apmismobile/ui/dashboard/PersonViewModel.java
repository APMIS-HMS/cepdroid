package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.model.PersonEntry;

/**
 * This class takes care of view model to manage data lifecycle in activity
 */
public class PersonViewModel extends ViewModel {

    private LiveData<PersonEntry> mPersonEntry;

    PersonViewModel(ApmisRepository apmisRepository) {
        mPersonEntry = apmisRepository.getUserData();
    }

    public LiveData<PersonEntry> getmPersonEntry() {
        return mPersonEntry;
    }
}
