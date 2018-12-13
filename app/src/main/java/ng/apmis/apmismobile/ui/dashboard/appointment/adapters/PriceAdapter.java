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
import ng.apmis.apmismobile.data.database.fundAccount.Price;

public class PriceAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Price> mPrices;

    public PriceAdapter(@NonNull Context context, int resource, List<Price> prices) {
        super(context, resource);
        mContext = context;
        mPrices = prices;
    }

    public void addAllClinics(List<Price> prices){
        mPrices.addAll(prices);
        notifyDataSetChanged();
    }

    public void clear(){
        mPrices.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        //adjust +1 for a first null item
        return mPrices.size()+1;
    }

    @Override
    public Object getItem(int position) {
        //adjust -1 to skip first null item
        if (position > 0)
            return mPrices.get(position-1);
        else
            return null;
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
        text.setText(position > 0? mPrices.get(position-1).toString() : "Select Price Level");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);

        //Always sets the first item in the dropdown list to "Select Price Level"
        text.setText(position > 0? mPrices.get(position-1).toString() : "Select Price Level");
        text.setTextColor(position > 0? Color.BLACK : mContext.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}