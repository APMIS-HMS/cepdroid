package ng.apmis.apmismobile.ui.dashboard.documentation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.Constants;

/**
 * Adapter responsible for the Data in the medical records list
 */
public class RecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String BASE_URL = Constants.BASE_URL;
    //The View Type Constant for the Date Header
    private static final int TYPE_DATE = 0;
    //The View Type Constant for the Medical Record
    private static final int TYPE_RECORD = 1;

    private Context mContext;
    private List<DatedDocumentationItem> datedDocumentationItems;
    private OnRecordClickedListener mListener;
    private ProgressDialog progressDialog;


    public RecordsAdapter(Context context, List<DatedDocumentationItem> documentations){
        this.mContext = context;
        this.datedDocumentationItems = documentations;
        notifyDataSetChanged();
    }

    public void initiateCallbackListener(Fragment fragment){
        this.mListener = (OnRecordClickedListener) fragment;
    }

    public void addAll(List<DatedDocumentationItem> documentationItems){
        this.datedDocumentationItems.addAll(documentationItems);
        notifyDataSetChanged();
    }

    public void clear(){
        this.datedDocumentationItems.clear();
        notifyDataSetChanged();
    }


    /**
     * Request password on record click to ensure privacy of records
     * @param documentation The record to open
     */
    private void onRecordClick(Documentation documentation){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogMinWidth);
        AlertDialog dialog = builder.create();
        View authorizeLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_apmis_authorize, null, false);
        dialog.setView(authorizeLayout);

        EditText passwordEdit = authorizeLayout.findViewById(R.id.password_edit_text);


        authorizeLayout.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());
        authorizeLayout.findViewById(R.id.authorize_button).setOnClickListener(v -> {
            String password = passwordEdit.getText().toString();
            confirmPasswordAndAccess(password, dialog, documentation);
        });
        dialog.show();

    }

    /**
     * Send a request to the server to authenticate the current user with a password
     * @param password The typed in password
     * @param alertDialog AlertDialog object
     * @param documentation The record to open
     */
    private void confirmPasswordAndAccess(String password, AlertDialog alertDialog, Documentation documentation){
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
                mListener.onRecordClicked(documentation);

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;

        switch (viewType){
            case TYPE_DATE:
                view = inflater.inflate(R.layout.date_header_view, parent, false);
                return new DateViewHolder(view);
            case TYPE_RECORD:
                view = inflater.inflate(R.layout.documentation_list_item, parent, false);
                return new RecordsViewHolder(view);
            default:
                view = inflater.inflate(R.layout.documentation_list_item, parent, false);
                return new RecordsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DateViewHolder){

            ((DateViewHolder) holder).dateTextView.setText(datedDocumentationItems.get(position).getDate());

        } else if (holder instanceof RecordsViewHolder){

            Documentation documentation = datedDocumentationItems.get(position).getDocumentation();

            ((RecordsViewHolder) holder).facilityTextView.setText(documentation.getFacilityName());
            ((RecordsViewHolder) holder).recordTitleTextView.setText(documentation.getDocument().getDocumentType().getTitle());
            ((RecordsViewHolder) holder).doctorTextView.setText(documentation.getCreatedBy());
            ((RecordsViewHolder) holder).dateTextView.setText(
                    AppUtils.dateToShortDateString(AppUtils.dbStringToLocalDate(documentation.getUpdatedAt()))
            );


            ((RecordsViewHolder) holder).mainRecordView.setOnClickListener(v -> {
                onRecordClick(datedDocumentationItems.get(position).documentation);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        DatedDocumentationItem item = datedDocumentationItems.get(position);

        //If the date string in a datedDocumentationItem is not null, then we have a date type
        if (item.date != null){
            return TYPE_DATE;
        } else {
            return TYPE_RECORD;
        }
    }

    @Override
    public int getItemCount() {
        return datedDocumentationItems.size();
    }

    public interface OnRecordClickedListener {
        void onRecordClicked(Documentation documentation);
    }

    class RecordsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_record_view)
        CardView mainRecordView;

        @BindView(R.id.facility_name)
        TextView facilityTextView;

        @BindView(R.id.record_title)
        TextView recordTitleTextView;

        @BindView(R.id.date_text)
        TextView dateTextView;

        @BindView(R.id.doctor_name)
        TextView doctorTextView;

        public RecordsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_text)
        TextView dateTextView;

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * This DatedDocumentationItem class is simply an encapsulation of a DateString and
     * a Documentation Medical Record
     */
    public static class DatedDocumentationItem {

        private String date;
        private Documentation documentation;

        public DatedDocumentationItem(String date, Documentation documentation){
            this.date = date;
            this.documentation = documentation;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Documentation getDocumentation() {
            return documentation;
        }

        public void setDocumentation(Documentation documentation) {
            this.documentation = documentation;
        }


    }
}
