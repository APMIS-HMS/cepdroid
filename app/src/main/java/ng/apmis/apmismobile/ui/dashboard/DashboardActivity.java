package ng.apmis.apmismobile.ui.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.appointment.AppointmentFragment;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyFragment;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatFragment;
import ng.apmis.apmismobile.ui.dashboard.find.FindFragment;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadFragment;
import ng.apmis.apmismobile.ui.dashboard.view.ViewFragment;
import ng.apmis.apmismobile.utilities.BottomNavigationViewHelper;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class DashboardActivity extends AppCompatActivity {

    PersonViewModel mPersonViewModel;

    @BindView(R.id.navigation)
    public BottomNavigationView mBottomNav;
    @BindView(R.id.img_profile)
    public CircleImageView profileImage;
    PopupMenu popupMenu;

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    ActionBar actionBar;

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

        //TODO Setup recycler view list and pass to recycler in observer

        mPersonViewModel.getPersonEntry().observe(this, personEntry -> {
            if (personEntry != null) {
                Log.v("personEntry", String.valueOf(personEntry));
                //TODO stop loading here
            } else {
                //TODO continue loading and pull data from server again
                InjectorUtils.provideRepository(this).getUserData();
            }
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        profileImage.setVisibility(View.VISIBLE);

        profileImage.setOnClickListener((view) -> {
            startActivity(new Intent(this, ProfileActivity.class));
//            popupMenu = new PopupMenu(this, view);
//            MenuInflater inflater = popupMenu.getMenuInflater();
//            inflater.inflate(R.menu.option_setttings, popupMenu.getMenu());
//
//            popupMenu.setOnMenuItemClickListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.profile:
//                        startActivity(new Intent(this, ProfileActivity.class));
//                        return true;
//                    case R.id.sign_out:
//                        new SharedPreferencesManager(this).storeLoggedInUserDetails("", "", "", "");
//                        startActivity(new Intent(this, LoginActivity.class));
//                        finish();
//                        return true;
//                }
//                return false;
//            });
//
//            popupMenu.show();
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.NOTIFICATION_ACTION)){
            jumpToNotifiedFragment();
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

    public void setToolBarTitle(String title, boolean welcomeScreen) {
        toolbarTitle.setText(title);
        if (welcomeScreen) {
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
            case R.id.view_menu:
                placeFragment(new ViewFragment());
                break;
            case R.id.buy_menu:
                placeFragment(new BuyFragment());
                break;
            case R.id.chat_menu:
                placeFragment(new ChatFragment());
                break;
            case R.id.read_menu:
                placeFragment(new ReadFragment());
                break;
            case R.id.find_menu:
                placeFragment(new FindFragment());
                break;
        }
    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }


}
