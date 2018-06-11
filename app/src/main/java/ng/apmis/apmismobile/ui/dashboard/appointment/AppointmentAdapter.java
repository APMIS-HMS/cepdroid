package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointments;

import static android.text.format.DateUtils.FORMAT_SHOW_TIME;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context mContext;
    public ArrayList<Appointments> appointmentList;
    int num = 1;

    AppointmentAdapter(Context context) {
        mContext = context;
        appointmentList = new ArrayList<Appointments>();
    }

    void setAppointmentList(ArrayList<Appointments> appointmentList) {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    void addToAppointmentList (Appointments appointment) {
        this.appointmentList.add(appointment);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointments currentAppointment = appointmentList.get(position);
        holder.facilityNameTv.setText(currentAppointment.getFacilityName());
        holder.clinicNameTv.setText(currentAppointment.getClinicName());
        holder.appointmentDateTv.setText(DateUtils.getRelativeDateTimeString(mContext, currentAppointment.getAppointmentDateTime(),0,0, FORMAT_SHOW_TIME));
        holder.appointmentTimeTv.setText(DateUtils.getRelativeTimeSpanString(currentAppointment.getAppointmentDateTime()));

    }

    @Override
    public int getItemViewType(int position) {
        if (appointmentList.get(position).getAppointmentDateTime() == new Date().getTime()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        if (num * 2 > appointmentList.size()) {
            return appointmentList.size();
        }
        return num * 2;
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView facilityNameTv, clinicNameTv, appointmentDateTv, appointmentTimeTv;

        AppointmentViewHolder(View itemView) {
            super(itemView);

            facilityNameTv = itemView.findViewById(R.id.facility_name_tv);
            clinicNameTv = itemView.findViewById(R.id.clinic_name_tv);
            appointmentDateTv = itemView.findViewById(R.id.appointment_date_tv);
            appointmentTimeTv = itemView.findViewById(R.id.appointment_time_tv);
        }

    }
}
