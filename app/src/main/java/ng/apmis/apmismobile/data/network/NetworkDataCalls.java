package ng.apmis.apmismobile.data.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.appointmentModel.OrderStatus;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.facilityModel.AppointmentType;
import ng.apmis.apmismobile.data.database.facilityModel.Category;
import ng.apmis.apmismobile.data.database.facilityModel.Clinic;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;
import ng.apmis.apmismobile.utilities.AnnotationExclusionStrategy;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * This class handles all call to the network for every segment of the application
 */

final class NetworkDataCalls {

    private static final String TAG = NetworkDataCalls.class.getSimpleName();

    //private static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";
    private static final String BASE_URL = Constants.BASE_URL;

    JSONObject dataResponse, errorResponse;
    private Gson gson;
    private Context context;
    RequestQueue queue;

    public NetworkDataCalls(Context context) {
        this.context = context;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        queue = Volley.newRequestQueue(context);
    }


    /**
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
                    String apmisId = response.getString("apmisId");
                    String nextOfKin = response.getString("nextOfKin");
                    String secondaryContactPhoneNo = response.getString("secondaryContactPhoneNo");

                    PersonEntry responseEntry = new PersonEntry(apmisId,title,firstName,lastName,gender,motherMaidenName,securityQuestion,securityAnswer,primaryContactPhoneNo,secondaryContactPhoneNo,dateOfBirth,"", "", "", "", "", "", "", "", "", nextOfKin);

                    Log.v("responseEntry", responseEntry.toString());
                    PersonEntry[] personEntries = {responseEntry};

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

    /**
     * Fetch {@link Patient} details list for every facility the Person is enrolled into
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
                params.put("Content-Type", "application/json; charset=UTF-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        queue.add(jsonArrayRequest);
    }

    /**
     * Fetch {@link Clinic}s list within the provided facility.
     * These clinics are those that have set out schedules in their data
     * @param context The calling context
     * @param facilityId The id of the facility which the clinic is located
     * @param accessToken The security access token obtained from login
     */
    public void fetchClinicsForFacility(Context context, String facilityId, String accessToken){
        Log.d("Clinic response", "Started fetch patient o");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "schedules?facilityId=" + facilityId, null, response -> {

            Log.v("Clinic response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<ClinicSchedule> clinicSchedules = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), ClinicSchedule[].class)));

                    Log.v("Clinic responseEntry", clinicSchedules.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setClinicsForFacility(clinicSchedules);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Clinic error", String.valueOf(error.getMessage()));
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

        queue.add(jsonArrayRequest);
    }

    /**
     * Fetch the {@link Category} list which the Facility renders/offers
     * @param context The calling context
     * @param facilityId The id of the facility rendering these service Categories
     * @param accessToken The security access token obtained from login
     */
    public void fetchCategoriesForFacility(Context context, String facilityId, String accessToken){
        Log.d("Services response", "Started fetch patient o");

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "organisation-services?facilityId=" + facilityId, null, response -> {

            Log.v("Services response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                JSONArray categoryArray = new JSONArray();

                if (jsonArray.length()>0) {
                    categoryArray = jsonArray.getJSONObject(0).getJSONArray("categories");
                }

                if (categoryArray.length()>0) {

                    List<Category> categories = new ArrayList<>(Arrays.asList(gson.fromJson(categoryArray.toString(), Category[].class)));

                    Log.v("Services responseEntry", categories.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setServicesForFacility(categories);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Services error", String.valueOf(error.getMessage()));
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

        queue.add(jsonArrayRequest);
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

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Employees error", String.valueOf(error.getMessage()));
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

        queue.add(jsonArrayRequest);
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
                params.put("Content-Type", "application/json; charset=UTF-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        queue.add(jsonArrayRequest);
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
                params.put("Content-Type", "application/json; charset=UTF-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        queue.add(jsonArrayRequest);
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

            Log.e("Submit appoint error", String.valueOf(error.getMessage()));
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

        queue.add(appointmentRequest);
    }

    /**
     * Fetch every {@link Appointment} that a patient has scheduled
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchAppointmentsForPerson(Context context, String personId, String accessToken){
        Log.d("Fetch appoints", "Started submit");


        JsonObjectRequest appointmentRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "get-patient-appointments?personId="+personId, null, response -> {

            Log.v("Fetch appoints response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    List<Appointment> appointments = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Appointment[].class)));

                    Log.v("Fetch appoints", appointments.toString());

                    InjectorUtils.provideRepository(context).insertAppointmentsForPatient(appointments);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, (VolleyError error) -> {

            Log.e("Fetch appoints error", String.valueOf(error.getMessage()));

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        queue.add(appointmentRequest);
    }

    /**
     * Fetch all {@link Documentation} regarding a Person(Patient)
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchMedicalRecordForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "documentations?personId=" + personId, null, response -> {

            Log.v("Records response", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                JSONArray documentationArray = new JSONArray();

                if (jsonArray.length()>0) {
                    documentationArray = jsonArray.getJSONObject(0).getJSONArray("documentations");
                }

                if (documentationArray.length()>0) {

                    List<Documentation> documentations = new ArrayList<>(Arrays.asList(gson.fromJson(documentationArray.toString(), Documentation[].class)));

                    for (int i=0; i < documentations.size(); ++i){
                        JSONObject body = new JSONObject();
                        try {
                            body = documentationArray.getJSONObject(i).getJSONObject("document").getJSONObject("body");
                        } catch (Exception e){
                            Log.e("Document", e.getLocalizedMessage());
                        }

                        documentations.get(i).getDocument().setBody(body);
                    }

                    Log.v("Records responseEntry", documentations.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setDocumentationsForPerson(documentations);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Records error", String.valueOf(error.getMessage()));
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

        queue.add(jsonArrayRequest);
    }

    /**
     * Fetch all {@link Prescription}s that have been given to a Patient
     * @param context The calling context
     * @param personId The personId of the Patient
     * @param accessToken The security access token obtained from login
     */
    public void fetchPrescriptionsForPerson(Context context, String personId, String accessToken){

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "get-person-prescriptions/0?personId=" + personId, null, response -> {

            Log.v("Prescription response", String.valueOf(response));


            try {
                JSONArray jsonArray = response.getJSONArray("data");

                if (jsonArray.length()>0) {

                    List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Prescription[].class)));

                    Log.v("Prescriptions R.entry", prescriptions.get(0).toString());

                    InjectorUtils.provideNetworkData(context).setPrescriptionsForPerson(prescriptions);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e("Records error", String.valueOf(error.getMessage()))) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");

                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        queue.add(jsonArrayRequest);
    }


}
