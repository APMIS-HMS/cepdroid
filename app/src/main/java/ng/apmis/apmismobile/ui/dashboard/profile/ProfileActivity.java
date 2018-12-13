package ng.apmis.apmismobile.ui.dashboard.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.profile.profileAction.ProfileActionFragment;
import ng.apmis.apmismobile.ui.dashboard.profile.viewEditProfile.EditProfileFragment;

import static ng.apmis.apmismobile.ui.dashboard.profile.profileAction.ProfileActionFragment.ACTION_MY_PROFILE;

public class ProfileActivity extends AppCompatActivity implements ProfileActionFragment.OnProfileActionInteractionListener {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    Fragment profileActionFragment;
    EditProfileFragment editProfileFragment;

    private OnBackPressedListener mListener = null;

    public interface OnBackPressedListener{
        boolean onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(generalToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionBar.setElevation(0);

        profileActionFragment = new ProfileActionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileActionFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void instantiateFragmentOnBackPressedListener(OnBackPressedListener listener){
        this.mListener = listener;
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out,
                        R.anim.fragment_pop_slide_in, R.anim.fragment_pop_slide_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }

    @Override
    public void onProfileAction(String action) {
        switch (action){
            case ACTION_MY_PROFILE:
                editProfileFragment = EditProfileFragment.newInstance();
                instantiateFragmentOnBackPressedListener(editProfileFragment);
                placeFragment(editProfileFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //Allow the EditProfileFragment to override the onBackPressed
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof OnBackPressedListener) || ((OnBackPressedListener) fragment).onBackPressed()) {
            super.onBackPressed();
        } else
            mListener.onBackPressed();//use the EditProfileFragment's onBackPressed
    }
}
