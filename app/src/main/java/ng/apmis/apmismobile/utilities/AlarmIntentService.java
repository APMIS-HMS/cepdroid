package ng.apmis.apmismobile.utilities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.util.Log;

import java.util.Date;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmIntentService extends IntentService {

    private static final String RESET_ALARMS = "alarms";

    public AlarmIntentService() {
        super("AlarmIntentService");
    }


    public static void startResetAlarms(Context context) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.setAction(RESET_ALARMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (RESET_ALARMS.equals(action)) {
            resetAlarms();
        }
    }

    private void resetAlarms() {
        APMISAPP.getInstance().diskIO().execute(() -> {
            for (Appointment x : InjectorUtils.provideRepository(this).getStaticAppointmentList()) {
                if (x.getIsActive()) {
                    createReminder(x.get_id(), x.getCreatedAt());
                }
            }
            if (isServiceRunning()) {
                Log.e("Service ran", "true");
            } else {
                Log.e("Service ran", "false");
            }
        });
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmIntentService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void createReminder(String appointmentId, String createdAt) {

        Date mYourSchedule = AppUtils.dbStringToLocalDate(createdAt);
        Intent alarmIntent = new Intent(this, AlarmBroadcast.class);

        alarmIntent.setAction("appointment_reminder");
        alarmIntent.putExtra("appointment_reminder_id", appointmentId);

        AlarmManager alarmManager = AlarmManagerSingleton.getInstance(this.getApplicationContext()).getAlarmManager();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                mYourSchedule.getTime() - 60 * 60 * 1000, //An hour behind
                pendingIntent);
        Log.d("Alarm", "Set");
    }
}
