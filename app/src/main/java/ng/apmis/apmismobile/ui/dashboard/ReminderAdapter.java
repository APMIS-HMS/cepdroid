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

    public ReminderAdapter(Context mContext) {
        this.mContext = mContext;

        reminders.add(new Reminder("Appointment", "Sabaoth Clinic appointment", "Back pain", "4:00PM"));
        reminders.add(new Reminder("Drug", "Panadol", "", "8:00AM"));
        reminders.add(new Reminder("Drug", "Loratadine", "You'll probably die without these", "12:00PM"));
        reminders.add(new Reminder("Appointment", "APMIS Hospital appointment", "Chest pain and chronic coughs", "4:00PM"));
        reminders.add(new Reminder("Drug", "Panadol", "Eat first before taking", "4:00PM"));

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

        holder.titleTimeText.setText(String.format("%s by %s", reminder.getName(), reminder.getTime()));
        holder.messageText.setText(reminder.getMessage());

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_image)
        ImageView typeImageView;

        @BindView(R.id.title_time_text)
        TextView titleTimeText;

        @BindView(R.id.message_text)
        TextView messageText;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
