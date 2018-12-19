package ng.apmis.apmismobile.ui.dashboard.find.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;

public class SearchTermsRowAdapter extends RecyclerView.Adapter<SearchTermsRowAdapter.SearchTermRowViewHolder>
        implements SearchTermsItemAdapter.OnPreviousItemSelectedListener {

    private Context context;
    private List<String> searchTermsRows;
    private List<Appointment> appointments = null;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public interface OnPreviousRowItemSelectedListener{
        void onPreviousRowItemSelected(String id, String name, String type);
    }

    public interface OnViewAllTermsClickedListener{
        void onViewAllTermsClicked(String searchTerm);
    }

    private OnPreviousRowItemSelectedListener previousRowItemSelectedListener;
    private OnViewAllTermsClickedListener viewAllListener;

    public SearchTermsRowAdapter(Context context, List<String> searchTermsRows){
        this.context = context;
        this.searchTermsRows = searchTermsRows;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        notifyDataSetChanged();
    }

    /**
     * Add all search rows from a List to this adapter
     * @param rows The List of rows to add from
     */
    public void addAll(List<String> rows) {
        this.searchTermsRows.addAll(rows);
        notifyDataSetChanged();
    }

    public void notifySubLists(List<Appointment> appointments){
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchTermRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_search_term_row, parent, false);

        return new SearchTermRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTermRowViewHolder holder, int position) {
        String searchTerm = searchTermsRows.get(position);

        List<SearchTermItem> previousItems = new ArrayList<>();

        //Start shimmer if adapter if sublists have values
        holder.previousVisitShimmer.startShimmer();

        if (appointments != null){
            holder.previousVisitShimmer.stopShimmer();
            holder.previousVisitShimmer.setVisibility(View.GONE);
            previousItems = generatePreviousItemTypesFromAppointments(searchTerm);

            if (previousItems.size() == 0) {
                holder.emptyView.setVisibility(View.VISIBLE);
                holder.emptyView.setText("Click here to find a "+searchTerm);
                holder.viewAll.setVisibility(View.GONE);
            } else {
                holder.emptyView.setVisibility(View.GONE);
                holder.viewAll.setVisibility(View.VISIBLE);
            }

            holder.emptyView.setOnClickListener(v -> viewAllListener.onViewAllTermsClicked(searchTerm));
            holder.viewAll.setOnClickListener(v -> viewAllListener.onViewAllTermsClicked(searchTerm));
        }

        holder.titleTermText.setText(searchTerm);


        SearchTermsItemAdapter adapter = new SearchTermsItemAdapter(context, previousItems);
        adapter.instantiatePreviousItemSelectedListener(this);
        holder.previousVisitRecycler.setHasFixedSize(true);
        holder.previousVisitRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.previousVisitRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        holder.previousVisitRecycler.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return searchTermsRows.size();
    }

    private List<SearchTermItem> generatePreviousItemTypesFromAppointments(String type){

        List<SearchTermItem> items = new ArrayList<>();

        for (Appointment appointment : appointments){
            SearchTermItem item = null;
            switch (type){
                case "Hospital":
                    String hospitalId = appointment.getFacilityId();
                    String name = appointment.getPatientDetails().getFacilityObj().getName();
                    String clinic = appointment.getClinicId();
                    String imageLogoUrl = appointment.getPatientDetails().getFacilityObj().getLogoObject().getPath();
                    item = new SearchTermItem(imageLogoUrl, hospitalId, name, clinic, "Hospital");
                    break;
                case "Doctor":
                    try {
                        String doctorId = appointment.getDoctorId();
                        String doctorName = appointment.getProviderDetails().getPersonDetails().getFirstName() +
                                " " + appointment.getProviderDetails().getPersonDetails().getLastName();
                        String department = appointment.getProviderDetails().getDepartmentId();
                        String doctorImageUrl = appointment.getProviderDetails().getPersonDetails().getProfileImageUriPath();
                        item = new SearchTermItem(doctorImageUrl, doctorId, doctorName, department, "Doctor");
                    } catch (Exception ignored){}
                    break;
                default:
                    break;
            }

            if (item != null)
                items.add(item);
        }

        return removeDuplicates(items);
    }

    private List<SearchTermItem> removeDuplicates(List<SearchTermItem> items){
        List<SearchTermItem> nonDuplicates = new ArrayList<>();

        for (SearchTermItem item : items) {
            if (!hasItem(nonDuplicates, item))
                nonDuplicates.add(item);
        }

        return nonDuplicates;
    }

    private boolean hasItem(List<SearchTermItem> listToCheck, SearchTermItem item){

        for (SearchTermItem searchTermItem: listToCheck){
            if (searchTermItem.toString().equals(item.toString()))
                return true;
        }

        return false;
    }

    public void instantiateOnViewAllClickedListener(OnViewAllTermsClickedListener listener){
        this.viewAllListener = listener;
    }

    public void instantiateOnPreviousRowItemSelectedListener(OnPreviousRowItemSelectedListener listener){
        this.previousRowItemSelectedListener = listener;
    }

    class SearchTermRowViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text)
        TextView titleTermText;

        @BindView(R.id.previous_visit_recycler)
        RecyclerView previousVisitRecycler;

        @BindView(R.id.previous_visit_shimmer)
        ShimmerFrameLayout previousVisitShimmer;

        @BindView(R.id.empty_view)
        TextView emptyView;

        @BindView(R.id.view_all_text)
        TextView viewAll;

        public SearchTermRowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onPreviousItemSelected(String itemId, String itemName, String itemType) {
        previousRowItemSelectedListener.onPreviousRowItemSelected(itemId, itemName, itemType);
    }

}
