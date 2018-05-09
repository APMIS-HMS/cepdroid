package ng.apmis.apmismobile.ui.signup;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.login.LoginActivity;

public class SignupActivity extends AppCompatActivity implements SignupFragmentA.OnFragmentInteractionListener, SignupFragmentB.OnFragmentInteractionListener {

    RequestQueue queue;

    static String dateOfBirth = "";
    static String genderString = "";
    static String securityQuestionString = "";

    static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    FragmentManager fm;

    ProgressDialog progressDialog;

    JSONObject personObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);

        ButterKnife.bind(this);
        queue = Volley.newRequestQueue(this);


        personObject = new JSONObject();

        fm = getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.container, new SignupFragmentA())
                .commit();

    }

    @Override
    public void clickContinue(View view) {
        fm.beginTransaction()
                .add(R.id.container, new SignupFragmentB())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickSignup(View view) {

        JSONObject uniquePerson = new JSONObject();
        //Assign all values to an Object with a person key
        try {
            uniquePerson.put("person", personObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Person Obj", uniquePerson.toString());
        Log.v("Person Starts", "Here");
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Sign Up");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        signUp(uniquePerson);
    }

    void onSpinnerOptionsSelection(Spinner any, final String spinnerType) {
        any.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerType.equals(getString(R.string.security_questions))) {
                    securityQuestionString = adapterView.getItemAtPosition(i).toString();
                } else {
                    genderString = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void getGenderOrSecurityQuestions(final String urlPath, Spinner spinner) {
        StringRequest any = new StringRequest(Request.Method.GET, BASE_URL + urlPath, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response.toString());
                    JSONArray jar = responseObj.getJSONArray("data");
                    if (urlPath.equals(getString(R.string.genders))) {
                        final String[] gendersArray = new String[jar.length()];
                        for (int i = 0; i < jar.length(); i++) {
                            gendersArray[i] = jar.getJSONObject(i).getString("name");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupSpinnerAdapters(gendersArray, spinner);
                            }
                        });
                    }
                    if (urlPath.equals(getString(R.string.security_questions))) {
                        final String[] securityQuestionsArray = new String[jar.length()];
                        for (int i = 0; i < jar.length(); i++) {
                            securityQuestionsArray[i] = jar.getJSONObject(i).getString("name");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupSpinnerAdapters(securityQuestionsArray, spinner);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Volley Error", String.valueOf(error));
            }
        });

        queue.add(any);
    }

    void signUp(JSONObject uniquePerson) {

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "save-person", uniquePerson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.v("Sign up response", String.valueOf(response));
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.v("SIgn up error", String.valueOf(error));
                Toast.makeText(SignupActivity.this, "There was an error try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(strRequest);
    }

    private void setupSpinnerAdapters(String[] dataList, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

}
