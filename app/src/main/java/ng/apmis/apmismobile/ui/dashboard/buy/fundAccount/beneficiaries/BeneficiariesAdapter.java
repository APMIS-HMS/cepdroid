package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.beneficiaries;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class BeneficiariesAdapter extends RecyclerView.Adapter<BeneficiariesAdapter.MyViewHolder> {

    private ArrayList<Beneficiaries> allBeneficiaries = new ArrayList<>();

    public BeneficiariesAdapter(ArrayList<Beneficiaries> beneficiaries) {
        allBeneficiaries = beneficiaries;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.beneficiaries_item, parent, false));
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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView userName;
        Button fundButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.beneficiary_image);
            userName = itemView.findViewById(R.id.beneficiary_name);
            fundButton = itemView.findViewById(R.id.beneficiary_fund);
        }
    }
}
