package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.utilities.AlarmBroadcast;
import ng.apmis.apmismobile.utilities.AlarmManagerSingleton;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context mContext;
    private ArrayList<Appointment> appointmentList;
    private int num = 1;
    private boolean isRecent = true;

    AppointmentAdapter(Context context) {
        mContext = context;
        appointmentList = new ArrayList<>();
    }

    void setAppointmentList(ArrayList<Appointment> appointmentList, boolean isRecent) {
        for (Appointment appointment : appointmentList) {
            //if it's not completed yet
            if (!appointment.getOrderStatusId().equalsIgnoreCase("Completed")){
                if (isRecent)
                    this.appointmentList.add(appointment);
            } else {//completed
                if (!isRecent)
                    this.appointmentList.add(appointment);
            }
        }

        this.isRecent = isRecent;
        notifyDataSetChanged();
    }

    void addToAppointmentList (Appointment appointment) {
        this.appointmentList.add(appointment);
        notifyDataSetChanged();
    }

    public void clear(){
        this.appointmentList = new ArrayList<>();
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment currentAppointment = appointmentList.get(position);
        holder.facilityNameTv.setText(currentAppointment.getFacilityName());
        holder.clinicNameTv.setText(currentAppointment.getClinicId());
        holder.appointmentDateTv.setText(new SimpleDateFormat("MMM dd *yy", Locale.UK).format(AppUtils.dbStringToLocalDate(currentAppointment.getStartDate())).replace("*", "'"));
        holder.appointmentTimeTv.setText(AppUtils.dateToReadableTimeString(AppUtils.dbStringToLocalDate(currentAppointment.getStartDate())));

        holder.popUpMenuImage.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(mContext, holder.popUpMenuImage);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {

                //Starting reminder
                Intent alarmIntent = new Intent(mContext, AlarmBroadcast.class);

                alarmIntent.setAction("appointment_reminder");
                alarmIntent.putExtra("appointment_reminder_id", currentAppointment.get_id());

                AlarmManager alarmManager = AlarmManagerSingleton.getInstance(mContext.getApplicationContext()).getAlarmManager();

                //AppUtils.dbStringToLocalDate(currentAppointment.getStartDate()).getTime()-60*60*1000
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        mContext.getApplicationContext(), 0, alarmIntent,
                        PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        new Date().getTime()+3000, //An hour behind
                        pendingIntent);
                Log.d("Alarm", "Set");
                return true;
            });

            popup.show(); //showing popup menu
        }); //closing the setOnClickListener method
    }

    @Override
    public int getItemCount() {
        if (!isRecent) {
            if (num * 2 > appointmentList.size()) {
                return appointmentList.size();
            }
            return num * 2;

        } else {
            return appointmentList.size();
        }
    }

    void showMore() {
        if ((num * 2) < appointmentList.size()) {
            num = appointmentList.size();
            notifyDataSetChanged();
        }
    }

    void showLess() {
        num = 1;
        notifyDataSetChanged();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView facilityNameTv, clinicNameTv, appointmentDateTv, appointmentTimeTv;
        ImageView popUpMenuImage;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            if (!isRecent) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_blue));
            }
            facilityNameTv = itemView.findViewById(R.id.facility_name_tv);
            clinicNameTv = itemView.findViewById(R.id.clinic_name_tv);
            appointmentDateTv = itemView.findViewById(R.id.appointment_date_tv);
            appointmentTimeTv = itemView.findViewById(R.id.appointment_time_tv);
            popUpMenuImage = itemView.findViewById(R.id.popup_menu_image);
        }

    }
}
