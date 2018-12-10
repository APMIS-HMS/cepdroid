package ng.apmis.apmismobile.ui.dashboard.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.ModuleAdapter;
import ng.apmis.apmismobile.ui.dashboard.ModuleListModel;
import ng.apmis.apmismobile.ui.dashboard.appointment.AppointmentFragment;
import ng.apmis.apmismobile.ui.dashboard.diagnoses.DiagnosisListFragment;
import ng.apmis.apmismobile.ui.dashboard.documentation.MedicalRecordsFragment;
import ng.apmis.apmismobile.ui.dashboard.healthProfile.HealthProfileFragment;
import ng.apmis.apmismobile.ui.dashboard.prescription.PrescriptionListFragment;
import ng.apmis.apmismobile.utilities.Constants;

public class ViewFragment extends Fragment {

    @BindView(R.id.list_items)
    ListView listItems;

    private static final String CLASSNAME = "VIEW";

    List<ModuleListModel> optionItems = new ArrayList<>();
    {
        optionItems.add(new ModuleListModel(Constants.APPOINTMENTS, R.drawable.ic_appointment));
        optionItems.add(new ModuleListModel(Constants.CLINICAL_DOCUMENTATION, R.drawable.ic_documentation));
        optionItems.add(new ModuleListModel(Constants.PRESCRIPTION, R.drawable.ic_prescription));
        optionItems.add(new ModuleListModel(Constants.VITALS, R.drawable.ic_health_vitals));
        optionItems.add(new ModuleListModel(Constants.INVESTIGATIONS, R.drawable.ic_investigations));

      /*  optionItems.add(new ModuleListModel(Constants.MEDICATION, R.drawable.ic_medication));
        optionItems.add(new ModuleListModel(Constants.HEALTH_INSURANCE, R.drawable.ic_health_insurance));
        optionItems.add(new ModuleListModel(Constants.REFERRAL, R.drawable.ic_referral));
        optionItems.add(new ModuleListModel(Constants.MENSTRUAL_CYCLE, R.drawable.ic_menstrual));
        optionItems.add(new ModuleListModel(Constants.CARE_TEAM, R.drawable.ic_care_team));
        optionItems.add(new ModuleListModel(Constants.CARE_PROVIDERS, R.drawable.ic_care_providers));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_view, container, false);
        ButterKnife.bind(this, rootView);





        ModuleAdapter moduleAdapter = new ModuleAdapter(getActivity(), optionItems);

        listItems.setAdapter(moduleAdapter);

        listItems.setDivider(null);

        listItems.setOnItemClickListener((parent, view, position, id) -> {
            ModuleListModel selectedOption = (ModuleListModel) parent.getItemAtPosition(position);

                switch (selectedOption.getmOption()) {
                    case Constants.CLINICAL_DOCUMENTATION:
                        setFragment(new MedicalRecordsFragment());
                        break;
                    case Constants.MEDICATION:
                        //setFragment(new AppointmentFragment());
                        break;
                    case Constants.PRESCRIPTION:
                        setFragment(new PrescriptionListFragment());
                        break;
                    case Constants.VITALS:
                        setFragment(new HealthProfileFragment());
                        break;
                    case Constants.APPOINTMENTS:
                        setFragment(new AppointmentFragment());
                        break;
                    case Constants.HEALTH_INSURANCE:
                        //setFragment(new AppointmentFragment());
                        break;
                    case Constants.INVESTIGATIONS:
                        setFragment(new DiagnosisListFragment());
                        break;
                    case Constants.REFERRAL:
                        //setFragment(new AppointmentFragment());
                        break;
                    case Constants.MENSTRUAL_CYCLE:
                        //setFragment(new AppointmentFragment());
                        break;
                    case Constants.CARE_TEAM:
                        //setFragment(new AppointmentFragment());
                        break;
                    case Constants.CARE_PROVIDERS:
                        //setFragment(new AppointmentFragment());
                        break;
                }
        });

        return rootView;
    }

    void setFragment (Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).profileImage.setVisibility(View.VISIBLE);
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.VIEW, true);
            ((DashboardActivity)getActivity()).mBottomNav.getMenu().findItem(R.id.view_menu).setChecked(true);
        }
        super.onResume();
    }

}
