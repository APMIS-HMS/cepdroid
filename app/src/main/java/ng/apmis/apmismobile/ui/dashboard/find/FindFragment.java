package ng.apmis.apmismobile.ui.dashboard.find;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.find.adapters.SearchTermsRowAdapter;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class FindFragment extends Fragment {

    @BindView(R.id.search_term_recycler)
    RecyclerView searchTermRecycler;

    @BindView(R.id.find_spinner)
    Spinner findSpinner;

    @BindView(R.id.find_bar)
    SearchView findSearchView;

    private SharedPreferencesManager prefs;

    private PreviousItemsViewModel previousItemsViewModel;

    private SearchTermsRowAdapter searchTermsRowAdapter;

    private static final String CLASSNAME = "FIND";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SharedPreferencesManager(getContext());

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, rootView);

        searchTermRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));

        List<String> searchTerms = Arrays.asList(getActivity().getResources().getStringArray(R.array.filter));

        searchTermsRowAdapter = new SearchTermsRowAdapter(getContext(), searchTerms);

        searchTermRecycler.setAdapter(searchTermsRowAdapter);

        initViewModel();

//        optionItems.add(new ModuleListModel("DOCTOR", R.drawable.ic_medical_records));
//        optionItems.add(new ModuleListModel("NURSE", R.drawable.drugs));
//        optionItems.add(new ModuleListModel("HOSPITAL", R.drawable.ic_prescription));
//        optionItems.add(new ModuleListModel("SERVICES", R.drawable.drugs));
//        optionItems.add(new ModuleListModel("PHARMACY", R.drawable.drugs));
        return rootView;
    }

    private void initViewModel() {
        PreviousItemsViewModelFactory previousItemsViewModelFactory = InjectorUtils.providePreviousItemsViewModelFactory(getContext());
        previousItemsViewModel = ViewModelProviders.of(this, previousItemsViewModelFactory).get(PreviousItemsViewModel.class);

        final Observer<List<Appointment>> appointmentObserver = appointments -> {

            if (appointments != null && appointments.size() > 0) {
                searchTermsRowAdapter.notifySubLists(appointments);
            }
        };

        //Observe the Appointments
        previousItemsViewModel.getPersonAppointments(prefs.getPersonId()).observe(this, appointmentObserver);
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);
        }
        super.onResume();
    }



}
