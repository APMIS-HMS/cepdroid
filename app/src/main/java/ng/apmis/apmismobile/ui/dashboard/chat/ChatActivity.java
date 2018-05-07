package ng.apmis.apmismobile.ui.dashboard.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyActivity;
import ng.apmis.apmismobile.ui.dashboard.find.FindActivity;
import ng.apmis.apmismobile.ui.dashboard.view.ViewActivity;

public class ChatActivity extends AppCompatActivity {

    static String [] chatOptions = {
            "FORUMS",
            "BOT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        LinearLayout parentLayout = new LinearLayout(this);

        ArrayAdapter<String> chatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatOptions);

        ListView lv = new ListView(this);

        this.setContentView(parentLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        parentLayout.addView(lv);

        lv.setAdapter(chatAdapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            openView(selected);
        });

    }

    public void openView(String selected) {
        switch (selected.toLowerCase()) {
            case "forums":
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case "bot":
                startActivity(new Intent(this, BuyActivity.class));
                break;

        }
    }

}
