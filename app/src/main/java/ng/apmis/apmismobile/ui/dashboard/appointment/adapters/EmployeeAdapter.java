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
import ng.apmis.apmismobile.data.database.facilityModel.Employee;

public class EmployeeAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Employee> mEmployee;

    public EmployeeAdapter(@NonNull Context context, int resource, List<Employee> employees) {
        super(context, resource);
        mContext = context;
        mEmployee = employees;
    }

    public void addAllEmployees(List<Employee> employees){
        mEmployee.addAll(employees);
        notifyDataSetChanged();
    }

    public void clear(){
        mEmployee.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mEmployee.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position > 0)
            return mEmployee.get(position-1);
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
        text.setText(position > 0? "Dr. " +
                mEmployee.get(position-1).getPersonDetails().getFirstName() + " " +
                mEmployee.get(position-1).getPersonDetails().getLastName()
                : "Select whom to see");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(position > 0? "Dr. " +
                mEmployee.get(position-1).getPersonDetails().getFirstName() + " " +
                mEmployee.get(position-1).getPersonDetails().getLastName()
                : "Select whom to see");
        text.setTextColor(position > 0? Color.BLACK : mContext.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}
