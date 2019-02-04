package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory.TransactionHistoryViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.transactionHistory.TransactionViewModel;
import ng.apmis.apmismobile.ui.dashboard.payment.FundWalletActivity;
import ng.apmis.apmismobile.utilities.InjectorUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Thadeus-APMIS on 10/23/2018.
 */

public class FundAccountFragment extends Fragment implements FundAccountAdapter.OnFundWalletClickedListener {

    public static final int FUND_WALLET_REQUEST = 1;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    @BindView(R.id.history_shimmer)
//    ShimmerFrameLayout transactionHistoryShimmer;

    @BindView(R.id.beneficiary_shimmer)
    ShimmerFrameLayout beneficiaryShimmer;

    FundAccountAdapter adapter;


    public interface OnWalletFundedListener {
        void onWalletFunded();
    }

    private OnWalletFundedListener mListener;
    TransactionViewModel transactionViewModel;
    TransactionHistoryViewModelFactory transactionHistoryViewModelFactory;
    SharedPreferencesManager sharedPreferencesManager;
    ArrayList<Beneficiaries> listItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buys, container, false);
        ButterKnife.bind(this, root);

        //transactionHistoryShimmer.startShimmer();
        beneficiaryShimmer.startShimmer();

        sharedPreferencesManager = new SharedPreferencesManager(getActivity());
        adapter = new FundAccountAdapter(getActivity());

        instantiateOnWalletFundedListener((OnWalletFundedListener) getParentFragment());

        recyclerView.setAdapter(adapter);
        adapter.instantiateFundWalletClickedListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        transactionHistoryViewModelFactory = InjectorUtils.provideTransactionViewModelFactory(getActivity());

        transactionViewModel = ViewModelProviders.of(this, transactionHistoryViewModelFactory).get(TransactionViewModel.class);
        transactionViewModel.getPersonWallet(sharedPreferencesManager.getPersonId()).observe(this, wallet -> {

            Log.e("Buy", "Requested Change");

            if (wallet != null){
                adapter.setTransactionItems(wallet.getTransactions());
            } else {
                adapter.setTransactionItems(null);
            }

            //transactionHistoryShimmer.stopShimmer();
            //transactionHistoryShimmer.setVisibility(View.INVISIBLE);
        });

        listItems = new ArrayList<>();
        listItems.add(new Beneficiaries("Seun Aloma", R.drawable.apmis_profile));
        listItems.add(new Beneficiaries("James Cracks", R.drawable.apmis_profile));
        listItems.add(new Beneficiaries("Alabe Boom", R.drawable.apmis_profile));

        if (!listItems.isEmpty()) {
            adapter.setBeneficiaryItems(listItems);
        } else {
            adapter.setBeneficiaryItems(new ArrayList<>());
        }

        beneficiaryShimmer.setVisibility(View.INVISIBLE);
        beneficiaryShimmer.stopShimmer();

        return root;
    }

    @Override
    public void onFundWalletClicked() {
        Intent i = new Intent(getActivity(), FundWalletActivity.class);
        startActivityForResult(i, FUND_WALLET_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FUND_WALLET_REQUEST:
                    mListener.onWalletFunded();
                    break;
            }
        }
    }

    public void instantiateOnWalletFundedListener(OnWalletFundedListener listener) {
        this.mListener = listener;
    }

}
