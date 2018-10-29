package ng.apmis.apmismobile.ui.dashboard.profile.viewEditProfile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.io.File;
import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

public class EditProfileViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<PersonEntry> mPersonEntry;
    private LiveData<PersonEntry> mDownloadedPerson;
    private LiveData<String> personPhotoPath;

    EditProfileViewModel(Context context, ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
        mPersonEntry = apmisRepository.getUserData();
        mDownloadedPerson = apmisRepository.getNetworkDataSource().getCurrentPersonData();

    }

    public LiveData<PersonEntry> getPersonEntry() {
        return mPersonEntry;
    }

    public LiveData<PersonEntry> getDownloadedPerson() {
        return mDownloadedPerson;
    }

    public LiveData<String> getPersonPhotoPath(PersonEntry person, File finalLocalFile){
        personPhotoPath = apmisRepository.getNetworkDataSource().getPersonProfilePhotoPath(person, finalLocalFile);
        return personPhotoPath;
    }

    public void updatePersonEntry(PersonEntry personEntry){
        apmisRepository.getNetworkDataSource().updatePersonDetails(personEntry);
    }

}
