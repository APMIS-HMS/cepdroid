package ng.apmis.apmismobile.ui.dashboard.read;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.buy.BuyFragment;
import ng.apmis.apmismobile.ui.dashboard.find.FindFragment;
import ng.apmis.apmismobile.ui.dashboard.view.ViewFragment;

public class ReadFragment extends Fragment {

    private static final String CLASSNAME = "READ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_read, container, false);

        ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);


        return rootView;
    }

}
