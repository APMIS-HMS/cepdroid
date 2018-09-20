package ng.apmis.apmismobile.utilities;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmManagerSingleton {

    private static AlarmManagerSingleton sInstance;
    private static final Object LOCK = new Object();
    private static AlarmManager alarmManager;
    private static Context mContext;

    private AlarmManagerSingleton (Context context) {
        mContext = context;
    }

    public static AlarmManagerSingleton getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance  = new AlarmManagerSingleton(context);
                alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            }
        }
        return sInstance;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }
}