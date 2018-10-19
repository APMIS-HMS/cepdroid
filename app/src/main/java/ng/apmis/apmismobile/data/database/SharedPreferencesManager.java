package ng.apmis.apmismobile.data.database;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

public class SharedPreferencesManager {

    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    private static final String IS_LOGGED_IN = "isLoggedIn";

    private static final String ACCESS_TOKEN = "token";

    private static final String PERSON_ID = "pid";

    private static final String EMAIL = "email";

    private static final String DB_ID = "dbib";

    private static final String APMIS_ID = "apmisid";

    private static final String USER_PASSWORD = "password";

    public SharedPreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, _context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Set whether or not if this is the first time
     * the app is ever being launched on this device
     * @param isFirstTime <code>true</code> if it's the first time
     */
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    /**
     * Check if if this is the first time
     * the app is ever being launched on this device
     * @return <code>true</code> if it's the first time, <code>false</code> otherwise
     */
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /**
     * Set the user as logged in or not
     * @param isLoggedIn <code>true</code> if to set the user logged in
     */
    public void setIsLoggedIn (boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    /**
     * Check if user is logged in
     * @return <code>true</code> if logged in, <code>false</code> otherwise
     */
    public boolean isLoggedIn () {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Store login details gotten from server upon login
     * @param accessToken The security access token
     * @param personId {@link PersonEntry} object id, gotten from login response
     * @param email email used to login
     * @param dbId user login id
     */
    public void storeLoggedInUserDetails(String accessToken, String personId, String email, String dbId) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(PERSON_ID, personId);
        editor.putString(EMAIL, email);
        editor.putString(DB_ID, dbId);
        editor.commit();
    }

    /**
     * Creates a JSON object and stores logged in user details in it
     * @return The JSONObject with the current logged in user details
     */
    public JSONObject getStoredLoggedInUser() {
        JSONObject loggedUser = new JSONObject();
        try {
            loggedUser.put(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN,""));
            loggedUser.put(PERSON_ID, pref.getString(PERSON_ID, ""));
            loggedUser.put(EMAIL, pref.getString(EMAIL, ""));
            loggedUser.put(DB_ID, pref.getString(DB_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return loggedUser;

    }

    /**
     * Store the Personalised APMIS ID given to everyone who logs in to APMIS
     * @param apmisId The generated APMIS ID
     */
    public void storeApmisId (String apmisId) {
        editor.putString(APMIS_ID, apmisId);
        editor.commit();
    }

    /**
     *
     * @return The Stored APMIS ID
     */
    public String getStoredApmisId () {
        return pref.getString(APMIS_ID, "");
    }

    /**
     * Store the user password
     * @param password Secret password used for login
     */
    public void storeUserPassword (String password) {
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }

    /**
     *
     * @return Secret password for login
     */
    public String getStoredUserPassword () {
        return pref.getString(USER_PASSWORD, "");
    }

    /**
     * The security access token used to access Apmis API methods
     * @return The Security access token
     */
    public String getStoredUserAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    /**
     *
     * @return The id for the {@link PersonEntry} object
     */
    public String getPersonId() {
        return pref.getString(PERSON_ID, "");
    }
}
