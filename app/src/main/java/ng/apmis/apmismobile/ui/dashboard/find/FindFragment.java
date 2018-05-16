package ng.apmis.apmismobile.ui.dashboard.find;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

public class FindFragment extends Fragment {

    private static final String CLASSNAME = "FIND";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);

        ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);

        return rootView;
    }

}
