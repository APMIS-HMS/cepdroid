package ng.apmis.apmismobile.ui.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyFragment;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatFragment;
import ng.apmis.apmismobile.ui.dashboard.find.FindFragment;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadFragment;
import ng.apmis.apmismobile.ui.dashboard.view.ViewFragment;
import ng.apmis.apmismobile.ui.login.LoginActivity;
import ng.apmis.apmismobile.utilities.BottomNavigationViewHelper;

public class DashboardActivity extends AppCompatActivity {

    PersonViewModel mPersonViewModel;

    @BindView(R.id.navigation)
    BottomNavigationView mBottomNav;
    @BindView(R.id.toolbar_title_tv)
    TextView toolBarTitleTv;
    @BindView(R.id.back_btn)
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

      /*  PersonFactory personFactory = InjectorUtils.providePersonFactory(DashboardActivity.this
        );
        mPersonViewModel = ViewModelProviders.of(this, personFactory).get(PersonViewModel.class);
*/
     /*   InjectorUtils.provideNetworkData(this).startPersonDataFetchService();

        //TODO Setup recycler view list and pass to recycler in observer

        mPersonViewModel.getmPersonEntry().observe(this, personEntry -> {
            if (personEntry != null) {
                Log.v("personEntry", String.valueOf(personEntry));
                //TODO stop loading here
            } else {
                //TODO continue loading and pull data from server again
                InjectorUtils.provideRepository(this).getUserData();
            }
        });*/

     getSupportFragmentManager().beginTransaction()
             .add(R.id.fragment_container, new DashboardFragment())
             .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
             .commit();


    }

    public void setToolBarTitle (String title, boolean welcomeScreen) {
        toolBarTitleTv.setText(title);
        if (welcomeScreen) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setVisibility(View.VISIBLE);
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

    private void placeFragment (Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_setttings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.sign_out:
                new SharedPreferencesManager(this).storeLoggedInUserKeys("","","","");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }
        return false;
    }

}
