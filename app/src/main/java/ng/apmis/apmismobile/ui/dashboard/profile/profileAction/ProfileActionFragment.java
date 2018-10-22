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
 */
public class ProfileActionFragment extends Fragment {

    private SharedPreferencesManager prefs;

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

//        profileImageView.setOnClickListener(v -> {
//
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.me);
//
//            new Thread(() -> {
//                Log.d("Image", "Upload image started");
//                new NetworkDataCalls(getContext()).uploadProfilePictureForPerson(prefs.getStoredApmisId(), prefs.getPersonId(), bitmap,  prefs.getStoredUserAccessToken());
//
//            }).start();
//        });

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

                Log.e("Viewmodel", "was called");

                if (personEntry != null) {
                    usernameText.setText(String.format("%s %s", personEntry.getFirstName(), personEntry.getLastName()));

                    ProfileActionFragment.this.attemptLoadImage(personEntry);
                }
            }
        };

        //Observe the Person
        mProfileActionViewModel.getPersonEntry().observe(this, personEntryObserver);

    }

    private void attemptLoadImage(PersonEntry person){
        File profilePhotoDir = new File(getContext().getFilesDir(), "profilePhotos");
        profilePhotoDir.mkdir();

        File localFile = null;

        Log.v("Image", person.getProfileImageFileName().toString());

        if (!TextUtils.isEmpty(person.getProfileImageFileName()))
            localFile = new File(profilePhotoDir, person.getProfileImageFileName());

        if (localFile != null && localFile.exists()){
            try {
                Glide.with(getContext()).load(localFile).into(profileImageView);
            } catch (Exception e){

            }

        } else if (localFile != null) {
            // Download image from web
            //TODO Show a loading progress bar

            File finalLocalFile = localFile;

            mProfileActionViewModel.getPersonPhotoPath(person, finalLocalFile).observe(this, s -> {
                if (!TextUtils.isEmpty(s)){
                    if (!s.equals("error"))
                        Glide.with(getContext()).load(finalLocalFile).into(profileImageView);
                    else
                        AppUtils.showShortToast(getContext(), "Error updating profile photo");
                }
            });

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
