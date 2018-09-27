package ng.apmis.apmismobile.ui.dashboard.documentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.documentationModel.Vitals;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Adapter class for handling the Vitals List in the Medical Records Details
 */
public class VitalsAdapter extends RecyclerView.Adapter<VitalsAdapter.VitalsViewHolder> {

    private Context mContext;
    private List<Vitals> vitals;

    public VitalsAdapter(Context context, List<Vitals> vitals){
        this.mContext = context;
        this.vitals = vitals;
    }


    @NonNull
    @Override
    public VitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_vitals_item, parent, false);

        return new VitalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VitalsViewHolder holder, int position) {

        Vitals vitalSign = vitals.get(position);

        holder.dateText.setText(String.format("Date %s", AppUtils.dateToReadableFullDateString(
                AppUtils.dbStringToLocalDate(vitalSign.getUpdatedAt())
                ))
        );

        holder.pulseRateValue.setText("Pulse Rate Value: "+vitalSign.getPulseRate().getPulseRateValue());
        holder.pulseLocation.setText("Location: "+vitalSign.getPulseRate().getLocation());
        holder.pulseRhythm.setText("Rhythm: "+vitalSign.getPulseRate().getRythm());


        holder.bodyHeight.setText("Height: "+vitalSign.getBodyMass().getHeight());
        holder.bodyWeight.setText("Weight: "+vitalSign.getBodyMass().getWeight());
        holder.bodyBMI.setText("BMI: "+vitalSign.getBodyMass().getBmi());


        holder.systolicPressure.setText("Systolic: "+vitalSign.getBloodPressure().getSystolic());
        holder.diastolicPressure.setText("Diastolic: "+vitalSign.getBloodPressure().getDiastolic());
        holder.locationPressure.setText("Location: "+vitalSign.getBloodPressure().getLocation());
        holder.pressurePosition.setText("Position: "+vitalSign.getBloodPressure().getPosition());

        holder.spo2.setText("SPO2: "+vitalSign.getAbdominalCondition().getSpo2());
        holder.girth.setText("Girth: "+vitalSign.getAbdominalCondition().getGirth());

        holder.respRate.setText("Respiratory rate: "+vitalSign.getRespiratoryRate());
        holder.temperature.setText("Temperature: "+vitalSign.getTemperature());

    }

    @Override
    public int getItemCount() {
        return vitals.size();
    }

    class VitalsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_text)
        TextView dateText;

        @BindView(R.id.pulse_rate_value)
        TextView pulseRateValue;
        @BindView(R.id.pulse_location)
        TextView pulseLocation;
        @BindView(R.id.pulse_rhythm)
        TextView pulseRhythm;

        @BindView(R.id.body_height)
        TextView bodyHeight;
        @BindView(R.id.body_weight)
        TextView bodyWeight;
        @BindView(R.id.body_bmi)
        TextView bodyBMI;

        @BindView(R.id.systolic_pressure)
        TextView systolicPressure;
        @BindView(R.id.diastolic_pressure)
        TextView diastolicPressure;
        @BindView(R.id.location_pressure)
        TextView locationPressure;
        @BindView(R.id.pressure_position)
        TextView pressurePosition;

        @BindView(R.id.spo2)
        TextView spo2;
        @BindView(R.id.girth)
        TextView girth;

        @BindView(R.id.resp_rate)
        TextView respRate;
        @BindView(R.id.temperature)
        TextView temperature;


        public VitalsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
