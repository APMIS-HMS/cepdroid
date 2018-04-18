package ng.apmis.apmismobile.ui.dashboard;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class DashboardActivity extends AppCompatActivity {

    PersonViewModel mPersonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        PersonFactory personFactory = InjectorUtils.providePersonFactory(DashboardActivity.this
        );
        mPersonViewModel = ViewModelProviders.of(this, personFactory).get(PersonViewModel.class);

        InjectorUtils.provideNetworkData(this).startPersonDataFetchService();

        //TODO Setup recycler view list and pass to recycler in observer

        mPersonViewModel.getmPersonEntry().observe(this, personEntry -> {
            if (personEntry != null) {
                Log.v("personEntry", String.valueOf(personEntry));
                //TODO stop loading here
            } else {
                //TODO continue loading and pull data from server again
                InjectorUtils.provideRepository(this).getUserData();
            }
        });


    }
}
