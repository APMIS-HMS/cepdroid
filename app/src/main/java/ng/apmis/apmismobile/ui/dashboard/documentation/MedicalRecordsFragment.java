package ng.apmis.apmismobile.ui.dashboard.documentation;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class MedicalRecordsFragment extends Fragment implements RecordsAdapter.OnRecordClickedListener {

    @BindView(R.id.records_shimmer)
    ShimmerFrameLayout recordsShimmer;

    public RecyclerView recordsRecycler;
    public RecordsAdapter recordsAdapter;
    private SharedPreferencesManager preferencesManager;
    private RecordsListViewModel recordsListViewModel;
    private List<RecordsAdapter.DatedDocumentationItem> datedDocumentationItems = new ArrayList<>();

    public MedicalRecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medical_records, container, false);
        ButterKnife.bind(this, view);

        Log.i("Called", "Called here");

        recordsShimmer.startShimmer();

        preferencesManager = new SharedPreferencesManager(getContext());

        recordsRecycler = view.findViewById(R.id.records_recycler);

        InjectorUtils.provideNetworkData(getActivity()).fetchMedicalRecordsForPerson(preferencesManager.getPersonId());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recordsRecycler.setLayoutManager(layoutManager);

        if (recordsAdapter != null)
            recordsRecycler.setAdapter(recordsAdapter);

        initViewModel();

        return view;
    }

    private void initViewModel() {
        RecordsListViewModelFactory factory = InjectorUtils.provideRecordsViewModelFactory(getActivity().getApplicationContext());
        recordsListViewModel = ViewModelProviders.of(this, factory).get(RecordsListViewModel.class);

        final Observer<List<Documentation>> documentationsObserver = documentations -> {

            this.datedDocumentationItems.clear();
            this.datedDocumentationItems = populateDocumentationRecordsWithDate(documentations);

            if (recordsAdapter == null) {

                recordsShimmer.setVisibility(View.GONE);
                recordsShimmer.stopShimmer();

                recordsAdapter = new RecordsAdapter(getActivity(), datedDocumentationItems);
                recordsAdapter.initiateCallbackListener(MedicalRecordsFragment.this);
                recordsRecycler.setAdapter(recordsAdapter);


                Log.d("init fresh", datedDocumentationItems.size()+"");

            } else {

                Log.d("init notify", datedDocumentationItems.size()+"");
                recordsAdapter.clear();
                recordsAdapter.addAll(datedDocumentationItems);
                recordsAdapter.notifyDataSetChanged();
            }
        };

        recordsListViewModel.getRecordsForPerson().observe(getActivity(), documentationsObserver);
    }

    private List<RecordsAdapter.DatedDocumentationItem> populateDocumentationRecordsWithDate(List<Documentation> documentations){
        List<RecordsAdapter.DatedDocumentationItem> datedDocumentationItems = new ArrayList<>();

        Comparator<Documentation> comparator = (o1, o2) -> o2.compareTo(o1);
        Collections.sort(documentations, comparator);

        Calendar calendar = Calendar.getInstance();
        int currentMonth = -1;
        int currentYear = -1;

        for (int i=0; i<documentations.size(); ++i){

            Date date = AppUtils.dbStringToLocalDate(documentations.get(i).getUpdatedAt());
            calendar.setTime(date);

            if (i == 0 || (calendar.get(Calendar.MONTH) != currentMonth) || calendar.get(Calendar.YEAR) != currentYear){

                currentMonth = calendar.get(Calendar.MONTH);
                currentYear = calendar.get(Calendar.YEAR);
                datedDocumentationItems.add(
                        new RecordsAdapter.DatedDocumentationItem(AppUtils.MONTHS[currentMonth]
                                + ", " + calendar.get(Calendar.YEAR), null));
            }

            datedDocumentationItems.add(new RecordsAdapter.DatedDocumentationItem(null, documentations.get(i)));
        }

        return datedDocumentationItems;
    }

    @Override
    public void onRecordClicked(Documentation documentation) {
        MedicalRecordsDetailsFragment detailsFragment = MedicalRecordsDetailsFragment.newInstance(documentation);
        setFragment(detailsFragment);
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitle(Constants.MEDICAL_RECORDS, false);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        recordsListViewModel.getRecordsForPerson().removeObservers(getActivity());
        super.onStop();
    }

    void setFragment (Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnRecordSelectedListener) {
//            mListener = (OnRecordSelectedListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnRecordSelectedListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnRecordSelectedListener {
//        void onRecordSelected(Documentation documentation);
//    }
}
