package ng.apmis.apmismobile.data.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * This class handles all call to the network for every segment of the application
 */

final class NetworkDataCalls {

    private static final String TAG = NetworkDataCalls.class.getSimpleName();

    private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    JSONObject dataResponse, errorResponse;
    private Context context;
    RequestQueue queue;

    public NetworkDataCalls(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    /**
     * Logs user into the application with user name and password and returns success or failure as JSONObject
     *
     * @param apmisId
     * @param password
     * @return
     */
    public JSONObject login(String apmisId, String password) {

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
                dataResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                try {
                    errorResponse.put("error", String.valueOf(error));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(loginRequest);
        return dataResponse;
    }

    /**
     * Creates a new person and sends apmis id and password via sms to user to be used to login to the application
     *
     * @param uniquePerson
     */
    public void signUp(JSONObject uniquePerson) {

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "save-person", uniquePerson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dataResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    errorResponse.put("signUpError", String.valueOf(error));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(strRequest);
    }

    public void getPersonData(Context context, String personId, String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "people/" + personId, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("response", String.valueOf(response));

                try {
                    String id = response.getString("_id");
                    String updatedAt = response.getString("updatedAt");
                    String createdAt = response.getString("createdAt");
                    String dateOfBirth = response.getString("dateOfBirth");
                    String firstName = response.getString("firstName");
                    String gender = response.getString("gender");
                    String lastName = response.getString("lastName");
                    String motherMaidenName = response.getString("motherMaidenName");
                    String primaryContactPhoneNo = response.getString("primaryContactPhoneNo");
                    String securityAnswer = response.getString("securityAnswer");
                    String securityQuestion = response.getString("securityQuestion");
                    String title = response.getString("title");
                    String wallet = response.getString("wallet");
                    String apmisId = response.getString("apmisId");
                    String nextOfKin = response.getString("nextOfKin");
                    String professions = response.getString("personProfessions");
                    String secondaryContactPhoneNo = response.getString("secondaryContactPhoneNo");

                    PersonEntry responseEntry = new PersonEntry(apmisId,title,firstName,lastName,gender,motherMaidenName,securityQuestion,securityAnswer,primaryContactPhoneNo,secondaryContactPhoneNo,dateOfBirth,"", "", "", professions, "", "", "", "", "", "", nextOfKin, wallet);

                    Log.v("responseEntry", responseEntry.toString());
                PersonEntry [] personEntries = {responseEntry};

                InjectorUtils.provideNetworkData(context.getApplicationContext()).setCurrentPersonData(personEntries);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Person error", String.valueOf(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }


    public void resetPassword() {

    }

    public String[] getGenders() {
        return null;
    }

    public String[] getSecurityQuestions() {
        return null;
    }

}
