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
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.login.LoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity{

    android.support.v7.app.ActionBar actionBar;
    SharedPreferencesManager loginPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        setContentView(R.layout.splash);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginPrefs = new SharedPreferencesManager(WelcomeScreenActivity.this);
                if (loginPrefs.isLoggedIn()) {
                    launchHomeScreen(loginPrefs.isLoggedIn());
                } else {
                    launchHomeScreen(false);
                }
            }
        }, 300);

    }

    public void launchHomeScreen (boolean isLoggedIn) {
        Toast.makeText(WelcomeScreenActivity.this, String.valueOf(isLoggedIn), Toast.LENGTH_SHORT).show();
        if (isLoggedIn) {
            loginPrefs.setIsLoggedIn(isLoggedIn);
            startActivity(new Intent(WelcomeScreenActivity.this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class));
            finish();
        }

    }


}
