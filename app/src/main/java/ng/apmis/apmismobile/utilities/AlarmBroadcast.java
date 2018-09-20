package ng.apmis.apmismobile.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Alarm", "here");

        if (intent.getAction() != null && intent.getAction().equals("appointment_reminder")) {

            Log.d("Alarm", "received");

            String appointmentId = intent.getExtras().getString("appointment_reminder_id");

            APMISAPP.getInstance().diskIO().execute(() -> {
                Appointment appointment = InjectorUtils.provideRepository(context).getAppointmentById(appointmentId);
                NotificationUtils.buildAppointmentNotification(context, appointment);
            });
        }

    }
}
