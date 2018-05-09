package ng.apmis.apmismobile.ui.dashboard.buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.find.FindActivity;

public class BuyActivity extends AppCompatActivity {

    String [] buyList = new String [] {
            "BUY",
            "FUND WALLET",
            "PAY BILLS"
    };
    String [] fundList = new String[]{
            "FUND PERSON",
            "THIRD PARTY",
            "CO-FUND OTHERS"
    };
    String [] buyables = new String[]{
            "DRUG",
            "HEALTH INSURANCE",
            "CONSUMABLES"
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
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(BuyActivity.this);
                android.view.LayoutInflater inflate = getLayoutInflater();
                android.view.View convert = (android.view.View)inflate.inflate(R.layout.buylist, null);
                dialog.setView(convert);
                ListView list1 = convert.findViewById(R.id.buylv);
                ArrayAdapter<String> adapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,buyables);
                list1.setAdapter(adapt);
                dialog.show();
              //  startActivity(new Intent(this, ViewActivity.class));
                break;
            case "fund wallet":
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(BuyActivity.this);
                android.view.LayoutInflater inflater = getLayoutInflater();
                android.view.View converted = inflater.inflate(R.layout.wallet_dialog, null);
                alert.setView(converted);
                ListView list = converted.findViewById(R.id.lv);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fundList);
                list.setAdapter(adapter);
                alert.show();

                //startActivity(new Intent(this, BuyActivity.class));
                break;
            case "pay bills":
                startActivity(new Intent(this, FindActivity.class));
                break;
        }
    }

    //private void play(android.text.Layout x, java.util.ArrayList z){}

}
