package ng.apmis.apmismobile.ui.dashboard.find;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadActivity;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class FindActivity extends AppCompatActivity {

    static String [] findOptions = {
            "HOSPITAL",
            "GENERAL PRACTITIONER",
            "SPECIALISTS",
            "NURSE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        LinearLayout parentLayout = new LinearLayout(this);

        setContentView(parentLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ArrayAdapter<String> findAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, findOptions);

        ListView lv = new ListView(this);

        lv.setAdapter(findAdapter);

        parentLayout.addView(lv);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            openView(selected);
        });

    }

    public void openView(String selected) {
        switch (selected.toLowerCase()) {
            case "hospital":
                startActivity(new Intent(this, Hospital_list.class));
                break;
            case "general practitioner":
                startActivity(new Intent(this, BuyActivity.class));
                break;
            case "specialists":
                startActivity(new Intent(this, FindActivity.class));
                break;
            case "nurse":
                startActivity(new Intent(this, ReadActivity.class));
                break;

        }
    }

}
