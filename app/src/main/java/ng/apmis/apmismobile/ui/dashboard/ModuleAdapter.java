package ng.apmis.apmismobile.ui.dashboard;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ng.apmis.apmismobile.R;

import java.util.List;

/**
 * Created by Thadeus-APMIS on 5/16/2018.
 */

public class ModuleAdapter extends BaseAdapter {

    private int lastPosition = -1;

    List<ModuleListModel> passedList;
    Context mContext;

    public ModuleAdapter (Context context, List<ModuleListModel> optionsList) {
        mContext = context;
        passedList = optionsList;
    }

    @Override
    public int getCount() {
        return passedList.size();
    }

    @Override
    public Object getItem(int position) {
        return passedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.module_list_item, parent, false);
        }

        if(position > lastPosition) {
            AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(mContext,
                    R.animator.bounce_in);
            set1.setTarget(convertView);
            set1.setStartDelay(20);
            set1.start();
            lastPosition = position;
        }


        // get current item to be displayed
        ModuleListModel currentItem = (ModuleListModel) getItem(position);

        // get the TextView for item name and item description
        ImageView listImage = convertView.findViewById(R.id.list_image);
        TextView textViewItemDescription = convertView.findViewById(R.id.list_text);

        //sets the text for item name and item description from the current item object
        listImage.setImageResource(currentItem.getmOptionImage());
        textViewItemDescription.setText(currentItem.getmOption());

        // returns the view for the current row
        return convertView;
    }
}
