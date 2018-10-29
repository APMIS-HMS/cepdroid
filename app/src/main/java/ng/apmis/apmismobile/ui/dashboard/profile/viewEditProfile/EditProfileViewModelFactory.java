package ng.apmis.apmismobile.ui.dashboard.profile.viewEditProfile;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import ng.apmis.apmismobile.data.ApmisRepository;

public class EditProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context mContext;
    ApmisRepository mRepository;

    public EditProfileViewModelFactory (Context context, ApmisRepository apmisRepository) {
        this.mContext = context;
        this.mRepository = apmisRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditProfileViewModel(mContext, mRepository);
    }
}
