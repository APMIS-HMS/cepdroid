package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;
/**
 * Created by Thadeus-APMIS on 11/9/2018.
 */

public class TransactionHistoryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApmisNetworkDataSource apmisNetworkDataSource;

    public TransactionHistoryViewModelFactory (ApmisNetworkDataSource apmisNetworkDataSource) {
        this.apmisNetworkDataSource = apmisNetworkDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TransactionViewModel(apmisNetworkDataSource);
    }

}
