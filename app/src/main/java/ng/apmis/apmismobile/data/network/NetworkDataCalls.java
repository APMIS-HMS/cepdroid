package ng.apmis.apmismobile.data.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.AppointmentType;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.cardModel.Card;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.facilityModel.Category;
import ng.apmis.apmismobile.data.database.facilityModel.Clinic;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.HMO;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.personModel.ProfileImageObject;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;
import ng.apmis.apmismobile.utilities.AnnotationExclusionStrategy;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * This class handles all call to the network for every segment of the application
 */

public final class NetworkDataCalls {

    private static final String TAG = NetworkDataCalls.class.getSimpleName();

    //private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";
    private static final String BASE_URL = Constants.BASE_URL;

    private static String REVERSE_QUERY = "&$sort[createdAt]=-1";

    JSONObject dataResponse, errorResponse;
    private Gson gson;
    private Context context;

    public NetworkDataCalls(Context context) {
        this.context = context;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }


    /**
     * Todo: Not used here <br/>
     * Logs user into the application with user name and password and returns success or failure as JSONObject
     * @param apmisId Unique User Id given to every APMIS user
     * @param password Secret Password
     * @return JSONObject containing login details of the user
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

        APMISAPP.getInstance().addToRequestQueue(loginRequest);
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
        APMISAPP.getInstance().addToRequestQueue(strRequest);
    }


    /**
     * Fetch the data contained in the {@link PersonEntry} object from the server
     * @param context The current context
     * @param personId The person Id obtained from login
     * @param accessToken The security access token obtained from login
     */
    public void getPersonData(Context context, String personId, String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "people/" + personId, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("response", String.valueOf(response));

//                try {
//                    String id = response.getString("_id");
//                    String updatedAt = response.getString("updatedAt");
//                    String createdAt = response.getString("createdAt");
//                    String dateOfBirth = response.getString("dateOfBirth");
//                    String firstName = response.getString("firstName");
//                    String gender = response.getString("gender");
//                    String lastName = response.getString("lastName");
//                    String email = response.getString("email");
//                    String motherMaidenName = response.getString("motherMaidenName");
//                    String primaryContactPhoneNo = response.getString("primaryContactPhoneNo");
//                    String securityAnswer = response.getString("securityAnswer");
//                    String securityQuestion = response.getString("securityQuestion");
//                    String title = response.getString("title");
//                    String apmisId = response.getString("apmisId");
//                    String nextOfKin = response.getString("nextOfKin");
//                    String secondaryContactPhoneNo = response.getString("secondaryContactPhoneNo");
//                    ProfileImageObject profileImageObject = new Gson().fromJson(response.get("profileImageObject").toString(), ProfileImageObject.class);
//
//                    Log.e("Image here", profileImageObject.toString());
//
//                    PersonEntry responseEntry = new PersonEntry(apmisId,title,firstName,lastName,gender,motherMaidenName,securityQuestion,securityAnswer,primaryContactPhoneNo,secondaryContactPhoneNo,dateOfBirth,email, "", "", "", "", "", profileImageObject, "", "", nextOfKin);
//
//                    Log.v("responseEntry", responseEntry.toString());
//                    PersonEntry[] personEntries = {responseEntry};
//
//                    InjectorUtils.provideNetworkData(context.getApplicationContext()).setCurrentPersonData(personEntries);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                PersonEntry personEntry = gson.fromJson(response.toString(), PersonEntry.class);
                Log.v("responseEntry", personEntry.toString());

                InjectorUtils.provideNetworkData(context.getApplicationContext()).setCurrentPersonData(personEntry);

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
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };
        APMISAPP.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Fetch the data of person that paid for a service in fund wallet
     * @param context The current context
     * @param personId The person Id that initiated the transaction
     * @param accessToken The security access token obtained from login
     */
    public void getPaidBy (Context context, String personId, String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "people/" + personId, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                PersonEntry personEntry = gson.fromJson(response.toString(), PersonEntry.class);
                Log.v("responseEntry", personEntry.toString());

                InjectorUtils.provideNetworkData(context.getApplicationContext()).setPaidByName(personEntry);

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
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };
        APMISAPP.getInstance().addToRequestQueue(jsonObjectRequest);
    }



    /**
     * Updates the person data with new/changed information on the server
     * @param personEntry The new {@link PersonEntry} data
     * @param accessToken The security access token obtained from login
     */
    public void updatePersonData(PersonEntry personEntry, String accessToken){
        Log.d("Update person ...", "Started update");

        JSONObject params = new JSONObject();
        try {
            params.put("firstName", personEntry.getFirstName());
            params.put("lastName", personEntry.getLastName());
            params.put("email", personEntry.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.PATCH, BASE_URL + "people/"+personEntry.get_id(), params, response -> {

            Log.v("Update person response", String.valueOf(response));

            PersonEntry person = gson.fromJson(response.toString(), PersonEntry.class);
            InjectorUtils.provideNetworkData(context).setCurrentPersonData(person);

        }, (VolleyError error) -> {

            Log.e("Update person error", String.valueOf(error.getMessage()));
            //Return a null object to indicate failure
            InjectorUtils.provideNetworkData(context).setCurrentPersonData(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }

    public void resetPassword() {

    }


    public String[] getGenders() {
        return null;
    }

    public String[] getSecurityQuestions() {
        return null;
    }

    /**
     * Fetch {@link Patient} details list for facilities the Person is enrolled into
     * @param context The current calling context
     * @param personId The person Id obtained from login
     * @param accessToken The security access token obtained from login
     */
    public void fetchPatientDetailsForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "patients?personId=" + personId, null, response -> {

            Log.v("Patient response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<Patient> patientDetails = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Patient[].class)));

                    Log.v("Patient responseEntry", patientDetails.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setPatientDetailsForPerson(patientDetails);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Patient error", String.valueOf(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch facility details
     * @param context
     * @param personId
     * @param accessToken
     */
    public void fetchRegisteredFacilities(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "patients?personId=" + personId + "&$limit=null&$select[facilityId]", null, response -> {

            Log.v("Reg Facility response", String.valueOf(response));

            List<String> facilityIds = new ArrayList<>();
            List<Facility> facilities = new ArrayList<>();

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    try {

                        for (int i = 0; i < jsonArray.length(); ++i){
                            String nextId = jsonArray.getJSONObject(i).getString("facilityId");

                            //In case of duplicates, filter them out. Even though the whole point of these
                            //facility id checks are to avoid duplicates from occurring in the first place
                            if (i == 0) {
                                facilityIds.add(nextId);
                                Facility facility = gson.fromJson(jsonArray.getJSONObject(i).getString("facilityObj"), Facility.class);
                                facility.setPatientIdForPerson(jsonArray.getJSONObject(i).getString("_id"));
                                facilities.add(facility);

                            } else if (!facilityIds.contains(nextId)) {
                                facilityIds.add(nextId);
                                Facility facility = gson.fromJson(jsonArray.getJSONObject(i).getString("facilityObj"), Facility.class);
                                facility.setPatientIdForPerson(jsonArray.getJSONObject(i).getString("_id"));
                                facilities.add(facility);
                            }
                        }

                        Log.v("Reg facilityId fetch", jsonArray.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v("Reg Facility respEntry", response.toString());

                    //return ids and facilities gotten
                    InjectorUtils.provideNetworkData(context).setRegisteredFacilities(facilityIds, facilities);

                } else {
                    //return empty ids facility list
                    InjectorUtils.provideNetworkData(context).setRegisteredFacilities(facilityIds, facilities);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //return null ids and facilities
                InjectorUtils.provideNetworkData(context).setRegisteredFacilities(null, null);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Reg Facility error", String.valueOf(error.getMessage()));
                //return null ids and facilities
                InjectorUtils.provideNetworkData(context).setRegisteredFacilities(null, null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch {@link ClinicSchedule}s list within the provided facility.
     * These clinics are those that have set out schedules in their data
     * @param context The calling context
     * @param facilityId The id of the facility which the clinic is located
     * @param accessToken The security access token obtained from login
     */
    public void fetchClinicsForFacility(Context context, String facilityId, String accessToken){
        Log.d("Clinic response", "Started fetch patient o");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "schedules?facilityId=" + facilityId + REVERSE_QUERY, null, response -> {

            Log.v("Clinic response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<ClinicSchedule> clinicSchedules = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), ClinicSchedule[].class)));

                    Log.v("Clinic responseEntry", clinicSchedules.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setClinicsForFacility(clinicSchedules);

                } else {
                    InjectorUtils.provideNetworkData(context).setClinicsForFacility(new ArrayList<>());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                InjectorUtils.provideNetworkData(context).setClinicsForFacility(null);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Clinic error", String.valueOf(error.getMessage()));
                InjectorUtils.provideNetworkData(context).setClinicsForFacility(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch the {@link Category} list which the Facility renders/offers
     * @param context The calling context
     * @param facilityId The id of the facility rendering these service Categories
     * @param accessToken The security access token obtained from login
     */
    public void fetchCategoriesForFacility(Context context, String facilityId, String accessToken){
        Log.d("Services response", "Started fetch patient o");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "organisation-services?facilityId=" + facilityId + REVERSE_QUERY, null, response -> {

            Log.v("Services response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                JSONArray categoryArray = new JSONArray();

                if (jsonArray.length()>0) {
                    categoryArray = jsonArray.getJSONObject(0).getJSONArray("categories");
                }

                if (categoryArray.length()>0) {

                    List<Category> categories = new ArrayList<>(Arrays.asList(gson.fromJson(categoryArray.toString(), Category[].class)));
                    //Input facility ids in the categories
                    for (Category category : categories)
                        category.setFacilityId(facilityId);

                    Log.v("Services responseEntry", categories.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setServicesForFacility(categories);

                } else {
                    InjectorUtils.provideNetworkData(context).setServicesForFacility(new ArrayList<>());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                InjectorUtils.provideNetworkData(context).setServicesForFacility(null);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Services error", String.valueOf(error.getMessage()));
                InjectorUtils.provideNetworkData(context).setServicesForFacility(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch the list of {@link Employee}s in a Facility
     * @param context The calling context
     * @param facilityId The id of the facility that employed these Employees
     * @param accessToken The security access token obtained from login
     */
    public void fetchEmployeesForFacility(Context context, String facilityId, String accessToken){
        Log.d("Employees response", "Started fetch");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "employees?facilityId=" + facilityId, null, response -> {

            Log.v("Employees response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<Employee> employees = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Employee[].class)));

                    Log.v("Employees responseEntry", employees.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setEmployeesForFacility(employees);

                } else {
                    InjectorUtils.provideNetworkData(context).setEmployeesForFacility(new ArrayList<>());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                InjectorUtils.provideNetworkData(context).setEmployeesForFacility(null);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Employees error", String.valueOf(error.getMessage()));
                InjectorUtils.provideNetworkData(context).setEmployeesForFacility(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch the types of appointments {@link AppointmentType} available
     * @param context The calling context
     * @param accessToken The security access token obtained from login
     */
    public void fetchAppointmentTypes(Context context, String accessToken){
        Log.d("Appointment response", "Started fetch");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "appointment-types", null, response -> {

            Log.v("Appointment response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<AppointmentType> appointmentTypes = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), AppointmentType[].class)));

                    Log.v("Appoint responseEntry", appointmentTypes.toString());

                    InjectorUtils.provideNetworkData(context).setAppointmentTypes(appointmentTypes);

                } else {
                    InjectorUtils.provideNetworkData(context).setAppointmentTypes(new ArrayList<>());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                InjectorUtils.provideNetworkData(context).setAppointmentTypes(null);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Appointment error", String.valueOf(error.getMessage()));
                InjectorUtils.provideNetworkData(context).setAppointmentTypes(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetches the available types of {@link OrderStatus}es from the server
     * @param context The calling context
     * @param accessToken The security access token obtained from login
     */
    public void fetchOrderStatuses(Context context, String accessToken) {
        Log.d("Order response", "Started fetch");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "order-status", null, response -> {

            Log.v("Order response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<OrderStatus> orderStatuses = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), OrderStatus[].class)));

                    Log.v("Order responseEntry", orderStatuses.toString());

                    InjectorUtils.provideNetworkData(context).setOrderStatuses(orderStatuses);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Appointment error", String.valueOf(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Submit an appointment to the server
     * @param context The calling context
     * @param appointment The Appointment just created
     * @param accessToken The security access token obtained from login
     */
    public void submitAppointment(Context context, Appointment appointment, String accessToken){
        Log.d("Submit appoint response", "Started submit");

        JSONObject params = new JSONObject();
        try {
            params.put("facilityId", appointment.getFacilityId());
            params.put("clinicId", appointment.getClinicId());
            params.put("appointmentTypeId", appointment.getAppointmentTypeId());
            params.put("doctorId", appointment.getDoctorId());
            params.put("orderStatusId", appointment.getOrderStatusId());
            params.put("startDate", appointment.getStartDate());
            params.put("patientId", appointment.getPatientId());
            params.put("appointmentReason", appointment.getAppointmentReason());
            params.put("category", appointment.getCategory());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "appointments", params, response -> {

            Log.v("Submit appoint response", String.valueOf(response));

            Appointment appointmentResponse = gson.fromJson(response.toString(), Appointment.class);

            InjectorUtils.provideNetworkData(context).setAppointment(appointmentResponse);

        }, (VolleyError error) -> {

            Log.e("Submit appoint error", String.valueOf(error.networkResponse.statusCode));
            //Return a null object
            InjectorUtils.provideNetworkData(context).setAppointment(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }

    /**
     * Fetch every {@link Appointment} that a patient has scheduled
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchAppointmentsForPerson(Context context, String personId, String accessToken){
        Log.d("Fetch appoints", "Started fetch");


        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "get-patient-appointments?personId="+personId + REVERSE_QUERY, null, response -> {

            Log.v("Fetch appoints response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    List<Appointment> appointments = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Appointment[].class)));

                    Log.v("Fetch appoints", appointments.toString());

                    InjectorUtils.provideNetworkData(context).setAppointments(appointments);
                    InjectorUtils.provideRepository(context).insertAppointmentsForPatient(appointments);

                } else {
                    //return an empty list if nothing is there
                    InjectorUtils.provideNetworkData(context).setAppointments(new ArrayList<>());
                }

            } catch (JSONException e) {
                //return null in case of errors
                InjectorUtils.provideNetworkData(context).setAppointments(null);
                e.printStackTrace();
            }

        }, (VolleyError error) -> {

            //return null in case of errors
            InjectorUtils.provideNetworkData(context).setAppointments(null);
            Log.e("Fetch appoints error", error.toString());

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }

    /**
     * Fetch all {@link Documentation} regarding a Person(Patient)
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchClinicalDocumentationForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "documentations?personId=" + personId + REVERSE_QUERY, null, response -> {

            Log.v("Records response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                JSONArray documentationArray = new JSONArray();

                if (jsonArray.length()>0) {
                    documentationArray = jsonArray.getJSONObject(0).getJSONArray("documentations");
                }

                //Iterate through all documentation items gotten
                if (documentationArray.length()>0) {

                    List<Documentation> documentations = new ArrayList<>(Arrays.asList(gson.fromJson(documentationArray.toString(), Documentation[].class)));

                    for (int i=0; i < documentations.size(); ++i){
                        JSONObject body = new JSONObject();
                        try {
                            //get the body from the documentation json array gotten earlier
                            body = documentationArray.getJSONObject(i).getJSONObject("document").getJSONObject("body");
                        } catch (Exception e){
                            Log.e("Document", e.getLocalizedMessage());
                        }

                        //Get every documentation item an set the body JSON Object
                        //Do this because the structure of the body object varies and cannot be
                        //parsed using GSON
                        documentations.get(i).getDocument().setBody(body);
                    }

                    Log.v("Records responseEntry", documentations.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setDocumentationsForPerson(documentations);

                } else {
                    //return an empty list if no documentations available
                    InjectorUtils.provideNetworkData(context).setDocumentationsForPerson(new ArrayList<>());
                }

            } catch (JSONException e) {
                InjectorUtils.provideNetworkData(context).setDocumentationsForPerson(null);
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Records error", String.valueOf(error.getMessage()));
                InjectorUtils.provideNetworkData(context).setDocumentationsForPerson(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };


        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
        APMISAPP.getInstance().getRequestQueue().getCache().clear();
    }

    /**
     * Fetch all {@link Prescription}s that have been given to a Patient
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchPrescriptionsForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "get-person-prescriptions/0?personId=" + personId + REVERSE_QUERY, null, response -> {

            Log.v("Prescription response", String.valueOf(response));


            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Prescription[].class)));

                    Log.v("Prescriptions R.entry", prescriptions.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setPrescriptionsForPerson(prescriptions);

                } else {
                    //Return empty array list if empty
                    InjectorUtils.provideNetworkData(context).setPrescriptionsForPerson(new ArrayList<>());
                }

            } catch (JSONException e) {
                InjectorUtils.provideNetworkData(context).setPrescriptionsForPerson(null);
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                InjectorUtils.provideNetworkData(context).setPrescriptionsForPerson(null);
                Log.e("Records error", String.valueOf(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fetch all {@link LabRequest} investigations that have been made on behalf of a Patient
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchLabRequestsForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "get-person-lab-requests/0?personId=" + personId + REVERSE_QUERY, null, response -> {

            Log.v("Lab Request response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<LabRequest> requests = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), LabRequest[].class)));

                    Log.v("Lab Request R.entry", requests.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setLabRequestsForPerson(requests);

                } else {
                    //return empty list
                    InjectorUtils.provideNetworkData(context).setLabRequestsForPerson(new ArrayList<>());
                }

            } catch (JSONException e) {
                InjectorUtils.provideNetworkData(context).setLabRequestsForPerson(null);
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                InjectorUtils.provideNetworkData(context).setLabRequestsForPerson(null);
                Log.e("Lab Request error", String.valueOf(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Upload profile photo from device to the server
     * @param personEntry The {@link PersonEntry} object with the photo
     * @param image The Bitmap image of the photo the user wishes to upload
     * @param accessToken The security access token obtained from login
     */
    public void uploadProfilePictureForPerson(PersonEntry personEntry, Bitmap image, String accessToken) {

        byte[] imageArr = AppUtils.convertBitmapToByteArray(image);
        String imageBase64String = Base64.encodeToString(imageArr, Base64.NO_WRAP);

        int size = imageArr.length / 1024;

        JSONObject job = new JSONObject();
        try {
            job.put("base64", "data:image/jpeg;base64," +imageBase64String);
            job.put("container", "personfolder");
            job.put("mimeType", "image/jpeg");
            job.put("uploadType", "profilePicture");
            job.put("docName", "me.jpeg");
            job.put("docType", "Image");
            job.put("size", size);
            job.put("user", personEntry.getApmisId());
            job.put("id", personEntry.get_id());
            job.put("facilityId", "x");//getting facility Id is quite a pain, but surprisingly, anything works


            Log.v("Image Profile", String.valueOf(job));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest uploadRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "file-upload-facade", job, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                dataResponse = response;
                Log.v("Image response", String.valueOf(response));
                try {
                    JSONObject profileImageJsonObject = response.getJSONObject("data")
                            .getJSONObject("profile").getJSONObject("profileImageObject");
                    ProfileImageObject profileImageObject = gson.fromJson(profileImageJsonObject.toString(), ProfileImageObject.class);

                    File profilePhotoDir = new File(context.getFilesDir(), "profilePhotos");
                    profilePhotoDir.mkdir();

                    File profilePhotoFile = new File(profilePhotoDir, profileImageObject.getFilename());
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(profilePhotoFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //compress uploaded image and save to file
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);


                    //Set other data for the person object
                    personEntry.setProfileImageObject(profileImageObject);
                    personEntry.setProfileImageFileName(profileImageObject.getFilename());
                    personEntry.setProfileImageUriPath(profileImageObject.getPath());
                    InjectorUtils.provideNetworkData(context).setCurrentPersonData(personEntry);

                } catch (JSONException e) {
                    InjectorUtils.provideNetworkData(context).setCurrentPersonData(null);
                    e.printStackTrace();
                }
            }

        }, error -> {
            InjectorUtils.provideNetworkData(context).setCurrentPersonData(null);
            Log.e("Image error", String.valueOf(error));

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer "+accessToken);

                return params;
            }
        };

        //Set(extend) timeout for upload request
        uploadRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));

        APMISAPP.getInstance().addToRequestQueue(uploadRequest);


//        OkHttpClient client = new OkHttpClient.Builder()
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        RequestBody formBody = new FormBody.Builder()
//                .add("base64", "data:image/jpeg;base64," +imageBase64String)
//                .add("container", "personfolder")
//                .add("mimeType", "image/jpeg")
//                .add("uploadType", "profilePicture")
//                .add("docName", "me.jpeg")
//                .add("docType", "Image")
//                .add("size", "6032")
//                .add("user", apmisId)
//                .add("id", personId)
//                .build();
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("base64", "me.jpeg",
//                        RequestBody.create(MediaType.parse("image/jpeg"), imageArr))
//                .addFormDataPart("container", "personfolder")
//                .addFormDataPart("mimeType", "image/jpeg")
//                .addFormDataPart("uploadType", "profilePicture")
//                .addFormDataPart("docName", "me.jpeg")
//                .addFormDataPart("docType", "Image")
//                .addFormDataPart("size", "6032")
//                .addFormDataPart("user", apmisId)
//                .addFormDataPart("id", personId)
//                .build();
//
//        String testURL = "http://192.168.43.2/polls/public_html/phpwebserviceapi/";
//
//
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .url(localURL + "file-upload-facade")
//                .addHeader("Authorization", localAuth)
//                .post(formBody)
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, IOException e) {
//                Log.e("Image call error", call.request().toString());
//                Log.e("Image error", e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, okhttp3.Response response) throws IOException {
//                //Log.e("Image call response", bodyToString2(call.request().body()));
//                Log.v("Image response", response.body().string());
//            }
//        });

    }

    /**
     * Makes a request for the image using the Person image URI and downloads the profile
     * picture image to a local file
     * @param context The calling context
     * @param person The person we're downloading the image for
     * @param downloadFile The download file the image is being written to
     */
    public void downloadProfilePictureForPerson(Context context, PersonEntry person, File downloadFile){
        Log.v("Image", "Download image started to "+downloadFile.getAbsolutePath());
        ImageRequest imageRequest = new ImageRequest(person.getProfileImageUriPath(), response -> {
            try {
                FileOutputStream out = new FileOutputStream(downloadFile);

                //compress bitmap and save to file
                response.compress(Bitmap.CompressFormat.PNG, 100, out);
                //notify that image has been saved
                InjectorUtils.provideNetworkData(context).setProfilePhotoPath(downloadFile.getPath());


                Log.v("Image reponse", "Download image completed to "+downloadFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.e("TAG", error.getLocalizedMessage());
                    //Notify an error occurred
                    InjectorUtils.provideNetworkData(context).setProfilePhotoPath("error");
                } catch (Exception e){

                }
            }
        });

        APMISAPP.getInstance().addToRequestQueue(imageRequest);
    }


    /**
     *
     * @param context
     * @param itemType
     * @param accessToken
     */
    public void searchForItems(Context context, String itemType, String searchQuery, String accessToken, int skipCount){
        String urlQuery;

        Log.e("Find net", "Skipped "+skipCount);

        switch (itemType) {
            case "Hospital":
                //urlQuery = "facilities?$select[name]&$select[email]&$select[logoObject]&facilityTypeId=Hospital&$sort[updatedAt]=-1&$skip="+skipCount;
                urlQuery = "find-facilities?name="+searchQuery+"&$select[name]&$select[email]&$select[logoObject]&facilityTypeId=Hospital&$sort[updatedAt]=-1&$skip="+skipCount;
                break;
            case "Doctor":
                urlQuery = "employees?isActive=true&professionId=Doctor&$skip="+skipCount;
                break;
            case "Nurse":
                urlQuery = "employees?isActive=true&professionId=Nurse&$skip="+skipCount;
                break;
            default:
                InjectorUtils.provideNetworkData(context).setFoundObjects(new ArrayList<>());
                return;
        }


        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + urlQuery, null, response -> {

            Log.v("Find Request response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<SearchTermItem> foundItems = parseFoundObjectsUsingType(itemType, jsonArray);

                    Log.v("Find Request R.entry", foundItems.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setFoundObjects(foundItems);

                } else {
                    InjectorUtils.provideNetworkData(context).setFoundObjects(new ArrayList<>());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                InjectorUtils.provideNetworkData(context).setFoundObjects(null);
            }

        }, error -> {
            Log.e("Lab Request error", String.valueOf(error.getMessage()));
            InjectorUtils.provideNetworkData(context).setFoundObjects(null);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        APMISAPP.getInstance().getRequestQueue().getCache().clear();
        jsonArrayRequest.setShouldCache(false);
        APMISAPP.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     *
     * @param type
     * @param jsonArray
     * @return
     */
    private List<SearchTermItem> parseFoundObjectsUsingType(String type, JSONArray jsonArray){
        List<SearchTermItem> foundItems = new ArrayList<>();

        switch (type){
            case "Hospital":
                List<Facility> facilities = (Arrays.asList(gson.fromJson(jsonArray.toString(), Facility[].class)));
                for (Facility facility : facilities){
                    String logoUrl = null;
                    if (facility.getLogoObject() != null)
                        logoUrl = facility.getLogoObject().getPath();
                    String hospitalId = facility.getId();
                    String name = facility.getName();
                    String email = facility.getEmail();
                    foundItems.add(new SearchTermItem(logoUrl, hospitalId, name, email, "Hospital"));
                }
                return foundItems;


            case "Doctor":
                List<Employee> employees = (Arrays.asList(gson.fromJson(jsonArray.toString(), Employee[].class)));
                for (Employee doctor : employees){
                    String doctorId = doctor.getId();
                    String doctorName = doctor.getPersonDetails().getFirstName() +
                            " " + doctor.getPersonDetails().getLastName();
                    String department = doctor.getDepartmentId();
                    String doctorImageUrl = doctor.getPersonDetails().getProfileImageUriPath();
                    foundItems.add(new SearchTermItem(doctorImageUrl, doctorId, doctorName, department, "Doctor"));
                }
                return foundItems;


            case "Nurse":
                List<Employee> nurses = (Arrays.asList(gson.fromJson(jsonArray.toString(), Employee[].class)));
                for (Employee nurse : nurses){
                    String nurseId = nurse.getId();
                    String nurseName = nurse.getPersonDetails().getFirstName() +
                            " " + nurse.getPersonDetails().getLastName();
                    String department = nurse.getDepartmentId();
                    String nurseImageUrl = nurse.getPersonDetails().getProfileImageUriPath();
                    foundItems.add(new SearchTermItem(nurseImageUrl, nurseId, nurseName, department, "Nurse"));
                }
                return foundItems;


            default:
                return foundItems;
        }
    }

    /**
     *
     * @param facilityId
     * @param accessToken
     */
    public void fetchFacilityDetails(Context context, String facilityId, String accessToken){
        Log.d("Fetch facility", "Started fetch");

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "facilities/"+facilityId, null, response -> {

            Log.v("Fetch facility response", String.valueOf(response));

            Facility facility = gson.fromJson(response.toString(), Facility.class);

            Log.v("Fetch facility", facility.toString());

            InjectorUtils.provideNetworkData(context).setFacilityData(facility);


        }, (VolleyError error) -> {

            Log.e("Fetch facility error", error.toString());

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }

    /**
     *
     * @param context
     * @param facilityId
     * @param accessToken
     */
    public void fetchServiceCategoryId(Context context, String facilityId, String accessToken){
        Log.d("Fetch categoryId", "Started fetch");

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "organisation-services?facilityId=" + facilityId
                + "&$select[categories.name]&$select[categories._id]", null, response -> {

            Log.v("Fetch categoryId resp", String.valueOf(response));

            String facilityServiceId = null;
            String categoryId = null;

            List<String> facilityServiceAndCategoryIds = null;

            try {


                JSONArray jsonArray = response.getJSONArray("data");

                //Get the facility service Id first
                facilityServiceId = jsonArray.getJSONObject(0).getString("_id");

                JSONArray categoryArray = jsonArray.getJSONObject(0).getJSONArray("categories");

                //Then obtain the category Id
                for (int i = 0; i < categoryArray.length(); ++i){
                    if (categoryArray.getJSONObject(i).getString("name").equals("Medical Records")) {
                        categoryId = categoryArray.getJSONObject(i).getString("_id");
                    }
                }

                Log.v("Fetch categoryId", categoryArray.toString());

                //Add them to the array list
                facilityServiceAndCategoryIds = new ArrayList<>();
                facilityServiceAndCategoryIds.add(facilityServiceId);
                facilityServiceAndCategoryIds.add(categoryId);


                InjectorUtils.provideNetworkData(context).setServiceCategoryIdForFacility(facilityServiceAndCategoryIds);

            } catch (JSONException e) {
                InjectorUtils.provideNetworkData(context).setServiceCategoryIdForFacility(null);
                e.printStackTrace();
            }

        }, (VolleyError error) -> {
            InjectorUtils.provideNetworkData(context).setServiceCategoryIdForFacility(null);
            Log.e("Fetch categoryId error", error.toString());

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);

    }


    /**
     *
     * @param context
     * @param facilityId
     * @param categoryId
     * @param accessToken
     */
    public void fetchServiceCategoryBillManager(Context context, String facilityId, String categoryId, String accessToken){
        Log.d("Fetch servCat", "Started fetch");

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "bill-managers?facilityId="+facilityId+"&categoryId="+categoryId, null, response -> {

            Log.v("Fetch servCat response", String.valueOf(response));

            BillManager billManager = gson.fromJson(response.toString(), BillManager.class);

            Log.v("Fetch servCat", billManager.toString());

            //Append the facility ID to the object
            billManager.setFacilityId(facilityId);

            InjectorUtils.provideNetworkData(context).setCategoryBillManager(billManager);


        }, (VolleyError error) -> {
            InjectorUtils.provideNetworkData(context).setCategoryBillManager(null);
            Log.e("Fetch servCat error", error.toString());

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }


    /**
     *
     * @param context
     * @param personId
     * @param accessToken
     */
    public void fetchPersonWallet(Context context, String personId, String accessToken){
        Log.d("Fetch wallet", "Started fetch");

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "people/"+personId+"?$select[wallet]", null, response -> {

            Log.v("Fetch wallet response", String.valueOf(response));

            try {

                JSONObject walletObject = response.getJSONObject("wallet");

                Wallet wallet = gson.fromJson(walletObject.toString(), Wallet.class);

                Log.v("Fetch wallet", wallet.toString());

                InjectorUtils.provideNetworkData(context).setPersonWallet(wallet);

            } catch (JSONException e){
                InjectorUtils.provideNetworkData(context).setPersonWallet(null);
                e.printStackTrace();
            }




        }, (VolleyError error) -> {
            InjectorUtils.provideNetworkData(context).setPersonWallet(null);
            Log.e("Fetch wallet error", error.toString());

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }


    /**
     *
     * @param context
     * @param accessToken
     */
    public void fetchNearbyFacilities (Context  context, String accessToken) {
        JsonObjectRequest nearbyFacilities = new JsonObjectRequest(Request.Method.GET, BASE_URL
                + "facilities?$limit=null&$select[address.geometry.location]&$select[name]&facilityTypeId=Hospital", null, response -> {

            Facility[] locationObject = null;
            try {
                locationObject = gson.fromJson(response.getJSONArray("data").toString(), Facility[].class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            InjectorUtils.provideNetworkData(context).setNearbyLocations(locationObject);

        }, (VolleyError error) -> {

            Log.e("Nearby location error", String.valueOf(error.getMessage()));
            //Return an error string
            InjectorUtils.provideNetworkData(context).setNearbyLocations(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        nearbyFacilities.setRetryPolicy(new DefaultRetryPolicy(120000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(nearbyFacilities);
    }

    /**
     *
     * @param context
     * @param referenceCode
     * @param amountPaid
     * @param accessToken
     */
    public void fetchPaymentVerificationData(Context context, String referenceCode, int amountPaid, String personId, String accessToken, boolean isCardReused, boolean shouldSaveCard, String encEmail, String encAuth){
        Log.d("Pay Verify", "Started verification");

        JSONObject params = new JSONObject();

        if (!isCardReused) {
            try {
                JSONObject ref = new JSONObject();
                ref.put("trxref", referenceCode);
                params.put("ref", ref);

                JSONObject payment = new JSONObject();
                payment.put("type", "e-payment");
                payment.put("route", "Paystack");
                params.put("payment", payment);

                params.put("entity", "Person");
                params.put("destinationId", personId);
                params.put("amount", amountPaid);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            try {
                Log.e("Pay", "auth:" + encAuth);
                Log.e("Pay", "email:" + encEmail);
                Log.e("Pay", "amount:" + amountPaid);

                JSONObject payment = new JSONObject();
                payment.put("type", "e-payment");
                payment.put("route", "Paystack");
                params.put("payment", payment);

                params.put("entity", "Person");
                params.put("destinationId", personId);

                params.put("authorization_code", encAuth);
                params.put("email", encEmail);
                params.put("amount", amountPaid*100);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL
                + "fund-wallet?isCardReused="+ String.valueOf(isCardReused) +"&saveCard=" + String.valueOf(shouldSaveCard),
                params, response -> {

            Log.v("Pay Verify response", String.valueOf(response));

            String status = null;
            String cardStatus = null;
            List<String> statuses = new ArrayList<>();//holds the status for the card payment, and the authorization save

            try {
                status = response.getString("status");
                cardStatus = String.valueOf(response.getJSONObject("data").getBoolean(isCardReused ? "card_auth_status": "card_save_status"));

                statuses.add(status);
                statuses.add(cardStatus);

            } catch (JSONException e) {
                statuses = null;
                e.printStackTrace();
            }

            InjectorUtils.provideNetworkData(context).setPaymentVerificationData(statuses);

        }, (VolleyError error) -> {

            Log.e("Pay Verify error", String.valueOf(error.getMessage()));
            //Return a null status list
            InjectorUtils.provideNetworkData(context).setPaymentVerificationData(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(120000, 6, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }


    /**
     *
     * @param context
     * @param personId
     * @param cardId
     * @param accessToken
     */
    public void removeCardFromWallet(Context context, String personId, String cardId, Wallet wallet, String accessToken){
        Log.v("Pay Remove Card", "Started removal");

        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.DELETE, BASE_URL
                + "fund-wallet/"+ personId +"?cardId=" + cardId,
                null, response -> {

            Log.v("Pay Remove response", String.valueOf(response));

            List<Card> cards = new ArrayList<>();
            String status = null;

            try {
                cards = Arrays.asList(gson.fromJson(response.getJSONObject("data").getJSONObject("wallet").getJSONArray("cards").toString(), Card[].class));
                status = response.getString("status");

            } catch (JSONException e) {
                cards = null;
                status = null;
                e.printStackTrace();
            }

            InjectorUtils.provideNetworkData(context).setCardRemovalStatus(status);
            //Update the user's wallet
            if (cards != null ){
                wallet.setCards(cards);
                InjectorUtils.provideNetworkData(context).setPersonWallet(wallet);
            }

        }, (VolleyError error) -> {

            Log.e("Pay Remove error", String.valueOf(error.getMessage()));
            //Return a null status list
            InjectorUtils.provideNetworkData(context).setCardRemovalStatus(null);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        appointmentRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(appointmentRequest);
    }


    /**
     * Register a person as a patient in a Facility
     * @param context The calling context
     * @param personId The id of the person being registered
     * @param facilityId The id of the facility the person is being registered into
     * @param accessToken The security access token obtained from login
     */
    public void registerPatientInFacility(Context context, String personId, String facilityId, String accessToken){
        Log.d("Reg patient", "Started registration");

        JSONObject params = new JSONObject();
        try {
            params.put("personId", personId);
            params.put("facilityId", facilityId);
            params.put("isActive", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest registrationRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "patients", params, response -> {

            Log.v("Reg patient response", String.valueOf(response));

            Patient registrationResponse = gson.fromJson(response.toString(), Patient.class);

            InjectorUtils.provideNetworkData(context).setRegisteredPatient(registrationResponse);

        }, (VolleyError error) -> {

            Log.e("Reg patient error", String.valueOf(error.getMessage()));
            //Return a null object
            InjectorUtils.provideNetworkData(context).setRegisteredPatient(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        registrationRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(registrationRequest);
    }


    /**
     *
     * @param context
     * @param accessToken
     */
    public void fetchHMOSInFacility (Context context, String facilityId, String accessToken) {
        JsonObjectRequest nearbyFacilities = new JsonObjectRequest(Request.Method.GET, BASE_URL
                + "hmos?facilityId=" + facilityId + "&setName=true&$select[hmos.hmo]", null, response -> {

            List<HMO> hmos = null;

            try {
                hmos = Arrays.asList(gson.fromJson(response.getJSONArray("data")
                        .getJSONObject(0).getJSONArray("hmos").toString(), HMO[].class));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            InjectorUtils.provideNetworkData(context).setHMOSInFacility(hmos);


        }, (VolleyError error) -> {

            Log.e("Nearby location error", String.valueOf(error.getMessage()));
            //Return null
            InjectorUtils.provideNetworkData(context).setHMOSInFacility(null);

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        nearbyFacilities.setRetryPolicy(new DefaultRetryPolicy(120000, 2, 1));
        APMISAPP.getInstance().addToRequestQueue(nearbyFacilities);
    }
}
