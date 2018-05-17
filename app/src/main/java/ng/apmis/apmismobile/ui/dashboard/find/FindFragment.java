package ng.apmis.apmismobile.ui.dashboard.find;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class FindFragment extends Fragment {

    @BindView(R.id.list_items)
    ListView listItems;
    private static final String CLASSNAME = "FIND";

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, rootView);

        ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);

        optionItems.add(new ModuleListModel("DOCTOR", R.drawable.ic_medical_records));
        optionItems.add(new ModuleListModel("NURSE", R.drawable.drugs));
        optionItems.add(new ModuleListModel("HOSPITAL", R.drawable.ic_prescription));
        optionItems.add(new ModuleListModel("SERVICES", R.drawable.drugs));
        optionItems.add(new ModuleListModel("PHARMACY", R.drawable.drugs));


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
