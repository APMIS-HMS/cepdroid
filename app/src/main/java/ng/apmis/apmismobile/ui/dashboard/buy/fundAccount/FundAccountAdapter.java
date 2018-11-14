package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private final int FUND = 1;
    private ArrayList<Object> items;
    private Context context;
    private ArrayList<Beneficiaries> beneficiaries;
    private ArrayList<Transaction> transactions;

    private OnFundWalletClickedListener mListener;
    int wasNull = 0;

    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    public FundAccountAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.beneficiaries = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    void setItems (ArrayList<Object> newItems) {
        if (this.items.size() < 1) {
            Log.e("was null", String.valueOf(++wasNull));
            this.items = newItems;
            filterObjects();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return items.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (items.get(oldItemPosition) instanceof  Transaction)
                        return ((Transaction)items.get(oldItemPosition)).getId().equals(((Transaction)newItems.get(newItemPosition)).getId());
                    return ((Beneficiaries)items.get(oldItemPosition)).getName().equals(((Beneficiaries)newItems.get(newItemPosition)).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (items.get(oldItemPosition) instanceof Transaction)
                        return ((Transaction)items.get(oldItemPosition)).getId().equals(((Transaction)newItems.get(newItemPosition)).getId());
                    return ((Beneficiaries)items.get(oldItemPosition)).getName().equals(((Beneficiaries)newItems.get(newItemPosition)).getName());
                }
            });
            items = newItems;
            this.items.clear();
            this.items.addAll(newItems);
            result.dispatchUpdatesTo(this);
            filterObjects();
        }
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
            case BENEFICIARIES:
                view = inflater.inflate(R.layout.beneficiaries, parent, false);
                Button fundWallet = view.findViewById(R.id.fund_wallet);

                fundWallet.setOnClickListener((views) -> parent.getContext().startActivity(new Intent(view.getContext(), FundWalletActivity.class)));
                holder = new BeneficiariesViewHolder(view);
                break;
            case FUND:
                view = inflater.inflate(R.layout.transaction_history, parent, false);
                holder = new FundHistoryViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.transaction_history, parent, false);
                holder = new FundHistoryViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() > 0) {
            if (items.get(position) instanceof Beneficiaries)
                return BENEFICIARIES;
            if (items.get(position) instanceof Transaction)
                return FUND;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == BENEFICIARIES) {
            beneficiaryView((BeneficiariesViewHolder) holder);
        }
        if (holder.getItemViewType() == FUND) {
            fundHistoryView((FundHistoryViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private void beneficiaryView(BeneficiariesViewHolder holder) {
        BeneficiariesAdapter adapter1 = new BeneficiariesAdapter(beneficiaries);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter1);

        holder.fundWalletButton.setOnClickListener(v -> mListener.onFundWalletClicked());
    }

    private void fundHistoryView(FundHistoryViewHolder holder) {
        TransactionHistoryAdapter adapter1 = new TransactionHistoryAdapter(context, transactions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter1);
        holder.recyclerView.setNestedScrollingEnabled(false);
    }

    private void filterObjects() {

        for (Object x : items) {
            if (x instanceof Beneficiaries) {
                this.beneficiaries.add((Beneficiaries) x);
            } else {
                this.transactions.add((Transaction) x);
            }
        }
    }


    class BeneficiariesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fund_wallet)
        Button fundWalletButton;

        RecyclerView recyclerView;

        public BeneficiariesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }

    class FundHistoryViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public FundHistoryViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }


}
