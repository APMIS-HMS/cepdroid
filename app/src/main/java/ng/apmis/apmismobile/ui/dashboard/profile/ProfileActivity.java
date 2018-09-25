package ng.apmis.apmismobile.ui.dashboard.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.username_text)
    TextView usernameText;

    @BindView(R.id.apmis_id_text)
    TextView apmisIdText;

    @BindView(R.id.reminders_button)
    Button remindersButton;

    @BindView(R.id.alerts_button)
    Button alertsButton;

    @BindView(R.id.facilities_button)
    Button facilitiesButton;

    @BindView(R.id.settings_button)
    Button settingsButton;

    @BindView(R.id.contact_button)
    Button contactButton;

    @BindView(R.id.logout_button)
    Button logoutButton;

    private SharedPreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        prefs = new SharedPreferencesManager(this);

        LiveData<PersonEntry> entry = InjectorUtils.provideRepository(this).getUserData();

        entry.observe(this, personEntry ->
                usernameText.setText(String.format("%s %s", personEntry.getFirstName(), personEntry.getLastName())));
        apmisIdText.setText(prefs.getStoredApmisId());

        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        for (View view : viewGroup.getTouchables()){
            if (view instanceof Button){
                view.setOnClickListener(v -> {
                    AppUtils.showShortToast(this, ((Button) view).getText()+"");
                });
            }
        }

    }
}
