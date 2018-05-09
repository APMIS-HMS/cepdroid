package ng.apmis.apmismobile.ui.dashboard.find;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ng.apmis.apmismobile.R;

public class Hospital_list extends AppCompatActivity {
    String [] hospitalList = {
        "GENERAL HOSPITAL AGEGE",
        "LAGOS STATE UNIVERSITY TEACHING HOSPITAL (LASUTH)",
        "LAGOS UNIVERSITY TEACHING HOSPITAL (LUTH)",
        "OBAFEMI AWOLOWO UNIVERSITY TEACHING HOSPITAL (OAUTH)",
        "UNIVERSITY OF IBADAN TEACHING HOSPITAL",
        "GENERAL HOSPITAL BADAGRY (unregistered)",
        "GENERAL HOSPITAL IGANDO (unregistered)",
        "GENERAL HOSPITAL AGO IWOYE (unregistered)",
        "TRINITY PRIVATE HOSPITAL IGBOELERIN (unregistered)",
        "GENERAL HOSPITAL APAPA (unregistered)"
    };
    String [] options = {"REGISTER HERE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ng.apmis.apmismobile.R.layout.activity_hospital_list);
        android.widget.LinearLayout parent = new android.widget.LinearLayout(this);
        android.widget.ArrayAdapter <String> hospitals = new android.widget.ArrayAdapter(this,android.R.layout.simple_list_item_1, hospitalList);
        android.widget.ListView listv = new android.widget.ListView(this);
        listv.setAdapter(hospitals);
        this.setContentView(parent, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        parent.addView(listv);

         listv.setOnItemClickListener((parentv, view, position, id) -> {
            String select = parentv.getItemAtPosition(position).toString();
            selectedItem(select);
        });

    }

    private void selectedItem(String sel){
         switch (sel.toLowerCase()) {
            case "general hospital agege":
                startActivity(new android.content.Intent(this, VisitActionsActivity.class));
                break;
            case "LAGOS STATE UNIVERSITY TEACHING HOSPITAL (LASUTH)":
                startActivity(new android.content.Intent(this, VisitActionsActivity.class));
                break;
            case "LAGOS UNIVERSITY TEACHING HOSPITAL (LUTH)":
                startActivity(new android.content.Intent(this, VisitActionsActivity.class));
                break;
            case "general hospital badagry (unregistered)":
                AlertDialog.Builder popup = new AlertDialog.Builder(Hospital_list.this);
                android.view.LayoutInflater inflate = getLayoutInflater();
                android.view.View view = inflate.inflate(R.layout.unregistered_hosp, null);
                popup.setView(view);
                ListView listView = view.findViewById(R.id.unreg);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, options);
                listView.setAdapter(arrayAdapter);
                popup.show();
                //startActivity(new android.content.Intent(this, ng.apmis.apmismobile.ui.dashboard.read.ReadActivity.class));
                break;
                case "GENERAL HOSPITAL IGANDO (unregistered)":
                    startActivity(new android.content.Intent(this, ng.apmis.apmismobile.ui.dashboard.read.ReadActivity.class));
                    break;




    }
}
}