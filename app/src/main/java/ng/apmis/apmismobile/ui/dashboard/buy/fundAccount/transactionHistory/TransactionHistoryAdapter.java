package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.Transaction;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder> {

    private ArrayList<Transaction> transactionList = new ArrayList<>();
    private Context context;

    public TransactionHistoryAdapter() {
        //TODO on instantiation, the first item {add button} is inflated by viewtype
        this.transactionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.title.setText(transaction.getDescription());
        holder.amount.setTextColor(transaction.getTransactionType().equalsIgnoreCase("cr") ? ContextCompat.getColor(context, android.R.color.holo_green_light) : ContextCompat.getColor(context, android.R.color.holo_red_light));
        holder.amount.setText(context.getString(R.string.trx_amount_placeholder, String.valueOf(transaction.getAmount())));
        holder.date.setText(AppUtils.dateToReadableFullDateString(AppUtils.dbStringToLocalDate(transaction.getCreatedAt())));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, title, amount, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.transaction_title);
            amount = itemView.findViewById(R.id.transaction_amount);
            date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View v) {
            Transaction trx = transactionList.get(getAdapterPosition());

            View view = LayoutInflater.from(context).inflate(R.layout.transaction_detail, null);

            TextView paidBy = view.findViewById(R.id.paid_by);
            InjectorUtils.provideNetworkData(context).getPaidByPersonData(trx.getPaidBy()).observe((LifecycleOwner) context, personEntry -> {

                if (personEntry != null)
                    paidBy.setText(context.getString(R.string.paid_by, personEntry.getTitle(), personEntry.getFirstName(), personEntry.getLastName()));

                    TextView trxAmount = view.findViewById(R.id.trx_amount);
                    trxAmount.setText(context.getString(R.string.trx_amount_placeholder, trx.getAmount().toString()));

                    TextView trxStatus = view.findViewById(R.id.trx_status);
                    trxStatus.setText(trx.getTransactionStatus());

                    TextView payDate = view.findViewById(R.id.pay_date);
                    payDate.setText(AppUtils.dateToVeryShortDateString(AppUtils.dbStringToLocalDate(trx.getCreatedAt())));

                    TextView payType = view.findViewById(R.id.pay_type);
                    payType.setText(trx.getTransactionMedium());

                    TextView trxDescription = view.findViewById(R.id.trx_description);
                    trxDescription.setText(trx.getDescription());

                    TextView trxType = view.findViewById(R.id.trx_type);
                    trxType.setText(trx.getTransactionType().equalsIgnoreCase("cr") ? "CREDIT" : "DEBIT");
            });


            new AlertDialog.Builder(context)
                    .setTitle("Transaction Detail")
                    .setView(view)
                    .setPositiveButton("Close", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }
}
