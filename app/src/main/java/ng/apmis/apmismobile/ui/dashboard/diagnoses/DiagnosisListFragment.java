package ng.apmis.apmismobile.ui.dashboard.diagnoses;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.diagnosesModel.InvestigationBody;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass displaying the list of investigations
 * made on behalf of the person(patient).
 */
public class DiagnosisListFragment extends Fragment {

    @BindView(R.id.diagnosis_recycler)
    RecyclerView diagnosisRecycler;

    @BindView(R.id.diagnosis_shimmer)
    ShimmerFrameLayout diagnosisShimmer;


    private SharedPreferencesManager preferencesManager;
    private DiagnosisAdapter diagnosisAdapter;
    private List<InvestigationBody> investigationBodies;
    private DiagnosisViewModel diagnosisViewModel;

    public DiagnosisListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnosis_list, container, false);

        ButterKnife.bind(this, view);

        investigationBodies = new ArrayList<>();
        preferencesManager = new SharedPreferencesManager(getContext());

        //Fetch lab requests from the server
        InjectorUtils.provideNetworkData(getActivity()).fetchLabRequestsForPerson(preferencesManager.getPersonId());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diagnosisRecycler.setLayoutManager(layoutManager);

        if (diagnosisAdapter != null)
            diagnosisRecycler.setAdapter(diagnosisAdapter);

        initViewModel();

        return view;
    }

    private void initViewModel() {

        DiagnosisViewModelFactory factory = InjectorUtils.provideDiagonosisViewModelFactory(getActivity().getApplicationContext());
        diagnosisViewModel = ViewModelProviders.of(this, factory).get(DiagnosisViewModel.class);

        final Observer<List<LabRequest>> labRequestObserver = labRequests -> {

            //Populate the prescription item list
            populateInvestigations(labRequests);

            if (diagnosisAdapter == null) {

                diagnosisShimmer.setVisibility(View.GONE);
                diagnosisShimmer.stopShimmer();

                diagnosisAdapter = new DiagnosisAdapter(getActivity(), investigationBodies);
                diagnosisRecycler.setAdapter(diagnosisAdapter);


                Log.d("init fresh", investigationBodies.size()+"");

            } else {

                Log.d("init notify", investigationBodies.size()+"");
                diagnosisAdapter.clear();
                Log.d("init notify after", investigationBodies.size()+"");
                diagnosisAdapter.addAll(investigationBodies);
                Log.d("init notify again", investigationBodies.size()+"");
                diagnosisAdapter.notifyDataSetChanged();
            }
        };

        //Observe the Prescriptions
        diagnosisViewModel.getLabRequestsForPerson().observe(getActivity(), labRequestObserver);

    }

    private void populateInvestigations(List<LabRequest> labRequests){
        investigationBodies.clear();

        //Sort by date instead
        Comparator<LabRequest> comparator = (o1, o2) -> o2.compareTo(o1);
        Collections.sort(labRequests, comparator);

        for (LabRequest labRequest : labRequests) {
            //Add each investigation item from the labRequest
            for (InvestigationBody investigationBody : labRequest.getInvestigationBodies()) {

                //set the date
                investigationBody.setLabRequestDate(labRequest.getUpdatedAt());

                //set the employee
                investigationBody.setLabRequestEmployee(labRequest.getEmployeeDetails());

                investigationBodies.add(investigationBody);
            }
        }
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).profileImage.setVisibility(View.GONE);
            ((DashboardActivity)getActivity()).setToolBarTitle(Constants.DIAGNOSES, false);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        diagnosisViewModel.getLabRequestsForPerson().removeObservers(getActivity());
        super.onStop();
    }

}
