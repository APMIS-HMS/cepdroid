package ng.apmis.apmismobile.onboarding;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    @BindView(R.id.previous)
    ImageButton previous;
    @BindView(R.id.next)
    ImageButton next;

    @BindView(R.id.image_btn1)
    ImageButton bottomDot1;
    @BindView(R.id.image_btn2)
    ImageButton bottomDot2;
    @BindView(R.id.image_btn3)
    ImageButton bottomDot3;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        //new SharedPreferencesManager(this).setFirstTimeLaunch(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.container, new DoctorOnboardingFragment())
                .addToBackStack(null)
                .commit();

        previous.setVisibility(View.INVISIBLE);
        bottomDot1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        previous.setOnClickListener((view) -> {
            if (fragmentManager.findFragmentById(R.id.container) instanceof MedicineOnboardingFragment) {
                previous.setVisibility(View.INVISIBLE);
                fragmentManager.popBackStack();
                switchDots("med", 0);
            }
            if (fragmentManager.findFragmentById(R.id.container) instanceof AppointmentOnboardingFragment) {
                fragmentManager.popBackStack();
                switchDots("app", 0);
            }
        });

        next.setOnClickListener((view) -> {

            if (fragmentManager.findFragmentById(R.id.container) instanceof DoctorOnboardingFragment) {
                fragmentManager.beginTransaction()
                        .add(R.id.container, new MedicineOnboardingFragment())
                        .addToBackStack(null)
                        .commit();
                previous.setVisibility(View.VISIBLE);
                switchDots("doc", 1);
            }
            if (fragmentManager.findFragmentById(R.id.container) instanceof MedicineOnboardingFragment) {
                fragmentManager.beginTransaction()
                        .add(R.id.container, new AppointmentOnboardingFragment())
                        .addToBackStack(null)
                        .commit();
                switchDots("med", 1);
            }
            if (fragmentManager.findFragmentById(R.id.container) instanceof AppointmentOnboardingFragment) {
                startActivity(new Intent(this, SignupActivity.class));
                finish();
            }


        });


    }

    //TODO add transition
    public void switchDots(String fragment, int direction) {
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
    }

}
