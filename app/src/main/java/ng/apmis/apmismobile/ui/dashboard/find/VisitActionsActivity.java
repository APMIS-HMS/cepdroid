package ng.apmis.apmismobile.ui.dashboard.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.chat.ChatActivity;

public class VisitActionsActivity extends AppCompatActivity {
    String[] options = {
            "CONSULTATIONS",
            "REQUEST A PROCEDURE",
            "SCHEDULE A LAB PROCEDURE"

    };

    String [] consult = {
            "TALK",
            "CHAT",
            "VIDEO"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_actions);

        LinearLayout layout = new LinearLayout(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        ListView listView = new ListView(this);
        listView.setAdapter(arrayAdapter);
        this.setContentView(layout, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));

        layout.addView(listView);
        listView.setOnItemClickListener((parentv, view, position, id) -> {
            String select = parentv.getItemAtPosition(position).toString();
            selectedItem(select);
        });


    }

    private void selectedItem(String sel) {
        switch (sel.toLowerCase()) {
            case "consultations":
                AlertDialog.Builder pop = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.options,null);
                pop.setView(v);
                ListView list = v.findViewById(R.id.opt);
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,consult);
                list.setAdapter(stringArrayAdapter);
                pop.show();
                list.setOnItemClickListener((parentv, view, position, id) -> {
                    if(parentv.getItemAtPosition(position).toString().equals("CHAT")){
                        startActivity(new Intent(this, ChatActivity.class));
                    }
                });

                //startActivity(new android.content.Intent(this, Hospital_list.class));
                break;
            case "request a procedure":
                Toast.makeText(this,"REQUESTED", Toast.LENGTH_LONG).show();
                //startActivity(new android.content.Intent(this, ng.apmis.apmismobile.ui.dashboard.buy.BuyActivity.class));
                break;
            case "schedule a lab procedure":
                Toast.makeText(this,"A FEW MOMENTS THANKS", Toast.LENGTH_LONG).show();
                //startActivity(new android.content.Intent(this, FindActivity.class));
                break;



        }
    }
}
