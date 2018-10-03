package ng.apmis.apmismobile.ui.dashboard.prescription;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

public class PrescriptionViewModel extends ViewModel {

    private MutableLiveData<List<Prescription>> mPrescriptions;
    private ApmisNetworkDataSource networkDataSource;

    public PrescriptionViewModel(ApmisRepository apmisRepository){
        this.networkDataSource = apmisRepository.getNetworkDataSource();

        mPrescriptions = networkDataSource.getPrescriptionsForPerson();
    }

    public MutableLiveData<List<Prescription>> getPrescriptionsForPerson() {
        return mPrescriptions;
    }
}
