package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

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

    private final int BENEFICIARIES = 2;
    private final int TRANSACTION = 3;
    private final int BENEFICIARIES_HEADER = 0;
    private final int TRANSACTION_HEADER = 1;

    private Context context;
    private ArrayList<Beneficiaries> beneficiaries;
    private ArrayList<Transaction> transactions;

    private OnFundWalletClickedListener mListener;


    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    public FundAccountAdapter(Context context) {
        this.context = context;
        this.beneficiaries = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    void setTransactionItems (List<Transaction> trxList) {
        if (!this.transactions.isEmpty()) {
            this.transactions = new ArrayList<>();
        }
        this.transactions.addAll(trxList);
        notifyDataSetChanged();
    }

    void setBeneficiaryItems (ArrayList<Beneficiaries> benList) {

        if (!this.beneficiaries.isEmpty()) {
            this.beneficiaries = new ArrayList<>();
        }
        this.beneficiaries.addAll(benList);
        notifyDataSetChanged();
    }

    void instantiateFundWalletClickedListener(OnFundWalletClickedListener listener) {
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

        if (position == 2) {
            return TRANSACTION_HEADER;
        }
        if (position == 1) {
            return BENEFICIARIES;
        }
        if (position == 3) {
            return TRANSACTION;
        }
        return BENEFICIARIES_HEADER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == BENEFICIARIES) {
            beneficiaryViewHolder((BeneficiariesViewHolder) holder);
        }

        if (holder.getItemViewType() == TRANSACTION) {
            transactionViewHolder((TransactionHistoryViewHolder) holder);
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private void beneficiaryViewHolder(BeneficiariesViewHolder holder) {
        BeneficiariesAdapter beneficiariesAdapter = new BeneficiariesAdapter(context, beneficiaries);
        holder.beneficiaryRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.beneficiaryRecycler.setAdapter(beneficiariesAdapter);
    }

    private void transactionViewHolder(TransactionHistoryViewHolder holder) {
        if (transactions.isEmpty()) {
            holder.emptyView.setVisibility(View.VISIBLE);
            holder.transactionRecycler.setVisibility(View.GONE);
        } else {
            TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(context, transactions);
            holder.transactionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.transactionRecycler.setAdapter(transactionHistoryAdapter);
            holder.transactionRecycler.setNestedScrollingEnabled(false);
        }
    }


    class BeneficiariesHeaderViewHolder extends RecyclerView.ViewHolder {

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

        BeneficiariesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_recycler)
        RecyclerView transactionRecycler;
        @BindView(R.id.empty_view)
        CardView emptyView;

        TransactionHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
