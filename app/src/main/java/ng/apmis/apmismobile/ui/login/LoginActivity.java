package ng.apmis.apmismobile.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.signup.SignupActivity;

/**
 * A login screen that offers login via apmisId/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.apmis_id) TextInputEditText apmisIdEditText;
    @BindView(R.id.password) TextInputEditText passwordEditText;
    @BindView(R.id.email_sign_in_button) Button submitButton;
    @BindView(R.id.remember_me) CheckBox rememberMe;
    @BindView(R.id.create_account) TextView createAccount;

    private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";
    RequestQueue queue;

    boolean fieldsOk = false;

    SharedPreferencesManager sharedPreferencesManager;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        queue = Volley.newRequestQueue(this.getApplicationContext());

        sharedPreferencesManager = new SharedPreferencesManager(this.getApplicationContext());

        submitButton.setOnClickListener((view) -> {
            checkFields();
        });

        rememberMe.setOnClickListener((view) -> {
            if (((CheckBox) view).isChecked()) {
                sharedPreferencesManager.storeUserPassword(passwordEditText.getText().toString());
            } else {
                sharedPreferencesManager.storeUserPassword("");
            }
        });

        createAccount.setOnClickListener( (view) -> startActivity (new Intent(this, SignupActivity.class)) );

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(sharedPreferencesManager.getStoredApmisId())) {
            apmisIdEditText.setText(sharedPreferencesManager.getStoredApmisId());
        } else {
            apmisIdEditText.setText("");
        }

        if (!TextUtils.isEmpty(sharedPreferencesManager.getStoredUserPassword())) {
            passwordEditText.setText(sharedPreferencesManager.getStoredUserPassword());
            rememberMe.setChecked(true);
        } else {
            passwordEditText.setText("");
        }
    }

    private void checkFields() {
        String apmisId = apmisIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!checkApmisId(apmisId)) {
            apmisIdEditText.setError("Check Apmis ID");
        } else {
            fieldsOk = true;
        }

        if (!checkPassword(password)) {
            passwordEditText.setError("Check password !!!");
            fieldsOk = false;
        } else {
            fieldsOk = true;
        }

        if (fieldsOk) {
            attemptLogin(apmisId, password);
        }

    }

    private void attemptLogin(String apmisId, String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        sharedPreferencesManager.storeApmisId(apmisId);

        APMISAPP.getInstance().networkIO().execute(() -> {

            JSONObject job = new JSONObject();
            try {
                job.put("email", apmisId);
                job.put("password", password);
                job.put("strategy", "local");
                Log.v("Person to Json", String.valueOf(job));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {

                try {
                    Log.v("accessToken", response.getString("accessToken"));
                    Log.v("user", response.getString("user"));
                    String token = response.getString("accessToken");

                    JSONObject userObj = response.getJSONObject("user");

                    String personId = userObj.getString("personId");
                    String email = userObj.getString("email");
                    String dbId = userObj.getString("_id");


                    sharedPreferencesManager.storeLoggedInUserKeys(token, personId, email, dbId);

                    Log.v("sharedPRef", String.valueOf(sharedPreferencesManager.storedLoggedInUser()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }, error -> {
                Log.d("error", String.valueOf(error.getMessage()) + "Error");
                progressDialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login Failed")
                        .setMessage("Please try again !!!")
                        .setPositiveButton("Dismiss", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });

            queue.add(loginRequest);

        });


    }


    private boolean checkApmisId(String apmisId) {

        if (apmisId.length() < 8 || !apmisId.contains("-") || apmisId.equals("")) {
            return false;
        }
        return true;
    }

    private boolean checkPassword (String password) {
        return !TextUtils.isEmpty(password);
    }

}

