package ng.apmis.apmismobile.ui.dashboard.healthProfile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class HealthProfileViewModel extends ViewModel{
    private MutableLiveData<List<Documentation>> mDocumentations;

    private ApmisNetworkDataSource apmisNetworkDataSource;
    private ApmisRepository apmisRepository;

    public HealthProfileViewModel(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        this.apmisNetworkDataSource = apmisRepository.getNetworkDataSource();

        mDocumentations = apmisNetworkDataSource.getDocumentationsForPerson();
    }

    public MutableLiveData<List<Documentation>> getRecordsForPerson() {
        return mDocumentations;
    }
}
