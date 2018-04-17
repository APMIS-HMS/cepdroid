package ng.apmis.apmismobile.ui.login;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

/**
 * A login screen that offers login via apmisId/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.apmis_id)AutoCompleteTextView apmisIdEditText;
    @BindView(R.id.password) EditText passwordEditText;
    @BindView(R.id.email_sign_in_button) Button submitButton;
    @BindView(R.id.login_progress) ProgressBar loading;

    private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";
    RequestQueue queue;

    boolean fieldsOk = false;

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        queue = Volley.newRequestQueue(this.getApplicationContext());

        sharedPreferencesManager = new SharedPreferencesManager(this.getApplicationContext());

        submitButton.setOnClickListener((view) -> {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
            checkFields();
        });




    }

    private void checkFields() {
        String apmisId = apmisIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!checkApmisId(apmisId)) {
            apmisIdEditText.setError("Check apmis Id");
        } else {
            fieldsOk = true;
        }

        if (!checkPassword(password)) {
            passwordEditText.setError("Check password !!!");
            fieldsOk = false;
        } else {
            fieldsOk = true;
        }

        if (!fieldsOk) {
            //Do nothing
        } else {
            attemptLogin(apmisId, password);
        }

    }

    private void attemptLogin(String apmisId, String password) {

        loading.setIndeterminate(true);
        loading.setVisibility(View.VISIBLE);

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
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {
                    //Log.d("response", String.valueOf(response));


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


                    loading.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.d("error", String.valueOf(error.getMessage()) + "Error");
                    loading.setVisibility(View.GONE);
                }
            });

            queue.add(loginRequest);

        });


    }


    private boolean checkApmisId(String apmisId) {

        if (apmisId.length() < 8 || !apmisId.contains("-") || apmisId.equals("")) {
            Toast.makeText(this, String.valueOf(apmisId.contains("-")), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword (String password) {
        //TODO consider condition to check for wrong password
        return true;
    }

}

