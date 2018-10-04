package ng.apmis.apmismobile.ui.dashboard.prescription;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;
import ng.apmis.apmismobile.data.database.prescriptionModel.PrescriptionItem;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass showing the list of {@link Prescription}s
 */
public class PrescriptionListFragment extends Fragment implements PrescriptionAdapter.OnAddToCartListener{

    private TextView badgeText;

    @BindView(R.id.prescriptions_shimmer)
    ShimmerFrameLayout prescriptionsShimmer;

    @BindView(R.id.prescriptionsRecycler)
    RecyclerView prescriptionsRecycler;

    private PrescriptionAdapter prescriptionAdapter;
    private SharedPreferencesManager preferencesManager;
    private PrescriptionViewModel prescriptionsViewModel;

    private List<PrescriptionItem> prescriptionItems = new ArrayList<>();

    private int cartCount = 0;

    public PrescriptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prescription_list, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        prescriptionsShimmer.startShimmer();

        preferencesManager = new SharedPreferencesManager(getContext());

        //Fetch prescriptions from the server
        InjectorUtils.provideNetworkData(getActivity()).fetchPrescriptionsForPerson(preferencesManager.getPersonId());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        prescriptionsRecycler.setLayoutManager(layoutManager);

        if (prescriptionAdapter != null)
            prescriptionsRecycler.setAdapter(prescriptionAdapter);

        initViewModel();

        return view;
    }



    private void initViewModel() {

        PrescriptionViewModelFactory factory = InjectorUtils.providePrescriptionsViewModelFactory(getActivity().getApplicationContext());
        prescriptionsViewModel = ViewModelProviders.of(this, factory).get(PrescriptionViewModel.class);

        final Observer<List<Prescription>> prescriptionsObserver = prescriptions -> {

            //Populate the prescription item list
            populatePrescriptionItems(prescriptions);

            if (prescriptionAdapter == null) {

                prescriptionsShimmer.setVisibility(View.GONE);
                prescriptionsShimmer.stopShimmer();

                prescriptionAdapter = new PrescriptionAdapter(getActivity(), prescriptionItems);
                prescriptionAdapter.initAddToCartListener(this);
                prescriptionsRecycler.setAdapter(prescriptionAdapter);


                Log.d("init fresh", prescriptionItems.size()+"");

            } else {

                Log.d("init notify", prescriptionItems.size()+"");
                prescriptionAdapter.clear();
                Log.d("init notify after", prescriptionItems.size()+"");
                prescriptionAdapter.addAll(prescriptionItems);
                Log.d("init notify again", prescriptionItems.size()+"");
                prescriptionAdapter.notifyDataSetChanged();
            }
        };

        //Observe the Prescriptions
        prescriptionsViewModel.getPrescriptionsForPerson().observe(getActivity(), prescriptionsObserver);

    }

    /**
     * Populate the PrescriptionItems list with with prescription items gotten from the
     * {@link Prescription} bodies. After every Prescription Body, a spacer (empty PrescriptionItem)
     * is inserted also.
     * @param prescriptions The {@link Prescription} bodies containing {@link PrescriptionItem}s
     */
    private void populatePrescriptionItems(List<Prescription> prescriptions){
        prescriptionItems.clear();

        //Sort by date instead
        Comparator<Prescription> comparator = (o1, o2) -> o2.compareTo(o1);
        Collections.sort(prescriptions, comparator);

        for (Prescription prescription : prescriptions) {

            int positionInPrescription = 0;
            //Add each prescription item from the prescription body
            for (PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {

                //Set date
                prescriptionItem.setPrescriptionDate(prescription.getUpdatedAt());
                //Indicate the size of the original prescription body list to each item to track first and last items
                prescriptionItem.setPrescriptionListSize(prescriptions.size());
                //Also indicate the position
                prescriptionItem.setPositionInPrescriptionList(positionInPrescription);
                //And the prescriber of the drug
                prescriptionItem.setPrescriberName(prescription.getEmployeeDetails().getFirstName() +
                            " " + prescription.getEmployeeDetails().getLastName());

                prescriptionItems.add(prescriptionItem);
                ++positionInPrescription;
            }

            //add a spacer after each Prescription Body
            prescriptionItems.add(new PrescriptionItem());
        }
    }

    @Override
    public void onStop() {
        prescriptionsViewModel.getPrescriptionsForPerson().removeObservers(getActivity());
        super.onStop();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_cart, menu);

        MenuItem item = menu.findItem(R.id.action_cart);
        item.setActionView(R.layout.layout_cart_menu);
        RelativeLayout badgeLayout = (RelativeLayout) item.getActionView();

        badgeText = badgeLayout.findViewById(R.id.actionbar_notification_text_view);
        badgeLayout.setOnClickListener(v -> {
            cartCount = 0;
            badgeText.setText(cartCount + "");
        });

    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).profileImage.setVisibility(View.GONE);
            ((DashboardActivity)getActivity()).setToolBarTitle(Constants.PRESCRIPTION, false);
        }
        super.onResume();
    }

    @Override
    public void onAddedToCart(PrescriptionItem prescriptionItem) {
        ++cartCount;
        badgeText.setText(cartCount+"");
    }
}
