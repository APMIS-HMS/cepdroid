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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private boolean isBeneficiaryInflated, isTransactionInflated = false;

    private Context context;
    private ArrayList<SegmentedObjects> segmentedObjectsList;
    private ArrayList<Beneficiaries> beneficiaries;
    private ArrayList<Transaction> transactions;

    private OnFundWalletClickedListener mListener;


    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    public FundAccountAdapter(Context context) {
        this.context = context;
        this.segmentedObjectsList = new ArrayList<>();
        this.beneficiaries = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    void setItems(ArrayList<Object> newItems) {
        boolean isEnteredTransaction = false;
        this.segmentedObjectsList = new ArrayList<>();
        this.beneficiaries = new ArrayList<>();
        this.transactions = new ArrayList<>();
        filterObjects(newItems);
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
        Log.e("Segmented items", String.valueOf(segmentedObjectsList));
        notifyDataSetChanged();
    }

    private void filterObjects (ArrayList<Object> items) {
    Log.e("items to filter", String.valueOf(items));
        for (Object x: items) {
            if (x instanceof Beneficiaries) {
                this.beneficiaries.add((Beneficiaries) x);
            } else {
                this.transactions.add((Transaction) x);
            }
        }
    }

    public void instantiateFundWalletClickedListener(OnFundWalletClickedListener listener) {
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
                view = inflater.inflate(R.layout.beneficiaries_header, parent, false);
                holder = new BeneficiariesHeaderViewHolder(view);
                return holder;
            case BENEFICIARIES:
                view = inflater.inflate(R.layout.beneficiaries, parent, false);
                holder = new BeneficiariesViewHolder(view);
                return holder;
            case TRANSACTION_HEADER:
                view = inflater.inflate(R.layout.transaction_header, parent, false);
                holder = new TransactionHistoryHeaderViewHolder(view);
                return holder;
            case TRANSACTION:
                view = inflater.inflate(R.layout.transaction_history, parent, false);
                holder = new TransactionHistoryViewHolder(view);
                return holder;
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {

        SegmentedObjects segmentedObjects = segmentedObjectsList.get(position);
        Log.e("segmentedObjects", String.valueOf(segmentedObjects.objectType));

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
        return ALL_HEADERS_WITH_EMPTY_VIEW;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == BENEFICIARIES) {
            if (!isBeneficiaryInflated)
                beneficiaryViewHolder((BeneficiariesViewHolder)holder);
                isBeneficiaryInflated = true;
        }

        if (holder.getItemViewType() == TRANSACTION) {
            if (!isTransactionInflated)
                transactionViewHolder((TransactionHistoryViewHolder)holder);
                isTransactionInflated = true;
        }

    }

    @Override
    public int getItemCount() {
        return segmentedObjectsList.size();
    }

    private void beneficiaryViewHolder (BeneficiariesViewHolder holder) {
        BeneficiariesAdapter beneficiariesAdapter = new BeneficiariesAdapter(context, beneficiaries);
        holder.beneficiaryRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.beneficiaryRecycler.setAdapter(beneficiariesAdapter);
    }

    private void transactionViewHolder (TransactionHistoryViewHolder holder) {
        TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(context, transactions);
        holder.transactionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.transactionRecycler.setAdapter(transactionHistoryAdapter);
        holder.transactionRecycler.setNestedScrollingEnabled(false);
    }


    class BeneficiariesHeaderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.fund_wallet)
        Button fundWalletButton;

        BeneficiariesHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fundWalletButton.setOnClickListener(v -> mListener.onFundWalletClicked());
        }
    }

    class TransactionHistoryHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.select_month)
        Spinner selectMonthSpinner;

        TransactionHistoryHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            selectMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    class BeneficiariesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.beneficiary_recycler)
        RecyclerView beneficiaryRecycler;

        public BeneficiariesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            beneficiaryRecycler.setVisibility(View.GONE);
        }

    }


    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_recycler)
        RecyclerView transactionRecycler;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
