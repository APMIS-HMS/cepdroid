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
import java.util.List;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.facilityModel.Service;

public class ServiceAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Service> mServices;

    public ServiceAdapter(@NonNull Context context, int resource, List<Service> services) {
        super(context, resource);
        mContext = context;
        mServices = services;
    }

    public void addAllServices(List<Service> clinics){
        mServices.addAll(clinics);
        notifyDataSetChanged();
    }

    public void clear(){
        mServices.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        //adjust +1 for a first null item
        return mServices.size()+1;
    }

    @Override
    public Object getItem(int position) {
        //adjust -1 to skip first null item
        return mServices.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        //adjust -1 to skip first null item
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
        text.setText(position > 0? mServices.get(position-1).getName() : "Select Service Category");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(position > 0? mServices.get(position-1).getName() : "Select Service Category");
        text.setTextColor(position > 0? Color.BLACK : mContext.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}
