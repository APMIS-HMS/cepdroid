package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.support.annotation.NonNull;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.personModel.Transaction;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries.BeneficiariesAdapter;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory.TransactionHistoryAdapter;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class FundAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BENEFICIARIES_HEADER = 0;
    private final int TRANSACTION_HEADER = 1;
    private final int BENEFICIARIES = 2;
    private final int TRANSACTION = 3;

    private final static long[] TIME_PERIODS_IN_DAYS = {1, 7, 30, 92, 184, 365};

    private Context context;
    private ArrayList<Beneficiaries> beneficiaries;
    private ArrayList<Transaction> transactions;
    private ArrayList<Transaction> segmentedTransactions;

    boolean shouldDisplayActionViews;

    private OnFundWalletClickedListener mListener;


    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    public FundAccountAdapter(Context context) {
        this.context = context;
    }

    void setTransactionItems (List<Transaction> trxList) {
        if (trxList == null){
            trxList = new ArrayList<>();
            //If the wallet failed to load, prevent actions like funding
            shouldDisplayActionViews = false;
        } else {
            shouldDisplayActionViews = true;
        }

        transactions = new ArrayList<>(trxList);
        segmentedTransactions = new ArrayList<>(truncateTransactionsWithDays(TIME_PERIODS_IN_DAYS[0], transactions));
        notifyDataSetChanged();
    }

    void setBeneficiaryItems (ArrayList<Beneficiaries> benList) {
        this.beneficiaries = new ArrayList<>(benList);
        notifyItemChanged(1);
    }

    void instantiateFundWalletClickedListener(OnFundWalletClickedListener listener) {
        this.mListener = listener;
    }

    private List<Transaction> truncateTransactionsWithDays(long days, List<Transaction> transactions){
        List<Transaction> truncatedTransactions = new ArrayList<>();

        int i = 0;
        Date today = new Date();
        Date nDaysAgo = new Date(today.getTime() - (days * 1000L * 60L * 60L * 24L));

        while (i < transactions.size()){
            if (AppUtils.dbStringToLocalDate(transactions.get(i).getCreatedAt()).after(nDaysAgo))
                truncatedTransactions.add(transactions.get(i));
            ++i;
        }

        Log.e("Buy", truncatedTransactions.size()+" is size");

        return truncatedTransactions;
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
        if (holder.getItemViewType() == BENEFICIARIES_HEADER){
            if (transactions != null && shouldDisplayActionViews) {
                ((BeneficiariesHeaderViewHolder)holder).fundWalletButton.setVisibility(View.VISIBLE);
            } else {
                ((BeneficiariesHeaderViewHolder)holder).fundWalletButton.setVisibility(View.INVISIBLE);
            }
        }

        else if (holder.getItemViewType() == TRANSACTION_HEADER){
            if (transactions != null && shouldDisplayActionViews) {
                ((TransactionHistoryHeaderViewHolder)holder).selectTimePeriodSpinner.setVisibility(View.VISIBLE);
            } else {
                ((TransactionHistoryHeaderViewHolder)holder).selectTimePeriodSpinner.setVisibility(View.INVISIBLE);
            }
        }

        else if (holder.getItemViewType() == BENEFICIARIES) {
            beneficiaryViewHolder((BeneficiariesViewHolder) holder);
        }

        else if (holder.getItemViewType() == TRANSACTION) {
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
        if (transactions == null){
            holder.transactionHistoryShimmer.startShimmer();
            return;
        }

        holder.transactionHistoryShimmer.stopShimmer();
        holder.transactionHistoryShimmer.setVisibility(View.GONE);

        if (segmentedTransactions.isEmpty()) {
            holder.emptyView.setVisibility(View.VISIBLE);
            holder.transactionRecycler.setVisibility(View.GONE);

        } else {
            holder.emptyView.setVisibility(View.GONE);
            holder.transactionRecycler.setVisibility(View.VISIBLE);

            //descending order of dates, from newest to oldest
            Collections.sort(segmentedTransactions, (o1, o2) -> o2.compareTo(o1));

            TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(context, segmentedTransactions);
            holder.transactionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.transactionRecycler.setAdapter(transactionHistoryAdapter);
            transactionHistoryAdapter.notifyDataSetChanged();
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

        @BindView(R.id.select_time_period)
        Spinner selectTimePeriodSpinner;

        TransactionHistoryHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (transactions != null && shouldDisplayActionViews) {
                selectTimePeriodSpinner.setVisibility(View.VISIBLE);
            } else {
                selectTimePeriodSpinner.setVisibility(View.INVISIBLE);
            }

            selectTimePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (transactions != null) {
                        long days = TIME_PERIODS_IN_DAYS[position];
                        segmentedTransactions.clear();
                        segmentedTransactions.addAll(new ArrayList<>(truncateTransactionsWithDays(days, transactions)));
                    }
                    notifyItemChanged(3);
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
        RelativeLayout emptyView;

        @BindView(R.id.history_shimmer)
        ShimmerFrameLayout transactionHistoryShimmer;

        TransactionHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
