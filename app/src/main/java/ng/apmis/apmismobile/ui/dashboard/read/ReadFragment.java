package ng.apmis.apmismobile.ui.dashboard.read;

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

public class ReadFragment extends Fragment {

    private static final String CLASSNAME = "READ";
    @BindView(R.id.list_items) ListView listItems;

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_read, container, false);
        ButterKnife.bind(this, rootView);

        optionItems.add(new ModuleListModel("HEALTH EDUCATION", R.drawable.ic_medical_records));
        optionItems.add(new ModuleListModel("FAMILY PLANNING", R.drawable.drugs));
        optionItems.add(new ModuleListModel("NEWS", R.drawable.ic_prescription));

        ModuleAdapter moduleAdapter = new ModuleAdapter(getActivity(), optionItems);

        listItems.setAdapter(moduleAdapter);

        listItems.setDivider(null);

        listItems.setOnItemClickListener((parent, view, position, id) -> {
            ModuleListModel selectedOption = (ModuleListModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), selectedOption.getmOption() , Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(CLASSNAME, false);
        }
        super.onResume();
    }

}
