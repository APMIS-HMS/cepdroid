package ng.apmis.apmismobile.ui.dashboard.buy;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.ModuleListModel;
import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.FundAccountFragment;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class BuyFragment extends android.support.v4.app.Fragment implements FundAccountFragment.OnWalletFundedListener{


    private static final String CLASSNAME = BuyFragment.class.getSimpleName();
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.balance_text)
    TextView balanceTextView;

    private SharedPreferencesManager pref;
    private String personId;

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        ButterKnife.bind(this, rootView);

        pref = new SharedPreferencesManager(getContext());
        personId = pref.getPersonId();

        /*
        * Set elevation on AppbarLayout
        * */
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            ViewCompat.setElevation(appBarLayout, 8);
        });

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        BuyCategoryAdapter adapter = new BuyCategoryAdapter(getChildFragmentManager());

        TabLayout tabLayout = rootView.findViewById(R.id.tabview);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        viewPager.setAdapter(adapter);


        initViewModel();

        return rootView;
    }

    BuyViewModel buyViewModel;
    Observer<Wallet> walletObserver = null;

    private void initViewModel(){
        BuyViewModelFactory viewModelFactory = InjectorUtils.provideBuyViewModelFactory(getContext());
        buyViewModel = ViewModelProviders.of(this, viewModelFactory).get(BuyViewModel.class);

        walletObserver = wallet -> {

            if (wallet != null) {
                balanceTextView.setText(String.format("₦%s", wallet.getBalance()+""));
            }
        };

        buyViewModel.getPersonWallet(personId).observe(this, walletObserver);

    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity) getActivity()).setToolBarTitle("Buy", false);
        }
        super.onResume();
    }

    @Override
    public void onWalletFunded() {
        buyViewModel.getPersonWallet(personId).removeObservers(this);
        buyViewModel.getPersonWallet(personId).observe(this, walletObserver);
    }
}
