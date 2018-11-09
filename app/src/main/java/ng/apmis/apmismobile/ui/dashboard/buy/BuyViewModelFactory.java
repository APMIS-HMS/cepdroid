package ng.apmis.apmismobile.ui.dashboard.buy;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.ui.dashboard.buy.payBills.BuyViewModel;

public class BuyViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApmisRepository mRepository;

    public BuyViewModelFactory (ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new BuyViewModel(mRepository);
    }
}
