package ng.apmis.apmismobile.onboarding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;

public class MedicineOnboardingFragment extends Fragment {

    @BindView(R.id.previous_btn)
    Button previous;
    @BindView(R.id.next_btn)
    Button nextBtn;

    public MedicineOnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_medicine_onboarding, container, false);

        ButterKnife.bind(this, rootView);

        previous.setOnClickListener((view) -> getActivity().onBackPressed());

        nextBtn.setOnClickListener((view) -> ((OnboardingActivity)getActivity()).onNextPressed());

        return rootView;
    }

}
