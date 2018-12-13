package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class BeneficiariesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Beneficiaries> beneficiaries;
    private Context context;
    private final int ADD_BENEFICIARY = 0;
    private final int BENEFICIARY = 1;

    public BeneficiariesAdapter(Context context, ArrayList<Beneficiaries> beneficiaries) {
        this.context = context;
        this.beneficiaries = new ArrayList<>();

        //Add arbitrary item in first position to enable showing add button in the first position of onbindview
        this.beneficiaries.add(new Beneficiaries(null, 0));

        this.beneficiaries.addAll(beneficiaries);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;

        switch (viewType) {
            case BENEFICIARY:
                view = layoutInflater.inflate(R.layout.beneficiaries_item, parent ,false);
                holder = new MyViewHolder(view);
                return holder;
            default:
                view = layoutInflater.inflate(R.layout.add_beneficiary, parent, false);
                holder = new AddBeneficiaryViewHolder(view);
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Beneficiaries beneficiary = this.beneficiaries.get(position);
        if (getItemViewType(position) == BENEFICIARY) {
            ((MyViewHolder)holder).imageView.setImageResource(beneficiary.getImage());
            ((MyViewHolder)holder).userName.setText(beneficiary.getName());
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return ADD_BENEFICIARY;
        } else {
            return BENEFICIARY;
        }
    }

    @Override
    public int getItemCount() {
        return this.beneficiaries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

    class AddBeneficiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView emptyText;
        ImageView addImage;
        RelativeLayout parentLayout;

        AddBeneficiaryViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.add_layout);
            addImage = itemView.findViewById(R.id.add_image);
            emptyText = itemView.findViewById(R.id.empty_text);
            itemView.setOnClickListener(this);
            if (getItemCount() == 1) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 160);
                parentLayout.setLayoutParams(layoutParams);
                emptyText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Adds a new beneficiary", Toast.LENGTH_SHORT).show();
        }
    }
}
