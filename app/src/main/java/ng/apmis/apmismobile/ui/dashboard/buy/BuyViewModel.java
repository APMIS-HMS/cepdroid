package ng.apmis.apmismobile.ui.dashboard.buy;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.personModel.Wallet;

public class BuyViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<Wallet> wallet;

    public BuyViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }

    public LiveData<Wallet> getPersonWallet(String personId) {
        wallet = apmisRepository.getNetworkDataSource().getPersonWallet(personId);
        return wallet;
    }

    public void clearPersonWallet(){
        apmisRepository.getNetworkDataSource().clearWallet();
    }

}
