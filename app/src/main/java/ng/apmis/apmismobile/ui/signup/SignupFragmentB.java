package ng.apmis.apmismobile.ui.signup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;

import static ng.apmis.apmismobile.ui.signup.SignupActivity.dateOfBirth;
import static ng.apmis.apmismobile.ui.signup.SignupActivity.genderString;
import static ng.apmis.apmismobile.ui.signup.SignupActivity.securityQuestionString;

public class SignupFragmentB extends Fragment {

    @BindView(R.id.sign_up)
    Button signupBtn;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.security_question_spinner)
    Spinner securityQuestionSpinner;
    @BindView(R.id.security_answer_et)
    TextInputEditText securityAnswerEt;

    private OnFragmentInteractionListener mListener;

    public SignupFragmentB() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_b, container, false);

        ButterKnife.bind(this, rootView);

        signupBtn.setOnClickListener((view) -> {
            if (confirm()) {
                mListener.clickSignup(view);
            }

        });

        securityAnswerEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (confirm()) {
                    mListener.clickSignup(signupBtn);
                }
                return true;
            }
            return false;
        });

        ((SignupActivity) getActivity()).getGenderOrSecurityQuestions(getString(R.string.genders), genderSpinner);
        ((SignupActivity) getActivity()).getGenderOrSecurityQuestions(getString(R.string.security_questions), securityQuestionSpinner);

        ((SignupActivity) getActivity()).onSpinnerOptionsSelection(genderSpinner, getString(R.string.genders));
        ((SignupActivity) getActivity()).onSpinnerOptionsSelection(securityQuestionSpinner, getString(R.string.security_questions));

        return rootView;
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void clickSignup(View view);
    }

    boolean confirm() {

        if (TextUtils.isEmpty(securityQuestionString)) {
            Toast.makeText(getActivity(), "Please select security question", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(genderString)) {
            Toast.makeText(getActivity(), "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (securityAnswerEt.getText().toString().length() < 1) {
            securityAnswerEt.setError(getString(R.string.input_error));
            return false;
        }

        try {
            SignupActivity.personObject.put("gender", genderString);
            SignupActivity.personObject.put("securityQuestion", securityQuestionString);
            SignupActivity.personObject.put("securityAnswer", securityAnswerEt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
