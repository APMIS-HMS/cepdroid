package ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.fundAccount.Price;
import ng.apmis.apmismobile.ui.dashboard.find.foundItems.FoundItemsActivity;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoundHospitalDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FoundHospitalDetailFragment extends Fragment {

    private static String KEY_ID = "facilityIdKey";
    private static String KEY_NAME = "facilityNameKey";

    private OnFragmentInteractionListener mListener;
    private String id, name;

    private FoundHospitalDetailViewModel foundHospitalViewModel;

    @BindView(R.id.logo_image)
    ImageView logoImage;

    @BindView(R.id.logo_loader)
    ProgressBar logoLoader;

    @BindView(R.id.email_text_view)
    TextView emailTextView;

    @BindView(R.id.price_loader)
    ProgressBar priceLoader;

    @BindView(R.id.register_button)
    Button registerButton;

    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    @BindView(R.id.price_service_view)
    LinearLayout priceServiceLayout;

    @BindView(R.id.service_spinner)
    Spinner serviceSpinner;

    @BindView(R.id.price_spinner)
    Spinner priceSpinner;

    @BindView(R.id.pay_button)
    Button payButton;

    private BillManager registrationBillManager;

    private ArrayAdapter registrationServiceArrayAdapter;
    private ArrayAdapter priceArrayAdapter;

    String registrationCategoryId;
    private Service selectedService;
    private Price selectedPrice;

    public interface OnFragmentInteractionListener{
        void onPayClicked(String facilityId);
    }

    public FoundHospitalDetailFragment() {
        // Required empty public constructor
    }

    public static FoundHospitalDetailFragment newInstance(String id, String name){
        FoundHospitalDetailFragment fragment = new FoundHospitalDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ID, id);
        args.putSerializable(KEY_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            if (getArguments().getSerializable(KEY_ID) != null) {
                id = getArguments().getString(KEY_ID);
                name = getArguments().getString(KEY_NAME);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital_detail, container, false);
        ButterKnife.bind(this, view);

        initViewModel();

        registerButton.setOnClickListener(v -> {
            registrationBillManager = null;
            TransitionManager.beginDelayedTransition(mainLayout);
            priceServiceLayout.setVisibility(View.VISIBLE);
            initBillManagerObserver();
        });

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (registrationBillManager != null && registrationBillManager.getServices().size() > 0) {
                    selectedService = registrationBillManager.getServices().get(position);

                    priceArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectedService.getPrices());
                    priceArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    priceSpinner.setAdapter(priceArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedService != null) {
                    selectedPrice = selectedService.getPrices().get(position);

                    TransitionManager.beginDelayedTransition(priceServiceLayout);
                    payButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void initViewModel(){
        FoundHospitalDetailViewModelFactory viewModelFactory = InjectorUtils.provideFoundHospitalDetailViewModelFactory(getContext());
        foundHospitalViewModel = ViewModelProviders.of(this, viewModelFactory).get(FoundHospitalDetailViewModel.class);

        final Observer<Facility> facilityObserver = facility -> {

            if (facility != null) {
                emailTextView.setText(facility.getEmail());

                if (facility.getLogoObject() != null) {

                    Glide.with(getContext())
                            .load(facility.getLogoObject().getPath())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    try {
                                        logoLoader.setVisibility(View.GONE);
                                    } catch (Exception ignored) {}
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    logoLoader.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(logoImage);
                } else {
                    logoLoader.setVisibility(View.GONE);
                    logoImage.setImageResource(R.drawable.ic_default_hospital);
                }
            }
        };

        final Observer<String> serviceIdObserver = s -> {
            if (s != null) {
                registrationCategoryId = s;
                priceLoader.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
            }
        };

        foundHospitalViewModel.clearFacilityData();
        foundHospitalViewModel.getFacility(id).observe(this, facilityObserver);

        foundHospitalViewModel.clearServiceCategoryId();
        foundHospitalViewModel.getServiceCategoryIdForFacility(id).observe(this, serviceIdObserver);

    }

    private void initBillManagerObserver(){
        final Observer<BillManager> billManagerObserver = billManager -> {
            if (billManager != null){
                registrationBillManager = billManager;

                if (billManager.getServices() != null && billManager.getServices().size() > 0)
                    setupSpinnerAdaptersWithBillManager(billManager);
                else
                    AppUtils.showShortToast(getContext(), "No registration service available");

            }
        };

        foundHospitalViewModel.clearBills();
        foundHospitalViewModel.getBillManagerForRegistration(id, registrationCategoryId).observe(this, billManagerObserver);

    }


    private void setupSpinnerAdaptersWithBillManager(BillManager billManager) {
        List<Service> services = billManager.getServices();

        if (registrationServiceArrayAdapter == null) {
            registrationServiceArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, services);
            registrationServiceArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            serviceSpinner.setAdapter(registrationServiceArrayAdapter);


        } else {
            registrationServiceArrayAdapter.notifyDataSetChanged();
        }

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            ((FoundItemsActivity)getActivity()).setToolBarTitle(name);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
