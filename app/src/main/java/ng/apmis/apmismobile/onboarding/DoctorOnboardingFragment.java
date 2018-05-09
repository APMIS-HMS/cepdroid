package ng.apmis.apmismobile.onboarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;

public class DoctorOnboardingFragment extends Fragment {

    @BindView(R.id.next_tv)
    TextView next;


    public DoctorOnboardingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_doctor_onboarding, container, false);

        ButterKnife.bind(this, rootView);

        next.setOnClickListener((view) -> ((OnboardingActivity)getActivity()).onNextPressed());

        return rootView;
    }


}
