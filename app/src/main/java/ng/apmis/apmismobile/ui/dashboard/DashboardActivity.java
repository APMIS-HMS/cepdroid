package ng.apmis.apmismobile.ui.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyActivity;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatActivity;
import ng.apmis.apmismobile.ui.dashboard.find.FindActivity;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadActivity;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listView;
    PersonViewModel mPersonViewModel;

    String [] modules = new String[] {
            "VIEW",
            "BUY",
            "FIND",
            "READ",
            "CHAT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

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

        ArrayAdapter<String> dbAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, modules);

        listView.setAdapter(dbAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
                    Toast.makeText(DashboardActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    openView(parent.getItemAtPosition(position).toString());
                }
        );
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
                Toast.makeText(this, "Profile was clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
        return false;
    }

    public void openView(String selected) {
        switch (selected.toLowerCase()) {
            case "view":
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case "buy":
                startActivity(new Intent(this, BuyActivity.class));
                break;
            case "find":
                startActivity(new Intent(this, FindActivity.class));
                break;
            case "read":
                startActivity(new Intent(this, ReadActivity.class));
                break;
            case "chat":
                startActivity(new Intent(this, ChatActivity.class));
                break;

        }
    }
}
