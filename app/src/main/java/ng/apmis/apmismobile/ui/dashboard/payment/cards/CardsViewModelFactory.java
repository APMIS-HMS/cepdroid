package ng.apmis.apmismobile.ui.dashboard.payment.cards;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

public class CardsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ApmisRepository mRepository;

    public CardsViewModelFactory(ApmisRepository apmisRepository) {
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CardsViewModel(mRepository);
    }
}
