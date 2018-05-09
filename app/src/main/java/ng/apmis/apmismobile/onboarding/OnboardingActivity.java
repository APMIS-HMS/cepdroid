package ng.apmis.apmismobile.onboarding;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.ui.signup.SignupActivity;

public class OnboardingActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    private static final int NUM_PAGES = 3;
    private PagerAdapter mPagerAdapter;

    @BindView(R.id.container)
    ViewPager mPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        new SharedPreferencesManager(this).setFirstTimeLaunch(false);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            //TODO replicate this in reverse to achieve button clicks
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void onNextPressed() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    //TODO add transition
    /*public void switchDots(String fragment, int direction) {
        switch (fragment) {
            case "doc":
                if (direction == 1) {
                    bottomDot1.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    bottomDot2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    bottomDot3.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                } else {
                    bottomDot1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    bottomDot2.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    bottomDot3.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                }
                break;
            case "med":
                if (direction == 1) {
                    bottomDot1.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    bottomDot2.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    bottomDot3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    bottomDot1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    bottomDot2.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                    bottomDot3.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                }
                break;
            case "app":
                bottomDot1.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                bottomDot2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                bottomDot3.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                break;
        }
    } */

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DoctorOnboardingFragment();
                case 1:
                    return new MedicineOnboardingFragment();
                case 2:
                    return new AppointmentOnboardingFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
