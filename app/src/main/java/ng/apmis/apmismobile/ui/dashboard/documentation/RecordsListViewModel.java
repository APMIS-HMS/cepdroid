package ng.apmis.apmismobile.ui.dashboard.documentation;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class RecordsListViewModel extends ViewModel {

    private MutableLiveData<List<Documentation>> mDocumentations;

    private ApmisNetworkDataSource apmisNetworkDataSource;
    private ApmisRepository apmisRepository;

    public RecordsListViewModel(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        this.apmisNetworkDataSource = apmisRepository.getNetworkDataSource();
    }

    public MutableLiveData<List<Documentation>> getRecordsForPerson(String personId) {
        mDocumentations = apmisNetworkDataSource.getDocumentationsForPerson(personId);
        return mDocumentations;
    }

}
