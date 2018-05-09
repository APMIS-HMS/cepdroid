package ng.apmis.apmismobile.ui.dashboard.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class ProfileActivity extends AppCompatActivity {

    String [] profileItems = new String[] {
            "Reminders",
            "Alerts",
            "My Facilities",
            "Settings"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        LinearLayout parentLayout = new LinearLayout(this);

        ArrayAdapter<String> viewMenus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileItems);


        ListView listView = new ListView(this);

        listView.setAdapter(viewMenus);

        this.setContentView(parentLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        parentLayout.addView(listView);




        listView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(ProfileActivity.this, parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show()

 );







    }



}
