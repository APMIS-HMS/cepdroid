package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.personModel.Transaction;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries.BeneficiariesAdapter;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory.TransactionHistoryAdapter;
import ng.apmis.apmismobile.ui.dashboard.payment.FundWalletActivity;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class FundAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BENEFICIARIES = 0;
    private final int TRANSACTION = 1;
    private final int BENEFICIARIES_HEADER = 2;
    private final int TRANSACTION_HEADER = 3;
    private final int ALL_HEADERS_WITH_EMPTY_VIEW = 4;

    private Context context;
    private ArrayList<SegmentedObjects> segmentedObjectsList;

    private OnFundWalletClickedListener mListener;

    private TransactionHistoryAdapter transactionHistoryAdapter;


    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    public FundAccountAdapter(Context context) {
        this.context = context;
        this.segmentedObjectsList = new ArrayList<>();
        this.transactionHistoryAdapter = new TransactionHistoryAdapter(context);
    }

    void setItems(ArrayList<Object> newItems) {
        boolean isEnteredTransaction = false;
        this.segmentedObjectsList.clear();
        for (int i = 0; i < newItems.size(); i++) {
            if (i == 0) {
                segmentedObjectsList.add(new SegmentedObjects(Type.BEN_HEADER.typeName, null));
            }
            if (newItems.get(i) instanceof Beneficiaries) {
                segmentedObjectsList.add(new SegmentedObjects(Type.BEN.typeName, (Beneficiaries) newItems.get(i)));
            } else {
                //Once it gets here, the beneficiary items are exhausted
                if (!isEnteredTransaction) {
                    segmentedObjectsList.add((new SegmentedObjects(Type.TRX_HEADER.typeName, null)));
                    isEnteredTransaction = true;
                }
            }
            if (newItems.get(i) instanceof Transaction) {
                segmentedObjectsList.add(new SegmentedObjects(Type.TRX.typeName, (Transaction) newItems.get(i)));
            }
        }
        notifyDataSetChanged();
    }

    public void instantiateWalletFundListener(OnFundWalletClickedListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case BENEFICIARIES_HEADER:
                view = inflater.inflate(R.layout.beneficiaries, parent, false);/*
                Button fundWallet = view.findViewById(R.id.fund_wallet);

                fundWallet.setOnClickListener((views) -> parent.getContext().startActivity(new Intent(view.getContext(), FundWalletActivity.class)));*/
                holder = new BeneficiariesHeaderViewHolder(view);
                return holder;
            case BENEFICIARIES:
                view = inflater.inflate(R.layout.beneficiaries_item, parent, false);
                holder = new BeneficiariesViewHolder(view);
                return holder;
            case TRANSACTION_HEADER:
                view = inflater.inflate(R.layout.transaction_history, parent, false);
                holder = new TransactionHistoryHeaderViewHolder(view);
                return holder;
            case TRANSACTION:
                view = inflater.inflate(R.layout.transaction_history_item, parent, false);
                holder = new TransactionHistoryViewHolder(view);
                return holder;
            default:
                view = inflater.inflate(R.layout.transaction_history, parent, false);
                holder = null;
                return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {

        SegmentedObjects segmentedObjects = segmentedObjectsList.get(position);
        Log.e("segmentedObjects", String.valueOf(segmentedObjects.objectType));

        if (segmentedObjects != null) {

            if (segmentedObjects.objectType.equals(Type.BEN_HEADER.typeName)) {
                return BENEFICIARIES_HEADER;
            }
            if (segmentedObjects.objectType.equals(Type.BEN.typeName)) {
                return BENEFICIARIES;
            }
            if (segmentedObjects.objectType.equals(Type.TRX_HEADER.typeName)) {
                return TRANSACTION_HEADER;
            }
            if (segmentedObjects.objectType.equals(Type.TRX.typeName)) {
                return TRANSACTION;
            }

        }
        return ALL_HEADERS_WITH_EMPTY_VIEW;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == BENEFICIARIES_HEADER) {
            beneficiaryHeaderView((BeneficiariesHeaderViewHolder) holder);
        }

        if (holder.getItemViewType() == TRANSACTION_HEADER) {
            transactionHeaderView((TransactionHistoryHeaderViewHolder) holder);
        }

        if (holder.getItemViewType() == BENEFICIARIES) {
            /*Beneficiaries beneficiary = (Beneficiaries) segmentedObjectsList.get(position).object;
            beneficiariesAdapter.setBeneficiaryItem(beneficiary);*/
        }

        if (holder.getItemViewType() == TRANSACTION) {
           /* Transaction transaction = (Transaction) segmentedObjectsList.get(position).object;
            transactionHistoryAdapter.setTransactionList(transaction);*/
        }

        if (holder.getItemViewType() == ALL_HEADERS_WITH_EMPTY_VIEW) {

        }
    }

    @Override
    public int getItemCount() {
        return segmentedObjectsList.size();
    }

    private void beneficiaryHeaderView(BeneficiariesHeaderViewHolder holder) {
        BeneficiariesAdapter beneficiariesAdapter = new BeneficiariesAdapter(context);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(beneficiariesAdapter);
        holder.fundWalletButton.setOnClickListener(v -> mListener.onFundWalletClicked());
    }

    private void transactionHeaderView(TransactionHistoryHeaderViewHolder holder) {
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setAdapter(transactionHistoryAdapter);
        holder.recyclerView.setNestedScrollingEnabled(false);
    }


    class BeneficiariesHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fund_wallet)
        Button fundWalletButton;

        RecyclerView recyclerView;
        public BeneficiariesHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }


    class BeneficiariesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView imageView;
        TextView userName;
        Button fundButton;

        public BeneficiariesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.beneficiary_image);
            userName = itemView.findViewById(R.id.beneficiary_name);
            fundButton = itemView.findViewById(R.id.beneficiary_fund);
            fundButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(new Intent(v.getContext(), FundWalletActivity.class));
        }
    }

    class TransactionHistoryHeaderViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        public TransactionHistoryHeaderViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }

    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {


        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);
        }
    }

    class SegmentedObjects {
        private String objectType;
        private Object object;

        SegmentedObjects(String type, Object object) {
            this.objectType = type;
            this.object = object;
        }

    }

    private enum Type {
        BEN("beneficiary"), TRX("transaction"), BEN_HEADER("ben-header"), TRX_HEADER("trx-header");

        private String typeName;

        Type(String type) {
            this.typeName = type;
        }

    }


}
