package ng.apmis.apmismobile.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.login.LoginActivity;
import ng.apmis.apmismobile.utilities.AlarmManagerSingleton;
import ng.apmis.apmismobile.utilities.Constants;

public class SignupActivity extends AppCompatActivity implements SignupFragmentA.OnFragmentInteractionListener, SignupFragmentB.OnFragmentInteractionListener {

    RequestQueue queue;

    public static String dateOfBirth = "";
    public static String genderString = "";
    public static String securityQuestionString = "";

    static final String BASE_URL = Constants.BASE_URL;

    FragmentManager fm;

    ProgressDialog progressDialog;

    public static JSONObject personObject;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        toolbarTitle.setText(R.string.sign_up);

        setSupportActionBar(generalToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
            
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        progressDialog = new ProgressDialog(this);

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
        progressDialog.setCancelable(false);
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
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString) && !selectedString.equals(getString(R.string.loading))) {
                        securityQuestionString = selectedString;
                    }
                } else {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString) && !selectedString.equals(getString(R.string.loading))) {
                        genderString = selectedString;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void getGenderOrSecurityQuestions(final String urlPath, Spinner spinner) {
        StringRequest any = new StringRequest(Request.Method.GET, BASE_URL + urlPath, response -> {
            try {
                JSONObject responseObj = new JSONObject(response.toString());
                JSONArray jar = responseObj.getJSONArray("data");
                if (urlPath.equals(getString(R.string.genders))) {
                    final String[] gendersArray = new String[jar.length()];
                    for (int i = 0; i < jar.length(); i++) {
                        gendersArray[i] = jar.getJSONObject(i).getString("name");
                    }
                    runOnUiThread(() -> setupSpinnerAdapters(gendersArray, spinner));
                }
                if (urlPath.equals(getString(R.string.security_questions))) {
                    final String[] securityQuestionsArray = new String[jar.length()];
                    for (int i = 0; i < jar.length(); i++) {
                        securityQuestionsArray[i] = jar.getJSONObject(i).getString("name");
                    }
                    runOnUiThread(() -> setupSpinnerAdapters(securityQuestionsArray, spinner));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("Volley Error gender sec", String.valueOf(error));
            getGenderOrSecurityQuestions(urlPath, spinner);
        });

        queue.add(any);
    }

    void signUp(JSONObject uniquePerson) {

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "save-person", uniquePerson, response -> {
            progressDialog.dismiss();
            Log.v("Sign up response", String.valueOf(response));
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        }, error -> {
            progressDialog.dismiss();
            Log.v("Sign up error", String.valueOf(error.getMessage()));
            Toast.makeText(SignupActivity.this, "There was an error try again", Toast.LENGTH_SHORT).show();
        });
        queue.add(strRequest);
    }

    private void setupSpinnerAdapters(String[] dataList, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

}
