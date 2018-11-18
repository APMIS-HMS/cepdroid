package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.personModel.Transaction;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries.BeneficiariesAdapter;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory.TransactionHistoryAdapter;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class FundAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BENEFICIARY_ITEM = 1;
    private final int TRANSACTION_ITEM = 2;
    private final int SEGMENT = -1;

    private ArrayList<Object> items;
    private Context context;
    int entries = 0;
    boolean firstTrx = true;

    private OnFundWalletClickedListener mListener;

    BeneficiariesAdapter beneficiariesAdapter = new BeneficiariesAdapter();
    TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter();


    public interface OnFundWalletClickedListener {
        void onFundWalletClicked();
    }

    FundAccountAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    void setItems(ArrayList<Object> newItems) {
        if (this.items.size() < 1) {
            Log.e("entries", String.valueOf(++entries));
            items = newItems;
            notifyDataSetChanged();
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
            case BENEFICIARY_ITEM:
                view = inflater.inflate(R.layout.beneficiaries_item, parent, false);
                holder = (RecyclerView.ViewHolder) view;
            case TRANSACTION_ITEM:
                view = inflater.inflate(R.layout.transaction_history_item, parent, false);
                break;
            default:
                //Return both segment headers with empty states
                holder = null;
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {

        //probable add null as first item
        if (items.get(position) instanceof Beneficiaries) {
            return BENEFICIARY_ITEM;
        }
        if (items.get(position) instanceof Transaction) {
            return TRANSACTION_ITEM;
        }

        return SEGMENT;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == BENEFICIARY_ITEM) {
            beneficiaryParentView(holder);
        }

        if (holder.getItemViewType() == TRANSACTION_ITEM) {
            //transactionHistoryParentView(holder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
/*
    private void beneficiaryParentView(RecyclerView.ViewHolder holder) {
        BeneficiariesViewHolder benHolder = (BeneficiariesViewHolder) holder;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        benHolder.recyclerView.setLayoutManager(layoutManager);
        benHolder.recyclerView.setAdapter(beneficiariesAdapter);
    }

    private void transactionHistoryParentView(RecyclerView.ViewHolder holder) {
        TransactionViewHolder trxHolder = (TransactionViewHolder) holder;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        trxHolder.recyclerView.setLayoutManager(layoutManager);
        trxHolder.recyclerView.setAdapter(transactionHistoryAdapter);
        //trxHolder.recyclerView.setNestedScrollingEnabled(false);
    }*/

    class BeneficiariesItemViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;


        BeneficiariesItemViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        //Todo this happens only once

        TransactionViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }


}
