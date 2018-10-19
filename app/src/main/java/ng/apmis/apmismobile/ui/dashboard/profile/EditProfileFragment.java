package ng.apmis.apmismobile.ui.dashboard.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements ProfileActivity.OnBackPressedListener{

    @BindView(R.id.profile_image)
    CircleImageView profileImageView;

    @BindView(R.id.name_text_view)
    TextView nameTextView;

    @BindView(R.id.apmis_id_text_view)
    TextView apmisIdTextView;

    @BindView(R.id.first_name_edit)
    EditText firstNameEdit;

    @BindView(R.id.last_name_edit)
    EditText lastNameEdit;

    @BindView(R.id.email_edit)
    EditText emailEdit;

    @BindView(R.id.phone_edit)
    EditText phoneEdit;

    @BindView(R.id.apmis_id_edit)
    EditText apmisIdEdit;

    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.change_password_text)
    TextView changePasswordTextView;

    @BindView(R.id.image_layout)
    RelativeLayout imageLayout;

    @BindView(R.id.name_edit_layout)
    LinearLayout nameEditLayout;

    @BindView(R.id.image_action_layout)
    LinearLayout imageActionLayout;

    @BindView(R.id.action_layout)
    LinearLayout actionLayout;

    @BindView(R.id.edit_profile_button)
    Button editProfileButton;

    @BindView(R.id.save_changes_button)
    Button saveChangesButton;

    @BindView(R.id.change_image_fab)
    FloatingActionButton changeImageFab;

    @BindView(R.id.details_layout)
    LinearLayout detailsLayout;

    boolean isEditing;

    private EditProfileViewModel editProfileViewModel;


    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int GALLERY_REQUEST_CODE = 2;

    private Intent cameraIntent, galleryIntent;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditing = true;

                TransitionManager.beginDelayedTransition(imageActionLayout);
                changeImageFab.setVisibility(View.GONE);
                actionLayout.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(detailsLayout);
                nameEditLayout.setVisibility(View.VISIBLE);
                saveChangesButton.setVisibility(View.VISIBLE);

            }
        });

        initViewModel();

        return view;
    }

    private void initViewModel(){
        EditProfileViewModelFactory factory = InjectorUtils.provideEditProfileViewModelFactory(getContext());
        editProfileViewModel = ViewModelProviders.of(this, factory).get(EditProfileViewModel.class);

        Observer<PersonEntry> personEntryObserver = new Observer<PersonEntry>() {
            @Override
            public void onChanged(@Nullable PersonEntry personEntry) {
                if (personEntry != null){
                    attemptLoadImage(personEntry);
                    nameTextView.setText(String.format("%s %s", personEntry.getFirstName(), personEntry.getLastName()));
                    apmisIdTextView.setText(personEntry.getApmisId());
                    firstNameEdit.setText(personEntry.getFirstName());
                    lastNameEdit.setText(personEntry.getLastName());
                    emailEdit.setText(personEntry.getEmail());
                    phoneEdit.setText(personEntry.getPrimaryContactPhoneNo());
                    apmisIdEdit.setText(personEntry.getApmisId());
                    passwordEdit.setText("PASSWORD");
                }
            }
        };

        editProfileViewModel.getPersonEntry().observe(this, personEntryObserver);

    }

    private void attemptLoadImage(PersonEntry person){
        File profilePhotoDir = new File(getContext().getFilesDir(), "profilePhotos");
        profilePhotoDir.mkdir();

        File localFile = null;

        Log.v("Image in edit", person.getProfileImageFileName().toString());

        if (!TextUtils.isEmpty(person.getProfileImageFileName()))
            localFile = new File(profilePhotoDir, person.getProfileImageFileName());

        if (localFile != null && localFile.exists()){
            try {
                Glide.with(getContext()).load(localFile).into(profileImageView);
            } catch (Exception e){

            }

        } else if (localFile != null){
            // Download image from web
            //TODO Show a loading progress bar

            File finalLocalFile = localFile;
            InjectorUtils.provideNetworkData(getContext()).fetchAndDownloadPersonProfilePhoto(person, finalLocalFile);

            editProfileViewModel.getPersonPhotoPath().observe(this, s -> {
                if (!TextUtils.isEmpty(s)){
                    if (!s.equals("error"))
                        Glide.with(getContext()).load(finalLocalFile).into(profileImageView);
                    else
                        AppUtils.showShortToast(getContext(), "Error updating profile photo");
                }
            });

        }
    }

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery"};

        final BottomSheetDialog builder = new BottomSheetDialog(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.bottom_dialog_image, null);

        builder.setContentView(dialogView);
        ImageButton btnImage = dialogView.findViewById(R.id.btn_select_image);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetImageFromGallery();
                builder.dismiss();
            }
        });

        ImageButton btnCamera = dialogView.findViewById(R.id.btn_select_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClickImageFromCamera();
                builder.dismiss();
            }
        });

        ImageButton btnCancel = dialogView.findViewById(R.id.btn_select_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            if (isEditing){
//                isEditing = false;
//
//                TransitionManager.beginDelayedTransition(imageActionLayout);
//                changeImageFab.setVisibility(View.VISIBLE);
//                actionLayout.setVisibility(View.VISIBLE);
//
//                TransitionManager.beginDelayedTransition(detailsLayout);
//                nameEditLayout.setVisibility(View.GONE);
//                saveChangesButton.setVisibility(View.GONE);
//            } else
//                getActivity().onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onBackPressed() {
        if (isEditing){
            isEditing = false;

            TransitionManager.beginDelayedTransition(imageActionLayout);
            changeImageFab.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.VISIBLE);

            TransitionManager.beginDelayedTransition(detailsLayout);
            nameEditLayout.setVisibility(View.GONE);
            saveChangesButton.setVisibility(View.GONE);

        } else {
            getActivity().onBackPressed();
        }
    }
}
