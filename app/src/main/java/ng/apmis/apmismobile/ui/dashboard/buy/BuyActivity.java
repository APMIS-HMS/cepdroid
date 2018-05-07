package ng.apmis.apmismobile.ui.dashboard.buy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatActivity;
import ng.apmis.apmismobile.ui.dashboard.find.FindActivity;
import ng.apmis.apmismobile.ui.dashboard.read.ReadActivity;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class BuyActivity extends AppCompatActivity {

    String [] buyList = new String [] {
            "BUY",
            "FUND WALLET",
            "PAY BILLS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        LinearLayout parentLayout = new LinearLayout(this);

        ArrayAdapter<String> viewMenus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, buyList);

        ListView listView = new ListView(this);

        listView.setAdapter(viewMenus);

        this.setContentView(parentLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        parentLayout.addView(listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            openView(selected);
        });


    }
    public void openView(String selected) {
        switch (selected.toLowerCase()) {
            case "buy":
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case "fund wallet":
                startActivity(new Intent(this, BuyActivity.class));
                break;
            case "pay bills":
                startActivity(new Intent(this, FindActivity.class));
                break;
        }
    }

}
