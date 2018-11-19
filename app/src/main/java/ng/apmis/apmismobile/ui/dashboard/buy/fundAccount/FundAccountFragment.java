package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.personModel.Transaction;
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
    RecyclerView recyclerView;
    FundAccountAdapter adapter;

    public interface OnWalletFundedListener {
        void onWalletFunded();
    }

    private OnWalletFundedListener mListener;
    TransactionViewModel transactionViewModel;
    TransactionHistoryViewModelFactory transactionHistoryViewModelFactory;
    SharedPreferencesManager sharedPreferencesManager;
    ArrayList<Object> allList;
    int tripCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buys, container, false);
        sharedPreferencesManager = new SharedPreferencesManager(getActivity());
        allList = new ArrayList<>();
        adapter = new FundAccountAdapter(getActivity());

        instantiateOnWalletFundedListener((OnWalletFundedListener) getParentFragment());

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.instantiateWalletFundListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        transactionHistoryViewModelFactory = InjectorUtils.provideTransactionViewModelFactory(getActivity());

        transactionViewModel = ViewModelProviders.of(this, transactionHistoryViewModelFactory).get(TransactionViewModel.class);

        transactionViewModel.getPersonWallet(sharedPreferencesManager.getPersonId()).observe(this, wallet -> {

            Log.e("trip count", String.valueOf(++tripCount));
            allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));
            allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));

            allList.addAll(wallet.getTransactions());

            adapter.setItems(allList);

        });

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
