package ng.apmis.apmismobile.ui.dashboard.payment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

public class FundWalletViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApmisRepository mRepository;

    public FundWalletViewModelFactory (ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new FundWalletViewModel(mRepository);
    }
}
