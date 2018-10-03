package ng.apmis.apmismobile.utilities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Date;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.ui.WelcomeScreenActivity;

public class NotificationUtils {

    private static final String APPOINTMENT_CHANNEL_ID = "appointment-notification";
    private static final int NOTIFICATION_ID = 5000;


    public static void buildAppointmentNotification(Context context, Appointment appointment){
        String notificationTitle = "Appointment at " + appointment.getFacilityName();
        String notificationBody = "Remember your appointment by " + AppUtils.dateToReadableTimeString(AppUtils.dbStringToLocalDate(appointment.getStartDate()));

        Intent intent = new Intent(context.getApplicationContext(), WelcomeScreenActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.NOTIFICATION_ACTION, Constants.APPOINTMENTS);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, APPOINTMENT_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_apmis)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setWhen(new Date().getTime());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
