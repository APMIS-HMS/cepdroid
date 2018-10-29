package ng.apmis.apmismobile.ui.dashboard.find;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

public class PreviousItemsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context mContext;
    ApmisRepository mRepository;

    public PreviousItemsViewModelFactory (Context context, ApmisRepository apmisRepository) {
        this.mContext = context;
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PreviousItemsViewModel(mContext, mRepository);
    }
}
