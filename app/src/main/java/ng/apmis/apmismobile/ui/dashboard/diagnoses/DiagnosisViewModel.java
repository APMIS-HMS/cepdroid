package ng.apmis.apmismobile.ui.dashboard.diagnoses;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

/**
 * A simple {@link ViewModel} class
 */
public class DiagnosisViewModel extends ViewModel {

    private MutableLiveData<List<LabRequest>> mLabRequests;
    private ApmisNetworkDataSource networkDataSource;

    public DiagnosisViewModel(ApmisRepository apmisRepository){
        networkDataSource = apmisRepository.getNetworkDataSource();
    }

    public MutableLiveData<List<LabRequest>> getLabRequestsForPerson(String personId) {
        mLabRequests = networkDataSource.getLabRequestsForPerson(personId);
        return mLabRequests;
    }


}
