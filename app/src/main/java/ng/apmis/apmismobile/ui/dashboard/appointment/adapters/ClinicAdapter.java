package ng.apmis.apmismobile.ui.dashboard.appointment.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.facilityModel.ClinicSchedule;

public class ClinicAdapter extends ArrayAdapter {

    private Context mContext;
    private List<ClinicSchedule> mClinics;

    public ClinicAdapter(@NonNull Context context, int resource, List<ClinicSchedule> clinics) {
        super(context, resource);
        mContext = context;
        mClinics = clinics;
    }

    public void addAllClinics(List<ClinicSchedule> clinics){
        mClinics.addAll(clinics);
        notifyDataSetChanged();
    }

    public void clear(){
        mClinics.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mClinics.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position > 0)
            return mClinics.get(position-1);
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
        text.setText(position > 0? mClinics.get(position-1).getClinic() : "Select Clinic");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(position > 0? mClinics.get(position-1).getClinic() : "Select Clinic");
        text.setTextColor(position > 0? Color.BLACK : mContext.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}
