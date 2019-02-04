package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

/**
 * Created by Thadeus-APMIS on 11/9/2018.
 */

public class TransactionViewModel extends ViewModel {

    private ApmisNetworkDataSource apmisNetworkDataSource;
    private LiveData<Wallet> wallet;

    public TransactionViewModel(ApmisNetworkDataSource apmisNetworkDataSource) {
        this.apmisNetworkDataSource = apmisNetworkDataSource;
    }

    public LiveData<Wallet> getPersonWallet(String personId) {
        wallet = apmisNetworkDataSource.getPersonWallet(personId, false);
        return wallet;
    }

    public void reFetchWallet(String personId){
        wallet = apmisNetworkDataSource.getPersonWallet(personId, true);
    }

}
