package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import ng.apmis.apmismobile.data.ApmisRepository;

public class DashboardFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ApmisRepository apmisRepository;
    private final Context context;

    public DashboardFragmentViewModelFactory(ApmisRepository apmisRepository, Context context) {
        this.apmisRepository = apmisRepository;
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DashboardFragmentViewModel(apmisRepository, context);
    }

}
