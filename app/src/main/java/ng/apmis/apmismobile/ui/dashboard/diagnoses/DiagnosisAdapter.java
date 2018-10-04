package ng.apmis.apmismobile.ui.dashboard.diagnoses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.diagnosesModel.InvestigationBody;
import ng.apmis.apmismobile.utilities.AppUtils;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {

    private Context mContext;
    private List<InvestigationBody> investigations;

    public DiagnosisAdapter(Context context, List<InvestigationBody> investigationBodies){
        this.mContext = context;
        this.investigations = new ArrayList<>(investigationBodies);
        notifyDataSetChanged();
    }

    public void clear() {
        this.investigations.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<InvestigationBody> investigations) {
        this.investigations.addAll(investigations);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiagnosisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_investigation_item, parent, false);

        return new DiagnosisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisViewHolder holder, int position) {

        InvestigationBody body = investigations.get(position);

        holder.requestingPhysicianTextView.setText(
                String.format("%s %s", body.getLabRequestEmployee().getFirstName(),
                        body.getLabRequestEmployee().getLastName()));

        if (!body.getIsUploaded()){
            holder.viewReportButton.setVisibility(View.GONE);
        } else {
            holder.viewReportButton.setVisibility(View.VISIBLE);
        }

        holder.dateTextView.setText(AppUtils.dateToShortDateString(AppUtils.dbStringToLocalDate(body.getLabRequestDate())));
        holder.investigationTextView.setText(body.getInvestigation().getName());


        if (body.getIsUploaded()){
            holder.statusTextView.setText("Ready");
            holder.statusTitleTextView.getCompoundDrawables()[2].clearColorFilter();
            holder.statusTitleTextView.getCompoundDrawables()[2].setColorFilter(mContext.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.OVERLAY);
        } else if (body.getSpecimenReceived()) {
            holder.statusTextView.setText("Specimen Received");
            holder.statusTitleTextView.getCompoundDrawables()[2].clearColorFilter();
            holder.statusTitleTextView.getCompoundDrawables()[2].setColorFilter(Color.GREEN, PorterDuff.Mode.OVERLAY);
        } else if (body.getSampleTaken()){
            holder.statusTextView.setText("Sample Taken");
            holder.statusTitleTextView.getCompoundDrawables()[2].clearColorFilter();
            holder.statusTitleTextView.getCompoundDrawables()[2].setColorFilter(Color.YELLOW, PorterDuff.Mode.OVERLAY);
        } else {
            holder.statusTextView.setText("Sample Not Taken");
            holder.statusTitleTextView.getCompoundDrawables()[2].clearColorFilter();
            holder.statusTitleTextView.getCompoundDrawables()[2].setColorFilter(Color.RED, PorterDuff.Mode.OVERLAY);
        }



        if (body.getSpecimenReceived()){
            holder.sampleTextView.setVisibility(View.VISIBLE);
            holder.dividerLine.setVisibility(View.VISIBLE);
            holder.sampleImageView.setVisibility(View.VISIBLE);

            holder.sampleTextView.setText(String.format("%s (%s)", body.getInvestigation().getSpecimen(), body.getSpecimenNumber()));

        } else {
            holder.sampleTextView.setVisibility(View.GONE);
            holder.dividerLine.setVisibility(View.GONE);
            holder.sampleImageView.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return investigations.size();
    }

    class DiagnosisViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.requesting_physician_text)
        TextView requestingPhysicianTextView;

        @BindView(R.id.view_report_button)
        Button viewReportButton;

        @BindView(R.id.date_text)
        TextView dateTextView;

        @BindView(R.id.investigation_text)
        TextView investigationTextView;

        @BindView(R.id.status_title)
        TextView statusTitleTextView;

        @BindView(R.id.status_text)
        TextView statusTextView;

        @BindView(R.id.sample_text)
        TextView sampleTextView;

        @BindView(R.id.divider_line_2)
        View dividerLine;

        @BindView(R.id.sample_image)
        ImageView sampleImageView;


        public DiagnosisViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
