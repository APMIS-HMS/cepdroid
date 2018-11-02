package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.fundAccount.Fund;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries.BeneficiariesAdapter;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.fundHistory.FundHistoryAdapter;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class FundAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BENEFICIARIES = 0;
    private final int FUND = 1;
    private ArrayList<Object> items;
    private Context context;
    private ArrayList<Beneficiaries> beneficiaries;
    private ArrayList<Fund> funds;

    public FundAccountAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
        this.beneficiaries = new ArrayList<>();
        this.funds = new ArrayList<>();
        filterObjects();
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
                holder = new BeneficiariesViewHolder(view);
                break;
            case FUND:
                view = inflater.inflate(R.layout.fund_history, parent, false);
                holder = new FundHistoryViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.fund_history, parent, false);
                holder = new FundHistoryViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Beneficiaries)
            return BENEFICIARIES;
        if (items.get(position) instanceof Fund)
            return FUND;
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == BENEFICIARIES) {
            beneficiaryView((BeneficiariesViewHolder)holder);
        }
        if (holder.getItemViewType() == FUND) {
            fundHistoryView((FundHistoryViewHolder)holder);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private void beneficiaryView (BeneficiariesViewHolder holder) {
        BeneficiariesAdapter adapter1 = new BeneficiariesAdapter(beneficiaries);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter1);
    }

    private void fundHistoryView (FundHistoryViewHolder holder) {
        FundHistoryAdapter adapter1 = new FundHistoryAdapter(funds);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter1);
        holder.recyclerView.setNestedScrollingEnabled(false);
    }

    private void filterObjects () {

        for (Object x: items) {
            if (x instanceof Beneficiaries) {
                this.beneficiaries.add((Beneficiaries) x);
            } else {
                this.funds.add((Fund)x);
            }
        }
    }


    class BeneficiariesViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public BeneficiariesViewHolder(View itemView) {
            super(itemView);
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
