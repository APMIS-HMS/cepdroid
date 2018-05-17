package ng.apmis.apmismobile.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import ng.apmis.apmismobile.data.database.SharedPreferencesManager;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.onboarding.OnboardingActivity;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.login.LoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    SharedPreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SharedPreferencesManager(WelcomeScreenActivity.this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
            setContentView(R.layout.splash);

            new Handler().postDelayed(() -> {

                if (prefs.isFirstTimeLaunch()) {

                    startActivity(new Intent(WelcomeScreenActivity.this, OnboardingActivity.class));
                    finish();

                } else {
                    if (prefs.isLoggedIn()) {
                        launchHomeScreen(prefs.isLoggedIn());
                    } else {
                        launchHomeScreen(false);
                    }

                }
            }, 2000);
        }


    }

    public void launchHomeScreen(boolean isLoggedIn) {
        if (isLoggedIn) {
            prefs.setIsLoggedIn(isLoggedIn);
            startActivity(new Intent(WelcomeScreenActivity.this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class));
            finish();
        }

    }


}
