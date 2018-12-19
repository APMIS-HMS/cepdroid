package ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.HMO;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.fundAccount.Price;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.ui.dashboard.find.foundItems.FoundItemsActivity;
import ng.apmis.apmismobile.ui.dashboard.payment.FundWalletActivity;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoundHospitalDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FoundHospitalDetailFragment extends Fragment {

    private static String KEY_ID = "facilityIdKey";
    private static String KEY_NAME = "facilityNameKey";
    private static String KEY_EMAIL = "facilityEmailKey";

    private static final String COVER_TYPE_WALLET = "wallet";
    private static final String COVER_TYPE_INSURANCE = "insurance";
    private static final String COVER_TYPE_FAMILY = "family";

    public static final int FUND_WALLET_REQUEST = 1;

    private OnFragmentInteractionListener mListener;
    private String facilityId, name, email;

    private FoundHospitalDetailViewModel foundHospitalViewModel;

    @BindView(R.id.logo_image)
    ImageView logoImage;

    @BindView(R.id.logo_loader)
    ProgressBar logoLoader;

    @BindView(R.id.hospital_name)
    TextView hospitalNameTextView;

    @BindView(R.id.email_text)
    TextView emailTextView;

    @BindView(R.id.phone_text)
    TextView phoneText;

    @BindView(R.id.website_text)
    TextView websiteText;

    @BindView(R.id.address_text)
    TextView addressText;

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

    @BindView(R.id.map_card)
    CardView mapCard;

    @BindView(R.id.pay_button)
    Button payButton;

    @BindView(R.id.family_cover_row)
    TableRow familyCoverRow;

    @BindView(R.id.insurance_row)
    TableRow insuranceRow;

    @BindView(R.id.reg_status_loading)
    TableRow regStatusTableRow;

    @BindView(R.id.reg_status_text)
    TextView regStatusTextView;

    @BindView(R.id.image_register)
    ImageView imageRegisterView;

    @BindView(R.id.pay_type_radio_group)
    RadioGroup payTypeRadioGroup;

    @BindView(R.id.hmo_spinner)
    Spinner hmoSpinner;


    SupportMapFragment mapFragment;

    private BillManager registrationBillManager;

    private ArrayAdapter registrationServiceArrayAdapter;
    private ArrayAdapter priceArrayAdapter;
    private ArrayAdapter hmoArrayAdapter;

    String facilityServiceId;
    String registrationCategoryId;

    private Service selectedService;
    private Price selectedPrice;
    private HMO selectedHMO;

    List<String> registeredIds = null;
    List<HMO> hmos = null;

    private String coverType;

    private SharedPreferencesManager pref;

    private Observer<Wallet> walletObserver;
    private Observer<Patient> patientObserver;
    private long walletFunds;

    private ProgressDialog progressDialog;

    @BindView(R.id.register_btn_group)
    LinearLayout regBtnGroup;


    public interface OnFragmentInteractionListener{
        void onPayClicked(String facilityId);
    }

    public FoundHospitalDetailFragment() {
        // Required empty public constructor
    }

    public static FoundHospitalDetailFragment newInstance(String id, String name, String email){
        FoundHospitalDetailFragment fragment = new FoundHospitalDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ID, id);
        args.putSerializable(KEY_NAME, name);
        args.putSerializable(KEY_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new SharedPreferencesManager(getContext());

        if (getArguments() != null) {

            if (getArguments().getSerializable(KEY_ID) != null) {
                facilityId = getArguments().getString(KEY_ID);
                name = getArguments().getString(KEY_NAME);
                email = getArguments().getString(KEY_EMAIL);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital_detail, container, false);
        ButterKnife.bind(this, view);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        hospitalNameTextView.setText(name);
        emailTextView.setText(email);

        coverType = COVER_TYPE_WALLET;

        initViewModel();

        progressDialog = new ProgressDialog(getContext());

        registerButton.setOnClickListener(v -> {
            registrationBillManager = null;
            TransitionManager.beginDelayedTransition(mainLayout);
            priceServiceLayout.setVisibility(View.VISIBLE);
            v.setVisibility(View.INVISIBLE);
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

                    //TransitionManager.beginDelayedTransition(priceServiceLayout);
                    payButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hmoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (hmos != null && hmos.size() > 0)
                    selectedHMO = hmos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payButton.setOnClickListener(v -> showPaymentDetailsDialog());

        payTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.pocket_radio:
                    familyCoverRow.setVisibility(View.GONE);
                    insuranceRow.setVisibility(View.GONE);
                    coverType = COVER_TYPE_WALLET;
                    break;
                case R.id.insurance_radio:
                    familyCoverRow.setVisibility(View.GONE);
                    insuranceRow.setVisibility(View.VISIBLE);
                    coverType = COVER_TYPE_INSURANCE;
                    break;
                case R.id.family_radio:
                    familyCoverRow.setVisibility(View.VISIBLE);
                    insuranceRow.setVisibility(View.GONE);
                    coverType = COVER_TYPE_FAMILY;
                    break;
                default:
                    familyCoverRow.setVisibility(View.GONE);
                    insuranceRow.setVisibility(View.GONE);
                    coverType = COVER_TYPE_WALLET;
                    break;
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 10f);
    }

    private void initViewModel(){
        FoundHospitalDetailViewModelFactory viewModelFactory = InjectorUtils.provideFoundHospitalDetailViewModelFactory(getContext());
        foundHospitalViewModel = ViewModelProviders.of(this, viewModelFactory).get(FoundHospitalDetailViewModel.class);

        final Observer<List<String>> registeredFacilityIdsObserver = facilityIds -> {

            if (facilityIds != null) {
                registeredIds = new ArrayList<>(facilityIds);
            }
        };

        //Get this value first, it should have been preloaded prior to this page
        foundHospitalViewModel.getRegisteredFacilityIds().observe(this, registeredFacilityIdsObserver);


        final Observer<Facility> facilityObserver = facility -> {

            if (facility != null) {

                mapFragment.getMapAsync(googleMap -> {

                    try {
                        LatLng hospitalLocation = new LatLng(facility.getAddress().getGeometry().getLocation().getLat(), facility.getAddress().getGeometry().getLocation().getLng());

                        googleMap.addMarker(new MarkerOptions().position(hospitalLocation).title(facility.getName()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLocation, 16.0f));
                    } catch (Exception e){
                        mapCard.setVisibility(View.GONE);
                    }

                });

                if (facility.getEmail() != null)
                    emailTextView.setText(facility.getEmail());

                if (facility.getPrimaryContactPhoneNo() != null)
                    phoneText.setText(facility.getPrimaryContactPhoneNo());

                if (facility.getWebsite() != null)
                    websiteText.setText(facility.getWebsite());

                try {
                    addressText.setText(facility.getAddress().getDescription());
                } catch (Exception ignored){}

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

        final Observer<List<String>> serviceIdObserver = ids -> {

            priceLoader.setVisibility(View.INVISIBLE);

            if (ids != null) {
                facilityServiceId = ids.get(0);
                registrationCategoryId = ids.get(1);


                if (registeredIds != null) {
                    if (registeredIds.contains(facilityId)) {
                        imageRegisterView.setVisibility(View.VISIBLE);
                        regStatusTextView.setText("Registered in this facility");
                    }
                    else {
                        //Continue the registration view process
                        regStatusTableRow.setVisibility(View.GONE);
                        regBtnGroup.setVisibility(View.VISIBLE);
                    }
                } else {
                    regStatusTextView.setText("Could not fetch your registration details.");
                }

            } else {
                regStatusTextView.setText("Unable to fetch service details");
            }
        };

        final Observer<List<HMO>> facilityHMOObserver = hmos -> {
            if (hmos != null) {
                this.hmos = hmos;
                hmoArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, hmos);
                hmoArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                hmoSpinner.setAdapter(hmoArrayAdapter);
            }
        };

        patientObserver = patient -> {
            if (patient != null){
                //Show patient completion dialog
                hideProgressDialog();
                showRegistrationCompletedDialog();
            } else {
                //Show error dialog
                displayErrorDialog();
            }
        };

        foundHospitalViewModel.clearFacilityData();
        foundHospitalViewModel.getFacility(facilityId).observe(this, facilityObserver);

        foundHospitalViewModel.clearServiceCategoryId();
        foundHospitalViewModel.getFacilityServiceCategoryIdsForFacility(facilityId).observe(this, serviceIdObserver);

        foundHospitalViewModel.clearFacilityHMOS();
        foundHospitalViewModel.getFacilityHmos(facilityId).observe(this, facilityHMOObserver);

    }

    private void showRegistrationCompletedDialog(){
        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setCancelable(false)
                .setMessage("You have registered as a patient in this Hospital.\nWould you like to set an appointment now?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                })
                .setNegativeButton("Maybe later", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                })
                .show();
    }

    private void displayErrorDialog(){
        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setTitle("Failed")
                .setMessage("Could not create appointment.\nPlease try again")
                .setPositiveButton("Close", (dialog, which) -> {
                    hideProgressDialog();
                    dialog.dismiss();
                })
                .show();
    }

    private void hideProgressDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initBillManagerObserver(){
        final Observer<BillManager> billManagerObserver = billManager -> {
            if (billManager != null){
                registrationBillManager = billManager;

                if (billManager.getServices() != null && billManager.getServices().size() > 0)
                    setupSpinnerAdaptersWithBillManager(billManager);
                else
                    AppUtils.showIndefiniteSnackBar(hospitalNameTextView, "No registration service available");
            }
        };

        foundHospitalViewModel.clearBills();
        foundHospitalViewModel.getBillManagerForRegistration(facilityId, registrationCategoryId).observe(this, billManagerObserver);

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

    /**
     * Show a bottom sheet fragment to display payment details
     */
    private void showPaymentDetailsDialog() {
        final BottomSheetDialog builder = new BottomSheetDialog(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.bottom_dialog_reg_payment, null);
        builder.setContentView(dialogView);

        LinearLayout layout = dialogView.findViewById(R.id.bottom_dialog_layout);
        TextView serviceText = dialogView.findViewById(R.id.service_text);
        TextView priceText = dialogView.findViewById(R.id.price_text);
        TextView apmisIdText = dialogView.findViewById(R.id.apmis_id_text);
        TextView nameText = dialogView.findViewById(R.id.name_text);
        TextView walletText = dialogView.findViewById(R.id.wallet_text);
        Button registerOrFundButton = dialogView.findViewById(R.id.register_fund_button);
        TextView warningText = dialogView.findViewById(R.id.warning_text);

        serviceText.setText(selectedService.getName());
        priceText.setText(String.format("₦%s", AppUtils.formatNumberWithCommas(selectedPrice.getPrice())));


        APMISAPP.getInstance().diskIO().execute(() -> {
            PersonEntry personEntry = InjectorUtils.provideRepository(getContext()).getStaticUserData();
            if (personEntry != null){
                logoImage.post(() -> {
                    apmisIdText.setText(personEntry.getApmisId());
                    nameText.setText(personEntry.getFirstName() + " " + personEntry.getLastName());
                });
            }
        });


        walletObserver = wallet -> {
            if (wallet != null){
                Log.e("WALLET", "Observed");
                walletFunds = wallet.getBalance();
                walletText.setText(String.format("₦%s", AppUtils.formatNumberWithCommas(walletFunds)));

                boolean isMoneyEnough = walletFunds >= selectedPrice.getPrice();

                if (isMoneyEnough){
                    registerOrFundButton.setText("Register");
                } else {
                    registerOrFundButton.setText("Fund Wallet");
                    TransitionManager.beginDelayedTransition(layout);
                    warningText.setVisibility(View.VISIBLE);
                }

                registerOrFundButton.setEnabled(true);
                registerOrFundButton.setOnClickListener(v -> {
                    if (isMoneyEnough){
                        try {
                            attemptFacilityRegistration();
                        } catch (JSONException e) {
                            AppUtils.showShortToast(getContext(), "Unable to register at the moment");
                            e.printStackTrace();
                        }
                        builder.cancel();
                    } else {
                        builder.cancel();
                        Intent i = new Intent(getContext(), FundWalletActivity.class);
                        startActivityForResult(i, FUND_WALLET_REQUEST);
                    }
                });

            }
        };

        foundHospitalViewModel.clearPersonWallet();
        foundHospitalViewModel.getPersonWallet(pref.getPersonId()).removeObservers(this);
        foundHospitalViewModel.getPersonWallet(pref.getPersonId()).observe(this, walletObserver);

        builder.show();
    }

    private void attemptFacilityRegistration() throws JSONException {
        showProgressDialog("Registering", "Please wait while we connect you to "+name);

        foundHospitalViewModel.clearPatientOnRegistration();

        String personId = pref.getPersonId();
        String facilityId = this.facilityId;
        String coverType = this.coverType;
        int cost = selectedPrice.getPrice();
        int amountPaid = selectedPrice.getPrice();
        String facilityServiceId = this.facilityServiceId;
        String registrationCategoryId = this.registrationCategoryId;
        String serviceId = selectedService.getId();
        String category = "Medical Records";//TODO make dynamic later
        String service = selectedService.getName();

        JSONObject coverObject = new JSONObject();

        //COVER
        switch (coverType) {
            case COVER_TYPE_WALLET:
                coverObject = null;
                break;
            case COVER_TYPE_INSURANCE:
                coverObject.put("id", selectedHMO.getHmo());
                coverObject.put("name", selectedHMO.getHmoName());
                coverObject.put("fileNo", 0);
                break;
            case COVER_TYPE_FAMILY:
                coverObject.put("fileNo", 0);
                break;
        }

        foundHospitalViewModel.registerPatient(personId, facilityId, coverType, cost,
                amountPaid, facilityServiceId, registrationCategoryId, serviceId, category, service, coverObject)
                .observe(this, patientObserver);
    }

    private void showProgressDialog(String title, String message){
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case FUND_WALLET_REQUEST:
                    showPaymentDetailsDialog();
                    break;
            }
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
