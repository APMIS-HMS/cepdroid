package ng.apmis.apmismobile.ui.dashboard.payment.cards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.cardModel.Card;
import ng.apmis.apmismobile.data.database.personModel.Wallet;

public class CardsViewModel extends ViewModel {

    private ApmisRepository repository;
    private LiveData<Wallet> wallet;
    private LiveData<List<String>> payData;
    private LiveData<String> removalStatus;

    public CardsViewModel(ApmisRepository repository){
        this.repository = repository;
    }

    public LiveData<Wallet> getWallet(String personId) {
        wallet = repository.getNetworkDataSource().getPersonWallet(personId, true);
        return wallet;
    }

    public LiveData<List<String>> getPayData(String referenceCode, int amountPaid, boolean isCardReused,
                                       boolean shouldSaveCard, String encEmail, String encAuth) {
        payData = repository.getNetworkDataSource().getPaymentVerificationData(referenceCode, amountPaid,
                isCardReused, shouldSaveCard, encEmail, encAuth);
        return payData;
    }

    public LiveData<String> getRemovalStatus(String cardId, Wallet wallet) {
        removalStatus = repository.getNetworkDataSource().getCardRemovalStatus(cardId, wallet);
        return removalStatus;
    }

    public void clearVerification(){
        repository.getNetworkDataSource().clearPaymentVerificationData();
    }

    public void clearCardRemovalStatus(){
        repository.getNetworkDataSource().clearCardRemovalStatus();
    }
}
