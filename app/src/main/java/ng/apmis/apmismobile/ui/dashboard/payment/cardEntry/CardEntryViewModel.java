package ng.apmis.apmismobile.ui.dashboard.payment.cardEntry;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;

public class CardEntryViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<List<String>> payData;

    public CardEntryViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }


    public LiveData<List<String>> getPayData(String referenceCode, int amountPaid, boolean isCardReused,
                                       boolean shouldSaveCard, String encEmail, String encAuth) {
        payData = apmisRepository.getNetworkDataSource().getPaymentVerificationData(referenceCode, amountPaid,
                isCardReused, shouldSaveCard, encEmail, encAuth);
        return payData;
    }

    public void clearVerification(){
        apmisRepository.getNetworkDataSource().clearPaymentVerificationData();
    }

}
