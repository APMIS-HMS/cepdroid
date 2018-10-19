package ng.apmis.apmismobile.ui.dashboard.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

public class EditProfileViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<PersonEntry> mPersonEntry;
    private LiveData<String> personPhotoPath;

    EditProfileViewModel(Context context, ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
        mPersonEntry = apmisRepository.getUserData();

    }

    public LiveData<PersonEntry> getPersonEntry() {
        return mPersonEntry;
    }

    public LiveData<String> getPersonPhotoPath(){
        personPhotoPath = apmisRepository.getNetworkDataSource().getPersonProfilePhotoPath();
        return personPhotoPath;
    }



}
