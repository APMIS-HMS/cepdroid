package ng.apmis.apmismobile.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.signup.SignupActivity;

public class AppointmentOnboardingFragment extends Fragment {

    @BindView(R.id.finish_btn)
    Button finishBtn;
    @BindView(R.id.previous_btn) Button previousBtn;

    public AppointmentOnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_appointment_onboarding, container, false);

        ButterKnife.bind(this, rootView);

        previousBtn.setOnClickListener((view) -> getActivity().onBackPressed());

        finishBtn.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), SignupActivity.class));
            getActivity().finish();
        });

        return rootView;

    }



}
