package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.ui.dashboard.payment.FundWalletActivity;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class BeneficiariesAdapter extends RecyclerView.Adapter<BeneficiariesAdapter.MyViewHolder> {

    private ArrayList<Beneficiaries> allBeneficiaries = new ArrayList<>();

    public BeneficiariesAdapter() {
        //TODO on instantiation, the first item {add button} is inflated by viewtype
        allBeneficiaries = new ArrayList<>();
    }

    public void setAllBeneficiaries (ArrayList<Beneficiaries> beneficiaries) {
        allBeneficiaries = beneficiaries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.beneficiaries_item, parent, false);
        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Beneficiaries beneficiary = allBeneficiaries.get(position);
        holder.imageView.setImageResource(beneficiary.getImage());
        holder.userName.setText(beneficiary.getName());
    }

    @Override
    public int getItemCount() {
        return allBeneficiaries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView imageView;
        TextView userName;
        Button fundButton;

        public MyViewHolder(View itemView) {
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
}
