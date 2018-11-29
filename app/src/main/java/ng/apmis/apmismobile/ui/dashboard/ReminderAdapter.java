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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context mContext;
    private List<Reminder> reminders = new ArrayList<>();

    private final static String TYPE_APPOINTMENT = "appointment";
    private final static String TYPE_DRUG = "drug";

    public ReminderAdapter(Context mContext) {
        this.mContext = mContext;

        reminders.add(new Reminder(TYPE_APPOINTMENT, "Sabaoth Clinic appointment", "Back pain", "4:00PM"));
        reminders.add(new Reminder(TYPE_DRUG, "Panadol", "", "8:00AM"));
        reminders.add(new Reminder(TYPE_DRUG, "Loratadine", "You'll probably die without these", "12:00PM"));
        reminders.add(new Reminder(TYPE_APPOINTMENT, "APMIS Hospital appointment", "Chest pain and chronic coughs", "4:00PM"));
        reminders.add(new Reminder(TYPE_DRUG, "Panadol", "Eat first before taking", "4:00PM"));

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
        holder.messageText.setText(reminder.getMessage());
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

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_image)
        ImageView typeImageView;

        @BindView(R.id.title_text)
        TextView titleText;

        @BindView(R.id.message_text)
        TextView messageText;

        @BindView(R.id.time_text)
        TextView timeText;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
