package ng.apmis.apmismobile.ui.dashboard.appointment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.utilities.AlarmBroadcast;
import ng.apmis.apmismobile.utilities.AlarmManagerSingleton;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Appointment> appointmentList;
    private ArrayList<SegmentedAppointmentItem> segmentedAppointmentItems;
    private final int TYPE_SEGMENT = 0;
    private final int TYPE_APPOINTMENT = 1;
    private final int TYPE_COLLAPSED = 2;

    private boolean areMissedCollapsed = true;
    private boolean arePastCollapsed = true;

    private RelativeLayout rootView;
    private RecyclerView mRecyclerView;

    private boolean isAnimating = false;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    /**
     * Enumeration to highlight the various states an appointment may be in
     */
    private enum AppointmentStatus {
        SCHEDULED ("Scheduled Appointments"), MISSED ("Missed Appointments"), PAST ("Past Appointments");

        /**
         * Name to use on the segment title
         */
        public String segmentName;

        AppointmentStatus(String name) {
            this.segmentName = name;
        }
    }

    AppointmentAdapter(Context context, RelativeLayout rootView) {
        mContext = context;
        this.rootView = rootView;
        appointmentList = new ArrayList<>();
        segmentedAppointmentItems = new ArrayList<>();
    }

    private void rearrangeAppointments(List<Appointment> appointments){

        Comparator<Appointment> compareAppointmentDates = (o1, o2) -> {
            //ascending order of dates
            return o1.compareTo(o2);
        };

        Comparator<Appointment> compareNames = (o1, o2) -> {
            //descending order of segment names
            return getStatus(o2).segmentName.compareTo(getStatus(o1).segmentName);
        };


        Collections.sort(appointments, compareAppointmentDates);
        Collections.sort(appointments, compareNames);

    }

    void setAppointmentList(ArrayList<Appointment> appointmentList, boolean isRecent) {
        segmentedAppointmentItems.clear();

        rearrangeAppointments(appointmentList);

        Appointment previousAppointmentInList = null;
        Appointment currentAppointmentInList;

        for (int i=0; i<appointmentList.size(); ++i){
            if (i>0)
                previousAppointmentInList = appointmentList.get(i-1);
            currentAppointmentInList = appointmentList.get(i);

            //Add the scheduled segment title first
            if (i==0){
                segmentedAppointmentItems.add(
                        new SegmentedAppointmentItem(AppointmentStatus.SCHEDULED.segmentName,
                                false, null));
            } else if (getStatus(currentAppointmentInList) != getStatus(previousAppointmentInList)){
                segmentedAppointmentItems.add(
                        new SegmentedAppointmentItem(getStatus(currentAppointmentInList).segmentName,
                                true, null));
            }

            //Then add other items
            segmentedAppointmentItems.add(
                    new SegmentedAppointmentItem(null, false, currentAppointmentInList)
            );

        }

        this.appointmentList.addAll(appointmentList);
        notifyDataSetChanged();
    }

    private AppointmentStatus getStatus(Appointment appointment) {
        //The current time now
        Date now = new Date();

        //if the appointment has been marked as completed
        if (appointment.getOrderStatusId().equalsIgnoreCase("Completed")){
            return AppointmentStatus.PAST;

        //else, if the appointment isn't completed, but the start time has passed
        } else if (now.after(AppUtils.dbStringToLocalDate(appointment.getStartDate()))){
            return AppointmentStatus.MISSED;

        //if the time hasn't passed, and the appointment isn't marked "completed"
        } else {
            return AppointmentStatus.SCHEDULED;
        }
    }

    void addToAppointmentList (Appointment appointment) {
        this.appointmentList.add(appointment);
        notifyDataSetChanged();
    }

    public void clear(){
        this.appointmentList = new ArrayList<>();
        this.segmentedAppointmentItems.clear();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;

        switch (viewType){
            case TYPE_APPOINTMENT:
                view = inflater.inflate(R.layout.appointment_list_item, parent, false);
                return new AppointmentViewHolder(view);
            case TYPE_SEGMENT:
                view = inflater.inflate(R.layout.appointment_segment, parent, false);
                return new SegmentTitleViewHolder(view);
            case TYPE_COLLAPSED:
                view = inflater.inflate(R.layout.layout_empty, parent, false);
                return new CollapsedViewHolder(view);
            default:
                view = inflater.inflate(R.layout.appointment_list_item, parent, false);
                return new AppointmentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SegmentedAppointmentItem segmentedAppointmentItem = segmentedAppointmentItems.get(position);

        if (holder instanceof AppointmentViewHolder) {
            ((AppointmentViewHolder) holder).facilityNameTv.setText(segmentedAppointmentItem.appointment.getFacilityName());
            ((AppointmentViewHolder) holder).clinicNameTv.setText(segmentedAppointmentItem.appointment.getClinicId());
            ((AppointmentViewHolder) holder).appointmentDateTv.setText(new SimpleDateFormat("MMM dd *yy", Locale.UK)
                    .format(AppUtils.dbStringToLocalDate(segmentedAppointmentItem.appointment.getStartDate())).replace("*", "'"));
            ((AppointmentViewHolder) holder).appointmentTimeTv.setText(AppUtils.dateToReadableTimeString(
                    AppUtils.dbStringToLocalDate(segmentedAppointmentItem.appointment.getStartDate())));

            ((AppointmentViewHolder) holder).popUpMenuImage.setOnClickListener(v -> {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mContext, ((AppointmentViewHolder) holder).popUpMenuImage);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {

                    //Starting reminder
                    Intent alarmIntent = new Intent(mContext, AlarmBroadcast.class);

                    alarmIntent.setAction("appointment_reminder");
                    alarmIntent.putExtra("appointment_reminder_id", segmentedAppointmentItem.appointment.get_id());

                    AlarmManager alarmManager = AlarmManagerSingleton.getInstance(mContext.getApplicationContext()).getAlarmManager();

                    //AppUtils.dbStringToLocalDate(currentAppointment.getStartDate()).getTime()-60*60*1000
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            mContext.getApplicationContext(), 0, alarmIntent,
                            PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            new Date().getTime() + 3000, //An hour behind
                            pendingIntent);
                    Log.d("Alarm", "Set");
                    return true;
                });

                popup.show(); //showing popup menu
            }); //closing the setOnClickListener method

        } else if (holder instanceof SegmentTitleViewHolder){
            ((SegmentTitleViewHolder) holder).titleText.setText(segmentedAppointmentItem.title);

            if (segmentedAppointmentItem.isCollapsibleSegment){

                //Work on drop down/up arrow for missed appointments
                if (segmentedAppointmentItem.title.equals(AppointmentStatus.MISSED.segmentName)) {

                    if (areMissedCollapsed) {
                        ((SegmentTitleViewHolder) holder).titleText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down), null);

                        ((SegmentTitleViewHolder) holder).titleText.setOnClickListener(v -> {
                            showMoreMissed(((SegmentTitleViewHolder) holder).titleText);
                        });
                    } else {
                        ((SegmentTitleViewHolder) holder).titleText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up), null);

                        ((SegmentTitleViewHolder) holder).titleText.setOnClickListener(v -> {
                            showLessMissed(((SegmentTitleViewHolder) holder).titleText);
                        });
                    }

                //Work on drop down/up arrow for past appointments
                } else {

                    if (arePastCollapsed) {
                        ((SegmentTitleViewHolder) holder).titleText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down), null);

                        ((SegmentTitleViewHolder) holder).titleText.setOnClickListener(v -> {
                            showMorePast(((SegmentTitleViewHolder) holder).titleText);
                        });
                    } else {
                        ((SegmentTitleViewHolder) holder).titleText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null, mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up), null);

                        ((SegmentTitleViewHolder) holder).titleText.setOnClickListener(v -> {
                            showLessPast(((SegmentTitleViewHolder) holder).titleText);
                        });
                    }
                }

            } else {
                ((SegmentTitleViewHolder) holder).titleText.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, null, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return segmentedAppointmentItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        SegmentedAppointmentItem item = segmentedAppointmentItems.get(position);

        //If the appointment value of the segmented appointment is not null, then we may have an appointment type or collapsed
        if (item.appointment != null){

            //Check if it's collapsed or not
            if (getStatus(item.appointment)==AppointmentStatus.MISSED && areMissedCollapsed)
                return TYPE_COLLAPSED;

            else if (getStatus(item.appointment)==AppointmentStatus.PAST && arePastCollapsed)
                return TYPE_COLLAPSED;

            else
                return TYPE_APPOINTMENT;

        } else {
            return TYPE_SEGMENT;
        }
    }


    void showMoreMissed(TextView view){
        if (isAnimating) return;
        isAnimating = true;
        new Handler().postDelayed(() -> isAnimating = false, 200);

        areMissedCollapsed = false;
        notifyMissedItemsChanged();
        view.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up), null);
    }

    void showLessMissed(TextView view){
        if (isAnimating) return;
        isAnimating = true;
        new Handler().postDelayed(() -> isAnimating = false, 200);

        areMissedCollapsed = true;
        notifyMissedItemsChanged();
        view.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down), null);
    }

    void showLessPast(TextView view){
        if (isAnimating) return;
        isAnimating = true;
        new Handler().postDelayed(() -> isAnimating = false, 200);

        arePastCollapsed = true;
        notifyPastItemsChanged();
        view.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up), null);
    }

    void showMorePast(TextView view) {
        if (isAnimating) return;
        isAnimating = true;
        new Handler().postDelayed(() -> isAnimating = false, 200);

        arePastCollapsed = false;
        notifyPastItemsChanged();
        view.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down), null);
    }

    private void notifyMissedItemsChanged(){
        for (int i=0; i<segmentedAppointmentItems.size(); ++i){

            if (segmentedAppointmentItems.get(i).appointment != null){
                if (getStatus(segmentedAppointmentItems.get(i).appointment) == AppointmentStatus.MISSED)
                    notifyItemChanged(i);
            } else {
                if (segmentedAppointmentItems.get(i).title.equals(AppointmentStatus.MISSED.segmentName))
                    notifyItemChanged(i);
            }
        }
    }

    private void notifyPastItemsChanged(){
        for (int i=0; i<segmentedAppointmentItems.size(); ++i){

            if (segmentedAppointmentItems.get(i).appointment != null){
                if (getStatus(segmentedAppointmentItems.get(i).appointment) == AppointmentStatus.PAST)
                    notifyItemChanged(i);
            } else {
                if (segmentedAppointmentItems.get(i).title.equals(AppointmentStatus.PAST.segmentName))
                    notifyItemChanged(i);
            }
        }
    }



    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView facilityNameTv, clinicNameTv, appointmentDateTv, appointmentTimeTv;
        ImageView popUpMenuImage;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            facilityNameTv = itemView.findViewById(R.id.facility_name_tv);
            clinicNameTv = itemView.findViewById(R.id.clinic_name_tv);
            appointmentDateTv = itemView.findViewById(R.id.appointment_date_tv);
            appointmentTimeTv = itemView.findViewById(R.id.appointment_time_tv);
            popUpMenuImage = itemView.findViewById(R.id.popup_menu_image);
        }

    }

    class SegmentTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text)
        TextView titleText;

        SegmentTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CollapsedViewHolder extends RecyclerView.ViewHolder {

        CollapsedViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class SegmentedAppointmentItem {

        private String title;
        private boolean isCollapsibleSegment;
        private Appointment appointment;

        public SegmentedAppointmentItem(String title, boolean isCollapsibleSegment, Appointment appointment) {
            this.title = title;
            this.isCollapsibleSegment = isCollapsibleSegment;
            this.appointment = appointment;
        }
    }


}
