package ng.apmis.apmismobile.data.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class handles all call to the network for every segment of the application
 */

final class NetworkService {

    private static final String TAG = NetworkService.class.getSimpleName();

    private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    JSONObject dataResponse, errorResponse;
    private Context context;
    RequestQueue queue;

    NetworkService (Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    /**
     * Logs user into the application with user name and password and returns success or failure as JSONObject
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

}
