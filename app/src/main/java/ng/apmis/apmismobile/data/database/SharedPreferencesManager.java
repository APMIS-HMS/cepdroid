package ng.apmis.apmismobile.data.database;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

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

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setIsLoggedIn (boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn () {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void storeLoggedInUserKeys (String accessToken, String personId, String email, String dbId) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(PERSON_ID, personId);
        editor.putString(EMAIL, email);
        editor.putString(DB_ID, dbId);
        editor.commit();
    }

    public JSONObject storedLoggedInUser () {
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

    public void storeApmisId (String apmisId) {
        editor.putString(APMIS_ID, apmisId);
        editor.commit();
    }

    public String getStoredApmisId () {
        return pref.getString(APMIS_ID, "");
    }

    public void storeUserPassword (String password) {
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }

    public String getStoredUserPassword () {
        return pref.getString(USER_PASSWORD, "");
    }


}
