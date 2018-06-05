package ng.apmis.apmismobile.ui.dashboard.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.ModuleAdapter;
import ng.apmis.apmismobile.ui.dashboard.ModuleListModel;

public class ViewFragment extends Fragment {

    @BindView(R.id.list_items) ListView listItems;
    private static final String CLASSNAME = "VIEW";

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_view, container, false);
        ButterKnife.bind(this, rootView);

        ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);

        optionItems.add(new ModuleListModel("MEDICAL RECORD", R.drawable.ic_medical_records));
        optionItems.add(new ModuleListModel("MEDICATION", R.drawable.ic_medications));
        optionItems.add(new ModuleListModel("PRESCRIPTION", R.drawable.ic_prescription));
        optionItems.add(new ModuleListModel("HEALTH PROFILE", R.drawable.ic_health_profile));
        optionItems.add(new ModuleListModel("APPOINTMENTS", R.drawable.ic_appointents));
        optionItems.add(new ModuleListModel("HEALTH INSURANCE", R.drawable.ic_health_insurance));
        optionItems.add(new ModuleListModel("DIAGNOSTIC REPORT", R.drawable.ic_diagnostic_report));
        optionItems.add(new ModuleListModel("REFERRAL", R.drawable.ic_referral));
        optionItems.add(new ModuleListModel("MENSTRUAL CYCLE", R.drawable.ic_menstrual));
        optionItems.add(new ModuleListModel("CARE TEAM", R.drawable.ic_care_team));
        optionItems.add(new ModuleListModel("CARE PROVIDERS", R.drawable.ic_care_providers));

        ModuleAdapter moduleAdapter = new ModuleAdapter(getActivity(), optionItems);

        listItems.setAdapter(moduleAdapter);

        listItems.setDivider(null);

        listItems.setOnItemClickListener((parent, view, position, id) -> {
            ModuleListModel selectedOption = (ModuleListModel) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), selectedOption.getmOption() , Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }


}
