package ng.apmis.apmismobile.ui.dashboard.profile.viewEditProfile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.network.NetworkDataCalls;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static ng.apmis.apmismobile.utilities.Constants.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements ProfileActivity.OnBackPressedListener {

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

    @BindView(R.id.image_loader)
    ProgressBar imageProgress;

    @BindView(R.id.save_changes_layout)
    RelativeLayout saveChangesLayout;

    @BindView(R.id.save_changes_progress)
    ProgressBar saveChangesProgress;

    @OnClick(R.id.change_image_fab)
    void selectImageClicked(){
        selectImageOption();
    }

    @OnClick(R.id.save_changes_button)
    void saveChangesClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogMinWidth);
        AlertDialog dialog = builder.create();
        View authorizeLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_apmis_authorize, null, false);
        dialog.setView(authorizeLayout);

        EditText passwordEdit = authorizeLayout.findViewById(R.id.password_edit_text);


        authorizeLayout.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());
        authorizeLayout.findViewById(R.id.authorize_button).setOnClickListener(v -> {
            String password = passwordEdit.getText().toString();
            confirmPasswordAndSave(password, dialog);
        });
        dialog.show();
    }

    //Flag to check if the view is in editing mode
    private boolean isEditing;
    //Flag to check if any edit has begun on this fragment
    private boolean hasEditStarted;

    private EditProfileViewModel editProfileViewModel;
    
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int GALLERY_REQUEST_CODE = 2;

    private Uri uri;

    String mCurrentPhotoPath;

    private SharedPreferencesManager prefs;

    private Intent cameraIntent, galleryIntent;

    private ProgressDialog progressDialog;

    private PersonEntry person;

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

        prefs = new SharedPreferencesManager(getContext());

        editProfileButton.setOnClickListener(v -> {
            isEditing = true;

            TransitionManager.beginDelayedTransition(imageActionLayout);
            changeImageFab.setVisibility(View.GONE);
            actionLayout.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(detailsLayout);
            nameEditLayout.setVisibility(View.VISIBLE);
            saveChangesLayout.setVisibility(View.VISIBLE);

            beginTextEdits();
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
                    person = personEntry;
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

        Observer<PersonEntry> downloadedPersonObserver = personEntry -> {
            //if it's not in edit mode, and the edit process hasn't begun at all (by calling save changes)
            //This check is done to prevent external observations from another service fetching the
            //same downloadedPersonData
            if (!(isEditing && hasEditStarted))
                return;

            if (personEntry != null) {
                AppUtils.showShortToast(getContext(), "Details saved successfully");
                saveChangesProgress.setVisibility(View.GONE);
                saveChangesButton.setClickable(true);
                onBackPressed();
            }
            else {
                AppUtils.showShortToast(getContext(), "Unable to upload");
                saveChangesProgress.setVisibility(View.GONE);
                saveChangesButton.setClickable(true);
            }
        };

        editProfileViewModel.getDownloadedPerson().observe(this, downloadedPersonObserver);

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
            imageProgress.setVisibility(View.GONE);
            try {
                Glide.with(getContext()).load(localFile).into(profileImageView);
            } catch (Exception e){

            }

        } else if (localFile != null){
            // Set default avatar and show download progress bar
            profileImageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_user_profile));
            imageProgress.setVisibility(View.VISIBLE);

            File finalLocalFile = localFile;

            //Download the image from the web
            editProfileViewModel.getPersonPhotoPath(person, finalLocalFile).observe(this, s -> {
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

    /**
     * Show a bottom sheet fragment to display image upload options
     */
    private void selectImageOption() {
        final BottomSheetDialog builder = new BottomSheetDialog(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.bottom_dialog_photo_upload, null);

        builder.setContentView(dialogView);
        Button btnCamera = dialogView.findViewById(R.id.btn_select_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                builder.dismiss();
            }
        });
        
        Button btnImage = dialogView.findViewById(R.id.btn_select_image);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
                builder.dismiss();
            }
        });

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();
    }

    /**
     * Set up and execute the take picture intent
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("TAG", "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI;
                //Use file provider for versions lollipop and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    photoURI = FileProvider.getUriForFile(getContext(),
                            getContext().getApplicationContext().getPackageName() + ".fileprovider",
                            photoFile);
                    takePictureIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    photoURI = Uri.fromFile(photoFile);
                }

                //keep reference to the photoUri
                this.uri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                //Check for permissions
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                } else {
                    //Save reference to the camera intent
                    cameraIntent = takePictureIntent;
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
                    } else {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                    }
                }
            }
        }
    }

    /**
     * Show device gallery to select an image
     */
    public void getImageFromGallery() {

        galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image From Gallery"), GALLERY_REQUEST_CODE);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Start the cropper activity
     */
    private void cropImage(){
        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity(uri).setFixAspectRatio(true)
                .start(getContext(), this);
    }

    /**
     * Make the required edittexts editable
     */
    private void beginTextEdits(){

        EditText[] changeables = {emailEdit, lastNameEdit, firstNameEdit };
        EditText[] unChangeables = {apmisIdEdit, phoneEdit, passwordEdit};

        for (EditText editText : changeables){
            editText.setLongClickable(true);
            editText.setClickable(true);
            //Make text focusable
            editText.setFocusable(true);
            //Allow focus when touched
            editText.setFocusableInTouchMode(true);
            //this grants the text focus
            editText.requestFocus();
        }

        for (EditText editText : unChangeables){
            editText.setEnabled(false);
        }
    }

    /**
     * Remove editable capabilities from edittext
     */
    private void endTextEdits(){

        EditText[] changeables = {firstNameEdit, lastNameEdit, emailEdit};
        EditText[] unChangeables = {apmisIdEdit, phoneEdit, passwordEdit};

        for (EditText editText : changeables){
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setLongClickable(false);
        }

        for (EditText editText : unChangeables){
            editText.setEnabled(true);
        }
    }

    private void saveChanges(){
        Log.v("Profile", "Saving Changes...");
        //TODO, perform some validation
        person.setFirstName(firstNameEdit.getText().toString());
        person.setLastName(lastNameEdit.getText().toString());
        person.setEmail(emailEdit.getText().toString());

        hasEditStarted = true;//flag to allow downloaded person data to be observed in this fragment
        editProfileViewModel.updatePersonEntry(person);
        saveChangesButton.setClickable(false);
        saveChangesProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Send a request to the server to authenticate the current user with a password
     * @param password The typed in password
     * @param alertDialog AlertDialog object
     */
    private void confirmPasswordAndSave(String password, AlertDialog alertDialog){
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        APMISAPP.getInstance().networkIO().execute(() -> {

            JSONObject job = new JSONObject();
            try {
                job.put("email", new SharedPreferencesManager(getContext()).getStoredApmisId());
                job.put("password", password);
                job.put("strategy", "local");
                Log.v("Person to Json", String.valueOf(job));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {
                progressDialog.dismiss();
                alertDialog.dismiss();
                saveChanges();

            }, error -> {
                Log.d("error", String.valueOf(error.getMessage()) + "Error");
                progressDialog.dismiss();
                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("Authentication Failed")
                        .setMessage("Please try again !!!")
                        .setPositiveButton("Dismiss", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });

            queue.add(loginRequest);
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            isEditing = savedInstanceState.getBoolean("isEditing", false);
            hasEditStarted = savedInstanceState.getBoolean("hasEditStarted", false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isEditing", isEditing);
        outState.putBoolean("hasEditStarted", hasEditStarted);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            cropImage();

        } else if (requestCode == GALLERY_REQUEST_CODE) {

            if (data != null) {
                uri = data.getData();
                cropImage();
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();

                File auxFile = new File(resultUri.getPath());

                //compress the image if it's above 50KB
                long length = auxFile.length()/1024;
                if (length > 50)
                    AppUtils.compressImage(getContext(), auxFile.getAbsolutePath(), 0);

                Bitmap bitmap = BitmapFactory.decodeFile(auxFile.getAbsolutePath());
                //userImage.setImageBitmap(bitmap);

                //compressed bitmap quality to 80%
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

                new Thread(() -> {
                    Log.d("Image", "Upload image started");
                    if (person != null) {
                        //TODO Use LiveData for this upload
                        new NetworkDataCalls(getContext()).uploadProfilePictureForPerson(person, bitmap, prefs.getStoredUserAccessToken());
                        getActivity().runOnUiThread(() -> imageProgress.setVisibility(View.VISIBLE));
                    } else
                        AppUtils.showShortToast(getContext(), "Failed to upload photo");
                }).start();

            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Overriding the onBackPressed method of the activity using listeners in the activity.
     * @return <code>true</code> if the activity should handle the onBackPressed,
     *          <code>false</code> if it should be overridden by this fragment.
     */
    @Override
    public boolean onBackPressed() {
        if (isEditing){
            endTextEdits();
            isEditing = false;

            TransitionManager.beginDelayedTransition(detailsLayout, new ChangeBounds());
            saveChangesLayout.setVisibility(View.GONE);
            nameEditLayout.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(imageActionLayout);
            changeImageFab.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.VISIBLE);

            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity(), "You need permission to use camera", Toast.LENGTH_SHORT).show();
        }
    }
}
