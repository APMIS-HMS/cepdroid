package ng.apmis.apmismobile.ui.dashboard.chat;

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
import ng.apmis.apmismobile.utilities.Constants;

public class ChatFragment extends Fragment {

    @BindView(R.id.list_items)
    ListView listItems;
    private static final String CLASSNAME = "CHAT";

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, rootView);

        optionItems.add(new ModuleListModel("FORUMS", R.drawable.ic_medical_records));
        optionItems.add(new ModuleListModel("MEDICAL BOT", R.drawable.drugs));

        ModuleAdapter moduleAdapter = new ModuleAdapter(getActivity(), optionItems);

        listItems.setAdapter(moduleAdapter);

        listItems.setDivider(null);

        listItems.setOnItemClickListener((parent, view, position, id) -> {
            ModuleListModel selectedOption = (ModuleListModel) parent.getItemAtPosition(position);

            if (selectedOption.getmOption().equals("MEDICAL BOT")) {
                Toast.makeText(getActivity(), selectedOption.getmOption(), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChatContextFragment())
                        .addToBackStack("bot")
                        .commit();
            } else {
                Toast.makeText(getActivity(), selectedOption.getmOption(), Toast.LENGTH_SHORT).show();
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new ChatContextFragment())
                        .addToBackStack(null)
                        .commit();*/
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        ((DashboardActivity) getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.CHAT, true);
        ((DashboardActivity)getActivity()).mBottomNav.getMenu().findItem(R.id.chat_menu).setChecked(true);
        super.onResume();
    }
}
