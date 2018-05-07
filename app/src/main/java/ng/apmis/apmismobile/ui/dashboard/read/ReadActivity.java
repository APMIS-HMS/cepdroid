package ng.apmis.apmismobile.ui.dashboard.read;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyActivity;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatActivity;
import ng.apmis.apmismobile.ui.dashboard.find.FindActivity;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class ReadActivity extends AppCompatActivity {

    static String [] readOptions = {
            "HEALTH EDUCATION",
            "FAMILY PLANNING",
            "PREGNANCY"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        LinearLayout parentLayout = new LinearLayout(this);

        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readOptions);

        ListView lv = new ListView(this);
        lv.setAdapter(optionAdapter);

        this.setContentView(parentLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        parentLayout.addView(lv);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            openView(selected);
        });

    }

    public void openView(String selected) {
        switch (selected.toLowerCase()) {
            case "health education":
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case "family planning":
                startActivity(new Intent(this, BuyActivity.class));
                break;
            case "pregnancy":
                startActivity(new Intent(this, FindActivity.class));
                break;

        }
    }

}
