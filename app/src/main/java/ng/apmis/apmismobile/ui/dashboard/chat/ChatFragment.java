package ng.apmis.apmismobile.ui.dashboard.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

public class ChatFragment extends Fragment {

    private static final String CLASSNAME = "CHAT";

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);

        return rootView;
    }


}
