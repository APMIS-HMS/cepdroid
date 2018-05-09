package ng.apmis.apmismobile.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.login.LoginActivity;

public class MedicineOnboardingFragment extends Fragment {

    @BindView(R.id.skip_tv)
    TextView skip;
    @BindView(R.id.next_tv)
    TextView nextBtn;

    public MedicineOnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_medicine_onboarding, container, false);

        ButterKnife.bind(this, rootView);

        skip.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        nextBtn.setOnClickListener((view) -> ((OnboardingActivity)getActivity()).onNextPressed());

        return rootView;
    }

}
