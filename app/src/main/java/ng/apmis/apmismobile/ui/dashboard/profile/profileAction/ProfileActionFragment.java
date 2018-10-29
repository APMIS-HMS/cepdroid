package ng.apmis.apmismobile.ui.dashboard.profile.profileAction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 * Displays the Actions associated with a Per
 */
public class ProfileActionFragment extends Fragment {

    private SharedPreferencesManager prefs;

    //Constant for the My Profile Click
    public static final String ACTION_MY_PROFILE = "action";

    @BindView(R.id.profile_image)
    CircleImageView profileImageView;

    @BindView(R.id.username_text)
    TextView usernameText;

    @BindView(R.id.apmis_id_text)
    TextView apmisIdText;

    @BindView(R.id.profile_button)
    Button myProfileButton;

    @BindView(R.id.reminders_button)
    Button remindersButton;

    @BindView(R.id.alerts_button)
    Button alertsButton;

    @BindView(R.id.facilities_button)
    Button facilitiesButton;

    @BindView(R.id.settings_button)
    Button settingsButton;

    @BindView(R.id.contact_button)
    Button contactButton;

    @BindView(R.id.logout_button)
    Button logoutButton;

    @BindView(R.id.image_loader)
    ProgressBar imageProgress;

    ProfileActionViewModel mProfileActionViewModel;

    private OnProfileActionInteractionListener mListener;

    public interface OnProfileActionInteractionListener {
        void onProfileAction(String action);
    }

    public ProfileActionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_action, container, false);
        ButterKnife.bind(this, view);

        prefs = new SharedPreferencesManager(getContext());

        initViewModel();

        //Tell Activity to change to the edit profile fragment
        myProfileButton.setOnClickListener(v -> mListener.onProfileAction(ACTION_MY_PROFILE));

        apmisIdText.setText(prefs.getStoredApmisId());

        return view;
    }

    private void initViewModel(){
        ProfileActionViewModelFactory profileActionViewModelFactory = InjectorUtils.provideProfileActionViewModelFactory(getContext());
        mProfileActionViewModel = ViewModelProviders.of(this, profileActionViewModelFactory).get(ProfileActionViewModel.class);

        final Observer<PersonEntry> personEntryObserver = new Observer<PersonEntry>() {
            @Override
            public void onChanged(@Nullable PersonEntry personEntry) {

                if (personEntry != null) {
                    usernameText.setText(String.format("%s %s", personEntry.getFirstName(), personEntry.getLastName()));

                    ProfileActionFragment.this.attemptLoadImage(personEntry);
                }
            }
        };

        //Observe the Person
        mProfileActionViewModel.getPersonEntry().observe(this, personEntryObserver);

    }

    /**
     * Attempt to load the profile image into the profile imageView
     * @param person The Person with the profile
     */
    private void attemptLoadImage(PersonEntry person){
        //create the profile photo directory in the app file directory
        File profilePhotoDir = new File(getContext().getFilesDir(), "profilePhotos");
        profilePhotoDir.mkdir();

        File localFile = null;

        if (!TextUtils.isEmpty(person.getProfileImageFileName()))
            localFile = new File(profilePhotoDir, person.getProfileImageFileName());

        if (localFile != null && localFile.exists()){
            try {
                //load locally
                Glide.with(getContext()).load(localFile).into(profileImageView);
            } catch (Exception e){

            }

        } else if (localFile != null) {
            // Set default avatar and show download progress bar
            profileImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_user_profile));
            imageProgress.setVisibility(View.VISIBLE);

            File finalLocalFile = localFile;

            //Download the image from the web
            mProfileActionViewModel.getPersonPhotoPath(person, finalLocalFile).observe(this, s -> {
                imageProgress.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(s)){
                    if (!s.equals("error"))
                        Glide.with(getContext()).load(finalLocalFile).into(profileImageView);
                    else
                        AppUtils.showShortToast(getContext(), "Error updating profile photo");
                }
            });

        }  else {
            profileImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_user_profile));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileActionInteractionListener) {
            mListener = (OnProfileActionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileActionInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
