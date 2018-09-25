package ng.apmis.apmismobile.ui.dashboard.prescription;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.prescriptionModel.PrescriptionItem;
import ng.apmis.apmismobile.utilities.AppUtils;

public class PrescriptionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PrescriptionItem> mPrescriptionItems;

    private final static int TYPE_PRESCRIPTION = 0;
    private final static int TYPE_SPACER = 1;

    private OnAddToCartListener mListener;


    public interface OnAddToCartListener {
        void onAddedToCart(PrescriptionItem prescriptionItem);
    }

    public PrescriptionAdapter(Context context, List<PrescriptionItem> prescriptionItems){
        this.mContext = context;
        this.mPrescriptionItems = new ArrayList<>(prescriptionItems);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mPrescriptionItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<PrescriptionItem> prescriptionItems) {
        this.mPrescriptionItems.addAll(prescriptionItems);
        notifyDataSetChanged();
    }

    public void initAddToCartListener(Fragment fragment){
        this.mListener = (OnAddToCartListener) fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view;

        switch (viewType){
            case TYPE_SPACER:
                view = inflater.inflate(R.layout.layout_spacer, parent, false);
                return new SpacerViewHolder(view);
            case TYPE_PRESCRIPTION:
                view = inflater.inflate(R.layout.layout_prescription_item, parent, false);
                return new PrescriptionViewHolder(view);
            default:
                view = inflater.inflate(R.layout.layout_prescription_item, parent, false);
                return new PrescriptionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PrescriptionItem prescriptionItem = mPrescriptionItems.get(position);

        if (holder instanceof PrescriptionViewHolder) {
            ((PrescriptionViewHolder) holder).genericNameText.setText(prescriptionItem.getGenericName());

            if (prescriptionItem.getPositionInPrescriptionList() == 0)
                ((PrescriptionViewHolder) holder).dateTextView.setVisibility(View.VISIBLE);
            else
                ((PrescriptionViewHolder) holder).dateTextView.setVisibility(View.GONE);

            ((PrescriptionViewHolder) holder).dateTextView.setText(AppUtils.dateToShortDateString(
                    AppUtils.dbStringToLocalDate(prescriptionItem.getPrescriptionDate())
            ));

            ((PrescriptionViewHolder) holder).instructionText.setText(prescriptionItem.getPatientInstruction());

            ((PrescriptionViewHolder) holder).dosageText.setText(prescriptionItem.getDosage());

            ((PrescriptionViewHolder) holder).frequencyText.setText(prescriptionItem.getFrequency());

            ((PrescriptionViewHolder) holder).prescriberText.setText(prescriptionItem.getPrescriberName());

            ((PrescriptionViewHolder) holder).addToCartButton.setOnClickListener(v -> {
                mListener.onAddedToCart(prescriptionItem);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPrescriptionItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        PrescriptionItem item = mPrescriptionItems.get(position);

        if (item.getPrescriptionListSize() == 0) {
            return TYPE_SPACER;
        } else {
            return TYPE_PRESCRIPTION;
        }
    }


    class PrescriptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.generic_name_text)
        TextView genericNameText;

        @BindView(R.id.date_text)
        TextView dateTextView;

        @BindView(R.id.instruction_text)
        TextView instructionText;

        @BindView(R.id.dosage_text)
        TextView dosageText;

        @BindView(R.id.frequency_text)
        TextView frequencyText;

        @BindView(R.id.prescriber_text)
        TextView prescriberText;

        @BindView(R.id.add_to_cart_button)
        Button addToCartButton;

        public PrescriptionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SpacerViewHolder extends RecyclerView.ViewHolder {

        public SpacerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
