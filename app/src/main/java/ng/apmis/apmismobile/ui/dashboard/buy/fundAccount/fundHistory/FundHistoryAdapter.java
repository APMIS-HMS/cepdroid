package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.fundHistory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Fund;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class FundHistoryAdapter extends RecyclerView.Adapter<FundHistoryAdapter.MyViewHolder> {

    ArrayList<Fund> fundList = new ArrayList<>();

    public FundHistoryAdapter(ArrayList<Fund> fundList) {
        this.fundList = fundList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fund_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundHistoryAdapter.MyViewHolder holder, int position) {
        Fund fund = fundList.get(position);

        holder.name.setText(fund.getBeneficiaryName());
        holder.title.setText(fund.getFundTitle());
        holder.amount.setText(fund.getAmount());
        holder.date.setText(fund.getDate());
    }

    @Override
    public int getItemCount() {
        return fundList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, title, amount, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.fund_title);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }
    }
}
