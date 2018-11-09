package ng.apmis.apmismobile.ui.dashboard.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;

public class FundWalletViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<String> payData;

    public FundWalletViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }

    public LiveData<String> getPayData(String referenceCode, int amountPaid) {
        payData = apmisRepository.getNetworkDataSource().getPaymentVerificationData(referenceCode, amountPaid);
        return payData;
    }

    public void clearVerification(){
        apmisRepository.getNetworkDataSource().clearPaymentVerificationData();
    }

}
