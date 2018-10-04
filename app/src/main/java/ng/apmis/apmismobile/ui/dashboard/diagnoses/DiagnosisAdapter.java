package ng.apmis.apmismobile.ui.dashboard.diagnoses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.diagnosesModel.InvestigationBody;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.Constants;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {

    private Context mContext;
    private List<InvestigationBody> investigations;

    private static final String BASE_URL = Constants.BASE_URL;
    private ProgressDialog progressDialog;


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

    public OnViewReportClickedListener mListener;

    public interface OnViewReportClickedListener {
        void onViewReportClicked(Intent i);
    }

    public void instantiateViewReportListener(OnViewReportClickedListener listener){
        this.mListener = listener;
    }

    /**
     * Request password on record click to ensure privacy of records
     * @param intent The report to open
     */
    private void onViewReportClick(Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogMinWidth);
        AlertDialog dialog = builder.create();
        View authorizeLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_apmis_authorize, null, false);
        dialog.setView(authorizeLayout);

        EditText passwordEdit = authorizeLayout.findViewById(R.id.password_edit_text);


        authorizeLayout.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());
        authorizeLayout.findViewById(R.id.authorize_button).setOnClickListener(v -> {
            String password = passwordEdit.getText().toString();
            confirmPasswordAndAccess(password, dialog, intent);
        });
        dialog.show();
    }

    /**
     * Send a request to the server to authenticate the current user with a password
     * @param password The typed in password
     * @param alertDialog AlertDialog object
     * @param intent The report to open
     */
    private void confirmPasswordAndAccess(String password, AlertDialog alertDialog, Intent intent){
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        APMISAPP.getInstance().networkIO().execute(() -> {

            JSONObject job = new JSONObject();
            try {
                job.put("email", new SharedPreferencesManager(mContext).getStoredApmisId());
                job.put("password", password);
                job.put("strategy", "local");
                Log.v("Person to Json", String.valueOf(job));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {
                progressDialog.dismiss();
                alertDialog.dismiss();
                mListener.onViewReportClicked(intent);

            }, error -> {
                Log.d("error", String.valueOf(error.getMessage()) + "Error");
                progressDialog.dismiss();
                new android.support.v7.app.AlertDialog.Builder(mContext)
                        .setTitle("Authentication Failed")
                        .setMessage("Please try again !!!")
                        .setPositiveButton("Dismiss", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });

            queue.add(loginRequest);
        });
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

        holder.viewReportButton.setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("conclusion", body.getReport().getConclusion());
            i.putExtra("recommendation", body.getReport().getRecommendation());
            i.putExtra("clinicalInformation", body.getLabRequestClinicalInformation());
            i.putExtra("outcome", body.getReport().getOutcome());
            try {
                i.putExtra("result", body.getReport().getResults().get(0).getResult());
            } catch (Exception e){

            }
            i.putExtra("diagnosis", body.getLabRequestDiagnosis());
            i.putExtra("labNumber", body.getLabRequestNumber());
            i.putExtra("investigation", body.getInvestigation().getName());

            onViewReportClick(i);
        });


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


        DiagnosisViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
