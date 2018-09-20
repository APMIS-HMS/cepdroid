package ng.apmis.apmismobile.ui.dashboard.appointment.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.utilities.AppUtils;

public class ScheduleAdapter extends ArrayAdapter{
    private Context mContext;
    private List<ScheduleItem> mSchedules;

    public ScheduleAdapter(@NonNull Context context, int resource, List<ScheduleItem> scheduleItems) {
        super(context, resource);
        mContext = context;
        mSchedules = scheduleItems;
    }

    public void addAllSchedules(List<ScheduleItem> schedules){
        mSchedules.addAll(schedules);
        notifyDataSetChanged();
    }

    public void clear(){
        mSchedules.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mSchedules.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position > 0)
            return mSchedules.get(position-1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position-1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);

        ScheduleItem schedule = null;
        Date startDate = null;
        Date endDate = null;

        if (position > 0) {
            schedule = mSchedules.get(position-1);
            startDate = AppUtils.dbStringToLocalDate(schedule.getStartTime());
            endDate = AppUtils.dbStringToLocalDate(schedule.getEndTime());
            text.setText(String.format("%s, %s - %s", schedule.getDay()+"s", AppUtils.dateToReadableTimeString(startDate), AppUtils.dateToReadableTimeString(endDate)));
        } else {
            text.setText("Select Schedule");
        }



        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);

        ScheduleItem schedule = null;
        Date startDate = null;
        Date endDate = null;

        if (position > 0) {
            schedule = mSchedules.get(position-1);
            startDate = AppUtils.dbStringToLocalDate(schedule.getStartTime());
            endDate = AppUtils.dbStringToLocalDate(schedule.getEndTime());
            text.setTextColor(Color.BLACK);
            text.setText(String.format("%s, %s - %s", schedule.getDay()+"s", AppUtils.dateToReadableTimeString(startDate), AppUtils.dateToReadableTimeString(endDate)));
        } else {
            text.setText("Select Schedule");
            text.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }



        return convertView;
    }
}