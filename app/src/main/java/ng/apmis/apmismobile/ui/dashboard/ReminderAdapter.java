package ng.apmis.apmismobile.ui.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.personModel.Reminder;
import ng.apmis.apmismobile.utilities.AppUtils;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context mContext;
    private List<Reminder> reminders = new ArrayList<>();

    private final static String TYPE_APPOINTMENT = "appointment";
    private final static String TYPE_DRUG = "drug";

    public ReminderAdapter(Context mContext) {
        this.mContext = mContext;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_reminder_item, parent, false);
        ReminderViewHolder viewHolder = new ReminderViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        Reminder reminder = reminders.get(position);

        holder.titleText.setText(String.format("%s", reminder.getName()));
        holder.timeText.setText(String.format("%s", reminder.getTime()));

        switch (reminder.getType()){
            case TYPE_APPOINTMENT:
                holder.typeImageView.setImageResource(R.drawable.ic_appointment_rem);
                break;
            case TYPE_DRUG:
                holder.typeImageView.setImageResource(R.drawable.ic_pill);
                break;
            default:
                holder.typeImageView.setImageResource(R.drawable.ic_pill);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void addAppointmentReminders(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {

            if (!checkIfToday(appointment)) //skip loop if it isn't today's appointment
                continue;

            String name = appointment.getFacilityName() + " appointment";
            String time = AppUtils.dateToReadableTimeString(AppUtils.dbStringToLocalDate(appointment.getStartDate()));
            reminders.add(new Reminder(TYPE_APPOINTMENT, name, time, appointment));
        }

        notifyDataSetChanged();
    }

    /**
     * Check if an appointment is scheduled today (in local time)
     * @param appointment
     * @return
     */
    public static boolean checkIfToday(Appointment appointment){
        //get the string date value now
        String today = AppUtils.dateToDbString(new Date()).substring(0, 10) + "%";

        //Get appointment date value in local time
        Date appointmentDate = AppUtils.dbStringToLocalDate(appointment.getStartDate());
        //Get appointment date value string
        String appointmentDateString = AppUtils.dateToDbString(appointmentDate).substring(0, 10) + "%";

        return today.equals(appointmentDateString);
    }

    public void clear() {
        reminders.clear();
        notifyDataSetChanged();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_image)
        ImageView typeImageView;

        @BindView(R.id.title_text)
        TextView titleText;

        @BindView(R.id.time_text)
        TextView timeText;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
