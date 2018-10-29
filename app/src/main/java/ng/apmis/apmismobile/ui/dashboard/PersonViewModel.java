package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

/**
 * This class takes care of view model to manage data lifecycle in activity
 */
public class PersonViewModel extends ViewModel {

    private LiveData<PersonEntry> mPersonEntry;
    private ApmisRepository apmisRepository;
    private LiveData<String> personPhotoPath;

    PersonViewModel(Context context, ApmisRepository apmisRepository) {
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
