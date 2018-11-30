package ng.apmis.apmismobile.ui.dashboard.payment.cards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.Wallet;

public class CardsViewModel extends ViewModel {

    private ApmisRepository repository;
    private LiveData<Wallet> wallet;

    public CardsViewModel(ApmisRepository repository){
        this.repository = repository;

    }

    public LiveData<Wallet> getWallet(String personId) {
        wallet = repository.getNetworkDataSource().getPersonWallet(personId, true);
        return wallet;
    }
}
