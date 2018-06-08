package ng.apmis.apmismobile.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.login.LoginActivity;

public class AppointmentOnboardingFragment extends Fragment {

    @BindView(R.id.finish_tv)
    TextView finish;
    @BindView(R.id.skip_tv) TextView skip;
    @BindView(R.id.selected)
    ImageButton selected;

    public AppointmentOnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_appointment_onboarding, container, false);

        ButterKnife.bind(this, rootView);

        skip.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
    });

        selected.setBackgroundResource(R.drawable.imagebutton_background_colored);

        finish.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return rootView;

    }



}
