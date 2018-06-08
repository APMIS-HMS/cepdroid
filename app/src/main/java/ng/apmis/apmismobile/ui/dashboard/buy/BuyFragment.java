package ng.apmis.apmismobile.ui.dashboard.buy;

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

public class BuyFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.list_items) ListView listItems;
    private static final String CLASSNAME = "BUY";

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);

        ButterKnife.bind(this, rootView);

        optionItems.add(new ModuleListModel("BUY", R.drawable.ic_buy));
        optionItems.add(new ModuleListModel("FUND WALLET", R.drawable.ic_fund_account));
        optionItems.add(new ModuleListModel("PAY BILLS", R.drawable.ic_pay_bills));

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
            ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);
        }
        super.onResume();
    }

}
