package ng.apmis.apmismobile.ui.dashboard.profile.profileAction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.io.File;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

public class ProfileActionViewModel extends ViewModel {

    private LiveData<PersonEntry> mPersonEntry;
    private ApmisRepository apmisRepository;
    private LiveData<String> personPhotoPath;

    ProfileActionViewModel(Context context, ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
        mPersonEntry = apmisRepository.getUserData();

    }

    public LiveData<PersonEntry> getPersonEntry() {
        return mPersonEntry;
    }

    public LiveData<String> getPersonPhotoPath(PersonEntry person, File finalLocalFile){
        personPhotoPath = apmisRepository.getNetworkDataSource().getPersonProfilePhotoPath(person, finalLocalFile);
        return personPhotoPath;
    }
}
