package ng.apmis.apmismobile.ui.dashboard.payment.cardEntry;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;

public class CardEntryViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<String> payData;

    public CardEntryViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }


    public LiveData<String> getPayData(String referenceCode, int amountPaid, boolean isCardReused, boolean shouldSaveCard) {
        payData = apmisRepository.getNetworkDataSource().getPaymentVerificationData(referenceCode, amountPaid, isCardReused, shouldSaveCard);
        return payData;
    }

    public void clearVerification(){
        apmisRepository.getNetworkDataSource().clearPaymentVerificationData();
    }

}
