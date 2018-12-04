package ng.apmis.apmismobile.ui.dashboard;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.ui.dashboard.appointment.AppointmentFragment;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyFragment;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatFragment;
import ng.apmis.apmismobile.ui.dashboard.find.FindFragment;
import ng.apmis.apmismobile.ui.dashboard.places.FacilityLocationFragment;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadFragment;
import ng.apmis.apmismobile.ui.dashboard.view.ViewFragment;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.BottomNavigationViewHelper;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class DashboardActivity extends AppCompatActivity implements DashboardFragment.OnQuickLinkListener{

    PersonViewModel mPersonViewModel;

    @BindView(R.id.navigation)
    public BottomNavigationView mBottomNav;

    @BindView(R.id.img_profile)
    public CircleImageView profileImage;

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    ActionBar actionBar;

    public String findSearchTerm = "Hospital";

    DashboardFragment.OnQuickLinkListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        toolbarTitle.setText(R.string.login);

        setSupportActionBar(generalToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        if (actionBar != null) {
            actionBar.setTitle("");
        }

        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });

        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

        PersonFactory personFactory = InjectorUtils.providePersonFactory(DashboardActivity.this);
        mPersonViewModel = ViewModelProviders.of(this, personFactory).get(PersonViewModel.class);
        InjectorUtils.provideNetworkData(this).startPersonDataFetchService();

        profileImage.setVisibility(View.VISIBLE);

        mPersonViewModel.getPersonEntry().observe(this, personEntry -> {
            if (personEntry != null) {
                Log.v("personEntry", String.valueOf(personEntry));
                attemptLoadImage(personEntry);
                //TODO stop loading here
            } else {
                //TODO continue loading and pull data from server again
                InjectorUtils.provideRepository(this).getUserData();
            }
        });

        DashboardFragment dashboardFragment = new DashboardFragment();

        dashboardFragment.initializeQuickLinkListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, dashboardFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();



        profileImage.setOnClickListener((view) -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.NOTIFICATION_ACTION)){
            jumpToNotifiedFragment();
        }

    }

    private void attemptLoadImage(PersonEntry person){
        File profilePhotoDir = new File(this.getFilesDir(), "profilePhotos");
        profilePhotoDir.mkdir();

        File localFile = null;

        if (!TextUtils.isEmpty(person.getProfileImageFileName()))
            localFile = new File(profilePhotoDir, person.getProfileImageFileName());

        if (localFile != null && localFile.exists()){
            try {
                Glide.with(this).load(localFile).into(profileImage);
            } catch (Exception e){

            }

        } else if (localFile != null){
            // Download image from web
            profileImage.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_user_profile));

            File finalLocalFile = localFile;

            mPersonViewModel.getPersonPhotoPath(person, finalLocalFile).observe(this, s -> {
                if (!TextUtils.isEmpty(s)){
                    if (!s.equals("error"))
                        Glide.with(this).load(finalLocalFile).into(profileImage);
                }
            });

        } else {
            profileImage.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_user_profile));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO Remove default module selection, and set none on back pressed

    public void setToolBarTitleAndBottomNavVisibility(String title, boolean isTopLevel) {
        toolbarTitle.setText(title);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(!isTopLevel);
        bottomNavVisibility(isTopLevel);
    }

    public void bottomNavVisibility (boolean show) {
        if (show) {
            mBottomNav.setVisibility(View.VISIBLE);
            return;
        }
        mBottomNav.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavVisibility(true);
    }

    private void jumpToNotifiedFragment(){
        Log.i("Appointment", getIntent().getExtras().getString(Constants.NOTIFICATION_ACTION));


        if (getIntent().getExtras().getString(Constants.NOTIFICATION_ACTION).equals(Constants.APPOINTMENTS)) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewFragment(), "HOME")
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AppointmentFragment(), Constants.APPOINTMENTS)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }


    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_menu:
                placeFragment(new DashboardFragment());
                break;
            case R.id.view_menu:
                placeFragment(new ViewFragment());
                break;
            case R.id.buy_menu:
                placeFragment(new BuyFragment());
                break;
            case R.id.chat_menu:
                placeFragment(new ChatFragment());
                break;
            case R.id.find_menu:
                placeFragment(new FindFragment());
                break;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("SearchTerm", findSearchTerm);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
                super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                        this, findViewById(R.id.search_card), "search_card").toBundle());
            } else {
                super.startActivity(intent);
            }

        } else {
            super.startActivity(intent);
        }


    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (!(fragment instanceof DashboardFragment))
            getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onQuickLinkClicked(String link) {
        switch (link) {
            case Constants.APPOINTMENTS:
                //placeFragment(new FacilityLocationFragment());
                break;
            case Constants.VITALS:
                //placeFragment(new FacilityLocationFragment());
                break;
            case Constants.LOCATION:
                placeFragment(new FacilityLocationFragment());
                break;
            case Constants.HELP:
                //placeFragment(new FacilityLocationFragment());
                break;
            default:
                break;
        }
    }
}
