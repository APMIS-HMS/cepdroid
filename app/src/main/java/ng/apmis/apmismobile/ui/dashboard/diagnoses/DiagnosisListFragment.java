package ng.apmis.apmismobile.ui.dashboard.diagnoses;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
import ng.apmis.apmismobile.ui.dashboard.documentation.MedicalRecordsDetailsFragment;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass displaying the list of investigations
 * made on behalf of the person(patient).
 */
public class DiagnosisListFragment extends Fragment implements DiagnosisAdapter.OnViewReportClickedListener{

    @BindView(R.id.main_background)
    FrameLayout frameLayout;

    @BindView(R.id.diagnosis_recycler)
    RecyclerView diagnosisRecycler;

    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diagnosisRecycler.setLayoutManager(layoutManager);

        //handles back stack pop call here
        if (diagnosisAdapter != null) {
            diagnosisRecycler.setAdapter(diagnosisAdapter);
            diagnosisShimmer.setVisibility(View.GONE);
            diagnosisShimmer.stopShimmer();

            if (diagnosisAdapter.getItemCount() > 0)
                frameLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.pale_cyan));
        }

        initViewModel();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 10f);
    }

    private void initViewModel() {

        DiagnosisViewModelFactory factory = InjectorUtils.provideDiagonosisViewModelFactory(getActivity().getApplicationContext());
        diagnosisViewModel = ViewModelProviders.of(this, factory).get(DiagnosisViewModel.class);

        final Observer<List<LabRequest>> labRequestObserver = labRequests -> {

            if (labRequests == null){
                //if null, show snack bar
                if ((diagnosisAdapter != null && diagnosisAdapter.getItemCount() == 0 ) ||
                        diagnosisAdapter == null) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                diagnosisShimmer.stopShimmer();
                diagnosisShimmer.setVisibility(View.GONE);
                Snackbar.make(emptyView, "Failed to load Investigations", Snackbar.LENGTH_LONG).show();
                return;
            }

            //Populate the investigation body list
            populateInvestigations(labRequests);

            if (diagnosisAdapter == null) {

                diagnosisShimmer.setVisibility(View.GONE);
                diagnosisShimmer.stopShimmer();

                if (investigationBodies.size() > 0)
                    frameLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.pale_cyan));

                diagnosisAdapter = new DiagnosisAdapter(getActivity(), investigationBodies);
                diagnosisAdapter.instantiateViewReportClickListener(this);
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

            diagnosisShimmer.stopShimmer();
            diagnosisShimmer.setVisibility(View.GONE);

            if (diagnosisAdapter.getItemCount() > 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        };

        //Observe the LabRequests(Investigations)
        diagnosisViewModel.getLabRequestsForPerson(preferencesManager.getPersonId()).observe(this, labRequestObserver);

    }

    /**
     * Populate the investigations array by getting individual investigations from each lab request
     * @param labRequests The List of Lab requests
     */
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
                //set the clinic information
                investigationBody.setLabRequestClinicalInformation(labRequest.getClinicalInformation());
                //set the diagnosis
                investigationBody.setLabRequestDiagnosis(labRequest.getDiagnosis());
                //set the lab number
                investigationBody.setLabRequestNumber(labRequest.getLabNumber());

                investigationBodies.add(investigationBody);
            }
        }
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).profileImage.setVisibility(View.GONE);
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.INVESTIGATIONS, false);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        diagnosisViewModel.getLabRequestsForPerson(preferencesManager.getPersonId()).removeObservers(this);
        super.onStop();
    }

    @Override
    public void onViewReportClicked(Intent i) {
        //Open the medical records from here
        MedicalRecordsDetailsFragment detailsFragment = MedicalRecordsDetailsFragment.newInstance(i);
        setFragment(detailsFragment);
    }

    /**
     * Utility method to replace the current fragment with another
     * @param fragment The Fragment to replace with
     */
    private void setFragment (Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out,
                        R.anim.fragment_pop_slide_in, R.anim.fragment_pop_slide_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
