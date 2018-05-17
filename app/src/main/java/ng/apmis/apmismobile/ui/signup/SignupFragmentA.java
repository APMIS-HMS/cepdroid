package ng.apmis.apmismobile.ui.signup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;

import static ng.apmis.apmismobile.ui.signup.SignupActivity.dateOfBirth;
import static ng.apmis.apmismobile.ui.signup.SignupActivity.genderString;
import static ng.apmis.apmismobile.ui.signup.SignupActivity.securityQuestionString;


public class SignupFragmentA extends Fragment implements View.OnTouchListener {

    @BindView(R.id.title_et)
    TextInputEditText titleEt;
    @BindView(R.id.firstname_et)
    TextInputEditText firstnameEt;
    @BindView(R.id.lastname_et)
    TextInputEditText lastnameEt;
    @BindView(R.id.mothers_maiden_name_et)
    TextInputEditText mothersMaidenEt;
    @BindView(R.id.dob)
    TextInputEditText dob;
    @BindView(R.id.phone_number_et)
    TextInputEditText phoneNumberEt;

    @BindView(R.id.continue_btn)
    Button continueBtn;

    DialogFragment dialogfragment;


    private OnFragmentInteractionListener mListener;

    public SignupFragmentA() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup_a, container, false);

        ButterKnife.bind(this, rootView);

        continueBtn.setOnClickListener((view) -> {
            try {
                if (checkFields()) {
                    mListener.clickContinue(view);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        dob.setOnTouchListener(this);

        dob.setOnEditorActionListener((v, actionId, event) -> {
            try {
                if (checkFields()) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        if (checkFields()) {
                            mListener.clickContinue(continueBtn);
                        }
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        });

        return rootView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            dialogfragment = new DatePicker();
            dialogfragment.show(getActivity().getFragmentManager(), "Select Date");
            return true;
        }
        return false;
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
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void clickContinue(View view);
    }

    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), this, year, month, day);

            return datepickerdialog;
        }

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            EditText et = (EditText) getActivity().findViewById(R.id.dob);
            et.setText(day + "/" + (month + 1) + "/" + year);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateOfBirth = String.valueOf(new Date(year, month, day).toInstant().toString());
            } else {
                dateOfBirth = String.valueOf(new Date(year, month, day).toString());
            }
            Log.v("Date Iso", dateOfBirth);
        }

    }

    boolean checkFields() throws JSONException {
        Pattern p = Pattern.compile("([0-9])");
        Matcher m = p.matcher(firstnameEt.getText().toString());

        if (titleEt.getText().toString().length() < 2 || m.find()) {
            titleEt.setError(getString(R.string.input_error));
            return false;
        }
        if (firstnameEt.getText().toString().length() < 1 || m.find()) {
            firstnameEt.setError(getString(R.string.input_error));
            return false;
        }
        if (lastnameEt.getText().toString().length() < 1 || m.find()) {
            lastnameEt.setError(getString(R.string.input_error));
            return false;
        }
        if (mothersMaidenEt.getText().toString().length() < 1 || m.find()) {
            mothersMaidenEt.setError(getString(R.string.input_error));
            return false;
        }
        if (phoneNumberEt.getText().toString().length() < 11) {
            phoneNumberEt.setError(getString(R.string.input_error));
        }
        if (TextUtils.isEmpty(dateOfBirth)) {
            dob.setError(getString(R.string.input_error));
            return false;
        }

        SignupActivity.personObject.put("title", titleEt.getText().toString());
        SignupActivity.personObject.put("firstName", firstnameEt.getText().toString());
        SignupActivity.personObject.put("lastName", lastnameEt.getText().toString());
        SignupActivity.personObject.put("motherMaidenName", mothersMaidenEt.getText().toString());
        SignupActivity.personObject.put("primaryContactPhoneNo", phoneNumberEt.getText().toString());
        SignupActivity.personObject.put("dateOfBirth", dateOfBirth);


        return true;
    }

}
